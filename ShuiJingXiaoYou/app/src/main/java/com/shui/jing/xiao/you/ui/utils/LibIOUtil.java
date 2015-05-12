package com.shui.jing.xiao.you.ui.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class LibIOUtil {

    public static final String FS = File.separator;

    // SD卡缓存
    private static final String DNS = "dns";
    private static final String CACHE = "cache";
    private static final String IMAGE = "image";
    private static final String GIF = "gif";
    private static final String DOWNLOAD = "download";
    private static final String LOG = "log";

    // 拍照缓存
    private static final String UPLOAD = "upload";
    private static final String UPLOAD_CAMERA_FILE = "camera_file.png";
    private static final String UPLOAD_SELECTED_FILE = "select_file.png";
    private static final String UPLOAD_COMPRESS_FILE = "compress_file.png";
    private static final String UPLOAD_ZOOM_FILE = "zoom_file.png";

    public static String unGzip(HttpEntity compressedEntity) throws IOException {
        if (compressedEntity == null)
            return "";
        InputStream compressed = compressedEntity.getContent();
        InputStream rawData = new GZIPInputStream(compressed);
        String results = LibIOUtil.convertStreamToJson(rawData);
        if (results != null) {
            if (results.trim().startsWith("{") && results.trim().endsWith("}")) {
                return results;
            } else if (results.trim().startsWith("[") && results.trim().endsWith("]")) {
                return results;
            } else {
                try {
                    return LibIOUtil.convertStreamToJson(compressedEntity.getContent());
                } catch (IllegalStateException e) {
                    return results;
                }
            }
        }
        return results;
    }

    public static String convertStreamToJson(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8 * 1024);
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e("convertStreamToString", "convertStreamToString error");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }

    public static String convertStreamToStr(InputStream is) {
        String result = "";
        byte[] response = null;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] ch = new byte[256];
        int len;
        try {
            while ((len = is.read(ch)) >= 0) {
                buf.write(ch, 0, len);
            }
            response = buf.toByteArray();
            result = new String(response, HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buf != null) {
                try {
                    buf.flush();
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return result;

    }

    public static boolean getExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return false;
        } else {
            return false;
        }
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean isParentDirExist(String filePath) {
        File file = new File(filePath);
        return file.getParentFile().exists();
    }

    public static boolean isDirExist(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean makeDirs(String path) {
        File file = new File(path);
        return file.mkdirs();
    }

    public static boolean makeParentDirs(String filePath) {
        File file = new File(filePath);
        return file.getParentFile().mkdirs();
    }

    public static String getBaseLocalLocation(Context context) {
        boolean isSDCanRead = LibIOUtil.getExternalStorageState();
        String baseLocation = "";
        if (isSDCanRead) {
            baseLocation = LibIOUtil.getSDCardPath();
        } else {
            baseLocation = context.getFilesDir().getAbsolutePath();
        }
        return baseLocation;
    }

    public static String getCacheLocation(Context context) {
        boolean isSDCanRead = LibIOUtil.getExternalStorageState();
        String baseLocation = "";
        if (isSDCanRead) {
            baseLocation = LibIOUtil.getSDCardPath();
        } else {
            baseLocation = context.getCacheDir().getAbsolutePath();
        }
        return baseLocation;
    }

    // 获取文件大小
    @SuppressWarnings("resource")
    public static int getFileSize(File file) {
        FileInputStream fis = null;
        int size = 0;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    // 添加到系统相册
    public static boolean addToSysGallery(Context context, String fileAbsolutePath, String fileName) {
        try {
            if (context == null)
                return false;
            MediaStore.Images.Media.insertImage(context.getContentResolver(), fileAbsolutePath, fileName, fileName);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static String createFileDir(String path) {
        if (!isDirExist(path)) {
            boolean isMakeSucc = makeDirs(path);
            if (!isMakeSucc) {
                return null;
            }
        }
        return path;
    }

    // 移动文件
    public static boolean moveFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        boolean success = srcFile.renameTo(new File(destPath));
        return success;
    }

    // 删除文件夹
    public static boolean deleteFolder(File folder) {
        String childs[] = folder.list();
        if (childs == null || childs.length <= 0) {
            return folder.delete();
        }
        for (int i = 0; i < childs.length; i++) {
            String childName = childs[i];
            String childPath = folder.getPath() + File.separator + childName;
            File filePath = new File(childPath);
            if (filePath.exists() && filePath.isFile()) {
                filePath.delete();
            } else if (filePath.exists() && filePath.isDirectory()) {
                deleteFolder(filePath);
            }
        }

        return folder.delete();
    }
}
