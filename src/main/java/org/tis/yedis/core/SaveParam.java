package org.tis.yedis.core;

/**
 * 类名称: SaveParam <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/15 下午8:43
 */
public class SaveParam {

    private Long seconds;

    private int changes;

    public SaveParam() {

    }

    public SaveParam(Long seconds, int changes) {
        this.seconds = seconds;
        this.changes = changes;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public int getChanges() {
        return changes;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }
}
