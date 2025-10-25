package com.codesui.footballfixtures.resources

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codesui.footballfixtures.screens.AboutScreen
import com.codesui.footballfixtures.screens.StartScreen
import com.codesui.footballfixtures.screens.TermsScreen
import com.codesui.footballfixtures.screens.fixture.FixtureScreen
import com.codesui.footballfixtures.screens.league.LeagueDetails
import com.codesui.footballfixtures.screens.player.PlayerScreen
import com.codesui.footballfixtures.widgets.NavDrawer

@Composable
fun AppNavigation(runAds :() -> Unit, openAds: () -> Unit, rewardedAds : () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.startScreen, builder ={
        composable(Routes.startScreen) {
            StartScreen(navController, runAds, openAds)
        }
        composable(Routes.mainScreen) {
            NavDrawer(navController, runAds, openAds, rewardedAds)
        }
        composable(Routes.aboutScreen) {
            AboutScreen(navController, runAds)
        }
        composable(Routes.termsScreen) {
            TermsScreen(navController, runAds)
        }
        composable(Routes.fixtureScreen+ "/{id}") {
            val matchId = it.arguments?.getString("id")
            FixtureScreen(navController, runAds, matchId?: "323235386")
        }
        composable(Routes.leagueScreen+ "/{id}") {
            val id = it.arguments?.getString("id")
            LeagueDetails(navController, runAds, id?: "39")
        }
        composable(Routes.playerScreen+ "/{id}") {
            val playerId = it.arguments?.getString("id")
            PlayerScreen(navController, runAds,playerId?: "323235386")
        }
    } )
}