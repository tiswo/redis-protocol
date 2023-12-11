package org.tis.yedis.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 类名称: Rdb <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/16 下午11:14
 */
public class Rdb {

    final int REDIS_RDB_VERSION = 6;

    public int getPid(){//TODO getPid
        return 0;
    }

    public void save(String fileName) throws IOException {

        String temp = String.format("temp-%d.rdb", getPid());
        String magic = String.format("REDIS%04d", REDIS_RDB_VERSION);

        FileChannel fileChannel = FileChannel.open(Paths.get(temp), StandardOpenOption.WRITE);
        fileChannel.write(ByteBuffer.wrap(magic.getBytes()));
    }

}
