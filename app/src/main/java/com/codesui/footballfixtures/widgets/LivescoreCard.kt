package com.codesui.footballfixtures.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.resources.Routes
import com.google.gson.JsonObject

@Composable
fun LivescoreCard(item: JsonObject, navController: NavController, runAds: () -> Unit){
    val isDarkTheme = isSystemInDarkTheme()

    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.DarkGray else Color.White
    val id = item.get("match_id").asString
    val homeTeam = item.get("match_hometeam_name").asString
    val awayTeam = item.get("match_awayteam_name").asString
    val homeTeamIcon = item.get("team_home_badge").asString
    val awayTeamIcon = item.get("team_away_badge").asString
    val homeScore = item.get("match_hometeam_score").asString
    val awayScore = item.get("match_awayteam_score").asString

    val status = if(item.get("match_status").asString.equals("Half Time")){
        "HT"
    } else {
        item.get("match_status").asString
    }

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
                    route = Routes.fixtureScreen + "/${id}"
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
                .padding(5.dp)
                .fillMaxWidth()
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$status'",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red,
                modifier = Modifier
                    .width(40.dp)
            )
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AsyncImage(
                        model = homeTeamIcon,
                        contentDescription = stringResource(id = R.string.app_name),
                        placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                        error = painterResource(id = R.mipmap.ic_launcher_foreground),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = homeTeam,
                        modifier = Modifier
                            .weight(4f)
                            .fillMaxHeight(),
                        color = textColor,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = homeScore,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        fontSize = 18.sp,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AsyncImage(
                        model = awayTeamIcon,
                        contentDescription = stringResource(id = R.string.app_name),
                        placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                        error = painterResource(id = R.mipmap.ic_launcher_foreground),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = awayTeam,
                        modifier = Modifier
                            .weight(4f)
                            .fillMaxHeight(),
                        color = textColor,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = awayScore,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        fontSize = 18.sp,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }

            }
        }
    }
}