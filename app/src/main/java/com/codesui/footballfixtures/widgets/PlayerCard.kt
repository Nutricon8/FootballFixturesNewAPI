package com.codesui.footballfixtures.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codesui.footballfixtures.navigation.Routes
import com.google.gson.JsonObject

@Composable
fun PlayerCard(navController: NavController, player: JsonObject, runAds: () -> Unit){
    val name = player.get("player_name").asString
    val teamName = player.get("team_name").asString
    val playerId = player.get("player_key").asString
    val goals = player.get("goals").asString
    val assists = player.get("assists").asString
    val position = player.get("player_place").asString

    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.Black else Color.White
    val bgColor2 = if (isDarkTheme) Color.Gray else Color.LightGray
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
            .clickable {
                navController.navigate(
                    route = Routes.playerScreen + "/${playerId}"
                )
                runAds.invoke()
            },
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ){
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = position,
                fontSize = 16.sp,
            )
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                    Text(
                        text = teamName,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = textColor,
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f)
                            .background(
                                color = bgColor2,
                                shape = RoundedCornerShape(16.dp) // 16.dp corner radius
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Goals",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = textColor,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 8.dp)
                        )
                        Text(
                            text = goals,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = textColor,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(48.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f)
                            .background(
                                color = bgColor2,
                                shape = RoundedCornerShape(16.dp) // 16.dp corner radius
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Assists",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = textColor,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 8.dp)
                        )
                        Text(
                            text = if (assists.isNullOrEmpty()) {
                                "0"
                            } else {
                                assists
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = textColor,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}