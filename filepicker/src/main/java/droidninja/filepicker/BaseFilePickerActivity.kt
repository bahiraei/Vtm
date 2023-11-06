package droidninja.filepicker

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.Toolbar
import droidninja.filepicker.utils.Orientation
import io.github.inflationx.viewpump.ViewPumpContextWrapper

/**
 * Created by droidNinja on 22/07/17.
 */

abstract class BaseFilePickerActivity : AppCompatActivity() {

    protected fun onCreate(savedInstanceState: Bundle?, @LayoutRes layout: Int) {
        super.onCreate(savedInstanceState)
        setTheme(PickerManager.theme)
        setContentView(layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //set orientation
        val orientation = PickerManager.orientation
        if (orientation == Orientation.PORTRAIT_ONLY) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else if (orientation == Orientation.LANDSCAPE_ONLY) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        initView()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    protected abstract fun initView()
}
