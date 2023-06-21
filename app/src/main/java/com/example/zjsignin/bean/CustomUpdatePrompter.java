package com.example.zjsignin.bean;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.xuexiang.xupdate.entity.PromptEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdatePrompter;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.io.File;

public class CustomUpdatePrompter implements IUpdatePrompter {

    private Context mContext;

    public CustomUpdatePrompter(Context context) {
        mContext = context;
    }

    @Override
    public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity) {
        showUpdatePrompt(updateEntity, updateProxy);
    }

    /**
     * 显示自定义提示
     *
     * @param updateEntity
     * @param updateProxy
     */
    private void showUpdatePrompt(final @NonNull UpdateEntity updateEntity, final @NonNull IUpdateProxy updateProxy) {
        String updateInfo = UpdateUtils.getDisplayUpdateInfo(mContext, updateEntity);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(String.format("是否升级到%s版本？", updateEntity.getVersionName()))
                .setMessage(updateInfo)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        updateProxy.startDownload(updateEntity, new OnFileDownloadListener() {
//                            @Override
//                            public void onStart() {
//                                HProgressDialogUtils.showHorizontalProgressDialog(mContext, "下载进度", false);
//                            }
//
//                            @Override
//                            public void onProgress(float progress, long total) {
//                                HProgressDialogUtils.setProgress(Math.round(progress * 100));
//                            }
//
//                            @Override
//                            public boolean onCompleted(File file) {
//                                dialog.dismiss();
//                                HProgressDialogUtils.cancel();
//                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                                builder.setTitle("安装方式")
//                                        .setMessage(updateInfo)
//                                        .setPositiveButton("方式一", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Uri fileUri = null;
//                                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                fileUri = Uri.fromFile(file);
//                                                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
//                                                mContext.startActivity(intent);
//
//                                            }
//                                        })
//                                        .setNegativeButton("方式二", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Uri fileUri = null;
//                                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//7.0私有目录访问限制适配
//                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                                fileUri = getUriForFile(mContext, "com.example.zjsignin.fileProvider", file);
//                                                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
//                                                mContext.startActivity(intent);
//
//                                            }
//                                        })
//                                        .setCancelable(false)
//                                        .create()
//                                        .show();
////                                installApk(file,mContext);
//                                return true;
//                            }
//
//                            @Override
//                            public void onError(Throwable throwable) {
//                                HProgressDialogUtils.cancel();
//                            }
//                        });
//                        CheckUpdateUtils.getInstance().downloadUpdateFile
                    }
                })
                .setNegativeButton("暂不升级", null)
                .setCancelable(false)
                .create()
                .show();
    }

    //安装应用
    private void installApk(File apk, Context context) {

        Uri fileUri = null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//7.0私有目录访问限制适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fileUri = getUriForFile(context, "com.example.zjsignin.fileProvider", apk);
        } else {
            fileUri = Uri.fromFile(apk);
        }
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}

