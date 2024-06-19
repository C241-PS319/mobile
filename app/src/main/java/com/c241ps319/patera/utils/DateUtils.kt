package com.c241ps319.patera.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    private val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formatDate(isoDate: String): String {
        val dateTime = ZonedDateTime.parse(isoDate, inputFormatter)
        return dateTime.format(dateFormatter)
    }

    fun formatTime(isoDate: String): String {
        val dateTime = ZonedDateTime.parse(isoDate, inputFormatter)
        return dateTime.format(timeFormatter)
    }
}
