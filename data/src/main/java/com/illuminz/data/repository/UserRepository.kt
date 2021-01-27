package com.illuminz.data.repository

import com.illuminz.data.models.common.Resource
import com.illuminz.data.models.master.MasterDataResponse
import com.illuminz.data.models.request.*
import com.illuminz.data.models.request.loginsignup.SignupRequest
import com.illuminz.data.models.sendmoney.AccountDetailsResponse
import com.illuminz.data.models.sendmoney.CountryResponse
import com.illuminz.data.models.transfers.TransactionDto
import com.illuminz.data.models.transfers.TransfersResponse
import com.illuminz.data.models.user.OccupationDto
import com.illuminz.data.models.user.UserDto
import com.illuminz.data.repository.listeners.RefreshListener

interface UserRepository {
    fun isLoggedIn(): Boolean
    fun saveLoginState()
    fun getCachedProfile(): UserDto?
    fun saveProfile(profile: UserDto?)
    fun saveEmail(email: String)
    fun saveName(name: String)
    fun updateKYCStatus(status: Int)
    fun clear()
    fun addRefreshTransfersListener(listener: RefreshListener)
    fun removeRefreshTransfersListener(listener: RefreshListener)
    fun refreshTransfers()
    fun addRefreshDataListener(listener: RefreshListener)
    fun removeRefreshDataListener(listener: RefreshListener)
    fun refreshProfileData()
    suspend fun loginUser(signupRequest: SignupRequest): Resource<UserDto>
    suspend fun sendVerification(signupRequest: SignupRequest): Resource<Any>
    suspend fun sendForgotVerification(signupRequest: SignupRequest): Resource<Any>
    suspend fun editPhoneNumber(signupRequest: SignupRequest): Resource<UserDto>
    suspend fun sendEditPhoneVerification(signupRequest: SignupRequest): Resource<Any>
    suspend fun signUp(signupRequest: SignupRequest): Resource<UserDto>
    suspend fun sendPhoneVerificationCode(signupRequest: SignupRequest): Resource<UserDto>
    suspend fun forgotPassword(signupRequest: SignupRequest): Resource<UserDto>
    suspend fun getOccupations(): Resource<List<OccupationDto>>
    suspend fun updatePassword(signupRequest: SignupRequest): Resource<UserDto>
    suspend fun logout(): Resource<Any>
    suspend fun updateEmail(email: String): Resource<Any>
    suspend fun updateFullName(fullName: String): Resource<Any>
    suspend fun getMasterData(): Resource<MasterDataResponse>
    suspend fun sendKYCVerification(getkYCStartRequest: KYCStartRequest): Resource<UserDto>
    suspend fun getTransfers(request: TransfersRequest): Resource<TransfersResponse>
    suspend fun contactUsForm(
        name: String?,
        email: String?,
        message: String?
    ): Resource<Any>

    suspend fun getWalletTransaction(request: TransfersRequest): Resource<TransfersResponse>
    suspend fun applyKycManually(kycManualRequest: KYCManualRequest): Resource<Any>
    suspend fun getCountryCurrentCharges(): Resource<List<CountryResponse>>
    suspend fun getAdminAccountInfo(): Resource<AccountDetailsResponse>
    suspend fun getCharges(): Resource<AccountDetailsResponse>
    suspend fun exchangeRateConversion(exchangeRateDto: ExchangeRateDto): Resource<UserDto>
    suspend fun transferMoney(transactionRequest: TransactionDto?): Resource<TransactionDto>
    suspend fun giveTransferFeedback(rateTransferRequest: RateTransferRequest): Resource<Any>


}