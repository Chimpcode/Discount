package com.chimpcode.discount.common

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by anargu on 2/2/18.
 */
fun toISO8601UTC(date : Date) : String {
    val tz : TimeZone = TimeZone.getTimeZone("UTC")
    val df : DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    df.setTimeZone(tz)
    return df.format(date)
}

fun fromISO8601UTC(dateStr : String) : Date {
    val tz : TimeZone = TimeZone.getTimeZone("UTC")
    val df : DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    df.setTimeZone(tz)

    return df.parse(dateStr)
}
