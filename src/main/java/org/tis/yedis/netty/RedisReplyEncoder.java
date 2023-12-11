/**
 * Copyright: Copyright (c)2011
 * Organization: Tis
 */
package org.tis.yedis.netty;

import org.tis.yedis.reply.Reply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 类名称: RedisReplyEncoder <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午7:01
 */

public class RedisReplyEncoder extends MessageToByteEncoder<Reply> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Reply msg, ByteBuf out) throws Exception {
        System.out.println("RedisReplyEncoder: " + msg);
        msg.write(out);
    }

}