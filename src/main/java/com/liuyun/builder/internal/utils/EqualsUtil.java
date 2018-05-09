package com.liuyun.builder.internal.utils;

public final class EqualsUtil {

    public static boolean areEqual(boolean b1, boolean b2) {
        return b1 == b2;
    }

    public static boolean areEqual(char c1, char c2) {
        return c1 == c2;
    }

    public static boolean areEqual(long l1, long l2) {
        return l1 == l2;
    }

    public static boolean areEqual(float f1, float f2) {
        return Float.floatToIntBits(f1) == Float.floatToIntBits(f2);
    }

    public static boolean areEqual(double d1, double d2) {
        return Double.doubleToLongBits(d1) == Double.doubleToLongBits(d2);
    }
    
    public static boolean areEqual(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }
    
}
