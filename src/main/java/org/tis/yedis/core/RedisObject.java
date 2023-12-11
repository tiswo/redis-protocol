package org.tis.yedis.core;

import java.io.Serializable;

/**
 * 类名称: RedisObject <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/17 上午12:05
 */
public class RedisObject implements Serializable{

    private int type;

    private Object o;

}
