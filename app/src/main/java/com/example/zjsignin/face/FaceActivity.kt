package com.example.zjsignin.face

import android.graphics.Bitmap
import android.hardware.Camera
import android.media.FaceDetector
import android.media.MediaPlayer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.bifan.detectlib.FaceDetectTextureView
import com.dylanc.longan.mainThread
import com.dylanc.longan.toast
import com.example.zjsignin.LiveDataBus
import com.example.zjsignin.R
import com.example.zjsignin.base.BaseBindingActivity
import com.example.zjsignin.base.BaseViewModel
import com.example.zjsignin.bean.SignUpUser
import com.example.zjsignin.databinding.ActivityFaceBinding
import com.hello.scan.ScanCallBack
import com.tencent.mmkv.MMKV
import search
import sigin
import upFile
import java.io.*
import java.util.*
import java.util.concurrent.Executors


class FaceActivity : BaseBindingActivity<ActivityFaceBinding, BaseViewModel>(), ScanCallBack {
    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
    var preFrameList: MutableList<Bitmap> = ArrayList<Bitmap>()

    var scanTool: ScanTool? = null
    var jgTimer = Timer()
    val jgTask: TimerTask = object : TimerTask() {
        override fun run() {
            mainThread {
                isSavingPic = false
                mViewModel.isShowLoading.value = false
                binding.jieguo.visibility = View.GONE
            }

        }
    }

    override fun initData() {


        scanTool = ScanTool()
        scanTool?.initSerial(this, "/dev/ttyACM0", 115200, this@FaceActivity)
        scanTool?.playSound(true)
        binding.test.setOnClickListener { startDetect() }

        binding.faceDetectView.framePreViewListener =
            object : FaceDetectTextureView.IFramePreViewListener {
                override fun onFrame(preFrame: Bitmap?): Boolean {
                    //每一帧回调
                    //这个这帧preFrame处理了就是进行了回收，返回true
                    //否则返回false，内部进行回收处理
                    return false
                }

                override fun onFaceFrame(
                    preFrame: Bitmap?,
                    faces: Array<FaceDetector.Face?>
                ): Boolean {
                    //faces是检测出来的人脸参数
                    //检测到人脸的回调,保存人脸图片到本地

                    if (!isSavingPic) {

                        if (faces.size > 0) {
                            preFrame?.let {
                                preFrameList.add(it)
                            }
                            if (preFrameList.size > 5) {
                                Log.i("FaceActivity", "preFrameList.size ==：" + preFrameList.size)
                                isSavingPic = true
                                preFrame?.let {
//                        executorService.submit(SavePicRunnable(it))
                                    executorService.submit {
                                        saveFacePicToLocal(preFrame)
                                        preFrameList.clear()
                                    }
                                }


                            }

                        }
                    }

                    //这个这帧preFrame处理了就是进行了回收，返回true
                    //否则返回false，内部进行回收处理
                    return true
                }
            }
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
        LiveDataBus.get().with("onScanCallBack", String::class.java)
            .observeForever {
                var signUpUser = JSON.parseObject(it, SignUpUser::class.java)
                sigin(signUpUser.id)
            }
    }

    private fun showToast(state: String) {
        var toast = Toast(this)
        var view: View = LayoutInflater.from(this).inflate(R.layout.tost_sb, null)
        when (state) {
            "1" -> {
                view = LayoutInflater.from(this).inflate(R.layout.tost_cg, null)
            }
            "2" -> {
                view = LayoutInflater.from(this).inflate(R.layout.tost_cf, null)
            }
        }
        toast.setView(view)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.setGravity(0, 0, 0);
        toast.show();
    }

    private var isSavingPic = false
    private val executorService = Executors.newSingleThreadExecutor()

    fun startDetect() {
        if (!binding.faceDetectView.isHasInit) {
            //必须是在view可见后进行初始化
            binding.faceDetectView.initView()
            binding.faceDetectView.initCamera()
            binding.faceDetectView.detectConfig.CameraType = Camera.CameraInfo.CAMERA_FACING_FRONT
            binding.faceDetectView.detectConfig.EnableFaceDetect = true
            binding.faceDetectView.detectConfig.MinDetectTime = 1000
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
        Log.e("FaceActivity", "saveFacePicToLocal=isSavingPic=" + isSavingPic)
        val picPath = this.externalCacheDir.toString() + File.separator + "face.jpg"
        var fileOutputStream: FileOutputStream? = null
        val facePicFile = File(picPath)
        try {
            facePicFile.createNewFile()
        } catch (e: IOException) {
            isSavingPic = false
            Log.e("FaceActivity", "保存失败$e,$picPath")
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
            Log.e("FaceActivity", "Exception," + e.toString())
        }


    }

    fun upImgFile(picPath: String) {
        mViewModel.isShowLoading.value = true
        upFile(File(picPath), {

//            var urlString = "https://img-blog.csdnimg.cn/20190618124505345.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2EzMTI4NjMwNjM=,size_16,color_FFFFFF,t_70"

            var urlString = it.url
//            detect(urlString,{
//                var urlString = "https://meeting.nbqichen.com:20882/profile/upload/2023/03/09/ac479084-cff7-40a1-a7fe-480e6c8f4523.jpg"
            search(urlString, {

                it.result?.let { result ->

                    if (!result.user_list.isNullOrEmpty()) {

                        for (item in result.user_list) {

                            sigin(item.user_id)
                        }
                    }


                }

            }, {
                setFinishData("-5000")


            }, {
                mViewModel.isShowLoading.value = false
            })
//            },{isSavingPic = false})
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
        params["codeNo"] = "" + MMKV.mmkvWithID("MyDataMMKV").getString("codeNo", "")
        params["userMeetingId"] = userMeetingId//用户参与会议id


//        {
//            "codeNo": "",
//            "userMeetingId": 0
//        }
        sigin(JSON.toJSONString(params), { success ->

            if (MMKV.mmkvWithID("MyDataMMKV").getString("shockStatus", "2").equals("1")) {

                if (mRingPlayer != null) {
                    mRingPlayer?.stop();
                    mRingPlayer?.release();
                    mRingPlayer = null;
                }
                //语音播报
                if (success.equals("1")) {

                    mRingPlayer = MediaPlayer.create(this, R.raw.cg);
                    mRingPlayer?.start();


                } else {
                    mRingPlayer = MediaPlayer.create(this, R.raw.cf);
                    mRingPlayer?.start();
                }
            }
            setFinishData(success)
        }, {
            setFinishData("0",it)
        }, {
            isSavingPic = false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        endDetect()
        scanTool?.release()
    }

    override fun onScanCallBack(data: String?) {
        if (TextUtils.isEmpty(data)) return
        Log.e("Hello", "回调数据 == > $data")
        data?.let {
            LiveDataBus.get().with("onScanCallBack").postValue(data)
        }
    }

    override fun onInitScan(isSuccess: Boolean) {
//        val str = if (isSuccess) "初始化成功" else "初始化失败"
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
//        Log.d("FaceActivity","str="+str)

    }

    fun setFinishData(state: String,msg: String?="签到失败") {
        binding.stateIv.setImageResource(R.mipmap.qdcwicon)
        binding.stateTv.setText("签到失败")
        binding.jgsb.visibility = View.VISIBLE
        binding.jgcg.visibility = View.GONE
        when (state) {
            "-5000" -> {
                binding.stateIv.setImageResource(R.mipmap.qdcwicon)
                binding.stateTv.setText("识别失败")

            }
            "1" -> {
                binding.jgsb.visibility = View.GONE
                binding.jgcg.visibility = View.VISIBLE
                binding.stateIv.setImageResource(R.mipmap.qdcgicon)
                binding.stateTv.setText("签到成功")

            }
            "2" -> {
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

            }
        }
        jgTimer.schedule(jgTask, 3000)
        binding.jieguo.visibility = View.VISIBLE

    }

}