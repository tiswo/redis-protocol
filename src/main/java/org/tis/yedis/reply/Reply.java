/**
 * Copyright: Copyright (c)2011
 * Organization: Tis
 */
package org.tis.yedis.reply;

import org.tis.yedis.netty.RedisCommandDecoder;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 类名称: Reply <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 上午12:02
 */
public interface Reply<T> {

    byte[] CRLF = new byte[]{RedisCommandDecoder.CR, RedisCommandDecoder.LF};

    T data();

    void write(ByteBuf os) throws IOException;
}