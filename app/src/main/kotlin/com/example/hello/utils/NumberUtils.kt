package com.example.hello.utils

object NumberUtils {
    fun toPersianDigits(input: String): String {
        var result = input
        val englishDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val persianDigits = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        
        for (i in 0..9) {
            result = result.replace(englishDigits[i], persianDigits[i])
        }
        return result
    }
}
