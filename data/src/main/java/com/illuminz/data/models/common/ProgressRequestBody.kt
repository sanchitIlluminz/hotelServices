package com.illuminz.data.models.common

import android.os.Handler
import android.os.Looper
import com.illuminz.data.utils.ApiUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class ProgressRequestBody(val file: File, val listener: UploadCallbacks) :
    RequestBody() {

    override fun contentType(): MediaType? {
        return ApiUtils.MIME_TYPE_VIDEO.toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileLength: Long = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(file)
        var uploaded: Long = 0

        `in`.use { `in` ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (`in`.read(buffer).also { read = it } != -1) { // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        }

    }

    inner class ProgressUpdater(private val uploaded: Long, private val total: Long) : Runnable {

        override fun run() {
            // val res = (uploaded.div(total)).times(100).toInt()
            val res = (100 * uploaded / total).toInt()
            listener.onProgressUpdate(res)

        }

    }

}

