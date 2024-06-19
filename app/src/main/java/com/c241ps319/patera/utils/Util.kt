package com.c241ps319.patera.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDateAndDay(): String {
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    val currentDate = Date()
    return dateFormat.format(currentDate)
}