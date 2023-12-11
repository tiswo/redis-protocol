package org.tis.yedis.command;

import org.tis.yedis.common.Constants;
import io.netty.buffer.ByteBuf;

/**
 * 类名称: Command <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午7:55
 */
public class Command {
    public static final byte[] EMPTY_BYTES = new byte[0];

    private final Object name;
    private final Object[] objects;
    private final boolean inline;

    public Command(Object[] objects) {
        this(null, objects, false);
    }

    public Command(Object[] objects, boolean inline) {
        this(null, objects, inline);
    }

    private Command(Object name, Object[] objects, boolean inline) {
        this.name = name;
        this.objects = objects;
        this.inline = inline;
    }

    public byte[] getName() {
        if (name != null)
            return getBytes(name);
        if(objects.length==0){
            return EMPTY_BYTES;
        }
        return getBytes(objects[0]);
    }

    public boolean isInline() {
        return inline;
    }

    private byte[] getBytes(Object object) {
        byte[] argument;
        if (object == null) {
            argument = EMPTY_BYTES;
        } else if (object instanceof byte[]) {
            argument = (byte[]) object;
        } else if (object instanceof Byte[]) {
            argument = (byte[]) object;
        } else if (object instanceof ByteBuf) {
            argument = ((ByteBuf) object).array();
        } else if (object instanceof String) {
            argument = ((String) object).getBytes(Constants.DEFAULT_CHARSET);
        } else {
            argument = object.toString().getBytes(Constants.DEFAULT_CHARSET);
        }
        return argument;
    }

    public void toArguments(Object[] arguments, Class<?>[] types) {
        for (int position = 0; position < types.length; position++) {
            if (types[position] == byte[].class) {
                if (position >= arguments.length) {
                    throw new IllegalArgumentException(
                            "wrong number of arguments for '"
                                    + new String(getName()) + "' command");
                }
                if (objects.length - 1 > position) {
                    arguments[position] = objects[1 + position];
                } else if(types.length >1){
                    throw new IllegalArgumentException(
                            "wrong number of arguments for '"
                                    + new String(getName()) + "' command");
                }
            } else {
                Class<?> c = types[position];
                if (c.isPrimitive()){
//                    TransferUtils.bytesToInt(objects[position + 1], 0 );
                }
                int left = objects.length - position - 1;
                byte[][] lastArgument = new byte[left][];

                for (int i = 0; i < left; i++) {
                    lastArgument[i] = (byte[]) objects[i + position + 1];
                }
                arguments[position] = lastArgument;
            }
        }
    }

}