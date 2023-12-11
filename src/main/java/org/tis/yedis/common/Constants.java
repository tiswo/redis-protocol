/**
 * Copyright: Copyright (c)2011
 * Organization: Tis
 */
package org.tis.yedis.common;

import java.nio.charset.Charset;

/**
 * 类名称: Constants <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午5:47
 */
public class Constants {
    public final static Charset DEFAULT_CHARSET= Charset.forName("UTF-8");
    public static final byte LF = '\n';
    public static final byte CR = '\r';
    public final static byte[] CRLF = new byte[]{CR, LF};
}
