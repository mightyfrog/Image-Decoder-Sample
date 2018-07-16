package org.mightyfrog.android.imagedecodersample

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.graphics.drawable.Animatable2
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val listener = ImageDecoder.OnHeaderDecodedListener { _, info, _ -> android.util.Log.e("ImageDecoder", "$info") }
        val src = ImageDecoder.createSource(contentResolver, drawableToUri(R.drawable.earth))
        ImageDecoder.decodeDrawable(src, listener)?.apply {
            imageView.setImageDrawable(this)
            if (this is Animatable2) {
                start()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun drawableToUri(resId: Int): Uri {
        return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${resources.getResourcePackageName(resId)}/${resources.getResourceTypeName(resId)}/${resources.getResourceEntryName(resId)}")
    }
}
