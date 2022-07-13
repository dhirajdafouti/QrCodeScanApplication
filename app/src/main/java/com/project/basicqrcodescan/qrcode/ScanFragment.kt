package com.project.basicqrcodescan.qrcode

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.project.basicqrcodescan.R
import com.project.basicqrcodescan.qrcode.ResultFragment.Companion.RESULT_SCANNER_FRAGMENT_TAG
import pub.devrel.easypermissions.EasyPermissions


class ScanFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var beepManager: BeepManager
    private lateinit var scannerView: DecoratedBarcodeView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scannerView = view.findViewById(R.id.QRScannerView)
        beepManager = BeepManager(activity)
        val formats = mutableListOf(BarcodeFormat.QR_CODE)
        scannerView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        scannerView.setStatusText(EMPTY)
        setUpPermission()
        scannerView.decodeContinuous { result ->
            result.let {
                beepManager.isBeepEnabled = false
                beepManager.playBeepSoundAndVibrate()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer,
                        ResultFragment.create(result.text),
                        RESULT_SCANNER_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit()
            }

        }
    }

    private fun setUpPermission() {
        val permission = activity?.let {
            ContextCompat.checkSelfPermission(it,
                android.Manifest.permission.CAMERA)
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

    }

    private fun makeRequest() {
        activity?.let {
            ActivityCompat.requestPermissions(it,
                arrayOf(android.Manifest.permission.CAMERA),
                MainActivity.CAMERA_REQUEST_CODE)
        }
    }

    private fun openScanner() {
        scannerView.resume()
    }

    override fun onPause() {
        super.onPause()
        scannerView.pause()
    }

    override fun onResume() {
        super.onResume()
        scannerView.resume()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            MainActivity.CAMERA_REQUEST_CODE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context,
                        "The Permissions are not Granted For Camera Access!!!",
                        Toast.LENGTH_SHORT).show()
                } else {
                    openScanner()
                    Log.d(MainActivity.TAG, "Permission Granted!!")
                }
        }
        return super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun enableFlash(enable: Boolean) {
        if (enable) {
            scannerView.setTorchOn();
        } else {
            scannerView.setTorchOff()
        }
    }


    companion object {
        const val SCANNER_FRAGMENT_TAG: String = "ScannerFragmentTag"
        const val EMPTY = ""
    }
}
