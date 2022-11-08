package qx.app.study;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyDownloadManager {
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }

    }

    private static final String TAG = "MyDownloadManager";
    private File downloadDir; // 文件保存路径
    private static MyDownloadManager instance; // 单例

    // 单线程任务队列
    public static Executor executor;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "MyDownloadManager #" + mCount.getAndIncrement());
        }
    };
    //线程池
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(128);
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(1, 1, 1,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);


    public MyDownloadManager() {
        // 初始化下载路径
        downloadDir = new File("/sdcard/");
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        executor = new SerialExecutor();
    }

    /**
     * 顺序执行下载任务
     */
    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(() -> {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static MyDownloadManager getInstance() {
        if (instance == null) {
            instance = new MyDownloadManager();
        }
        return instance;
    }

    /**
     * 添加下载任务
     *
     * @param path
     */
    public void addDownloadTask(final String path,Activity activity) {
        Log.e(TAG, "进入run : " + path);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "进入run");
                download(path,activity);
            }
        });
    }

    /**
     * 下载文件
     *
     * @param path
     */
    private void download(String path,Activity activity) {
        verifyStoragePermissions(activity);
        String fileName = MD5(path);

        File savePath = new File(downloadDir, fileName); // 下载文件路径
        //File finallyPath = new File(downloadDir, fileName + ".png"); // 下载完成后加入
        if (savePath.exists()) {
            // 文件存在则已下载
            Log.e(TAG, "file is existed");
            return;
        }
        // 如果是Wifi则开始下载
        if (savePath.exists() && savePath.delete()) { // 如果之前存在文件，证明没有下载完成，删掉重新创建
            savePath = new File(downloadDir, fileName);
            Log.e(TAG, "download 111111");
        }
        Log.e(TAG, "download start");
        try {
            byte[] bs = new byte[1024];
            int len;
            URL url = new URL(path);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(savePath, true);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            is.close();
            os.close();
//            MediaStore.Images.Media.insertImage(getContentResolver(),bmp,fileName, null);
//            bmp.recycle();
//            bmp = null;
//            LocalBroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(downloadFile.getAbsolutePath())));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 添加删除任务
     *
     * @param path
     */
    public void addDeleteTask(final String path) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                delete(path);
            }
        });
    }

    /**
     * 删除本地文件
     *
     * @param path
     */
    private void delete(String path) {
        String fileName = MD5(path);
        File savePath = new File(downloadDir, fileName + ".png");
        Log.i(TAG, savePath.getPath());
        if (savePath.exists()) {
            if (savePath.delete()) {
                Log.i(TAG, "file is deleted");
            }
        }
    }

    /**
     * 返回下载路径
     *
     * @return
     */
    public File getDownloadDir() {
        return downloadDir;
    }

    public String getfileName(String path) {
        String f = MD5(path);
        return f;
    }

    public static String MD5(String path) {
        // URL url = new URL(path);
        //String url = path;
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //final long startTime = System.currentTimeMillis();
        //String filename=url.substring(url.lastIndexOf("/") + 1);

        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = df.format(new Date());
        //命名图片名字
        String filename = time + ".jpg";
        return filename;
    }

    /**
     * 根据 Android Q 区分地址
     *
     * @param context
     * @return
     */
    public static String getPath(Context context) {
        // equalsIgnoreCase() 忽略大小写
        String fileName = "";
        if (Build.VERSION.SDK_INT >= 29) {
            fileName = context.getExternalFilesDir("").getAbsolutePath() + "/current/";
        } else {
            if ("Xiaomi".equalsIgnoreCase(Build.BRAND)) { // 小米手机
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            } else if ("HUAWEI".equalsIgnoreCase(Build.BRAND)) {
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            } else if ("HONOR".equalsIgnoreCase(Build.BRAND)) {
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            } else if ("OPPO".equalsIgnoreCase(Build.BRAND)) {
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            } else if ("vivo".equalsIgnoreCase(Build.BRAND)) {
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            } else if ("samsung".equalsIgnoreCase(Build.BRAND)) {
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            } else {
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";
            }
        }
        File file = new File(fileName);
        if (file.mkdirs()) {
            return fileName;
        }
        return fileName;
    }
    /**
     * 判断android Q  (10 ) 版本
     *
     * @return
     */
    public static boolean isAdndroidQ() {
        return Build.VERSION.SDK_INT >= 29;
    }
    /**
     * 复制文件
     *
     * @param oldPathName
     * @param newPathName
     * @return
     */
    public static boolean copyFile(String oldPathName, String newPathName) {
        try {
            File oldFile = new File(oldPathName);
            if (!oldFile.exists()) {
                return false;
            } else if (!oldFile.isFile()) {
                return false;
            } else if (!oldFile.canRead()) {
                return false;
            }

            FileInputStream fileInputStream = new FileInputStream(oldPathName);
            FileOutputStream fileOutputStream = new FileOutputStream(newPathName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入相册 部分机型适配(区分手机系统版本 Android Q)
     *
     * @param context
     * @param filePath
     * @return
     */
    public static boolean insertMediaPic(Context context, String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;
        File file = new File(filePath);
        //判断android Q  (10 ) 版本
        if (isAdndroidQ()) {
            if (file == null || !file.exists()) {
                return false;
            } else {
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis() + "");
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
            return true;
        }

    }
}
