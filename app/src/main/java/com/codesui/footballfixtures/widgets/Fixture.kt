package com.codesui.footballfixtures.widgets


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.codesui.footballfixtures.navigation.Routes
import com.google.gson.JsonObject

@Composable
fun Fixture(item:JsonObject,navController: NavController, runAds: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.Black else Color.White
    val id = item.get("match_id").asString
    val homeTeam = item.get("match_hometeam_name").asString
    val awayTeam = item.get("match_awayteam_name").asString
    val homeTeamIcon = item.get("team_home_badge").asString
    val awayTeamIcon = item.get("team_away_badge").asString
    val date = if(item.get("match_status").asString.equals("Finished")) {
        "FT " +  item.get("match_hometeam_score").asString + "-" + item.get("match_awayteam_score").asString
    } else item.get("match_date").asString

    val time = item.get("match_time").asString
    Card(
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
            .aspectRatio(6f)
            .clickable {
                navController.navigate(
                    route = Routes.fixtureScreen + "/${id}"
                )
                runAds.invoke()
            },
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
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
                        .size(30.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = homeTeam,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .weight(1.3f)
                    .padding(0.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date,
                    fontSize = 18.sp,
                    fontWeight =  FontWeight.Light,
                    color = textColor,
                    modifier = Modifier
                        .border(
                            1.dp,
                            colorResource(id = R.color.purple_700),
                            CircleShape
                        )
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                )
                Text(
                    text = time,
                    fontSize = 16.sp,
                    color = textColor,
                    fontWeight =  FontWeight.SemiBold
                )
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
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
                        .size(30.dp)
                        .clip(RectangleShape)
                )
                Text(
                    text = awayTeam,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

            }
        }
    }
}
