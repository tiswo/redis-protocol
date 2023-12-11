package org.tis.yedis.reply;

import org.tis.yedis.common.Constants;
import io.netty.buffer.ByteBuf;
import org.tis.yedis.utils.TransferUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类名称: MultiBulkReply <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 上午12:18
 */
public class MultiBulkReply implements Reply<Reply[]> {

    public static final char MARKER = '*';

    public static final MultiBulkReply EMPTY = new MultiBulkReply(new Reply[0]);

    private Reply[] replies;
    private int size = -2;
    private int index = 0;

    public MultiBulkReply() {

    }

    public MultiBulkReply(Reply[] replies) {
        this.replies = replies;
        size = replies.length;
    }

    @Override
    public Reply[] data() {
        return replies;
    }

    @Override
    public void write(ByteBuf os) throws IOException {
        os.writeByte(MARKER);
        if (replies == null) {
            os.writeBytes(TransferUtils.NEG_ONE_WITH_CRLF);
        } else {
            os.writeBytes(TransferUtils.longToBytes(replies.length, true));
            for (Reply reply : replies) {
                reply.write(os);
            }
        }
    }

    public List<String> asStringList(Charset charset) {
        if (replies == null)
            return null;
        List<String> strings = new ArrayList<String>(replies.length);
        for (Reply reply : replies) {
            if (reply instanceof BulkReply) {
                strings.add(((BulkReply) reply).asString(charset));
            } else {
                throw new IllegalArgumentException("Could not convert " + reply + " to a string");
            }
        }
        return strings;
    }

    public Set<String> asStringSet(Charset charset) {
        if (replies == null) return null;
        Set<String> strings = new HashSet<String>(replies.length);
        for (Reply reply : replies) {
            if (reply instanceof BulkReply) {
                strings.add(((BulkReply) reply).asString(charset));
            } else {
                throw new IllegalArgumentException("Could not convert " + reply + " to a string");
            }
        }
        return strings;
    }

    public String toString() {
        return asStringList(Constants.DEFAULT_CHARSET).toString();
    }
}
