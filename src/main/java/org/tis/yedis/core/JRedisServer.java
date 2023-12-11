package org.tis.yedis.core;

import java.util.List;

/**
 * 类名称: DefaultRedisExecutor <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午7:32
 */
public class JRedisServer {

    private static RedisDB[] dbs;

    private SaveParam[] saveParams;

    private long dirty;

    private long lastSave;

    private String masterHost;

    private int masterPort;

    private List<RedisClient> clients;

    private List<RedisClient> slaves;
}