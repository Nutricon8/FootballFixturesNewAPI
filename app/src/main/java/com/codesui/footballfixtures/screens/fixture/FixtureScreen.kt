package com.codesui.footballfixtures.screens.fixture

import androidx.compose.animation.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.api.Requests.RetrofitClient
import com.codesui.footballfixtures.widgets.ErrorDialog
import com.codesui.footballfixtures.widgets.IndeterminateCircularIndicator
import com.codesui.footballfixtures.widgets.NoInternetDialog
import com.codesui.footballfixtures.widgets.Topbar
import com.codesui.footballfixtures.widgets.isInternetAvailable
import com.codesui.footballfixtures.screens.league.StandingsScreen
import com.codesui.powerkingtips.ads.AdmobBanner
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FixtureScreen (navController: NavController, runAds :() -> Unit, matchId: String){
    val tabItems = listOf("Stats", "Table", "Lineup")
    val pagerState = rememberPagerState{ tabItems.size }
    val coroutineScope = rememberCoroutineScope()
    val match = remember { mutableStateOf<JsonObject?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }
    val params = mapOf("action" to "get_events", "match_id" to matchId, "APIkey" to stringResource(id = R.string.api_key))
    var isButtonClicked by remember { mutableStateOf(true) }
    LaunchedEffect(isButtonClicked) {
        if (isButtonClicked) {
            try {
                val response = RetrofitClient.apiService.getFixture(params)
                match.value = response.first().asJsonObject
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
            isButtonClicked = false // Reset state after task
        }
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
        match.value != null -> {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Topbar(title = match.value!!.get("league_name").asString) {
                    navController.popBackStack()
                    runAds .invoke()
                }
                TopBarComponent(Modifier, match.value!!)
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .padding(all = 5.dp)
                        .fillMaxWidth()
                        .background(color = Color.Transparent)
                        .clip(RoundedCornerShape(30.dp)),
                    indicator = {
                            tabPositions ->  TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        height = 0.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    }

                ) {
                    tabItems.forEachIndexed { index, tabItem ->
                        val color = remember{
                            Animatable(Color.Gray)
                        }

                        LaunchedEffect(pagerState.currentPage == index){
                            color.animateTo(
                                if(pagerState.currentPage == index) Color.Gray else Color.White
                            )
                        }
                        Tab(
                            selected = pagerState.currentPage == index,
                            modifier = Modifier
                                .padding(5.dp)
                                .background(
                                color = color.value,
                                shape = RoundedCornerShape(30.dp)
                            ),
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                                runAds.invoke()
                            },
                            text = {
                                Text(
                                    text = tabItem,
                                    style = if(pagerState.currentPage == index) TextStyle(
                                        color = Color.White,
                                        fontSize = 15.sp
                                    ) else TextStyle(
                                        color = Color.Black,
                                        fontSize =  14.sp
                                    )
                                )
                            },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = Color.Black
                        )

                    }
                }
                HorizontalPager(
                    state = pagerState,
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {index ->
                    when(index) {
                        0 -> MatchStats(match.value!!.getAsJsonArray("statistics"))
                        1 -> StandingsScreen(navController, match.value!!.get("league_id").asString)
                        2 -> LineupScreen(navController, match.value!!.getAsJsonObject("lineup"))
                    }
                }
                AdmobBanner()
            }
        }
    }
}

@Composable
fun TopBarComponent(modifier: Modifier = Modifier, item: JsonObject) {
    val homeTeam = item.get("match_hometeam_name").asString
    val awayTeam = item.get("match_awayteam_name").asString
    val homeTeamIcon = item.get("team_home_badge").asString
    val awayTeamIcon = item.get("team_away_badge").asString
    val homeScore = item.get("match_hometeam_score").asString
    val awayScore = item.get("match_awayteam_score").asString
    val leagueImage = item.get("league_logo").asString
    val stadium = item.get("match_stadium").asString
    val round = item.get("match_round").asString
    val status = if(item.get("match_live").asString.equals("0") && item.get("match_status").asString.equals("Finished")) {
        "FT"
    } else if(item.get("match_live").asString.equals("1") && !item.get("match_status").asString.equals("Finished")){
        item.get("match_status").asString + "`"
    }else item.get("match_time").asString
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.Black else Color.White


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
    ) {
        AsyncImage(
            model = leagueImage,
            contentDescription = stringResource(id = R.string.app_name),
            placeholder = painterResource(id = R.drawable.w1),
            error = painterResource(id = R.drawable.w1),
            contentScale = ContentScale.FillBounds,
            modifier = modifier.fillMaxWidth()
        )
        Card(
            modifier = modifier
                .align(Alignment.Center)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = bgColor),
            shape = RectangleShape
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stadium,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = "Round $round",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Light,
                    color = textColor,
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = modifier.padding(start = 5.dp)
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
                                .size(40.dp)
                        )

                        Text(
                            text = homeTeam,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = modifier.padding(start = 10.dp),
                            color = textColor,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }
                    Text(
                        text = if (homeScore.isEmpty() && awayScore.isEmpty()) "VS" else "$homeScore-$awayScore",
                        style = MaterialTheme.typography.headlineSmall,
                        color = textColor
                    )
                    Column(
                        modifier = modifier.padding(end =5.dp)
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
                                .size(40.dp)
                        )
                        Text(
                            text = awayTeam,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = modifier.padding(start = 10.dp),
                            color = textColor,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }

                }
                Text(
                    text = status,
                    color = textColor,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}