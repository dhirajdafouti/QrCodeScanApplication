package com.project.basicqrcodescan


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.project.basicqrcodescan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //https://www.youtube.com/watch?v=drH63NpSWyk
    //https://www.youtube.com/watch?v=Sb4avOp7D_k
    // https://www.youtube.com/watch?v=iryHXuwuJ3Q
    //https://www.youtube.com/watch?v=6OFniVVzmgQ
    // https://github.com/niharika2810/zxing-android-embedded(done)
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
    //https://github.com/zxing/zxing
    //https://www.youtube.com/watch?v=AiNi9K94W5c
    private var flashActive = false
    private var hideFlashActive = false
    private lateinit var scanFragment: ScanFragment

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        scanFragment = ScanFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, scanFragment, ScanFragment.SCANNER_FRAGMENT_TAG).commit()

        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, fragment: Fragment) {
                super.onFragmentResumed(fm, fragment)
                if (fragment is ScanFragment) {
                    hideFlashMenu(false)
                } else if (fragment is ResultFragment) {
                    hideFlashMenu(true)
                }

            }
        }, true)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                true
            }
            R.id.action_about -> {
                if (!flashActive) {
                    flashActive = true
                    scanFragment.enableFlash(true)
                }
                true
            }
            R.id.action_flash -> {
                if (flashActive) {
                    flashActive = false
                    scanFragment.enableFlash(false)
                }
                invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (!hasFlash()) {
            menu?.findItem(R.id.action_flash)?.isVisible = false
        }
        menu?.findItem(R.id.action_flash)?.isVisible = !hideFlashActive
        if (flashActive) {
            menu?.findItem(R.id.action_flash)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_flash_off_24)
        } else {
            menu?.findItem(R.id.action_flash)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_flash_on_24)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun hideFlashMenu(hide: Boolean) {
        hideFlashActive = hide
        invalidateOptionsMenu()
    }


    companion object {
        const val CAMERA_REQUEST_CODE = 1
        val TAG: String = MainActivity::class.java.simpleName
    }
}