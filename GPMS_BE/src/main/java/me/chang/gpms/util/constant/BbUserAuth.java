package me.chang.gpms.util.constant;

@Deprecated
public enum BbUserAuth {
    /**
     * 权限：普通用户
     */
    AUTHORITY_USER("user"),

    /**
     * 权限：管理员
     */
    AUTHORITY_ADMIN("admin"),

    /**
     * 权限：版主
     */
    AUTHORITY_MODERATOR("moderator");

    private final String value;

    BbUserAuth(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
