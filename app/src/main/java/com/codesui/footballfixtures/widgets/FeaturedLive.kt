package com.codesui.footballfixtures.widgets

import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
fun FeaturedLive(item: JsonObject, navController: NavController, runAds: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()

    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.Black else Color.White
    val id = item.get("match_id").asString
    val homeTeam = item.get("match_hometeam_name").asString
    val awayTeam = item.get("match_awayteam_name").asString
    val homeTeamIcon = item.get("team_home_badge").asString
    val awayTeamIcon = item.get("team_away_badge").asString
    val homeScore = item.get("match_hometeam_score").asString
    val awayScore = item.get("match_awayteam_score").asString
    val league = item.get("league_name").asString

    val status = if(item.get("match_status").asString.equals("Half Time")){
        "HT"
    } else {
        item.get("match_status").asString
    }
    Card (
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                1.dp,
                colorResource(id = R.color.purple_700),
                RoundedCornerShape(5)
            )
            .clickable {
                navController.navigate(
                    route = Routes.fixtureScreen + "/${id}"
                )
                runAds.invoke()
            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            //verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = league,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 5.dp),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = textColor,
                maxLines = 2,
                overflow = TextOverflow.Clip
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(3.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = homeTeamIcon,
                        contentDescription = stringResource(id = R.string.app_name),
                        placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                        error = painterResource(id = R.mipmap.ic_launcher_foreground),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = homeTeam,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }



                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.5f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$homeScore-$awayScore",
                        fontSize = 18.sp,
                        fontWeight =  FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxSize()
                            .weight(1f)
                            .border(
                                1.dp,
                                colorResource(id = R.color.purple_700),
                                CircleShape
                            )
                            .padding(5.dp)
                    )
                    Text(
                        text = status,
                        fontSize = 16.sp,
                        fontWeight =  FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxSize()
                            .weight(1f)
                    )
                }

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = awayTeamIcon,
                        contentDescription = stringResource(id = R.string.app_name),
                        placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                        error = painterResource(id = R.mipmap.ic_launcher_foreground),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = awayTeam,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
}