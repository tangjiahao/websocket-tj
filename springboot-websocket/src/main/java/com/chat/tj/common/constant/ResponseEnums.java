package com.chat.tj.common.constant;

public enum ResponseEnums {

    PARAM_WARNING(1, "参数错误"),
    DB_WARNING(2, "数据库异常"),
    CODE_WARNING(3, "代码运行异常"),
    FILE_WARNING(4, "文件异常");

    private int code;

    private String name;

    ResponseEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
