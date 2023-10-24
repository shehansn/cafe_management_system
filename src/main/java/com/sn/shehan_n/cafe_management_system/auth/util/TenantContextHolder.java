package com.sn.shehan_n.cafe_management_system.auth.util;

public class TenantContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setTenantId(String tenant) {
        CONTEXT.set(tenant);
    }

    public static String getTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * Private constructor to hide implicit public constructor
     */
    private TenantContextHolder() {

    }
}
