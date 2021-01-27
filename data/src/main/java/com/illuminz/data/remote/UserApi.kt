package com.illuminz.data.remote

import com.illuminz.data.models.common.ApiResponse
import com.illuminz.data.models.master.MasterDataResponse
import com.illuminz.data.models.request.ExchangeRateDto
import com.illuminz.data.models.request.KYCManualRequest
import com.illuminz.data.models.request.KYCStartRequest
import com.illuminz.data.models.request.RateTransferRequest
import com.illuminz.data.models.request.loginsignup.SignupRequest
import com.illuminz.data.models.sendmoney.AccountDetailsResponse
import com.illuminz.data.models.sendmoney.CountryResponse
import com.illuminz.data.models.transfers.TransactionDto
import com.illuminz.data.models.transfers.TransfersResponse
import com.illuminz.data.models.user.OccupationDto
import com.illuminz.data.models.user.UserDto
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("user/login")
    suspend fun login(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/sendPhoneVerification")
    suspend fun sendPhoneVerification(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/sendPhoneVerificationCode")
    suspend fun sendPhoneVerificationCode(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/sendEditPhoneVerification")
    suspend fun sendEditPhoneVerification(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/editPhoneNumber")
    suspend fun editPhoneNumber(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/signUp")
    suspend fun signUp(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/forgotPassword")
    suspend fun forgotPassword(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/sendForgotPasswordVerification")
    suspend fun sendForgotVerification(@Body request: SignupRequest): Response<ApiResponse<UserDto>>

    @POST("user/kyc_verification")
    suspend fun sendKYCVerification(@Body request: KYCStartRequest): Response<ApiResponse<UserDto>>

    @POST("user/applyKycManually")
    suspend fun applyKycManually(@Body kycManual: KYCManualRequest): Response<ApiResponse<UserDto>>

    @GET("user/getOccupationList")
    suspend fun getOccupations(): Response<ApiResponse<List<OccupationDto>>>

    @GET("user/getConstantData")
    suspend fun getConstantData(@Query("type") type: Int): Response<ApiResponse<MasterDataResponse>>

    @GET("user/getKycVerificationToken")
    suspend fun getKycVerificationToken(): Response<ApiResponse<Any>>

    @POST("user/changePassword")
    suspend fun updatePassword(
        @Body signUpRequest: SignupRequest
    ): Response<ApiResponse<UserDto>>

    @POST("user/logout")
    suspend fun logout(
    ): Response<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("user/editProfile")
    suspend fun updateFullName(@Field("fullName") fullName: String): Response<ApiResponse<UserDto>>

    @FormUrlEncoded
    @POST("user/updateEmail")
    suspend fun updateEmail(@Field("email") fullName: String): Response<ApiResponse<UserDto>>

    @GET("user/getTransaction")
    suspend fun getMyTransfers(
        @Query("pageNumber") pageNumber: Int?,
        @Query("beneficiaryName") beneficiaryName: String? = null,
        @Query("accountNumber") accountNumber: String? = null,
        @Query("status") status: Int? = null,
        @Query("paymentType") paymentType: String? = null
    ): Response<ApiResponse<TransfersResponse>>


    @GET("user/getWalletTransaction")
    suspend fun getWalletTransaction(
        @Query("pageNumber") pageNumber: Int?,
        @Query("type") type: String? = null
    ): Response<ApiResponse<TransfersResponse>>

//    @GET("user/getWalletTransaction")
//    suspend fun getMyWalletTransactions(): Response<ApiResponse<WalletResponse>>

    @FormUrlEncoded
    @POST("user/contactUsForm")
    suspend fun contactUsForm(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("message") message: String?
    ): Response<Any>

    @POST("user/transferMoney")
    suspend fun transferMoney(
        @Body transactionRequest: TransactionDto?
    ): Response<ApiResponse<TransactionDto>>

    @POST("user/transactionFeedback")
    suspend fun transferMoneyFeedback(
        @Body rateTransferRequest: RateTransferRequest
    ): Response<ApiResponse<UserDto>>

    @POST("user/exchangeRateConversion")
    suspend fun exchangeRate(
        @Body exchangeDto: ExchangeRateDto
    ): Response<ApiResponse<UserDto>>

    @GET("user/getAdminAccountInfo")
    suspend fun getAdminAccountInfo(): Response<ApiResponse<AccountDetailsResponse>>

    @GET("user/getCountryCurrencyCharges")
    suspend fun getCountryCurrencyCharges(): Response<ApiResponse<List<CountryResponse>>>

    @GET("user/getCharges")
    suspend fun getCharges():Response<ApiResponse<AccountDetailsResponse>>



    /* @POST("user/login")
     suspend fun login(@Body request: LoginRequest): Response<ApiResponse<UserDto>>

     //@GET("user/student/bookings/{type}")
     @GET("bookings/student")
     suspend fun getMyClasses(*//*
        @Path("type") type: String?,*//*
        @Query("title") title: String?,
        @Query("skip") skip: Int?,
        @Query("limit") limit: Int?,
        @Query("upcoming") upcoming: Boolean?
    ): Response<ApiResponse<MyClassesResponse>>

    @GET("user/follows")
    suspend fun getMyFollowings(
        @Query("type") type: String?,
        @Query("skip") skip: Int?,
        @Query("limit") limit: Int?,
        @Query("name") name: String?
    ): Response<ApiResponse<FollowingResponse>>

    @GET("user/teacher/follows")
    suspend fun getMyFollowers(
        @Query("skip") skip: Int?,
        @Query("limit") limit: Int?,
        @Query("name") name: String?
    ): Response<ApiResponse<FollowingResponse>>

    @GET("class/all/count-active-instant-classes")
    suspend fun getFilterScreenDataClass(
        @Query("interestIds") interestIds: String?,
        @Query("priceLowerLimit") priceLowerLimit: Float?,
        @Query("priceUpperLimit") priceUpperLimit: Float?,
        @Query("rating") rating: Float?,
        @Query("durationArray") durationArray: String?,
        @Query("deType") deType: String?
    ): Response<ApiResponse<InstantClassResponse>>

    @GET("class/all/count-schedule-classes")
    suspend fun getFilterScheduleScreenDataClass(
        @Query("interestIds") interestIds: String?,
        @Query("priceLowerLimit") priceLowerLimit: Float?,
        @Query("priceUpperLimit") priceUpperLimit: Float?,
        @Query("rating") rating: Float?,
        @Query("durationArray") durationArray: String?,
        @Query("level") level: String?,
        @Query("languageIds") languageIds: String?,
        @Query("deType") deType: String?
    ): Response<ApiResponse<SearchScheduleClassResponse>>

    @GET("class/instant-classes/search-history")
    suspend fun getRecentSearch(): Response<ApiResponse<List<SearchDto>>>

    @GET("class/instant/detail")
    suspend fun getGetClassData(@Query("instantClassId") interestIds: String):
            Response<ApiResponse<ClassDetailDto>>

    @POST("class/instant/book")
    suspend fun bookClass(
        @Body bookClassRequest: BookClassRequest
    ): Response<ApiResponse<BookingResponse>>

    @FormUrlEncoded
    @POST("class/cancel")
    suspend fun cancelBooking(
        @Field("bookingId") bookingId: String
    ): Response<ApiResponse<BookingDetailResponse>>

    @GET("class/schedule/reviews")
    suspend fun getScheduleClassReviews(
        @Query("id") title: String,
        @Query("limit") limit: Int?,
        @Query("skip") skip: Int?
    ): Response<ApiResponse<ScheduleClassReviewsResponse>>*/
}