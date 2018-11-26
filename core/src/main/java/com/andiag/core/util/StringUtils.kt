package com.andiag.core.util

object StringUtils {

    fun toTitleCase(s: String?): String? {
        if (s != null) {
            val actionableDelimiters = " '-/"
            val sb = StringBuilder()
            var capNext = true
            for (c in s.toCharArray()) {
                sb.append(if (capNext) Character.toUpperCase(c) else Character.toLowerCase(c))
                capNext = actionableDelimiters.indexOf(c) >= 0
            }
            return sb.toString()
        }
        return null
    }

}
