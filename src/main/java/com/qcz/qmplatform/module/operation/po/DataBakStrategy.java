package com.qcz.qmplatform.module.operation.po;

import java.io.Serializable;

/**
 * 数据备份策略
 */
public class DataBakStrategy implements Serializable {

    /**
     * 备份周期（1：星期一，2：星期二，:4：星期三，8：星期四，16：星期五，32：星期六，64：星期天）按位取值相加
     */
    private int period;

    /**
     * 剩余磁盘大小多少才进行备份（G）
     */
    private int limitDiskSpace;

    /**
     * 备份开关（0：关；1：开）
     */
    private int enable;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getLimitDiskSpace() {
        return limitDiskSpace;
    }

    public void setLimitDiskSpace(int limitDiskSpace) {
        this.limitDiskSpace = limitDiskSpace;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
