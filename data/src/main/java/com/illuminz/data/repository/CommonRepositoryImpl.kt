package com.illuminz.data.repository

import com.illuminz.data.extensions.toApiError
import com.illuminz.data.extensions.toApiFailure
import com.illuminz.data.models.common.AppVersionResponse
import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.common.UploadCallbacks
import com.illuminz.data.remote.CommonApi
import com.illuminz.data.utils.ApiUtils
import java.io.File
import javax.inject.Inject


class CommonRepositoryImpl @Inject constructor(
    private val commonApi: CommonApi
) : CommonRepository, UploadCallbacks {
    override fun onProgressUpdate(res: Int) {

    }

    override suspend fun getAppVersion(): Resource<AppVersionResponse> {
        return try {
            val response = commonApi.getAppVersion()
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun uploadFile(file: File): Resource<String> {
        return try {
            val image = ApiUtils.imageToPart(file, "uploadFile")
            //val type=ApiUtils.stringToRequestBody("ANDROID")
            val response = commonApi.uploadFile(image)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data?.id)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }
    /* private var pd: ProgressDialog? = null
     override suspend fun updateFcmToken(token: String): Resource<Any> {
         return try {
             val response = commonApi.updateFcmToken(token)
             if (response.isSuccessful) {
                 Resource.success()
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun uploadFile(file: File): Resource<String> {
         return try {
             val image = ApiUtils.imageToPart(file, "content")
             val response = commonApi.uploadFile(image)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body?.id)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun uploadFileFor(file: File): Resource<ImageResponse> {
         return try {
             val image = ApiUtils.imageToPart(file, "content")
             val response = commonApi.uploadFile(image)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun uploadVideo(file: File): Resource<ImageResponse> {
         return try {
             val fileBody = ProgressRequestBody(file, this)
             val video = ApiUtils.videoToPart(file, "content", fileBody)
             val response = commonApi.uploadVideo(video)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             pd?.dismiss()
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun uploadVideo(
         file: File,
         requestBody: ProgressRequestBody, thumbnail: File?,
         shouldCreateGIF: Boolean
     ): Resource<ImageResponse> {
         return try {
             val video = ApiUtils.videoToPart(file, "content", requestBody)
             val image = thumbnail?.let { ApiUtils.imageToPart(it, "thumbnail") }
             val shouldCreate = ApiUtils.stringToRequestBody(shouldCreateGIF.toString())
             val response = commonApi.uploadVideo(video, image, shouldCreate)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             pd?.dismiss()
             Resource.error(throwable.toApiFailure())
         }
     }


     override suspend fun uploadDoc(file: File, type: String): Resource<ImageResponse> {
         return try {
             val fileToSend: MultipartBody.Part = if (type == "IMAGE")
                 ApiUtils.imageToPart(file, "content")
             else {
                 ApiUtils.pdfToPart(file, "content")
             }
             val response = commonApi.uploadDoc(fileToSend)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun getNotifications(getInstantClassesRequest: GetInstantClassesRequest):
             Resource<NotificationResponse> {
         return try {
             val response = commonApi.getNotifications(
                 skip = getInstantClassesRequest.skip,
                 limit = getInstantClassesRequest.limit
             )
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun changeToggleStatus(
         type: String,
         status: Boolean
     ): Resource<Any> {
         return try {
             val response = commonApi.changeToggleStatus(type, status)
             if (response.isSuccessful) {
                 Resource.success()
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun getCancellationReasons(type: String): Resource<List<String>> {
         return try {
             val response = commonApi.getCancellationReason(type)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun getReportReasons(type: String): Resource<List<String>> {
         return try {
             val response = commonApi.getReportReason(type)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun cancelScheduleClass(
         bookingId: String,
         cancellationRemark: String,
         type: String
     ): Resource<Any> {
         return try {
             val response = commonApi.cancelScheduleClass(bookingId, cancellationRemark, type)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun reportTeacherStudent(
         bookingId: String,
         cancellationRemark: String,
         type: String
     ): Resource<Any> {
         return try {
             val response = commonApi.reportTeacherStudent(bookingId, cancellationRemark, type)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun submitFeedback(feedBackRequest: FeedBackRequest): Resource<Any> {
         return try {
             val response = commonApi.submitFeedback(feedBackRequest)
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun getFeedBackCategories(): Resource<ApiResponse<InterestsResponse>> {
         return try {
             val response = commonApi.getFeedBackCategories()
             if (response.isSuccessful) {
                 Resource.success(response.body())
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun acceptRejectReschedule(
         bookingId: String, status: String,
         isTeacher: Boolean
     ): Resource<Any> {
         return try {
             val response = commonApi.rescheduleAction(bookingId, status, isTeacher)
             if (response.isSuccessful) {
                 Resource.success(response.body())
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun cancelReschedule(bookingId: String): Resource<Any> {
         return try {
             val response = commonApi.rescheduleCancel(bookingId)
             if (response.isSuccessful) {
                 Resource.success(response.body())
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override suspend fun getAppVersion(): Resource<AppVersionResponse> {
         return try {
             val response = commonApi.getAppVersion()
             if (response.isSuccessful) {
                 Resource.success(response.body()?.body)
             } else {
                 Resource.error(response.toApiError())
             }
         } catch (throwable: Throwable) {
             Resource.error(throwable.toApiFailure())
         }
     }

     override fun onProgressUpdate(res: Int) {
         pd?.progress = res
     }*/
}