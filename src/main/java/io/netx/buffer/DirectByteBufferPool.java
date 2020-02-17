package io.netx.buffer;

import sun.misc.Unsafe;
import sun.misc.VM;

import java.util.List;

public class DirectByteBufferPool {

    protected static final Unsafe unsafe = Unsafe.getUnsafe();

    private long base;

    private int pgsize;

    private int pgcount;

    private long address;

    DirectByteBuffer head;
    DirectByteBuffer tail;

    DirectByteBufferPool(int cap) {
        boolean pa = VM.isDirectMemoryPageAligned();
        pgsize = unsafe.pageSize();

        long size = Math.max(1L, (long)cap + (pa ? pgsize : 0));

        base = 0;
        try {
            base = unsafe.allocateMemory(size);
        } catch (OutOfMemoryError x) {
            throw x;
        }
        unsafe.setMemory(base, size, (byte) 0);

        if (pa && (base % pgsize != 0)) {
            // Round up to page boundary
            address = base + pgsize - (base & (pgsize - 1));
        } else {
            address = base;
        }
        head = new DirectByteBuffer(((int)size / pgsize) * pgsize, pgsize, 0, address, (int)size / pgsize, this );
        tail = head;

    }

    public void free(DirectByteBuffer buffer) {
        DirectByteBuffer bufferprev = findAddresslastless(buffer.getAddress() + buffer.getSize());
        //放回队列时涉及到合并
        if (bufferprev.getAddress() + bufferprev.getSize() == buffer.getAddress()) {
            bufferprev.setPgcount(bufferprev.getRefCount() + buffer.getRefCount());
            bufferprev.setSize(bufferprev.getSize() + buffer.getSize());
            if (bufferprev.next != null) {
                if (bufferprev.getAddress() + bufferprev.getSize() == bufferprev.next.getAddress()) {
                    bufferprev.setSize(bufferprev.getSize() + bufferprev.next.getSize());
                    bufferprev.next.prev = null;
                    bufferprev.next = bufferprev.next.next;
                    bufferprev.next.prev.next = null;
                    bufferprev.next.prev = bufferprev;
                }
            }
        } else {
            DirectByteBuffer buffernext = bufferprev.next;
            if (buffernext != null) {
                buffernext.prev = null;
                bufferprev.next = buffer;
                buffer.prev = bufferprev;
                if (buffer.getAddress() + buffer.getSize() == buffernext.getAddress()) {
                    buffer.setSize(buffer.getSize() + buffernext.getSize());
                    buffer.setPgcount(buffer.getPgcount() + buffernext.getPgcount());
                    buffer.next = buffernext.next;
                    buffernext.next = null;
                    if (buffer.next != null) {
                        buffer.next.prev = buffer;
                    }
                } else {
                    buffer.next = buffernext;
                    buffernext.prev = buffer;
                }
            } else {
                bufferprev.next = buffer;
                buffer.prev = bufferprev;
                buffer.next = null;
            }
        }

    }

    public DirectByteBuffer allocate(int capacity) {
        int count = capacity / pgsize;
        DirectByteBuffer buffer = findFirstBuffer(count);
        if (buffer == null) {
            //TODO 可以抛出异常
            return null;
        }
        DirectByteBuffer buffer0 = new DirectByteBuffer(capacity, pgsize, 1, buffer.getAddress(), count, this);
        buffer.setAddress(buffer.getAddress() + pgsize * count);
        buffer.setPgcount(buffer.getRefCount() - count);
        if (buffer.getPgcount() == 0) {
            buffer.next.prev = buffer.prev;
            buffer.prev.next = buffer.next;
            buffer.next = buffer.prev = null;
        }
        return buffer0;
    }

    public void freePool() {
        unsafe.freeMemory(base);
    }

    private DirectByteBuffer findFirstBuffer(int count) {
        DirectByteBuffer res = null;
        if (head == tail) {
            if (head.getPgcount() > count) {
                res = head;
            }
        } else {
            res = head;
            while (res.getRefCount() < count && res != null) {
                res = res.next;
            }
        }
        return res;
    }

    private DirectByteBuffer findAddresslastless(long address) {
        DirectByteBuffer res = head;
        DirectByteBuffer prev = head;
        while (res.getAddress() < address) {
            prev = res;
            res = res.next;
        }
        return prev;
    }
}
