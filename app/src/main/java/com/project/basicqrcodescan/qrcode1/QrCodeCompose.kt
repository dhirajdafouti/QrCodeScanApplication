package com.project.basicqrcodescan.qrcode1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.project.basicqrcodescan.qrcode1.ui.theme.BasicQrCodeScanTheme

class QrCodeCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicQrCodeScanTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                  checkRow()
                }
            }
        }
    }


}
@Composable
private fun checkRow() {
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Hello",color = MaterialTheme.colors.primary)
        Text(text = "India",color = MaterialTheme.colors.primary)
    }
}



