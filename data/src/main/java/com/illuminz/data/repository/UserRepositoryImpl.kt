package com.illuminz.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.illuminz.data.extensions.toApiError
import com.illuminz.data.extensions.toApiFailure
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
import com.illuminz.data.remote.SocketManager
import com.illuminz.data.remote.UserApi
import com.illuminz.data.repository.listeners.RefreshListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val authorizationRepository: AuthorizationRepository,
    private val preferences: SharedPreferences,
    private val gson: Gson,
    private val socketManager: SocketManager
) : UserRepository {
    companion object {
        private const val KEY_PROFILE = "KEY_PROFILE"
        private const val KEY_LOGIN = "KEY_LOGIN"
    }

    private var cachedProfile: UserDto?
    private val refreshListeners by lazy { mutableSetOf<RefreshListener>() }

    init {
        cachedProfile = getProfileFromPreference()
    }

    override fun addRefreshDataListener(listener: RefreshListener) {
        refreshListeners.add(listener)
    }

    override fun removeRefreshDataListener(listener: RefreshListener) {
        refreshListeners.remove(listener)
    }

    override fun refreshProfileData() {
        GlobalScope.launch(Dispatchers.Main) {
            refreshListeners.forEach {
                it.refreshData()
            }
        }
    }


    override fun addRefreshTransfersListener(listener: RefreshListener) {
        refreshListeners.add(listener)
    }

    override fun removeRefreshTransfersListener(listener: RefreshListener) {
        refreshListeners.remove(listener)
    }

    override fun refreshTransfers() {
        GlobalScope.launch(Dispatchers.Main) {
            refreshListeners.forEach {
                it.refreshData()
            }
        }
    }

    private fun getProfileFromPreference(): UserDto? {
        val profileJson = preferences.getString(KEY_PROFILE, null)
        if (!profileJson.isNullOrBlank()) {
            return gson.fromJson(profileJson, UserDto::class.java)
        }
        return null
    }

    override fun saveLoginState() {
        preferences.edit().putBoolean(KEY_LOGIN, true).apply()
    }

    override fun saveProfile(profile: UserDto?) {
        cachedProfile = profile
        saveProfileInPreference(profile)
    }

    override fun saveEmail(email: String) {
        val profile = getProfileFromPreference()
        profile?.email = email
        saveProfile(profile)
    }

    override fun saveName(name: String) {
        val profile = getProfileFromPreference()
        profile?.name = name
        saveProfile(profile)
    }

    override fun updateKYCStatus(status: Int) {
        val profile = getProfileFromPreference()
        profile?.kyc_status = status
        saveProfile(profile)
    }

    private fun updateAddress(address: AddressDto) {
        val profile = getProfileFromPreference()
        profile?.address = address
        saveProfile(profile)
    }

    private fun saveProfileInPreference(profile: UserDto?) {
        if (profile != null) {
            val profileJson = gson.toJson(profile)
            preferences.edit().putString(KEY_PROFILE, profileJson).apply()
        } else {
            preferences.edit().remove(KEY_PROFILE).apply()
        }
    }

    override fun clear() {
        cachedProfile = null
        preferences.edit()
            .remove(KEY_PROFILE)
            .apply()
        preferences.edit()
            .remove(KEY_LOGIN)
            .apply()
        authorizationRepository.clear()
    }

    override fun isLoggedIn(): Boolean {
        return preferences.contains(KEY_LOGIN)
    }

    override fun getCachedProfile(): UserDto? = cachedProfile

    override suspend fun loginUser(signupRequest: SignupRequest): Resource<UserDto> {
        return try {
            val response = userApi.login(signupRequest)
            if (response.isSuccessful) {
                val profile = response.body()?.data
                saveProfile(profile)
                authorizationRepository.saveAuthorization(profile)
                saveLoginState()
                socketManager.disconnect()  // Disconnect after a new authorization is received
                Resource.success(profile)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun sendVerification(signupRequest: SignupRequest): Resource<Any> {
        return try {
            val response = userApi.sendPhoneVerification(signupRequest)

            if (response.isSuccessful) {
                Resource.success()
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun sendForgotVerification(signupRequest: SignupRequest): Resource<Any> {
        return try {
            val response = userApi.sendForgotVerification(signupRequest)

            if (response.isSuccessful) {
                Resource.success()
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun editPhoneNumber(signupRequest: SignupRequest): Resource<UserDto> {
        return try {
            val response = userApi.editPhoneNumber(signupRequest)

            if (response.isSuccessful) {
                val profile = response.body()?.data
                //saveProfile(profile)
                Resource.success(profile)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun sendEditPhoneVerification(signupRequest: SignupRequest): Resource<Any> {
        return try {
            val response = userApi.sendEditPhoneVerification(signupRequest)

            if (response.isSuccessful) {
                Resource.success()
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun signUp(signupRequest: SignupRequest): Resource<UserDto> {
        return try {
            val response = userApi.signUp(signupRequest)

            if (response.isSuccessful) {
                //Resource.success(response.body()?.data)
                val profile = response.body()?.data
                saveProfile(profile)
                authorizationRepository.saveAuthorization(profile)
                saveLoginState()
                socketManager.disconnect()  // Disconnect after a new authorization is received
                Resource.success(profile)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun sendPhoneVerificationCode(signupRequest: SignupRequest): Resource<UserDto> {
        return try {
            val response = userApi.sendPhoneVerificationCode(signupRequest)

            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun forgotPassword(signupRequest: SignupRequest): Resource<UserDto> {
        return try {
            val response = userApi.forgotPassword(signupRequest)

            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getOccupations(): Resource<List<OccupationDto>> {
        return try {
            val response = userApi.getOccupations()

            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun updatePassword(request: SignupRequest): Resource<UserDto> {
        return try {
            val response = userApi.updatePassword(request)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun logout(): Resource<Any> {
        return try {
            val response = userApi.logout()
            if (response.isSuccessful) {
                Resource.success()
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun updateEmail(email: String): Resource<Any> {
        return try {
            val response = userApi.updateEmail(email)
            if (response.isSuccessful) {
                Resource.success()
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun updateFullName(fullName: String): Resource<Any> {
        return try {
            val response = userApi.updateFullName(fullName)
            if (response.isSuccessful) {
                Resource.success()
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getMasterData(): Resource<MasterDataResponse> {
        return try {
            val response = userApi.getConstantData(10)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun sendKYCVerification(getkYCStartRequest: KYCStartRequest): Resource<UserDto> {
        return try {
            val response = userApi.sendKYCVerification(getkYCStartRequest)
            if (response.isSuccessful) {
                updateKYCStatus(2)
                getkYCStartRequest.address?.let { updateAddress(it) }
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getTransfers(request: TransfersRequest): Resource<TransfersResponse> {
        return try {
            val response = userApi.getMyTransfers(
                pageNumber = request.pageNumber,
                beneficiaryName = request.beneficiaryName,
                accountNumber = request.accountNumber,
                status = request.status
            )
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getWalletTransaction(request: TransfersRequest): Resource<TransfersResponse> {
        return try {
            val response = userApi.getWalletTransaction(
                pageNumber = request.pageNumber,
                type = request.type
            )
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun applyKycManually(kycManualRequest: KYCManualRequest): Resource<Any> {
        return try {
            val response = userApi.applyKycManually(kycManualRequest)
            if (response.isSuccessful) {
                updateKYCStatus(3)
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getCountryCurrentCharges(): Resource<List<CountryResponse>> {
        return try {
            val response = userApi.getCountryCurrencyCharges()

            if (response.isSuccessful) {
                Resource.success(response.body()?.data)

            } else {
                Resource.error(response.toApiError())
            }


        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getAdminAccountInfo(): Resource<AccountDetailsResponse> {
        return try {
            val response = userApi.getAdminAccountInfo()

            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())

            }

        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun getCharges(): Resource<AccountDetailsResponse> {
        return try {
            val response = userApi.getCharges()

            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())

            }

        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun exchangeRateConversion(exchangeRateDto: ExchangeRateDto): Resource<UserDto> {
        return try {
            val response = userApi.exchangeRate(exchangeRateDto)

            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }

        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun transferMoney(transactionRequest: TransactionDto?): Resource<TransactionDto> {
        return try {
            val response = userApi.transferMoney(transactionRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun giveTransferFeedback(rateTransferRequest: RateTransferRequest): Resource<Any> {
        return try {
            val response = userApi.transferMoneyFeedback(rateTransferRequest)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(response.toApiError())
            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }

    override suspend fun contactUsForm(
        name: String?,
        email: String?,
        message: String?
    ): Resource<Any> {
        return try {
            val response = userApi.contactUsForm(name, email, message)

            if (response.isSuccessful) {
                Resource.success()
            } else {
                Resource.error(response.toApiError())

            }
        } catch (throwable: Throwable) {
            Resource.error(throwable.toApiFailure())
        }
    }


}