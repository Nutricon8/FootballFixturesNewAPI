package com.codesui.footballfixtures.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.Requests.RetrofitClient
import com.codesui.footballfixtures.widgets.FeaturedLive
import com.codesui.footballfixtures.screens.league.FeaturedLeague
import com.codesui.footballfixtures.screens.player.PlayerCard
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun HomeScreen(navController: NavController, runAds: () -> Unit, rewardedAds: () -> Unit) {
    val leagues = remember { mutableStateOf<List<JsonObject>?>(null) }
    val topScorers = remember { mutableStateOf<List<JsonObject>?>(null) }
    val fixtures = remember { mutableStateOf<List<JsonObject>?>(null) }
    var leagueId by remember { mutableStateOf("56") }

    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }

    val leaguesParams = mapOf("action" to "get_leagues", "APIkey" to stringResource(id = R.string.api_key))
    val topScorerParams = mapOf("action" to "get_topscorers", "league_id" to leagueId, "APIkey" to stringResource(id = R.string.api_key))
    val fixturesParams = mapOf("action" to "get_events", "from" to getCurrentDate(), "to" to getCurrentDate(), "match_live" to "1","timezone" to "Africa/Nairobi", "APIkey" to stringResource(id = R.string.api_key))

    LaunchedEffect(leagueId) {
        try {
            val leaguesResponse = RetrofitClient.apiService.getLeagues(leaguesParams)
            leagues.value = leaguesResponse.map { it.asJsonObject }

            val topScorersResponse = RetrofitClient.apiService.getTopScorers(topScorerParams)
            topScorers.value = topScorersResponse.map { it.asJsonObject }

            val fixturesResponse = RetrofitClient.apiService.getFixtures(fixturesParams)
            fixtures.value = fixturesResponse.map { it.asJsonObject }


        } catch (e: Exception) {
            error.value = e.message
        } finally {
            isLoading.value = false
        }
    }


    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading.value -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        when {
            leagues.value != null -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = leagues.value!!) { item ->
                        FeaturedLeague(item, leagueId, onMutableValueChange = { leagueId = it }, runAds)
                    }
                }
            }
        }
        when{
            //top scorers
            topScorers.value != null -> {
                Heading("Top Scorers")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = topScorers.value!!) { player ->
                        PlayerCard(navController, player, runAds)
                    }
                }
            }
        }
        when{
            fixtures.value != null -> {
                Heading("Live Matches")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    items(items = fixtures.value!!) { fixture ->
                        /*when{
                            fixture.get("match_live").asString.equals("0") && !fixture.get("match_status").asString.equals("Finished") -> {

                            }
                        }*/
                        FeaturedLive(fixture, navController, runAds)

                    }
                }
            }
        }
    }
}

@Composable
private fun Heading(title: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
private fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val currentDate = Date()
    return dateFormat.format(currentDate)
}