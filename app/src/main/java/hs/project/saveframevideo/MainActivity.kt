package hs.project.saveframevideo

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.util.Objects


class MainActivity : AppCompatActivity() {

    private val test1 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    private val test2 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private val test3 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(test1)

        val bitmap = mediaMetadataRetriever.getFrameAtTime(10000 * 1000)
        bitmap?.let { saveImg(it) }

    }

    private fun saveImg(bitmap: Bitmap) {
        try {

            var outStream: OutputStream? = null

            val filename = "testThumbnail"

            val resolver = contentResolver
            val contentVal = contentValuesOf()
            contentVal.put(MediaStore.MediaColumns.DISPLAY_NAME, filename.plus(".jpg"))
            contentVal.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")

            val imgUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentVal)
            outStream = Objects.requireNonNull(imgUri)?.let { resolver.openOutputStream(it) }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            Objects.requireNonNull(outStream)

        } catch (e: Exception) {

        }
    }


    private fun saveBitmapToJPG(bitmap: Bitmap, strFilePath: String, filename: String) {
        val file = File(strFilePath)
        if (!file.exists()) {
            file.mkdirs()
        }

        val fileItem = File(strFilePath + filename);
        var outStream: OutputStream? = null

        try {
            fileItem.createNewFile()
            outStream = FileOutputStream(fileItem)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}