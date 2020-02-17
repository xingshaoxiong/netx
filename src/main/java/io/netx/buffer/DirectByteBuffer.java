package io.netx.buffer;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.misc.VM;

import java.nio.BufferUnderflowException;


public class DirectByteBuffer extends ByteBuffer {

    protected static final Unsafe unsafe = Unsafe.getUnsafe();

    private DirectByteBufferPool bufferPool;

    public DirectByteBuffer prev;

    public DirectByteBuffer next;

    private int refCount;

    private int position;

    private int limit;

    private int cap;

    private long size;

    private int pgsize;

    private int pgcount;

    private long address;

    DirectByteBuffer(int cap, int pgsize, int refCount, long address, int pgcount, DirectByteBufferPool bufferPool) {
        this.cap = cap;
        this.size = pgsize * pgcount;
        this.pgcount = pgcount;
        this.pgsize = pgsize;
        this.bufferPool = bufferPool;
        this.address = address;
        this.refCount = refCount;
        this.position = 0;
        this.limit = cap;
        this.prev = null;
        this.next = null;
    }

    public void free() throws Exception {
        if (refCount <= 0) {
            throw new Exception("该内存不存在");
        } else {
            refCount--;
            if (refCount == 0) {
                bufferPool.free(this);
            }
        }
    }

    public long address() {
        return address;
    }

    private long ix(int i) {
        return address + ((long)i << 0);
    }

    public byte get() {
        return ((unsafe.getByte(ix(position++))));
    }

    public byte get(int i) {
        return ((unsafe.getByte(ix(i))));
    }



    public DirectByteBuffer put(byte x) {

        unsafe.putByte(ix(position++), ((x)));
        return this;
    }

    public DirectByteBuffer put(int i, byte x) {

        unsafe.putByte(ix(i), ((x)));
        return this;
    }

    @Override
    public int compareTo(ByteBuffer o) {
        return 0;
    }

    public DirectByteBufferPool getBufferPool() {
        return bufferPool;
    }

    public int getRefCount() {
        return refCount;
    }

    public int getPosition() {
        return position;
    }

    public int getLimit() {
        return limit;
    }

    public int getCap() {
        return cap;
    }

    public long getSize() {
        return size;
    }

    public int getPgsize() {
        return pgsize;
    }

    public int getPgcount() {
        return pgcount;
    }

    public long getAddress() {
        return address;
    }

    public void setPrev(DirectByteBuffer prev) {
        this.prev = prev;
    }

    public void setNext(DirectByteBuffer next) {
        this.next = next;
    }

    public void setRefCount(int refCount) {
        this.refCount = refCount;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPgcount(int pgcount) {
        this.pgcount = pgcount;
    }

    public void setAddress(long address) {
        this.address = address;
    }
}
