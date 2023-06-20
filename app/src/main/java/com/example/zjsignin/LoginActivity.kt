package com.example.zjsignin

import android.graphics.Color
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dylanc.longan.activity
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.zjsignin.base.BaseBindingActivity
import com.example.zjsignin.base.BaseViewModel
import com.example.zjsignin.base.StatusBarUtil
import com.example.zjsignin.bean.CustomUpdateParser
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


class LoginActivity : BaseBindingActivity<ActLoginBinding, BaseViewModel>() {

    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true)
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    override fun onResume() {
        super.onResume()
        XUpdate.newBuild(this)
            .updateUrl(PageRoutes.Api_appVersion)
//            .promptThemeColor(ResUtils.getColor(R.color.text4c93fd))
//            .promptButtonTextColor(Color.WHITE)
//            .promptTopResId(R.mipmap.bg_update_top)
            .updateParser(CustomUpdateParser(this))
            .update();
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


