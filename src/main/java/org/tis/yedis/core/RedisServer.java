package org.tis.yedis.core;

import org.tis.yedis.reply.BulkReply;
import org.tis.yedis.reply.IntegerReply;
import org.tis.yedis.reply.MultiBulkReply;
import org.tis.yedis.reply.StatusReply;

/**
 * 类名称: RedisExecutor <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午7:26
 */
public interface RedisServer {

    BulkReply get(byte[] key) throws RuntimeException;

    StatusReply set(byte[] key, byte[] value) throws RuntimeException;

    IntegerReply expire(byte[] key, int expiration);

    IntegerReply del(byte[]... keys);

    MultiBulkReply keys(byte[] pattern);

    IntegerReply exists(byte[] key);

    BulkReply info(byte[] section);

    default StatusReply quit() {
        return StatusReply.QUIT;
    }

    default StatusReply ping() {
        return StatusReply.PONG;
    }

    default StatusReply replconf(){
        new RedisClient().write();
        return StatusReply.OK;
    }

    default BulkReply psync() {
        return new BulkReply("$ set A b".getBytes());
    }

    default BulkReply sync() {
        return new BulkReply("$ set A b".getBytes());
    }
}