package com.project.basicqrcodescan.qrcode1

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

class QrCodeAnalysis(private val qrCodeScanner: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val supportedImage = listOf(ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888, ImageFormat.YUV_444_888)

    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImage) {
            val bytes=image.planes.first().buffer
        }
    }
}