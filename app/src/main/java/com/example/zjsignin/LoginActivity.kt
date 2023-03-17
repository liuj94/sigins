package com.example.zjsignin

import android.graphics.Color
import com.dylanc.longan.activity
import com.dylanc.longan.startActivity
import com.dylanc.longan.toast
import com.example.zjsignin.base.BaseBindingActivity
import com.example.zjsignin.base.BaseViewModel
import com.example.zjsignin.base.StatusBarUtil
import com.example.zjsignin.bean.ZjData
import com.example.zjsignin.databinding.ActLoginBinding
import com.example.zjsignin.face.FaceActivity
import com.example.zjsignin.net.RequestCallback
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response


class LoginActivity : BaseBindingActivity<ActLoginBinding, BaseViewModel>()  {

    override fun initTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, Color.TRANSPARENT)
        //设置状态栏字体颜色
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true)
    }

    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun initData() {


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
//                        kv.putString("ZjData",data.toString())
                        kv.putString("codeNo", data.codeNo)
                        kv.putString("shockStatus", data.speechStatus)
                        activity?.let {
                            XXPermissions.with(activity)
                                .permission(Permission.CAMERA)
                                .permission(Permission.READ_MEDIA_IMAGES)
                                .request(object : OnPermissionCallback {

                                    override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                                        if (all) {
                                            startActivity<FaceActivity>()

                                        } else {
                                            toast("获取手机权限失败")
                                        }

                                    }

                                    override fun onDenied(permissions: MutableList<String>, never: Boolean) {


                                    }
                                })


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


