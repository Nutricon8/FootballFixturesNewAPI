package com.codesui.footballfixtures.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.Requests.RetrofitClient
import com.codesui.footballfixtures.widgets.ErrorDialog
import com.codesui.footballfixtures.widgets.IndeterminateCircularIndicator
import com.codesui.footballfixtures.widgets.NoInternetDialog
import com.codesui.footballfixtures.widgets.Topbar
import com.codesui.footballfixtures.resources.isInternetAvailable
import com.codesui.powerkingtips.ads.AdmobBanner
import com.google.gson.JsonObject

@Composable
fun PlayerScreen(navController: NavController, runAds :() -> Unit, playerId: String) {
    val player = remember { mutableStateOf<JsonObject?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.Gray else Color.LightGray
    val params = mapOf("action" to "get_players", "player_id" to playerId, "APIkey" to stringResource(id = R.string.api_key))
    var isButtonClicked by remember { mutableStateOf(true) }
    LaunchedEffect(isButtonClicked) {
        if (isButtonClicked) {
            try {
                val response = RetrofitClient.apiService.getPlayer(params).first().asJsonObject
                player.value = response
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
            isButtonClicked = false // Reset state after task
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Topbar(title = "Player Screen") {
            navController.popBackStack()
            runAds .invoke()
        }
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

            player.value != null -> {
                val age = player.value!!.get("player_age").asString
                val firstName = player.value!!.get("player_name").asString
                val teamName = player.value!!.get("team_name").asString
                val photo = player.value!!.get("player_image").asString
                val injured = if(player.value!!.get("player_injured").asString.equals("Yes")) "\uD83C\uDFE5" else "⚡"
                val position = player.value!!.get("player_type").asString
                val number = player.value!!.get("player_number").asString.ifEmpty {"-"}
                val matches = player.value!!.get("player_match_played").asString
                val goals = player.value!!.get("player_goals").asString
                val assists = player.value!!.get("player_assists").asString.ifEmpty {"-"}
                val saves = player.value!!.get("player_saves").asString.ifEmpty {"-"}
                val form = player.value!!.get("player_rating").asString.ifEmpty {"-"}
                val minutes = player.value!!.get("player_minutes").asString.ifEmpty {"-"}
                val accuracy = player.value!!.get("player_passes_accuracy").asString.ifEmpty {"-"}

                //val league = stats.getAsJsonObject("league").get("name").asString
                val yellowCards = player.value!!.get("player_yellow_cards").asString
                val redCards = player.value!!.get("player_red_cards").asString
                //Text(text = player.value!!.toString())

                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.3f),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxHeight()
                                .weight(1f),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            AsyncImage(
                                model = photo,
                                contentDescription = stringResource(id = R.string.app_name),
                                placeholder = painterResource(id = R.mipmap.ic_launcher_foreground),
                                error = painterResource(id = R.drawable.baseline_account_circle_24),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(50.dp))
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(
                                        color = bgColor,
                                        shape = RoundedCornerShape(16.dp) // 16.dp corner radius
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = position,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = textColor,
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(start = 8.dp)
                                )
                                Text(
                                    text = number,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = textColor,
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(end = 8.dp)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxHeight()
                                .weight(1f)
                                .padding(5.dp),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalArrangement = Arrangement.SpaceAround,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    firstName,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = teamName,
                                    fontWeight = FontWeight.Light,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(
                                    "$age years",
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Start,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.wrapContentWidth()
                                )
                                Text(
                                    text = injured,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(text = "\uD83D\uDFE5 " + redCards)
                                Text(text = "\uD83D\uDFE8 " + yellowCards)
                            }
                        }
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = "Matches",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "Goals",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "Assists",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = "Saves",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                            }
                            .drawBehind {
                                val strokeWidth = Stroke.DefaultMiter
                                val y = size.height // - strokeWidth

                                drawLine(
                                    Color.LightGray,
                                    Offset(0f, y),
                                    Offset(size.width, y),
                                    strokeWidth
                                )
                            }
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = matches,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = goals,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = assists,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                        Text(
                            text = saves,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(Alignment.CenterVertically),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,

                    ){

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .weight(1f)
                                .background(
                                    color = bgColor,
                                    shape = RoundedCornerShape(16.dp) // 16.dp corner radius
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Form",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 8.dp)
                            )
                            Text(
                                text = form,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(end = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .weight(1f)
                                .background(
                                    color = bgColor,
                                    shape = RoundedCornerShape(16.dp) // 16.dp corner radius
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Minutes",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 8.dp)
                            )
                            Text(
                                text = minutes,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(end = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .weight(1f)
                                .background(
                                    color = bgColor,
                                    shape = RoundedCornerShape(16.dp) // 16.dp corner radius
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Accuracy",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 8.dp)
                            )
                            Text(
                                text = accuracy,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(end = 8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    AdmobBanner()
                }
            }
        }
    }
}
