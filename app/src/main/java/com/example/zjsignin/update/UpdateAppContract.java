package com.example.zjsignin.update;


import com.example.zjsignin.bean.CustomResult;
import com.lzy.okgo.model.Progress;

import java.io.File;

public class UpdateAppContract {


    public interface OnCheckCallback {

        void isLatest(); //最新版本

        void hasUpdate(CustomResult info);

        void onFail();

        void onFinish();

    }

    public interface OnDownloadCallback {

        void onDownLoadStart();

        void onProgress(Progress progress);

        void onFail(String msg);

        void onSuccess(File path);
    }

    public interface OnCheckWebCallback {
        void onFinish();
    }

}
