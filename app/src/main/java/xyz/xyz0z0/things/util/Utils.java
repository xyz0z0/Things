package xyz.xyz0z0.things.util;

import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class Utils {

    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0;
    }

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static <T> T checkNotNull(T reference) {
        if(reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
}
