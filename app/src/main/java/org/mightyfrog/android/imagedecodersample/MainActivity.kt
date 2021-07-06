package org.mightyfrog.android.imagedecodersample

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.graphics.drawable.Animatable2
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mightyfrog.android.imagedecodersample.databinding.ActivityMainBinding

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val listener = ImageDecoder.OnHeaderDecodedListener { _, info, _ ->
            android.util.Log.e(
                "ImageDecoder",
                "$info"
            )
        }
        val src = ImageDecoder.createSource(contentResolver, drawableToUri(R.drawable.earth))
        launch {
            withContext(Dispatchers.IO) { ImageDecoder.decodeDrawable(src, listener) }.apply {
                binding.imageView.setImageDrawable(this)
                if (this is Animatable2) {
                    start()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        cancel()
    }

    private fun drawableToUri(resId: Int): Uri {
        return Uri.parse(
            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${
                resources.getResourcePackageName(
                    resId
                )
            }/${resources.getResourceTypeName(resId)}/${resources.getResourceEntryName(resId)}"
        )
    }
}
