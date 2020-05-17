package com.android.friendycar.presentation.common

import android.content.Context
import android.text.format.DateFormat
import android.util.TypedValue
import androidx.fragment.app.Fragment
import com.android.friendycar.model.common.getValue
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.roundToInt

fun String.isNumeric(): Boolean {
    return this.matches("-?\\d+(\\.\\d+)?".toRegex()) //match a number with optional '-' and decimal.
}

fun String.isValidEmailAddress(): Boolean {
    val ePattern =
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    val p = Pattern.compile(ePattern)
    val m = p.matcher(this)
    return m.matches()
}

fun String.setTodate(): String {
    val dateFormat = SimpleDateFormat("d MMM,h:mm a")
    //dateFormat.timeZone = TimeZone.getTimeZone("EET-1")
    // dateFormat.timeZone = TimeZone.getTimeZone("Egypt")
    dateFormat.timeZone = TimeZone.getDefault()
    val dayAfter17 = (Calendar.getInstance().time).time + 17L * 24 * 60 * 60 * 1000
    return dateFormat.format(dayAfter17)
}

fun String.getPlaceName(): String {
    if (this.isEmpty() || this.indexOf("(") == -1) return ""
    return this.substring(0, this.indexOf("(") - 2)
}

fun String.getCountryId(): Int {
    if (this == "/countries/64" || this.isEmpty() || this.lastIndexOf("/") == -1) return 64
    return this.substring(this.lastIndexOf("/") + 1).toInt()
}

fun String.setFromdate(): String {
    val dateFormat = SimpleDateFormat("d MMM,h:mm a")
    // dateFormat.timeZone = TimeZone.getTimeZone("UTC+2")
    dateFormat.timeZone = TimeZone.getDefault()
    val dayAfter10 = (Calendar.getInstance().time).time + 10L * 24 * 60 * 60 * 1000
    return dateFormat.format(dayAfter10)

}

fun String.formatDateToanth(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val outputFormat = SimpleDateFormat("d MMM,hh:mm a")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.formatDateLongToanth(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val outputFormat = SimpleDateFormat("d MMM,yyyy")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.formatDateReview(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
    val outputFormat = SimpleDateFormat("d MMM,yyyy")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.formatDateBorrowing(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
    val outputFormat = SimpleDateFormat("EEE d/MM/yy, hh:mm a")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.formatDateBorrowingDetail(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
    val outputFormat = SimpleDateFormat("MMM dd, yyyy, hh:mm a")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.formatConfirmRequest(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val outputFormat = SimpleDateFormat("EEE d/MM/yy, HH:mm")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun Date.apiDateFormat(): String {
    return DateFormat.format("yyyy-MM-dd HH:mm", this).toString()


}

fun Int.getDateFormatFilter(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    dateFormat.timeZone = TimeZone.getDefault()
    val daystring = (Calendar.getInstance().time).time + (this.toLong() * 24 * 60 * 60 * 1000)
    return dateFormat.format(daystring)
}

fun Int.getDateFormatFilterWithoutTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    dateFormat.timeZone = TimeZone.getDefault()
    val daystring = (Calendar.getInstance().time).time + (this.toLong() * 24 * 60 * 60 * 1000)
    return dateFormat.format(daystring)
}

fun String.getMinMaxYear(): Int {
    var cal: Calendar = Calendar.getInstance()
    cal.get(Calendar.YEAR)
    if (this == "Min") {
        return cal.get(Calendar.YEAR) - 10

    } else
        return cal.get(Calendar.YEAR) + 1
}

fun Date.toSimpleString(): String {

    val dateFormat = SimpleDateFormat("EEE,MMMd")
    return dateFormat.format(this)
}

fun Date.toSimpleTodayString(): String {
    return " Today " /*+ DateFormat.format("hh:mm", this).toString()*/

}

fun String.dayAfterYear(): Date {
    var cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.YEAR, 1)
    var nextYear: Date = cal.time
    return nextYear
}

fun IntRange.rangeIntToArrayStrings(): ArrayList<String>? {
    var returenedList = arrayListOf<String>()
    for (int in this) {
        if (int == 0)
            returenedList.add("00")
        else returenedList.add(int.toString())
    }
    return returenedList
}

fun String.beforeOrNot(anotherDate: String): Boolean {
    val date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(this)
    val dateTo = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(anotherDate)
    return date.before(dateTo)


}

fun String.getPreviousDate(numDatePrevious: Int): String {
    var inputDate = this
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
    try {
        val date = format.parse(inputDate)
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, -numDatePrevious)
        inputDate = format.format(c.time)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        inputDate = ""
    }
    return inputDate
}

fun String.stringToDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(this)

}

fun String.getDatesBetweenUsing(
    startDate: Date, endDate: Date
): List<Date> {
    val datesInRange = arrayListOf<Date>()
    val calendar: Calendar = /*GregorianCalendar()*/Calendar.getInstance()
    calendar.time = startDate
    val endCalendar: Calendar = Calendar.getInstance()
    endCalendar.time = endDate
    while (calendar.before(endCalendar)) {
        val result = calendar.time
        datesInRange.add(result)
        calendar.add(Calendar.DATE, 1)
    }
    return datesInRange
}

fun List<Date>.getStartList(): MutableList<String>? {
    var listDatesStrings = arrayListOf<String>()
    for (date in this.indices) {
        if (date == 0) {
            listDatesStrings.add(this[date].toSimpleTodayString())

        } else
            listDatesStrings.add(this[date].toSimpleString())
    }
    return listDatesStrings
}


fun String.passwordValidation(): Boolean {
    var flag = true

    val characterPatten = Pattern.compile("[a-zA-Z ]")
    val digitCasePatten = Pattern.compile("[0-9 ]")
    if (this.length < 8) {
        flag = false
    }


    if (!characterPatten.matcher(this).find()) {
        flag = false
    }
    if (!digitCasePatten.matcher(this).find()) {
        flag = false
    }
    return flag
}

fun String.getAge(year: Int, month: Int, day: Int): String {
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()

    dob.set(year, month, day)

    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    val ageInt = age + 1

    return ageInt.toString()
}

fun getDateDiff(
    format: SimpleDateFormat,
    oldDate: String?,
    newDate: String?
): Long {
    return try {
        TimeUnit.DAYS.convert(
            format.parse(newDate).time - format.parse(oldDate).time, TimeUnit.MILLISECONDS
        )
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun dipToPixel(i: Int, context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        (i).toFloat(),
        context.resources.displayMetrics
    )
        .roundToInt()
