package com.jtwaller.tbdforreddit

import android.content.Context
import org.joda.time.Period

fun Period.printLongestUnit(c: Context): String {
    val tmp = this.normalizedStandard()
    val res = c.resources

    return if (tmp.years > 0) String.format(res.getString(R.string.year_abbrev), tmp.years)
    else if (tmp.months > 0) String.format(res.getString(R.string.month_abbrev), tmp.months)
    else if (tmp.weeks > 0) String.format(res.getString(R.string.week_abbrev), tmp.weeks)
    else if (tmp.days > 0) String.format(res.getString(R.string.day_abbrev), tmp.days)
    else if (tmp.hours > 0) String.format(res.getString(R.string.hour_abbrev), tmp.hours)
    else if (tmp.minutes > 0) String.format(res.getString(R.string.minute_abbrev), tmp.minutes)
    else kotlin.String.format(res.getString(R.string.second_abbrev), tmp.seconds)
}

fun Period.toLongAgeString(c: Context): String {
    val tmp = this.normalizedStandard()
    val res = c.resources

    return if (tmp.years > 0) String.format(res.getString(R.string.years_ago), tmp.years)
    else if (tmp.months > 0) String.format(res.getString(R.string.months_ago), tmp.months)
    else if (tmp.weeks > 0) String.format(res.getString(R.string.weeks_ago), tmp.weeks)
    else if (tmp.days > 0) String.format(res.getString(R.string.days_ago), tmp.days)
    else if (tmp.hours > 0) String.format(res.getString(R.string.hours_ago), tmp.hours)
    else if (tmp.minutes > 0) String.format(res.getString(R.string.minutes_ago), tmp.minutes)
    else kotlin.String.format(res.getString(R.string.seconds_ago), tmp.seconds)
}