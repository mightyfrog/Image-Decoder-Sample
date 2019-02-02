package org.mightyfrog.android.imagedecodersample

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.graphics.drawable.Animatable2
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val listener = ImageDecoder.OnHeaderDecodedListener { _, info, _ -> android.util.Log.e("ImageDecoder", "$info") }
        val src = ImageDecoder.createSource(contentResolver, drawableToUri(R.drawable.earth))
        launch {
            withContext(Dispatchers.IO) { ImageDecoder.decodeDrawable(src, listener) }.apply {
                imageView.setImageDrawable(this)
                if (this is Animatable2) {
                    start()
                }
            }
        }
    }

    private fun drawableToUri(resId: Int): Uri {
        return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${resources.getResourcePackageName(resId)}/${resources.getResourceTypeName(resId)}/${resources.getResourceEntryName(resId)}")
    }
}
