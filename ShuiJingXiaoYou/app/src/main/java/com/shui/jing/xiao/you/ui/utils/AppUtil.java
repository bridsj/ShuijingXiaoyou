package com.shui.jing.xiao.you.ui.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;

public class AppUtil {

    public static String getAppName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static String getAppName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();

        } catch (NameNotFoundException e) {
        }

        return null;
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static Drawable getAppIcon(Context context) {
        return context.getApplicationInfo().loadIcon(context.getPackageManager());
    }

    public static Drawable getAppIcon(Context context, String packageName) {

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.applicationInfo.loadIcon(pm);

        } catch (NameNotFoundException e) {
        }

        return null;
    }

    public static Drawable getActivityIcon(Context context, String packageName) {

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(packageName);
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
            if (activities.size() > 0) {
                return activities.get(0).loadIcon(pm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static int getPid(Context context, String processName) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            if (processName.equals(appProcess.processName)) {
                return appProcess.pid;
            }
        }
        return 0;
    }

    public static String getAppMetaData(Context context, String metaData) {
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object data = ai.metaData.get(metaData);
            if (data instanceof String) {
                return (String) data;
            } else if (data instanceof Integer) {
                return String.valueOf(data);
            }

        } catch (NameNotFoundException e) {
        }
        return "";
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);

        } catch (NameNotFoundException e) {
            packageInfo = null;
        }

        return packageInfo != null;
    }

    public static boolean isRunningForeground(Context context) {
        try {
            String currentPackageName = getForegroundPackageName(context);
            if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getForegroundPackageName(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
                if (runningAppProcesses != null) {
                    return runningAppProcesses.get(0).pkgList[0];
                }
            } else {
                List<ActivityManager.RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
                if (runningTaskInfos.size() > 0) {
                    return runningTaskInfos.get(0).topActivity.getPackageName();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public static Intent getMainIntent(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }


}
