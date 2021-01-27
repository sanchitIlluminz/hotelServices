package com.illuminz.data.remote

import com.illuminz.data.models.common.ApiResponse
import com.illuminz.data.models.common.AppVersionResponse
import com.illuminz.data.models.common.FeedBackRequest
import com.illuminz.data.models.common.ImageResponse
import com.illuminz.data.models.common.notification.NotificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CommonApi {
    @FormUrlEncoded
    @PUT("user/account/firebase-token/refresh")
    suspend fun updateFcmToken(@Field("registrationToken") token: String): Response<Any>

    @Multipart
    @POST("user/uploadFiles")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part/*,
        @Part("deviceType") type: RequestBody*/
    ): Response<ApiResponse<ImageResponse>>

    @Multipart
    @POST("user/upload/attachments")
    suspend fun uploadDoc(
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<ImageResponse>>

    @Multipart
    @POST("user/upload/videos")
    suspend fun uploadVideo(
        @Part file: MultipartBody.Part,
        @Part thumbnail: MultipartBody.Part? = null,
        @Part("shouldCreateGIF") shouldCreateGIF: RequestBody? = null
    ): Response<ApiResponse<ImageResponse>>

    @GET("notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int?,
        @Query("skip") skip: Int?
    ): Response<ApiResponse<NotificationResponse>>

    @GET("class/cancellation-reasons")
    suspend fun getCancellationReason(
        @Query("type") type: String?
    ): Response<ApiResponse<List<String>>>

    @GET("user/report-reasons")
    suspend fun getReportReason(
        @Query("type") type: String?
    ): Response<ApiResponse<List<String>>>

    @FormUrlEncoded
    @PUT("user/settings")
    suspend fun changeToggleStatus(
        @Field("type") type: String,
        @Field("value") status: Boolean
    ): Response<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("class/cancel")
    suspend fun cancelScheduleClass(
        @Field("bookingId") bookingId: String,
        @Field("cancellationRemark") cancellationRemark: String,
        @Field("type") type: String
    ): Response<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("user/report")
    suspend fun reportTeacherStudent(
        @Field("reportId") bookingId: String,
        @Field("reason") reason: String,
        @Field("role") type: String
    ): Response<ApiResponse<Any>>

    @POST("user/feedback")
    suspend fun submitFeedback(
        /* @Field("subject") subject: String,
         @Field("description") description: String,*/
        @Body feedBackRequest: FeedBackRequest
    ): Response<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("class/reschedule/approval")
    suspend fun rescheduleAction(
        @Field("id") id: String,
        @Field("status") status: String?,
        @Field("isTeacher") isTeacher: Boolean?
    ): Response<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("class/reschedule/cancel")
    suspend fun rescheduleCancel(
        @Field("id") id: String
    ): Response<ApiResponse<Any>>

    @GET("appVersion")
    suspend fun getAppVersion(/*@Query("type") type: String = "USER"*/): Response<ApiResponse<AppVersionResponse>>

}