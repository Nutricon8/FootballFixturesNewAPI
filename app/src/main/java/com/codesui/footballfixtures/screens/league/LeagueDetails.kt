package com.codesui.footballfixtures.screens.league

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.codesui.footballfixtures.resources.PreferencesManager
import com.codesui.footballfixtures.widgets.Topbar
import com.codesui.powerkingtips.ads.AdmobBanner

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeagueDetails(navController: NavController, runAds :() -> Unit, leagueId: String){
    val tabItems = listOf("Standings", "Top Scorers")
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val preferencesManager = PreferencesManager(LocalContext.current)
        var selectedTabIndex by remember { mutableStateOf(preferencesManager.getInteger("selectedTabIndex", 0)) }
        val pagerState = rememberPagerState { tabItems.size }
        val isDarkTheme = isSystemInDarkTheme()
        val textColor = if (isDarkTheme) Color.White else Color.Black

        LaunchedEffect(key1 = selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
        LaunchedEffect(
            key1 = pagerState.currentPage,
            key2 = pagerState.isScrollInProgress
        ) {
            if (!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }

        Column (
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Topbar(title = "League") {
                navController.popBackStack()
                runAds .invoke()
            }
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { index, tabItem ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            preferencesManager.saveInteger("selectedTabIndex", index)
                            selectedTabIndex = index
                            runAds.invoke()
                        },
                        text = {
                            Text(text = tabItem)
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = textColor
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
                    0 -> StandingsScreen(navController, leagueId)
                    1 -> TopScorersScreen(navController, leagueId, runAds)
                }
            }
            AdmobBanner()
        }
    }
}