package com.itjiguang.yanxuan.result;

import java.io.Serializable;

public class Result implements Serializable {

    // 保存成功返回true,保存失败返回false
    private boolean flag;
    // 描述信息
    private String message;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
