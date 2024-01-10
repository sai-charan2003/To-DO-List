package com.example.to_do.dateconverters

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun convertDateFormat(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val currentDate = LocalDate.now()
    val date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    return if (date == currentDate) {
        "Today"
    } else {
        outputFormat.format(inputFormat.parse(inputDate)!!)
    }
}
fun convertDateFormatfororder(inputDate: String): String {
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}
fun stringToLocalDate(dateString: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return LocalDate.parse(dateString, formatter)
}
fun getFormattedDate(input: String): LocalDate {
    return if (input.lowercase() == "today") {
        LocalDate.now()
    } else {
        // Return current date or handle other cases as needed
        LocalDate.now()
    }
}