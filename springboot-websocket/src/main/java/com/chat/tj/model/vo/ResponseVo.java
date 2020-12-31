package com.chat.tj.model.vo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 对象方法 ok error
 * 静态方法 success failed content
 *
 * @author yinchao
 * @date 2020/1/7 9:10
 */
@Getter
@Setter
public class ResponseVo<T> {
    /**
     * "1": 成功 (默认)"2": 失败
     */
    private int flag = FLAG_CODE.SUCCESS.getCode();

    /**
     * 提示信息：成功（默认）
     */
    private String message = FLAG_CODE.SUCCESS.getMessage();

    /**
     * 默认错误代码:200 正常
     */
    private int errorCode = STATUS_CODE.NORMAL.getCode();

    /**
     * 数据
     */
    private T content;

    public ResponseVo<T> setContent(T content) {
        this.content = content;
        return this;
    }
/*************构造方法**************/

    /**
     * 无参构造
     */
    public ResponseVo() {
    }

    /**
     * 有参构造
     */
    public ResponseVo(T content) {
        this.content = content;
    }
    /**************常用方法*************/

    /**
     * 自定义状态码 异常返回
     *
     * @param errorCode 错误码
     * @param message   错误信息
     * @return 返回信息
     */
    public ResponseVo<T> error(int errorCode, String message) {
        this.flag = FLAG_CODE.FAILED.getCode();
        this.errorCode = errorCode;
        this.message = message;
        return this;
    }

    public ResponseVo<T> error(String message) {
        this.flag = FLAG_CODE.FAILED.getCode();
        this.message = message;
        return this;
    }

    public ResponseVo<T> error(STATUS_CODE authStatus) {
        this.flag = FLAG_CODE.FAILED.getCode();
        this.errorCode = authStatus.getCode();
        this.message = authStatus.getMessage();
        return this;
    }

    public ResponseVo<T> error(ERROR_CODE errorCode) {
        this.flag = FLAG_CODE.FAILED.getCode();
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
        return this;
    }

    public ResponseVo<T> ok(String message) {
        ResponseVo<T> ok = ok();
        ok.setMessage(message);
        return ok;
    }

    public ResponseVo<T> ok() {
        this.flag = FLAG_CODE.SUCCESS.getCode();
        this.errorCode = STATUS_CODE.NORMAL.getCode();
        this.message = FLAG_CODE.SUCCESS.getMessage();
        this.content = null;
        return this;
    }

    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>();
    }

    public static <T> ResponseVo<T> success(String message) {
        return new ResponseVo<T>().ok(message);
    }

    public static <T> ResponseVo<T> failed(String message) {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.error(message);
        return responseVo;
    }

    public static <T> ResponseVo<T> content(T content) {
        return new ResponseVo<>(content);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**************枚举状态码*************/
    /**
     * 状态码
     */
    public enum STATUS_CODE {
        /**
         * 正常
         */
        NORMAL(200, "正常"),
        /**
         * 用户已退出
         */
        LOGOUT(3000, "账户已退出,请重新登录"),
        /**
         * 用户未登录
         */
        LOGIN_WITHOUT(3001, "您未登录账户,请登录"),
        /**
         * 登录已过期
         */
        LOGIN_EXPIRE(3002, "当前登录已过期,请重新登录"),
        /**
         * 登录失效，多次登录,token,密码被重置
         */
        LOGIN_INVALID(3003, "当前登录已失效,请重新登录"),
        /**
         * token不正确
         */
        LOGIN_TOKEN_ERROR(3004, "当前登录状态无效,请重新登录"),
        /**
         * 用户在其它端登录
         */
        LOGIN_OTHER_CLIENT(3005, "当前账户已在其它端登录,请重新登录"),
        /**
         * 账号登录异常
         */
        LOGIN_EXCEPTION(3009, "当前账户登录状态异常,请重新登录"),

        /**
         * 验证码错误
         */
        VERIFY_CODE_ERROR(4001, "验证码错误"),
        /**
         * 用户名或密码错误
         */
        USER_ID_PWD_ERROR(4002, "用户名或密码错误"),
        /**
         * 用户已锁定
         */
        USER_LOCK(4100, "用户已锁定,请联系管理员"),
        /**
         * 账户一级锁定
         */
        USER_LEVEL_ONE_LOCK(4101, "账户一级锁定"),
        /**
         * 账户二级锁定
         */
        USER_LEVEL_TWO_LOCK(4102, "账户二级锁定"),
        /**
         * 账户三级锁定
         */
        USER_LEVEL_THREE_LOCK(4103, "账户三级锁定"),
        /**
         * 管理员锁定
         */
        USER_MANAGER_LOCK(4104, "管理员锁定"),
        /**
         * 账号状态异常
         */
        USER_ERROR(4109, "账号状态异常"),
        /**
         * 没有权限
         */
        WITHOUT_AUTH(4200, "当前账户权限不足"),

        WITHOUT_SHARE(4201, "该内容不可分享");

        STATUS_CODE(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }

    /**
     * 错误码
     */
    public enum ERROR_CODE {
        /**
         * 运行错误
         */
        RUN_ERROR(5000, "运行错误"),
        /**
         * 参数错误
         */
        PARAM_ERROR(5001, "参数错误"),
        /**
         * 未知异常
         */
        UNKNOWN_ERROR(5555, "未知错误");

        ERROR_CODE(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }

    /**
     * 标记码
     */
    public enum FLAG_CODE {
        /**
         * 成功状态
         */
        SUCCESS(1, "成功"),
        /**
         * 失败状态
         */
        FAILED(2, "失败");

        FLAG_CODE(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
