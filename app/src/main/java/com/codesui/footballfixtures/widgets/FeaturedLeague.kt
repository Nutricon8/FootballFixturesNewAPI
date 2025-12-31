package com.codesui.footballfixtures.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codesui.footballfixtures.R
import com.google.gson.JsonObject

@Composable
fun FeaturedLeague (league: JsonObject, id: String, onMutableValueChange: (String) -> Unit, runAds: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.DarkGray else Color.White
    val leagueLogo = league.get("league_logo").asString
    val leagueName = league.get("league_name").asString
    val leagueId = league.get("league_id").asString

    Card (
        modifier = Modifier
            .padding(2.dp)
            .width(200.dp)
            .wrapContentHeight()
            .padding(3.dp)
            .clickable {
                onMutableValueChange(leagueId)
                runAds.invoke()
            },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box (
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = leagueLogo,
                contentDescription = leagueName,
                placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.DarkGray,
                            if (id.equals(leagueId)) MaterialTheme.colorScheme.primary else bgColor//Color.Black
                        ),
                        startY = 50f
                    )
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ){
                Text(
                    leagueName,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}