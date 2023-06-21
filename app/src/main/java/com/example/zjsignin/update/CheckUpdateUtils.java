package com.example.zjsignin.update;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.example.zjsignin.bean.CustomResult;
import com.example.zjsignin.net.RequestCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;


import java.io.File;

public class CheckUpdateUtils {

    private static volatile CheckUpdateUtils singleton = null;

    public static CheckUpdateUtils getInstance() {
        if (singleton == null) {
            synchronized (CheckUpdateUtils.class) {
                if (singleton == null) {
                    singleton = new CheckUpdateUtils();
                }
            }
        }
        return singleton;
    }
    public int getPackageVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.example.zjsignin", 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public void checkUpdate(Context context, @NonNull UpdateAppContract.OnCheckCallback onCheckCallback) {

        OkGo.<CustomResult>get("https://meeting.nbqichen.com/prod-api/common/appVersion/new/3")
                .tag(context)
                .execute(new RequestCallback<CustomResult>() {
                    @Override
                    public void onMySuccess(CustomResult response) {
                        super.onMySuccess(response);
                        String versionCodeStr = response.getVersionName().replace(".", "").replace(".", "").trim();

                        int versionCode = Integer.parseInt(versionCodeStr);

                        if (getPackageVersionCode(context) < versionCode) {
                            onCheckCallback.hasUpdate(response);
                        }
                    }

                    @Override
                    public void onError(Response<CustomResult> response) {
                        super.onError(response);
                        onCheckCallback.onFail();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        onCheckCallback.onFinish();
                    }
                });
    }

    public void downloadUpdateFile(Context context, String path, String name, @NonNull UpdateAppContract.OnDownloadCallback onDownloadCallback) {

        final  String filePath =
                File.separator + "data" + File.separator + "data" + File.separator + "com.example.zjsignin" + File.separator + "cache" + File.separator;

        OkGo.<File>get(path)
                .tag(context)
                .execute(new FileCallback(filePath,name) {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        onDownloadCallback.onDownLoadStart();
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        onDownloadCallback.onSuccess(response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        onDownloadCallback.onProgress(progress);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        onDownloadCallback.onFail("下载失败");
                    }
                });


    }

    public void installApk(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10086);
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //File file = new File(path);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, "com.example.zjsignin.fileProvider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }



}
