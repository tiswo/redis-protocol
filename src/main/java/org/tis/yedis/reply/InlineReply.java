/**
 * Copyright: Copyright (c)2011
 * Organization: Tis
 */
package org.tis.yedis.reply;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 类名称: InlineReply <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 上午12:26
 */
public class InlineReply implements Reply{

    private final Object o;

    public InlineReply(Object o) {
        this.o = o;
    }

    @Override
    public Object data() {
        return o;
    }

    @Override
    public void write(ByteBuf os) throws IOException {
        if (o == null) {
            os.writeBytes(CRLF);
        } else if (o instanceof String) {
            os.writeByte('+');
            os.writeBytes(((String) o).getBytes("US-ASCII"));
            os.writeBytes(CRLF);
        } else if (o instanceof ByteBuf) {
            os.writeByte('+');
            os.writeBytes(((ByteBuf) o).array());
            os.writeBytes(CRLF);
        } else if (o instanceof byte[]) {
            os.writeByte('+');
            os.writeBytes((byte[]) o);
            os.writeBytes(CRLF);
        } else if (o instanceof Long) {
            os.writeByte(':');
            os.writeByte(((Long) o).byteValue());
            os.writeBytes(CRLF);
        } else {
            os.writeBytes("ERR invalid inline response".getBytes("US-ASCII"));
            os.writeBytes(CRLF);
        }
    }
}
