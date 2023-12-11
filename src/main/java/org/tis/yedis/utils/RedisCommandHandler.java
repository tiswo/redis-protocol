package org.tis.yedis.utils;

import org.tis.yedis.command.Command;
import org.tis.yedis.reply.Reply;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 类名称: RedisCommandHandler <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午7:09
 */
@ChannelHandler.Sharable
public class RedisCommandHandler extends SimpleChannelInboundHandler<Boolean> {

    private Map<String, Wrapper> methods = new HashMap<String, Wrapper>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Boolean msg) throws Exception {
        System.out.println(msg);
    }

    interface Wrapper {
        Reply<?> execute(Command command) throws RuntimeException;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}