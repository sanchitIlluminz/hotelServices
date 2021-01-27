package com.core.utils

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

object ValidationUtils {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordLengthValid(password: String): Boolean {
        return password.length >= 8
    }

    fun isPhoneNumberLengthValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 9
    }

    fun isBankAccountNumberLengthValid(bankAccountNumber: String): Boolean {
        return (bankAccountNumber.length in 9..18)
    }

    fun isVerificationCodeLengthValid(verificationCode: String): Boolean {
        return verificationCode.length == 4
    }

    fun isValidIFSCode(str: String?): Boolean {
        // Regex to check valid IFSC Code.
        val regex = "^[A-Z]{4}0[A-Z0-9]{6}$"

        // Compile the ReGex
        val p: Pattern = Pattern.compile(regex)

        // If the string is empty
        // return false
        if (str == null) {
            return false
        }

        // Pattern class contains matcher()
        // method to find matching between
        // the given string and
        // the regular expression.
        val m: Matcher = p.matcher(str)

        // Return if the string
        // matched the ReGex
        return m.matches()
    }
}