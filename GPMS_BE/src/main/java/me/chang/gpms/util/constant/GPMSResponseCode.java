package me.chang.gpms.util.constant;

/**
 * GPMS约定状态码
 */
public enum GPMSResponseCode {

    /**
     * 成功
     */
    OK(200),

    /**
     * 默认
     */
    DEFAULT(0),

    /**
     * 客户端请求错误
     */
    CLIENT_ERROR(400),

    /**
     * 客户端错误：未授权，请登录
     */
    CLIENT_NO_LOGIN(401),

    /**
     * 客户端错误：权限不足
     */
    CLIENT_NO_AUTHORITY(403),

    /**
     * 服务器错误：内部错误
     */
    SERVER_INTERNAL_ERROR(500)

    ;

    private final int value;

    GPMSResponseCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
