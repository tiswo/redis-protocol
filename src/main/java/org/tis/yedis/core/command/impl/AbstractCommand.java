package org.tis.yedis.core.command.impl;

import org.tis.yedis.core.command.Command;

/**
 * 类名称: AbstractCommand <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/16 下午11:37
 */
public abstract class AbstractCommand implements Command {

    private String name;

    private int arity;

    private char[] flags;

    private char flag;
}
