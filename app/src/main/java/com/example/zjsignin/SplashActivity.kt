package com.example.zjsignin

import com.example.zjsignin.base.BaseBindingActivity
import com.example.zjsignin.base.BaseViewModel
import com.example.zjsignin.databinding.ActSplashBinding

class SplashActivity : BaseBindingActivity<ActSplashBinding, BaseViewModel>() {


    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java


    override fun initData() {

        com.dylanc.longan.startActivity<LoginActivity>()
        finish()
    }

}