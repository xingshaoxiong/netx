package io.netx.net;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
//当时为了统一，全使用了ByteBuffer, 其实使用Byte[]同理，两者之间的转换也很方便
public class DelimiterBasedFrameDecoder extends ChannelInBoundHandlerAdapter {

    private final ByteBuffer delimiter;

    private final int maxLength;

    private ByteBuffer lastRemain;

    public DelimiterBasedFrameDecoder(ByteBuffer delimiter) {
        this(delimiter, 0);
    }

    public DelimiterBasedFrameDecoder(ByteBuffer delimiter, int maxLength) {
        if (delimiter.position() != 0) {
            delimiter.flip();
        }
        this.delimiter = delimiter;
        if (maxLength == 0) {
            maxLength = 1024;
        }
        this.maxLength = maxLength;
        lastRemain = ByteBuffer.allocate(maxLength);
    }




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        List<ByteBuffer> res = decode(msg);
        if (res.size() > 0) {
            for (ByteBuffer buffer : res) {
                ctx.fireChannelRead(buffer);
            }
        }
    }

    private List<ByteBuffer> decode(Object msg) throws Exception {
        List<ByteBuffer> res = new ArrayList<>();
        if (msg instanceof ByteBuffer) {
            ByteBuffer buffer = (ByteBuffer)msg;
            buffer.flip();
            if (buffer.limit() >= maxLength) {
                throw new Exception("msg超过了最大字节长度");
            }
            int delimiterLength = delimiter.limit();
            int length = buffer.limit();
            int index = 0;

            while (index < length) {
                index = findIndex(buffer, delimiter);
                if (index == -1) {
                    while(buffer.hasRemaining()) {
                        lastRemain.put(buffer.get());
                    }
                    break;
                }
                if (res.size() == 0) {
                    ByteBuffer buffer0 = ByteBuffer.allocate(maxLength);
                    while(lastRemain.hasRemaining()) {
                        buffer0.put(lastRemain.get());
                    }
                    while (buffer.position() < index) {
                        buffer0.put(buffer.get());
                    }
                    res.add(buffer0);
                } else {
                    ByteBuffer buffer0 = ByteBuffer.allocate(maxLength);
                    while (buffer.position() < index) {
                        buffer0.put(buffer.get());
                    }
                    res.add(buffer0);
                }
                for (int i = 0; i < delimiterLength; i++) {
                    buffer.get();
                }
            }
        }
        return res;
    }

    private int findIndex(ByteBuffer buffer, ByteBuffer delimiter) {
        int pos1 = buffer.position();
        int limit1 = buffer.limit();
        int pos2 = 0;
        int index = pos1;
        int count = delimiter.limit();
        while (pos1 < limit1 && count > 0) {
            if(buffer.get(pos1) == delimiter.get(pos2)) {
                count--;
                pos1++;
                pos2++;
            } else {
                count = delimiter.limit();
                pos1++;
                pos2 = 0;
            }
        }
        if (count == 0) {
            index = pos1 - delimiter.limit() - 1;
        } else {
            index = -1;
        }
        return index;
    }
}
