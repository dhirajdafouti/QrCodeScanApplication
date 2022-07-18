package com.project.basicqrcodescan.barcode

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.journeyapps.barcodescanner.CameraPreview
import com.project.basicqrcodescan.R
import com.project.basicqrcodescan.databinding.ActivityMainBinding
import com.project.basicqrcodescan.databinding.ActivityQrCodeSampleComposeBinding
import com.project.basicqrcodescan.qrcode.MainActivity
import java.util.concurrent.Executors

//https://beakutis.medium.com/using-googles-mlkit-and-camerax-for-lightweight-barcode-scanning-bb2038164cdc
//https://medium.com/@mt1729/an-android-journey-barcode-scanning-with-mobile-vision-api-and-camera2-part-1-8a97cc0d6747
//https://miguel-lasa.medium.com/barcode-scanner-with-camerax-and-mlkit-bde53fbc2b8f
//https://yushulx.medium.com/android-barcode-detection-from-fast-moving-objects-ec42683d8f56
//https://github.com/bea-droid/barcodescanner
class QrCodeSampleCameraX : AppCompatActivity() {
    private lateinit var cameraPreview: PreviewView
    private lateinit var barCodeText: TextView
    private lateinit var binding: ActivityQrCodeSampleComposeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeSampleComposeBinding.inflate(layoutInflater)
        cameraPreview = binding.previewCameraMlKit
        barCodeText = binding.textview
        setContentView(binding.root)
        val permission = setUpPermission()
        if (permission) {
            bindCameraUseCases()
        } else {
            setUpPermission()
        }

    }

    private fun setUpPermission(): Boolean {
        val permission = let {
            ContextCompat.checkSelfPermission(it,
                android.Manifest.permission.CAMERA)
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
        return true
    }

    private fun bindCameraUseCases() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // setting up the preview use case
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPreview.surfaceProvider)
                }

            // configure our MLKit BarcodeScanning client

            /* passing in our desired barcode formats - MLKit supports additional formats outside of the
            ones listed here, and you may not need to offer support for all of these. You should only
            specify the ones you need */
            val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_CODE_93,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E,
                Barcode.FORMAT_PDF417
            ).build()

            // getClient() creates a new instance of the MLKit barcode scanner with the specified options
            val scanner = BarcodeScanning.getClient(options)

            // setting up the analysis use case
            val analysisUseCase = ImageAnalysis.Builder()
                .build()

            // define the actual functionality of our analysis use case
            analysisUseCase.setAnalyzer(
                Executors.newSingleThreadExecutor(),
                { imageProxy ->
                    processImageProxy(scanner, imageProxy)
                }
            )

            // configure to use the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    previewUseCase,
                    analysisUseCase)
            } catch (illegalStateException: IllegalStateException) {
                // If the use case has already been bound to another lifecycle or method is not called on main thread.
                Log.e(TAG, illegalStateException.message.orEmpty())
            } catch (illegalArgumentException: IllegalArgumentException) {
                // If the provided camera selector is unable to resolve a camera to be used for the given use cases.
                Log.e(TAG, illegalArgumentException.message.orEmpty())
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
    ) {

        imageProxy.image?.let { image ->
            val inputImage =
                InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)

                    // `rawValue` is the decoded value of the barcode
                    barcode?.rawValue?.let { value ->
                        barCodeText.text = value
                    }
                }
                .addOnFailureListener {
                    // This failure will happen if the barcode scanning model
                    // fails to download from Google Play Services

                    Log.e(TAG, it.message.orEmpty())
                }.addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must
                    // call image.close() on received images when finished
                    // using them. Otherwise, new images may not be received
                    // or the camera may stall.

                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }


    private fun makeRequest() {
        let {
            ActivityCompat.requestPermissions(it,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1
        val TAG: String = QrCodeSampleCameraX::class.java.simpleName
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
                    Log.d(MainActivity.TAG, "Permission Granted!!")
                }
        }
        return super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}