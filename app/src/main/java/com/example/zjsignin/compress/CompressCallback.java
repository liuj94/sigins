package com.example.zjsignin.compress;

import java.io.File;

public class CompressCallback {

    public interface CompressSuccess {
        void onSuccess(File file);
    }

    public interface CompressError {
        void onError(Throwable throwable);
    }

}


