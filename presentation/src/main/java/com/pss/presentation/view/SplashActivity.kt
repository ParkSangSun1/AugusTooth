package com.pss.presentation.view

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pss.presentation.R
import com.pss.presentation.widget.extension.startActivityWithFinish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.pss.presentation.base.BaseActivity
import com.pss.presentation.databinding.ActivitySplashBinding


class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private var permissionArray = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val MULTI_PERMISSION = 999
    var permissionNoRealTime: ArrayList<String> = ArrayList() //실시간 퍼미션 거부된 값을 담는다

    override fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1500)
            checkPermission()
        }
    }

    private fun startApp() = this.startActivityWithFinish(this, MainActivity::class.java)

    private fun checkPermission() {
        try {
            val permissionOK: ArrayList<String> = ArrayList() //퍼미션 허용된 값
            val permissionNO: ArrayList<String> = ArrayList() //퍼미션 거부된 값을 담는다
            if (permissionArray.isNotEmpty()) {
                var check = 0
                for (data in permissionArray) { //매니페스트에 등록된 허용받을 퍼미션이 허용되었는지 확인
                    check = ContextCompat.checkSelfPermission(this, data)
                    if (check != PackageManager.PERMISSION_GRANTED) { //권한이 부여되지 않았을 경우
                        permissionNO.add(data)
                    } else { //퍼미션 권한이 부여 되었을 경우
                        permissionOK.add(data)
                    }
                }

                // 퍼미션 거부된 값이 있을 경우
                if (permissionNO.size > 0) {
                    ActivityCompat.requestPermissions(
                        this,
                        permissionNO.toArray(arrayOfNulls(permissionNO.size)),
                        MULTI_PERMISSION
                    )
                } else {
                    startApp()
                }
            } else {
                Toast.makeText(this, "퍼미션 허용을 확인할 데이터가 없습니다... ", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MULTI_PERMISSION -> {
                if (grantResults.isNotEmpty()) { //퍼미션 권한이 부여되지 않는 배열 길이가 0보다 클경우
                    if (permissionNoRealTime.isNotEmpty()) {
                        permissionNoRealTime.clear()
                    }
                    var i = 0
                    while (i < permissions.size) {
                        //배열을 순회하면서
                        if (grantResults[i] !== PackageManager.PERMISSION_GRANTED) { //권한이 부여되어 있지 않을 경우 확인
                            permissionNoRealTime.add(permissions[i])
                        }
                        i++
                    }

                    // 실시간으로 권한 허용이 거부된 값이 있을 경우
                    if (permissionNoRealTime.isNotEmpty()) {
                        val title = "퍼미션 허용 확인"
                        val message = """
                    퍼미션을 허용해야 앱을 이용가능합니다
                    [설정>권한]에서 퍼미션을 허용하시거나, 
                    앱을 재실행해 권한을 허용해주세요
                    """.trimIndent()
                        val buttonNo = "종 료"
                        val buttonYes = "설 정"
                        AlertDialog.Builder(this)
                            .setTitle(title) //팝업창 타이틀 지정
                            //.setIcon(R.drawable.ic_launcher_foreground) //팝업창 아이콘 지정
                            .setMessage(message) //팝업창 내용 지정
                            .setCancelable(false) //외부 레이아웃 클릭시도 팝업창이 사라지지않게 설정
                            .setPositiveButton(
                                buttonYes
                            ) { dialog, which ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                val uri: Uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                            }
                            .setNegativeButton(
                                buttonNo
                            ) { dialog, which ->
                                // TODO Auto-generated method stub
                                try {
                                    finishAffinity()
                                    overridePendingTransition(0, 0)
                                    //android.os.Process.killProcess(android.os.Process.myPid());
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            .show()
                    }
                    // 실시간으로 모든 권한이 허용된 경우
                    else {
                        startApp()
                    }
                }
                return
            }
        }
    }
}


