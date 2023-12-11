package org.tis.yedis.core;

import org.tis.yedis.reply.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类名称: DefaultRedisExecutor <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午7:32
 */
public class DefaultRedisServer implements RedisServer {

    private ConcurrentHashMap<String, byte[]> database = new ConcurrentHashMap<>();

    @Override
    public BulkReply get(byte[] key) throws RuntimeException {
        byte[] data = database.get(new String(key));
        if (data == null) {
            return new BulkReply();
        }
        return new BulkReply(data);
    }

    @Override
    public StatusReply set(byte[] key, byte[] value) throws RuntimeException {
        database.put(new String(key), value);
        return StatusReply.OK;
    }


    @Override
    public IntegerReply expire(byte[] key, int expiration) {
        return null;
    }


    @Override
    public IntegerReply del(byte[]... keys) {//至少要一个参数
        if (keys == null || keys.length < 1) {
            throw new IllegalArgumentException(
                    "wrong number of arguments for 'del' command");
        }
        int total = 0;
        for(int i=0; i < keys.length; i++){
           if(database.remove(new String(keys[i])) != null){
               total++;
           }
        }
        return new IntegerReply(total);
    }

    @Override
    public IntegerReply exists(byte[] key) {
        int status = database.containsKey(new String(key))? 1 : 0;
        return new IntegerReply(status);
    }

    @Override
    public MultiBulkReply keys(byte[] pattern) {
        Set<String> keySet = database.keySet();
        List<Reply<byte[]>> replies = new ArrayList<Reply<byte[]>>();
        for(String key: keySet){
            byte[] bytes = key.getBytes();
            boolean expired = false;
            if (matches(bytes, pattern, 0, 0) && !expired) {
                replies.add(new BulkReply(bytes));
            }
        }
        return new MultiBulkReply(replies.toArray(new Reply[replies.size()]));
    }

    private static boolean matches(byte[] key, byte[] pattern, int kp, int pp) {
        if (kp == key.length) {
            return pp == pattern.length || (pp == pattern.length - 1 && pattern[pp] == '*');
        } else if (pp == pattern.length) {
            return false;
        }
        byte c = key[kp];
        byte p = pattern[pp];
        switch (p) {
            case '?':
                return matches(key, pattern, kp + 1, pp + 1);
            case '*':
                return matches(key, pattern, kp + 1, pp + 1) || matches(key, pattern, kp + 1, pp);
            case '\\':
                return c == pattern[pp + 1] && matches(key, pattern, kp + 1, pp + 2);
            case '[':
                boolean found = false;
                pp++;
                do {
                    byte b = pattern[pp++];
                    if (b == ']') {
                        break;
                    } else {
                        if (b == c) found = true;
                    }
                } while (true);
                return found && matches(key, pattern, kp + 1, pp);
            default:
                return c == p && matches(key, pattern, kp + 1, pp + 1);
        }
    }

    @Override
    public BulkReply info(byte[] section) {
        return new BulkReply();
    }
}