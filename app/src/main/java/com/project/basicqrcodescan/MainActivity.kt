package com.project.basicqrcodescan

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.project.basicqrcodescan.databinding.ActivityMainBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainActivity : AppCompatActivity() {
    //https://www.youtube.com/watch?v=drH63NpSWyk
    //https://www.youtube.com/watch?v=Sb4avOp7D_k
    // https://www.youtube.com/watch?v=iryHXuwuJ3Q
    //https://www.youtube.com/watch?v=6OFniVVzmgQ
    // https://github.com/niharika2810/zxing-android-embedded
    //https://www.youtube.com/watch?v=asl1mFtkMkc
    //https://github.com/prof18/Secure-QR-Reader
    //https://youtu.be/Sb4avOp7D_k
    //https://youtu.be/drH63NpSWyk
    //https://www.youtube.com/watch?v=wfucGSKngq4
    //https://www.youtube.com/watch?v=AiNi9K94W5c
    //https://www.youtube.com/playlist?list=PLirRGafa75rQOi3so_ngAHqDmq_Djifwu
    //https://www.youtube.com/watch?v=6OFniVVzmgQ
    //https://www.youtube.com/watch?v=wfucGSKngq4
    //https://github.com/zxing/zxing/
    private var flashActive = false
    private var hideFlashActive = false

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setUpPermission()

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun setUpPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                        "The Permissions are not Granted For Camera Access!!!",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "Permission Granted!!")
                }
        }
        return super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        const val CAMERA_REQUEST_CODE = 1
        val TAG: String = MainActivity::class.java.simpleName
    }
}