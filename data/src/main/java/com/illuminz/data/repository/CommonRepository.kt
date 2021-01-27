package com.illuminz.data.repository

import com.illuminz.data.models.common.AppVersionResponse
import com.illuminz.data.models.common.Resource
import java.io.File

interface CommonRepository {
    suspend fun getAppVersion(): Resource<AppVersionResponse>
    suspend fun uploadFile(file: File): Resource<String>
    /*  suspend fun updateFcmToken(token: String): Resource<Any>
      suspend fun uploadFile(file: File): Resource<String>
      suspend fun uploadFileFor(file: File): Resource<ImageResponse>
      suspend fun uploadVideo(file: File): Resource<ImageResponse>
      suspend fun getAppVersion(): Resource<AppVersionResponse>
      suspend fun uploadVideo(
          file: File,
          requestBody: ProgressRequestBody,
          thumbnail: File?,
          shouldCreateGIF: Boolean = false
      ): Resource<ImageResponse>

      suspend fun uploadDoc(file: File, type: String): Resource<ImageResponse>
      suspend fun getNotifications(getInstantClassesRequest: GetInstantClassesRequest): Resource<NotificationResponse>
      suspend fun changeToggleStatus(type: String, status: Boolean): Resource<Any>
      suspend fun getCancellationReasons(type: String): Resource<List<String>>
      suspend fun getReportReasons(type: String): Resource<List<String>>
      suspend fun cancelScheduleClass(
          bookingId: String,
          cancellationRemark: String,
          type: String
      ): Resource<Any>

      suspend fun reportTeacherStudent(
          bookingId: String,
          cancellationRemark: String,
          type: String
      ): Resource<Any>

      suspend fun submitFeedback(feedBackRequest: FeedBackRequest): Resource<Any>
      suspend fun getFeedBackCategories(): Resource<ApiResponse<InterestsResponse>>

      suspend fun acceptRejectReschedule(
          bookingId: String,
          status: String,
          isTeacher: Boolean
      ): Resource<Any>

      suspend fun cancelReschedule(
          bookingId: String
      ): Resource<Any>*/
}