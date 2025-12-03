package com.example.lumena.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    private val iso = DateTimeFormatter.ISO_LOCAL_DATE
    @RequiresApi(Build.VERSION_CODES.O)
    fun todayIso(): String = LocalDate.now().format(iso)
}
