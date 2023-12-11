package org.tis.yedis.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 类名称: RedisDB <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/15 下午8:05
 */
public class RedisDB {

    private Map<String, RedisObject> db = new HashMap<>();

    private Map<String, Long> expires = new HashMap<>();


    public Map<String, RedisObject> getDb() {
        return db;
    }

    public void setDb(Map<String, RedisObject> db) {
        this.db = db;
    }

    public Map<String, Long> getExpires() {
        return expires;
    }

    public void setExpires(Map<String, Long> expires) {
        this.expires = expires;
    }
}
