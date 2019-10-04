package com.pk.consistency.util;

public class LockUtil {


    public static boolean lock(String key) {
        key = CacheEnum.DEFAULT_LOCK_PREX + key;
        return true;
    }

    public static boolean unlock(String key) {
        return false;
    }

    public static boolean lockWithRetry(String key, int times) {
        return true;
    }

}
