package com.example.zjsignin

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dylanc.longan.activity
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.zjsignin.base.BaseBindingActivity
import com.example.zjsignin.base.BaseViewModel
import com.example.zjsignin.base.StatusBarUtil
import com.example.zjsignin.bean.CustomUpdateParser
import com.example.zjsignin.bean.CustomUpdatePrompter
import com.example.zjsignin.bean.ZjData
import com.example.zjsignin.databinding.ActLoginBinding
import com.example.zjsignin.face.FaceActivity
import com.example.zjsignin.face.ToastUtils
import com.example.zjsignin.net.RequestCallback
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xutil.app.PathUtils


class LoginActivity : BaseBindingActivity<ActLoginBinding, BaseViewModel>() {

    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true)
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val hasInstallPermission: Boolean =
                this.getPackageManager().canRequestPackageInstalls()
            if (!hasInstallPermission) {
                AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("暂无安装未知来源权限")
                    .setPositiveButton("去开启", DialogInterface.OnClickListener { dialog, which ->
                        startInstallPermissionSettingActivity(this)
                    })
                    .setNegativeButton("取消",  DialogInterface.OnClickListener { dialog, which ->
                        toast("暂无安装未知来源权限，无法更新版本")
                    })
                    .setCancelable(false)
                    .create()
                    .show()



            }else{
                XUpdate.newBuild(this)
                    .apkCacheDir(PathUtils.getAppExtCachePath()) //设置下载缓存的根目录
                    .updateUrl(PageRoutes.Api_appVersion)
                    .updateParser(CustomUpdateParser(this))
                    .updatePrompter( CustomUpdatePrompter(this))
                    .update();
            }
        }else{
            XUpdate.newBuild(this)
                .apkCacheDir(PathUtils.getAppExtCachePath()) //设置下载缓存的根目录
                .updateUrl(PageRoutes.Api_appVersion)
                .updateParser(CustomUpdateParser(this))
                .updatePrompter( CustomUpdatePrompter(this))
                .update();
        }

    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */

    fun startInstallPermissionSettingActivity(context: Context?) {
        if (context == null) {
            return
        }
        val intent = Intent()
        //获取当前apk包URI，并设置到intent中（这一步设置，可让“未知应用权限设置界面”只显示当前应用的设置项）
        val packageURI = Uri.parse("package:" + context.packageName)
        intent.data = packageURI
        //设置不同版本跳转未知应用的动作
        if (Build.VERSION.SDK_INT >= 26) {
            //intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
            intent.action = Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
        } else {
            intent.action = Settings.ACTION_SECURITY_SETTINGS
        }
        //Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        (context as Activity).startActivityForResult(
            intent,
            10086
        )

    }
    override fun initData() {
       var nodeNoLogin = kv.getString("nodeNoLogin","")
        if(!nodeNoLogin.isNullOrEmpty()){
            binding.userName.setText(nodeNoLogin)
        }

        binding.tuichu.setOnClickListener {
            MaterialDialog(this@LoginActivity).show {
                customView(    //自定义弹窗
                    viewRes = R.layout.tc_user_add,//自定义文件
                    dialogWrapContent = true,    //让自定义宽度生效
                    scrollable = true,            //让自定义宽高生效
                    noVerticalPadding = true    //让自定义高度生效
                ).apply {
                    findViewById<TextView>(R.id.add).setOnClickListener {
                        Log.d("password",findViewById<EditText>(R.id.password).text.toString().trim())
                        if(findViewById<EditText>(R.id.password).text.toString().trim().equals("123456")){
                            finish()
                        } else{
                            ToastUtils.toast(this@LoginActivity,"密码错误")
                        }
                    }
                    findViewById<TextView>(R.id.qx).setOnClickListener {
                        this.dismiss()
                    }
                }

                cancelOnTouchOutside(false)    //点击外部不消失
            }
        }
        binding.login.setOnClickListener {

            mViewModel.isShowLoading.value = true
            val params = HashMap<String, String>()
            params["nodeNo"] = binding.userName.text.toString().trim()
            OkGo.get<ZjData>(PageRoutes.Api_login + binding.userName.text.toString().trim())
                .tag(PageRoutes.Api_login)
                .execute(object : RequestCallback<ZjData>() {
                    override fun onSuccessNullData() {
                        super.onSuccessNullData()
                        toast("该设备码无效，请重新输入")
                    }

                    override fun onMySuccess(data: ZjData) {
                        super.onMySuccess(data)
                        kv.putString("nodeNoLogin",binding.userName.text.toString().trim())
                        kv.putString("codeNo", data.codeNo)
                        kv.putString("deviceImg", data.deviceImg)
                        kv.putString("shockStatus", data.speechStatus)
                        kv.putString("meetingId", data.meetingId)
                        //faceDetect 开启人脸 1开启2关闭
                        if (data.faceDetect == 1) {
                            kv.putBoolean("ischunScan", true)
                            activity?.let {
                                XXPermissions.with(activity)
                                    .permission(Permission.CAMERA)
                                    .permission(Permission.READ_MEDIA_IMAGES)
                                    .request(object : OnPermissionCallback {

                                        override fun onGranted(
                                            permissions: MutableList<String>,
                                            all: Boolean
                                        ) {
                                            if (all) {
                                                startActivity<FaceActivity>()

                                            } else {
                                                toast("获取权限失败")
                                            }

                                        }

                                        override fun onDenied(
                                            permissions: MutableList<String>,
                                            never: Boolean
                                        ) {


                                        }
                                    })


                            }
                        } else {
                            kv.putBoolean("ischunScan", false)
                            startActivity<FaceActivity>()
                        }



                    }

                    override fun onError(response: Response<ZjData>) {
                        super.onError(response)
                        toast("登录失败，请稍候再试")

                    }

                    override fun onFinish() {
                        super.onFinish()
                        mViewModel.isShowLoading.value = false
                    }


                })
        }

    }


    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {

            System.exit(0);
        }
    }

}


