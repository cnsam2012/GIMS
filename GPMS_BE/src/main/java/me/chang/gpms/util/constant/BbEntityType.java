package me.chang.gpms.util.constant;

public enum BbEntityType {

    /**
     * 实体类型：报告
     */
    ENTITY_TYPE_POST(1),

    /**
     * 实体类型：评论/评价
     */
    ENTITY_TYPE_COMMENT(2),

    /**
     * 实体类型：人
     * 实体类型为人时，即为该学生的最后评分
     */
    ENTITY_TYPE_USER(3);

    private final int value;

    BbEntityType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
