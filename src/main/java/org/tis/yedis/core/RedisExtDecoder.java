package org.tis.yedis.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.redis.RedisDecoder;

import java.util.List;

/**
 * 类名称: RedisExtDecoder <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/16 下午8:21
 */
public class RedisExtDecoder extends ByteToMessageDecoder {


    private RedisDecoder decoder = new RedisDecoder();


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.getByte(0) == 'R'){
            new RdbDumper().dump(in);
        }
    }
}
