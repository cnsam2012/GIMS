package me.chang.gpms.util.constant;

public enum GPMSActivationStatus {
    /**
     * 激活成功
     */
    ACTIVATION_SUCCESS(0),

    /**
     * 重复激活
     */
    ACTIVATION_REPEAT(1),

    /**
     * 激活失败
     */
    ACTIVATION_FAILURE(2);

    private final int value;

    GPMSActivationStatus(int i) {
        this.value = i;
    }

    public int value() {
        return value;
    }
}
