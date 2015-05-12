package com.shui.jing.xiao.you.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.ViewConfiguration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class DeviceUtil {

    public static boolean isMiui(Context context) {

        if ("xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            return true;
        }

        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        i.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
        if (isIntentAvailable(context, i)) {
            return true;
        }
        //双重判断
        boolean isMiUi = "miui".equalsIgnoreCase(Build.ID);

        if ("xiaomi".equalsIgnoreCase(Build.BRAND)) {
            isMiUi = true;
        }
        if (Build.MODEL != null) {
            String str = Build.MODEL.toLowerCase();
            if (str.contains("xiaomi")) {
                isMiUi = true;
            }
            if (str.contains("miui")) {
                isMiUi = true;
            }
        }
        return isMiUi;
    }

    public static boolean isSumsungV4_4_4() {
        if (isSamsung()) {
            if (Build.VERSION.RELEASE.equals("4.4.4")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSumsungV5() {
        if (isSamsung()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSumsungCorePrime() {
        if (isSamsung()) {
            if (Build.DISPLAY.contains("G3608ZMU1AOA4")) {
                return true;
            }
        }
        return false;
    }


    public static boolean isFlyme4() {
        return Build.DISPLAY.startsWith("Flyme OS 4");
    }

    public static boolean isOnePlusLOLLIPOP() {
        return Build.BRAND.equals("ONEPLUS") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    private static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    public static boolean isSamsung() {
        if ("samsung".equalsIgnoreCase(Build.BRAND) || "samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
            return true;
        }

        return false;
    }

    public static boolean isLG() {
        if ("lge".equalsIgnoreCase(Build.BRAND) || "lge".equalsIgnoreCase(Build.MANUFACTURER)) {
            if (Build.MODEL != null) {
                String str = Build.MODEL.toLowerCase();
                if (str.contains("lg")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isMeizu() {
        String brand = Build.BRAND;
        if (brand == null) {
            return false;
        }

        return brand.toLowerCase(Locale.ENGLISH).indexOf("meizu") > -1;
    }

    public static boolean hasSmartBar() {
        if (!isMeizu()) {
            return false;
        }

        try {
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return true;
    }

    @SuppressLint("NewApi")
    public static boolean hasVirtualButtons(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolean hasPermanentMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasVirtualButtons = !hasPermanentMenuKey;
            return hasVirtualButtons;
        } else {
            return false;
        }
    }

    public static String getDeviceId(Context context) {
        String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (TextUtils.isEmpty(deviceId) || "000000000000000".equals(deviceId)) {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            deviceId = wm.getConnectionInfo().getMacAddress();
        }

        return deviceId;
    }

    public static long getTotalMemory() {
        String memInfoFile = "/proc/meminfo";// 系统内存信息文件

        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(memInfoFile);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            String totalMemInfo = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            String[] memInfos = totalMemInfo.split("\\s+");

            initial_memory = Integer.valueOf(memInfos[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

//			Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
        } catch (Exception e) {
        }

        return initial_memory;
    }
}
