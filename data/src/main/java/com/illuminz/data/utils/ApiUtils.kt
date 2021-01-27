package com.illuminz.data.utils

import com.illuminz.data.models.common.ProgressRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object ApiUtils {
    private const val MIME_TYPE_IMAGE = "image/*"
    const val MIME_TYPE_VIDEO = "video/*"
    private const val MIME_TYPE_PDF = "application/pdf"


    fun stringToRequestBody(string: String): RequestBody =
        string.toRequestBody(MultipartBody.FORM)

    private fun imageToRequestBody(file: File): RequestBody =
        file.asRequestBody(MIME_TYPE_IMAGE.toMediaTypeOrNull())

    private fun pdfToRequestBody(file: File): RequestBody =
        file.asRequestBody(MIME_TYPE_PDF.toMediaTypeOrNull())

    private fun videoToRequestBody(file: File): RequestBody =
        file.asRequestBody(MIME_TYPE_VIDEO.toMediaTypeOrNull())

    fun imageToPart(imageFile: File, parameterName: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            parameterName,
            imageFile.name,
            imageToRequestBody(imageFile)
        )
    }

    fun videoToPart(videoFile: File, parameterName: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            parameterName,
            videoFile.name,
            videoToRequestBody(videoFile)
        )
    }

    fun videoToPart(
        videoFile: File,
        parameterName: String,
        requestBody: ProgressRequestBody
    ): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            parameterName,
            videoFile.name,
            requestBody
        )
    }

    fun pdfToPart(pdfFile: File, parameterName: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            parameterName,
            pdfFile.name,
            pdfToRequestBody(pdfFile)
        )
    }

    fun imageToRequestBodyKey(parameterName: String, fileName: String): String =
        "$parameterName\"; filename=\"$fileName"
}