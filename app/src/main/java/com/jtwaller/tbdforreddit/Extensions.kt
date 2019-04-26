package com.jtwaller.tbdforreddit

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.CHILDREN
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.COMMENT
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.DATA
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.KIND
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.LISTING
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.REPLIES
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

fun Int.abbreviateThousands(c: Context): String {
    return if (this < 1000) {
        this.toString()
    } else {
        String.format(c.getString(R.string.thousands_abbrev), this.toFloat() / 1000)
    }
}

val JsonElement.kind: String
    get() = asJsonObject.get(KIND).asString

val JsonElement.data: JsonElement
    get() = asJsonObject.get(DATA)

fun JsonElement.getReplies(): JsonElement {
    if (kind != COMMENT) throw RuntimeException("Cannot obtain replies from non-comment object")
    return asJsonObject.data.asJsonObject.get(REPLIES)
}

fun JsonElement.getChildren(): JsonArray {
    if (kind != LISTING) throw RuntimeException("Invalid reply list")
    return data.asJsonObject.get(CHILDREN).asJsonArray
}