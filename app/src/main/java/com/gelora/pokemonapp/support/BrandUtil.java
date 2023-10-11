package com.gelora.pokemonapp.support;

import android.os.Build;

import androidx.annotation.NonNull;

final class BrandUtil {

    public enum BRAND {
        SAMSUNG,
        HTC,
        SONY,
        LG,
        XIAOMI,
        HUAWEI,
        MEIZU,
        MOTOROLA,
        ZTE,
        COOLPAD,
        LENOVO,
        OPPO,
        VIVO,
        GIONEE,
        SMARTISAN
    }

    private BrandUtil() {

    }

    public static boolean checkBrand(@NonNull BRAND brand) {
        if (Build.MANUFACTURER != null && Build.MANUFACTURER.toUpperCase().contains(brand.toString())) {
            return true;
        } else if (Build.BRAND != null && Build.BRAND.toUpperCase().contains(brand.toString())) {
            return true;
        } else if (Build.MODEL != null && Build.MODEL.toUpperCase().contains(brand.toString())) {
            return true;
        } else {
            return false;
        }
    }

}
