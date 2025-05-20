package com.example.club.feature.admin.events.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.club.feature.admin.events.R
import com.example.club.shared.event.domain.entity.AgeRatings
import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.util.formatting.formatDateSelected
import com.example.club.util.formatting.formatTimeSelected


@Composable
fun EventDetailsDialog(
    event: EventDetails,
    onDismiss: () -> Unit
) {
    Popup(alignment = Alignment.Center) {
        Box(
            modifier = Modifier
                //.fillMaxSize()
                 .background(
                       MaterialTheme.colorScheme.surfaceVariant,
                       shape = MaterialTheme.shapes.medium
                   )
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)

                    .padding(10.dp)
            ) {
                // Заголовок мероприятия
                Text(
                    text = stringResource(
                        R.string.eventDetail_title, event.title
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                EventImage(event)
                // Информация о мероприятии
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource( R.string.eventDetail_dataTime))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append(formatDateSelected(event.date) )
                            append(" ")
                            append(  formatTimeSelected(event.date))
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.eventDetail_duration))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append(event.duration.toString())
                            append(" мин")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text =
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.eventDetail_genre))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append(event.genre.joinToString(", "))
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append( stringResource(R.string.eventDetail_ageRating))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            val ageRatingText = when (event.ageRating) {
                                AgeRatings.G -> "Для всех"
                                AgeRatings.PG -> "С родительским контролем"
                                AgeRatings.PG13 -> "13+"
                                AgeRatings.R -> "16+"
                                AgeRatings.NC17 -> "18+"
                            }
                            append(ageRatingText)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text =buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append( stringResource(R.string.eventDetail_description))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append(event.description)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append( stringResource(R.string.eventDetail_artists))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append( event.artists.joinToString(", ") { it.name })
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append( stringResource(R.string.eventDetail_status))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                            append(event.status.name)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onDismiss) {
                        Text(stringResource(id = R.string.error_close))
                    }
            }

        }
    }

}

@Composable
fun EventImage(item: EventDetails) {
    //val baseUrl = "http://localhost:8090/"
    val imagePath = item.imgPreview
    println(imagePath)
    //val fullUrl = "$baseUrl$imagePath"

    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .focusable()
            .padding(top = 8.dp, start = 5.dp, end = 5.dp)
    ) {
        val context = LocalContext.current
        val imageRequest = ImageRequest.Builder(context)
            .data(imagePath)
            .crossfade(true)
            .build()
        val painter = rememberAsyncImagePainter(imageRequest)

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

