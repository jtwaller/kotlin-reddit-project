package com.jtwaller.tbdforreddit

import android.content.Context
import org.joda.time.Period

fun Period.printLongestUnit(c: Context): String {
    val tmp = this.normalizedStandard()
    val res = c.resources

    return if (tmp.years > 0) kotlin.String.format(res.getString(R.string.year_abbrev), tmp.years)
    else if (tmp.months > 0) kotlin.String.format(res.getString(R.string.month_abbrev), tmp.months)
    else if (tmp.weeks > 0) kotlin.String.format(res.getString(R.string.week_abbrev), tmp.weeks)
    else if (tmp.days > 0) kotlin.String.format(res.getString(R.string.day_abbrev), tmp.days)
    else if (tmp.hours > 0) kotlin.String.format(res.getString(R.string.hour_abbrev), tmp.hours)
    else if (tmp.minutes > 0) kotlin.String.format(res.getString(R.string.minute_abbrev), tmp.minutes)
    else kotlin.String.format(res.getString(R.string.second_abbrev), tmp.seconds)
}