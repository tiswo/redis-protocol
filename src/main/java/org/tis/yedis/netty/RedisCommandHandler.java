package org.tis.yedis.netty;

import org.tis.yedis.command.Command;
import org.tis.yedis.core.RedisServer;
import org.tis.yedis.reply.ErrorReply;
import org.tis.yedis.reply.Reply;
import org.tis.yedis.reply.StatusReply;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public class RedisCommandHandler extends SimpleChannelInboundHandler<Command> {

    private Map<String, Wrapper> methods = new HashMap<String, Wrapper>();

    interface Wrapper {
        Reply<?> execute(Command command) throws RuntimeException;
    }

    public RedisCommandHandler(final RedisServer rs) {
        Class<? extends RedisServer> aClass = rs.getClass();
        for (final Method method : aClass.getMethods()) {
            final Class<?>[] types = method.getParameterTypes();
            methods.put(method.getName(), new Wrapper() {
                @Override
                public Reply<?> execute(Command command) throws RuntimeException {
                    Object[] objects = new Object[types.length];
                    try {
                        command.toArguments(objects, types);
                        return (Reply<?>) method.invoke(rs, objects);
                    } catch (Exception e) {
                        if(e instanceof InvocationTargetException){
                            return new ErrorReply("ERR " + e.getCause().getMessage());
                        }else{
                            return new ErrorReply("ERR " + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command msg)
            throws Exception {
        String name = new String(msg.getName());
        if(name.length()==0){
            return;
        }
        Wrapper wrapper = methods.get(name.toLowerCase());
        Reply<?> reply;
        if (wrapper == null) {
            reply = new ErrorReply("ERR unknown command '" + name + "'");
        } else {
            reply = wrapper.execute(msg);
        }
        if (reply == StatusReply.QUIT) {
            ctx.writeAndFlush(reply);
            ctx.close();
        } else {
//            if (msg.isInline()) {
//                if (reply == null) {
//                    reply = new InlineReply(null);
//                } else {
//                    reply = new InlineReply(reply.data());
//                }
//            }
            if (reply == null) {
                reply = ErrorReply.NYI_REPLY;
            }
            ctx.write(reply);
            ctx.flush();
        }
    }
}