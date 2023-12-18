package com.example.news.data.remote.dtos

import android.os.Build
import com.example.news.domain.model.New
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializable
data class NewDto(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)

fun NewDto.mapToNew(): New {
    return New(
        publisher = source?.name.orEmpty(),
        title = title.orEmpty(),
        url = url.orEmpty(),
        urlToImage = urlToImage.orEmpty(),
        publishedAt = publishedAt.orEmpty().formatTimeAgo()
    )
}

fun String.formatTimeAgo(): String {

    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX").withZone(ZoneOffset.UTC)
            val date = fmt.parse(this)
            val time = Instant.from(date)
            val now = Instant.now(Clock.systemUTC())

            val duration = Duration.between(time, now)
            val minDiff = duration.toMinutes()

            val fmtOut = DateTimeFormatter.ofPattern("dd-mm-yyyy hh:mm:ss").withZone(ZoneOffset.UTC)
            val fallBackString = fmtOut.format(time)

            if (minDiff > 1440) {
                val dayAgo = minDiff / 1440
                return dayAgo.toInt().toString().plus(" day ago")
            } else if (minDiff > 60) {
                val hour = minDiff / 60
                return hour.toInt().toString().plus(" hours ago")
            } else if (minDiff > 5) {
                return minDiff.toInt().toString().plus(" mins ago")
            } else if (minDiff > 0) {
                return "Just now"
            }

            return fallBackString
        } else {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val formattedDate = formatter.format(parser.parse("2018-12-14T09:55:00"))
            return formattedDate.toString()
        }

    } catch (e: Exception) {
        ""
    }

}



