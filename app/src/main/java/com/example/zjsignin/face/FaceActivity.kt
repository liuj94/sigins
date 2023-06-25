package com.example.zjsignin.face

import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Camera
import android.media.FaceDetector
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import com.alibaba.fastjson.JSON
import com.bifan.detectlib.FaceDetectTextureView
import com.bumptech.glide.Glide
import com.dylanc.longan.mainThread
import com.dylanc.longan.toast
import com.example.zjsignin.AppManager
import com.example.zjsignin.PageRoutes
import com.example.zjsignin.PageRoutes.Companion.BaseUrl
import com.example.zjsignin.R
import com.example.zjsignin.base.BaseBindingActivity
import com.example.zjsignin.base.BaseViewModel
import com.example.zjsignin.bean.MeetingUserDeData
import com.example.zjsignin.bean.SignUpUser
import com.example.zjsignin.databinding.ActivityFaceBinding
import com.example.zjsignin.net.RequestCallback
import com.hello.scan.ScanCallBack
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import search
import sigin
import upFile
import java.io.*
import java.util.*
import java.util.concurrent.Executors


class FaceActivity : BaseBindingActivity<ActivityFaceBinding, BaseViewModel>(), ScanCallBack {
    var scanTool: ScanTool? = null
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var preFrameList: MutableList<Bitmap> = ArrayList<Bitmap>()

    var isSleep = true
    var isFrist = true
    var ischunScan = false
    override fun initData() {
        val hideNavIntent = Intent()
        hideNavIntent.action = "android.intent.action.systemui"
        hideNavIntent.putExtra("navigation_bar", "dismiss")
        sendBroadcast(hideNavIntent)
        AppManager.getAppManager().addActivity(this)
        scanTool = ScanTool()
        scanTool?.initSerial(this, "/dev/ttyACM0", 115200, this@FaceActivity)
        scanTool?.playSound(true)
        ischunScan = kv.getBoolean("ischunScan", false)
        binding.lockView.setLayout(this, R.layout.slide_layout, kv.getString("deviceImg", "1")) {
            Log.d("initData", "进入隐藏")
            binding.lockView.visibility = View.GONE
            isSleep = false
        };
        binding.lockView.setOnHidden { //进入隐藏
            Log.d("initData", "进入隐藏")
            binding.lockView.visibility = View.GONE
            isSleep = false
        }
        binding.lockView.visibility = View.VISIBLE
        binding.lockView.show()
        var img = kv.getString("deviceImg", "1")
        if (img!!.startsWith("http")) {
            Glide.with(this).load(img).into(binding.tu)
        } else {
            if (img.startsWith("/")) {
                Glide.with(this).load(BaseUrl + img).into(binding.tu)
            } else {
                Glide.with(this).load(BaseUrl + "/" + img).into(binding.tu)
            }
        }
        binding.test.setOnClickListener { startDetect() }
        Glide.with(this@FaceActivity).load(BaseUrl).into(binding.img)
        binding.name.text = ""
        binding.phone.text = ""
        binding.jieguo.visibility = View.GONE
        binding.jgsb.visibility = View.GONE
        binding.jgcg.visibility = View.GONE
        binding.faceDetectView.framePreViewListener =
            object : FaceDetectTextureView.IFramePreViewListener {
                override fun onFrame(preFrame: Bitmap?): Boolean {
                    //每一帧回调
                    //这个这帧preFrame处理了就是进行了回收，返回true
                    //否则返回false，内部进行回收处理
                    return false
                }

                override fun onSleep() {

                    if (!isSleep) {
                        if (!issScan) {
                            binding.lockView.visibility = View.VISIBLE
                            binding.lockView.show()
                            isSleep = true
                        }

                    }

                }

                override fun onFaceFrame(
                    preFrame: Bitmap?,
                    faces: Array<FaceDetector.Face?>
                ): Boolean {
                    if(isFrist){
                        mainThread { binding.lockView.hide()
                            binding.lockView.visibility = View.GONE }
                        isFrist = false
                        isSleep = false
                    }else{
                        if (!isSleep) {
                            //faces是检测出来的人脸参数
                            //检测到人脸的回调,保存人脸图片到本地
                            if (isHasInit) {
                                if (!isSavingPic && !isScanTool) {

                                    if (faces.size > 0) {
                                        preFrame?.let {
                                            preFrameList.add(it)
                                        }
                                        if (preFrameList.size > 2) {

                                            isSavingPic = true
                                            preFrame?.let {
                                                executorService.submit {
                                                    saveFacePicToLocal(preFrame)
                                                    preFrameList.clear()
                                                }
                                            }


                                        }

                                    }
                                }
                            }
                        }
                    }





                    return true
                }
            }

        if(!ischunScan){
            binding.tu.visibility = View.GONE
            mViewModel.isShowLoading.value = true
            val task: TimerTask = object : TimerTask() {
                override fun run() {
                    mainThread {

                        binding.test.performClick()
                        mViewModel.isShowLoading.value = false


                    }

                }
            }

            val timer = Timer()
            timer.schedule(task, 1500)
        }else{
            binding.faceDetectView.visibility = View.GONE
//            binding.scan.visibility = View.GONE
            issScan = true
            binding.tu.visibility = View.VISIBLE
        }

//        binding.scan.setOnClickListener {
//            if (issScan) {
//                issScan = false
//                binding.scan.setText("切换纯扫码")
//                binding.faceDetectView.visibility = View.VISIBLE
//                binding.faceDetectView.startCameraPreview()
//            } else {
//                issScan = true
//                binding.scan.setText("切换人脸模式")
//                binding.faceDetectView.stopCameraPreview()
//                binding.faceDetectView.faceRectView.clearBorder()
//                binding.faceDetectView.visibility = View.GONE
//            }
//
//        }

    }

    var issScan = false
//    private fun showToast(state: String) {
//        var toast = Toast(this)
//        var view: View = LayoutInflater.from(this).inflate(R.layout.tost_sb, null)
//        when (state) {
//            "1" -> {
//                view = LayoutInflater.from(this).inflate(R.layout.tost_cg, null)
//            }
//            "2" -> {
//                view = LayoutInflater.from(this).inflate(R.layout.tost_cf, null)
//            }
//        }
//        toast.setView(view)
//        toast.setDuration(Toast.LENGTH_LONG)
//        toast.setGravity(0, 0, 0);
//        toast.show();
//    }

    private var isSavingPic = false
    private var isScanTool = false
    private var isHasInit = false
    private val executorService = Executors.newSingleThreadExecutor()

    fun startDetect() {
        isHasInit = true
        if (!binding.faceDetectView.isHasInit) {
            //必须是在view可见后进行初始化
            binding.faceDetectView.initView()
            binding.faceDetectView.initCamera()
            binding.faceDetectView.detectConfig.CameraType = Camera.CameraInfo.CAMERA_FACING_FRONT
            binding.faceDetectView.detectConfig.EnableFaceDetect = true
            binding.faceDetectView.detectConfig.MinDetectTime = 500
            binding.faceDetectView.detectConfig.Simple = 0.2f //图片检测时的压缩取样率，0~1，越小检测越流畅
            binding.faceDetectView.detectConfig.MaxDetectTime = 2000 //进入智能休眠检测，以2秒一次的这个速度检测
            binding.faceDetectView.detectConfig.EnableIdleSleepOption = true //启用智能休眠检测机制
            binding.faceDetectView.detectConfig.IdleSleepOptionJudgeTime =
                1000 * 60 //1分钟内没有检测到人脸，进入智能休眠检测
        }
        binding.faceDetectView.startCameraPreview()
    }

    fun endDetect() {
        binding.faceDetectView.stopCameraPreview()
        binding.faceDetectView.faceRectView.clearBorder()
    }

    private fun saveFacePicToLocal(bitmap: Bitmap) {

        val picPath =
            this.externalCacheDir.toString() + File.separator + System.currentTimeMillis() + "face.jpg"
        var fileOutputStream: FileOutputStream? = null
        val facePicFile = File(picPath)
        try {
            facePicFile.createNewFile()
        } catch (e: IOException) {
            isSavingPic = false

            e.printStackTrace()
        }
        try {
            fileOutputStream = FileOutputStream(facePicFile)
        } catch (e: FileNotFoundException) {
            isSavingPic = false
            e.printStackTrace()
        }
        if (fileOutputStream != null) {
            val bos = BufferedOutputStream(fileOutputStream)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            try {
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("FaceActivity", e.toString())
                isSavingPic = false
            }
        } else {
            isSavingPic = false
        }
        bitmap.recycle()
        Log.e("FaceActivity", "保存完成,$picPath")
        try {
            mainThread {
                var faceFile: File = File(picPath)
                var flag: Boolean = faceFile.exists()
                Log.e("FaceActivity", "flag,$flag")
                if (flag) {

                    upImgFile(picPath)
                }
            }

        } catch (e: Exception) {
            isSavingPic = false

        }


    }

    fun upImgFile(picPath: String) {
        mViewModel.isShowLoading.value = true
        upFile(File(picPath), {

//            var urlString = "https://img-blog.csdnimg.cn/20190618124505345.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2EzMTI4NjMwNjM=,size_16,color_FFFFFF,t_70"

            var urlString = it.url + "&meetingId=" + kv.getString("meetingId", "0")
//            detect(urlString,{
//                var urlString = "https://meeting.nbqichen.com:20882/profile/upload/2023/03/09/ac479084-cff7-40a1-a7fe-480e6c8f4523.jpg"
            search(urlString, {

                it.result?.let { result ->

                    if (!result.user_list.isNullOrEmpty()) {
                        if (result.user_list.size > 0) {
                            getUserData(result.user_list[0].user_id)
                        }

                    }


                }

            }, {
//                setFinishData("-5000", it)
                toast("人脸搜索失败请出示二维码")
                isSavingPic = false
                mViewModel.isShowLoading.value = false
            }, {
                mViewModel.isShowLoading.value = false
            })
//            getUserData("154")
        }, {
            toast("图片上传失败")
            isSavingPic = false
            mViewModel.isShowLoading.value = false
        }, {
            isSavingPic = false
            try {
                mainThread {
                    File(picPath).delete()
                }

            } catch (e: Exception) {


            }

        })

    }

    var mRingPlayer: MediaPlayer? = null

    private fun sigin(userMeetingId: String) {

        var params = HashMap<String, String>()
        params["codeNo"] = "" + kv.getString("codeNo", "")
        params["userMeetingId"] = userMeetingId//用户参与会议id


        sigin(JSON.toJSONString(params), { success ->
            setFinishData(success)
        }, {
//            setFinishData("0", it)
            setFinishData("0", "签到失败")
        }, {
            isSavingPic = false
            mViewModel.isShowLoading.value = true
        })
    }

    override fun onDestroy() {
        AppManager.getAppManager().removeActivity(this)
        if(!ischunScan){
        endDetect()}
        scanTool?.pauseReceiveData()
        scanTool?.release()
        super.onDestroy()

    }

    override fun onScanCallBack(data: String?) {
//        if (!isSleep) {
            if (!isScanTool && !isSavingPic) {
                isScanTool = true
                if (AppManager.getAppManager().activityClassIsLive(FaceActivity::class.java)) {
                    if (data.isNullOrEmpty()) {
                        isScanTool = false
                        binding.name.text = ""
                        binding.phone.text = ""
                        setFinishData("0", "签到失败")
                        return
                    }
                    mViewModel.isShowLoading.value = true

                    getmeetingCode(data)

                } else {
                    isScanTool = false
                }
            }
//        }

    }

    private fun getmeetingCode(meetingCode: String) {
        mViewModel.isShowLoading.value = true
        OkGo.get<SignUpUser>(PageRoutes.Api_meetingCode + meetingCode)
            .tag(PageRoutes.Api_meetingCode + meetingCode)
            .headers("Authorization", kv.getString("token", ""))
            .execute(object : RequestCallback<SignUpUser>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: SignUpUser) {
                    super.onMySuccess(data)
                    try {
                        var signUpUser = data

                        if (!signUpUser.meetingId.equals(kv.getString("meetingId", "0"))) {
                            binding.name.text = ""
                            binding.phone.text = ""
                            setFinishData("0", "签到失败")
                            return
                        }


                        if (signUpUser.id.isNullOrEmpty()) {
                            binding.name.text = ""
                            binding.phone.text = ""
                            setFinishData("0", "签到失败")
                            return
                        }
                        getUserData(signUpUser.id)

                    } catch (e: Exception) {
                        binding.name.text = ""
                        binding.phone.text = ""
                        setFinishData("0", "签到失败")
                        isScanTool = false

                    }


                }

                override fun onError(response: Response<SignUpUser>) {
                    super.onError(response)
                    mViewModel.isShowLoading.value = false
                    binding.name.text = ""
                    binding.phone.text = ""
                    setFinishData("0", "签到失败")
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }

    fun setFinishData(state: String, msg: String? = "签到失败") {
        if (isSleep) {
            ToastUtils.toast(this,msg)
        }
        if (mRingPlayer != null) {
            mRingPlayer?.stop();
            mRingPlayer?.release();
            mRingPlayer = null;
        }
        binding.jieguo.visibility = View.VISIBLE
        binding.stateIv.setImageResource(R.mipmap.qdcwicon)
        binding.stateTv.setText(msg)
        binding.jgsb.visibility = View.GONE
        binding.jgcg.visibility = View.VISIBLE
        when (state) {

            "-5000" -> {
                if (kv.getString("shockStatus", "1").equals("1")) {
                    //语音播报
                    mRingPlayer = MediaPlayer.create(this, R.raw.cwqx);
                    mRingPlayer?.start();
                }
                binding.jgsb.visibility = View.VISIBLE
                binding.jgcg.visibility = View.GONE
                binding.sbtv.setText("查无信息")
                binding.stateIv.setImageResource(R.mipmap.qdcwicon)
                binding.stateTv.setText(msg)

            }
            "1" -> {
                if (kv.getString("shockStatus", "1").equals("1")) {
                    mRingPlayer = MediaPlayer.create(this, R.raw.cg);
                    mRingPlayer?.start();
                }
                binding.jgsb.visibility = View.GONE
                binding.jgcg.visibility = View.VISIBLE
                binding.stateIv.setImageResource(R.mipmap.qdcgicon)
                binding.stateTv.setText("签到成功")

            }
            "2" -> {
                if (kv.getString("shockStatus", "1").equals("1")) {
                    //语音播报
                    mRingPlayer = MediaPlayer.create(this, R.raw.cf);
                    mRingPlayer?.start();
                }
                binding.jgsb.visibility = View.GONE
                binding.jgcg.visibility = View.VISIBLE
                binding.stateIv.setImageResource(R.mipmap.qdjgicon)
                binding.stateTv.setText("重复签到")

            }
            "0" -> {
                binding.jgsb.visibility = View.GONE
                binding.jgcg.visibility = View.VISIBLE
                binding.stateIv.setImageResource(R.mipmap.qdcwicon)
                binding.stateTv.setText(msg)
                if (kv.getString("shockStatus", "1").equals("1")) {
                    if (msg != null) {
                        if (msg.equals("未找到该用户的信息")) {
                            mRingPlayer = MediaPlayer.create(this, R.raw.cwqx);
                            mRingPlayer?.start();
                        } else if (msg.contains("无权限")) {
                            mRingPlayer = MediaPlayer.create(this, R.raw.wdqx);
                            mRingPlayer?.start();
                        } else {
                            mRingPlayer = MediaPlayer.create(this, R.raw.qdsb);
                            mRingPlayer?.start();
                        }
                    } else {
                        mRingPlayer = MediaPlayer.create(this, R.raw.qdsb);
                        mRingPlayer?.start();
                    }


                }

            }
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                mainThread {
                    isSavingPic = false
                    isScanTool = false
                    mViewModel.isShowLoading.value = false
                    binding.jieguo.visibility = View.GONE
                    binding.jgsb.visibility = View.GONE
                    binding.jgcg.visibility = View.GONE
                }

            }
        }, 3000)


    }

    private fun getUserData(id: String) {

        OkGo.get<MeetingUserDeData>(PageRoutes.Api_user_data + id)
            .tag(PageRoutes.Api_user_data + id)

            .execute(object : RequestCallback<MeetingUserDeData>() {
                override fun onSuccessNullData() {
                    super.onSuccessNullData()

                }

                override fun onMySuccess(data: MeetingUserDeData) {
                    super.onMySuccess(data)
                    Glide.with(this@FaceActivity).load(BaseUrl + data.avatar).into(binding.img)
                    binding.name.text = "" + data.name
                    binding.phone.text = "" + data.corporateName

                }

                override fun onError(response: Response<MeetingUserDeData>) {
                    Glide.with(this@FaceActivity).load(BaseUrl).into(binding.img)
                    binding.name.text = ""
                    binding.phone.text = ""

                }

                override fun onFinish() {
                    super.onFinish()
                    sigin(id)
                }


            })
    }

    override fun onResume() {
        super.onResume()

    }
    /*
       *  隐藏导航栏
       * */
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            val gameView = this.window.decorView
//            gameView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//        }
//    }
}