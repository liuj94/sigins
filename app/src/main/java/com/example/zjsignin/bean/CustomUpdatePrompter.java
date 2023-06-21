package com.example.zjsignin.bean;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.xuexiang.xupdate.entity.PromptEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdatePrompter;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xutil.display.HProgressDialogUtils;

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
                        updateProxy.startDownload(updateEntity, new OnFileDownloadListener() {
                            @Override
                            public void onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(mContext, "下载进度", false);
                            }

                            @Override
                            public void onProgress(float progress, long total) {
                                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                            }

                            @Override
                            public boolean onCompleted(File file) {
                                dialog.dismiss();
                                HProgressDialogUtils.cancel();
                                Intent installIntent = new Intent("android.intent.action.application");
                                // 需要静默安装的apk路径（必须传入的参数）
                                installIntent.putExtra("quiet_install", file.getPath());
                                // 如果静默安装完成需要自启动应用，则传入应用的包名（非必须传入的参数）
                                installIntent.putExtra("packName", "com.example.zjsignin");
                                // 如果静默安装完成需要自动删除安装包，则传入参数true；传入参数false则不删除安装包（非必须传入的参数）
                                installIntent.putExtra("deletePack", false);
                                mContext.sendBroadcast(installIntent);
                                return true;
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                HProgressDialogUtils.cancel();
                            }
                        });

                    }
                })
                .setNegativeButton("暂不升级", null)
                .setCancelable(false)
                .create()
                .show();
    }
}

