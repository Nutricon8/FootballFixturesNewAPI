package com.codesui.footballfixtures.screens.league

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.api.Requests.RetrofitClient
import com.codesui.footballfixtures.widgets.EmptyScreen
import com.codesui.footballfixtures.widgets.ErrorDialog
import com.codesui.footballfixtures.widgets.IndeterminateCircularIndicator
import com.codesui.footballfixtures.widgets.NoInternetDialog
import com.codesui.footballfixtures.widgets.isInternetAvailable
import com.google.gson.JsonObject

@Composable
fun StandingsScreen(navController: NavController, leagueId: String){
    val standings = remember { mutableStateOf<List<JsonObject>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }

    val params = mapOf("action" to "get_standings", "league_id" to leagueId, "APIkey" to stringResource(id = R.string.api_key))
    var isButtonClicked by remember { mutableStateOf(true) }
    LaunchedEffect(isButtonClicked) {
        if (isButtonClicked) {
            try {
                val response = RetrofitClient.apiService.getStandings(params)
                standings.value = response.map { it.asJsonObject }
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
            isButtonClicked = false // Reset state after task
        }
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ){
        when {
            isLoading.value -> {
                IndeterminateCircularIndicator()
            }

            error.value != null -> {
                when{
                    !isInternetAvailable(LocalContext.current) -> {
                        NoInternetDialog {
                            isButtonClicked = true
                            isLoading.value = true
                            error.value = null
                        }
                    }

                    isInternetAvailable(LocalContext.current) -> {
                        ErrorDialog()
                    }
                }
            }

            standings.value != null -> {
                if (standings.value!!.isEmpty()) {
                    EmptyScreen("league")
                } else {
                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = "Team",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5.5f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "MP",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "W",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "D",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "L",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "GD",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "PT",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = standings.value!!) { standing ->
                            TeamStats(standing, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamStats(team: JsonObject, navController: NavController) {

    val rank = team.get("overall_league_position").asString
    val teamLogo = team.get("team_badge").asString
    val teamName = team.get("team_name").asString
    val teamId = team.get("team_id").asString
    val teamPoints = team.get("overall_league_PTS").asString
    val gamesPlayed = team.get("overall_league_payed").asString
    val teamWin = team.get("overall_league_W").asString
    val teamDraw = team.get("overall_league_D").asString
    val teamLose = team.get("overall_league_L").asString

    val goalFor = team.get("away_league_GF").asString.toInt()
    val goalAgainst = team.get("away_league_GA").asString.toInt()
    val goalDiff = (goalFor - goalAgainst).toString()

    Row (
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
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(5.5f)
                .wrapContentHeight(Alignment.CenterVertically),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rank,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .wrapContentHeight(Alignment.CenterVertically),
            )
            AsyncImage(
                model = teamLogo,
                contentDescription = stringResource(id = R.string.app_name),
                placeholder = painterResource(id = R.drawable.w1),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .width(20.dp)
                    .height(20.dp)
                    .clip(CircleShape)
            )
            Text(
                text = teamName,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f)
                    .wrapContentHeight(Alignment.CenterVertically),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = gamesPlayed,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.CenterVertically),
        )
        Text(
            text = teamWin,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.CenterVertically),
        )
        Text(
            text = teamDraw,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.CenterVertically),
        )
        Text(
            text = teamLose,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.CenterVertically),
        )
        Text(
            text = goalDiff,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.CenterVertically),
        )
        Text(
            text = teamPoints,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.CenterVertically),
        )
    }
}