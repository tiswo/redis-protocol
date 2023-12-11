package org.tis.yedis.core.command;

import org.tis.yedis.core.RedisClient;

/**
 * 接口名称: Command <br>
 * 接口描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/16 下午11:35
 */
public interface Command {

    void process(RedisClient client);

}