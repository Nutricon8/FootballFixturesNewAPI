package com.codesui.footballfixtures.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.navigation.Routes
import com.google.gson.JsonObject

@Composable
fun LeagueCard(league : JsonObject, navController: NavController, runAds: () -> Unit){
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.Black else Color.White
    val leagueLogo = league.get("league_logo").asString
    val countryLogo = league.get("country_logo").asString
    val leagueName = league.get("league_name").asString
    val leagueCountry = league.get("country_name").asString
    val leagueId = league.get("league_id").asString
    val season = league.get("league_season").asString

    Card (
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .drawBehind {
                val strokeWidth = Stroke.DefaultMiter
                val y = size.height
                drawLine(
                    Color.LightGray,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
            .padding(2.dp)
            .clickable {
                navController.navigate(
                    route = Routes.leagueScreen + "/${leagueId}"
                )
                runAds.invoke()
            },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = leagueLogo,
                contentDescription = stringResource(id = R.string.app_name),
                placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                error = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .wrapContentHeight()
                    .clip(CircleShape),
            )
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "$leagueName - $season",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,

                ) {
                    Text(
                        text = leagueCountry,
                        modifier = Modifier
                            .wrapContentWidth(),
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = countryLogo,
                        contentDescription = stringResource(id = R.string.app_name),
                        placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                        error = painterResource(id = R.mipmap.ic_launcher_foreground),
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .width(25.dp)
                            .height(20.dp)
                            .wrapContentHeight()
                            .clip(RectangleShape),
                    )
                }
            }
        }

    }
}