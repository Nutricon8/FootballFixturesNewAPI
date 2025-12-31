package com.codesui.footballfixtures.screens.fixture

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codesui.footballfixtures.widgets.EmptyScreen
import com.codesui.footballfixtures.navigation.Routes
import com.google.gson.JsonObject

@Composable
fun LineupScreen(navController: NavController, lineup: JsonObject) {
    val homeLineup = remember { mutableStateOf<List<JsonObject>?>(null) }
    val awayLineup = remember { mutableStateOf<List<JsonObject>?>(null) }
    LaunchedEffect(Unit) {
        homeLineup.value = lineup.getAsJsonObject("home").asJsonObject.getAsJsonArray("starting_lineups").map { it.asJsonObject }
        awayLineup.value = lineup.getAsJsonObject("away").asJsonObject.getAsJsonArray("starting_lineups").map { it.asJsonObject }
    }

    Box(modifier = Modifier.fillMaxSize()){
        when {
            homeLineup.value != null &&  awayLineup.value != null  -> {
                if (homeLineup.value!!.isEmpty()) {
                    EmptyScreen("line up")
                } else {
                    Row (modifier = Modifier.fillMaxSize()){
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            items(items = homeLineup.value!!) { player ->
                                LineupCard(navController, player)
                            }
                        }
                        Divider(
                            color = Color.Black,
                            modifier = Modifier
                                .padding(3.dp)
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            items(items = awayLineup.value!!) { player ->
                                LineupCard(navController, player)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LineupCard(navController: NavController, player: JsonObject) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            .clickable {
                navController.navigate(
                    route = Routes.playerScreen + "/${player.get("player_key").asString}"
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = player.get("lineup_position").asString,
            textAlign = TextAlign.Center,
            color = Color.Red,
            modifier = Modifier
                .width(20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .weight(1f)
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                player.get("lineup_player").asString,
                fontWeight = FontWeight.Light,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Divider(modifier = Modifier.width(10.dp))
            Text(
                player.get("lineup_number").asString,
                modifier = Modifier
                    .width(25.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.LightGray,
                            radius = this.size.maxDimension / 2
                        )
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}