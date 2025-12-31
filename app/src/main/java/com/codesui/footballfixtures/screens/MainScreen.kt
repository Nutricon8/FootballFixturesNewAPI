package com.codesui.footballfixtures.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.codesui.footballfixtures.fragments.FixturesScreen
import com.codesui.footballfixtures.fragments.HomeScreen
import com.codesui.footballfixtures.fragments.LeaguesScreen
import com.codesui.footballfixtures.fragments.LivescoresScreen
import com.codesui.footballfixtures.fragments.ResultsScreen
import com.codesui.footballfixtures.navigation.NavItem
import com.codesui.footballfixtures.resources.PreferencesManager
import com.codesui.powerkingtips.ads.AdmobBanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen (
    rememberDrawerState: DrawerState,
    rememberCoroutineScope: CoroutineScope,
    navController: NavController,
    runAds :() -> Unit,
    rewardedAds : () -> Unit
) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Fixtures", Icons.Default.DateRange),
        NavItem("Live", Icons.AutoMirrored.Filled.List),
        NavItem("Results", Icons.Default.Menu),
        NavItem("Leagues", Icons.Default.Star)
    )
    val preferencesManager = PreferencesManager(LocalContext.current)
    var selectedIndex by remember { mutableStateOf(preferencesManager.getInteger("selectedIndex", 0)) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        navItemList.get(selectedIndex).label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        rememberCoroutineScope.launch {
                            rememberDrawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            NavigationBar{
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            preferencesManager.saveInteger("selectedIndex", index)
                            selectedIndex = index
                            runAds.invoke()
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ){innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex, navController, runAds, rewardedAds)
    }
}
@Composable
fun ContentScreen(modifier: Modifier, selectedIndex : Int, navController: NavController, runAds: () -> Unit, rewardedAds: () -> Unit) {
    Column (
        modifier = modifier
    ){
        when(selectedIndex) {
            0 -> HomeScreen(navController, runAds, rewardedAds)
            1 -> FixturesScreen(navController, runAds, rewardedAds)
            2 -> LivescoresScreen(navController, runAds, rewardedAds)
            3 -> ResultsScreen(navController, runAds, rewardedAds)
            4 -> LeaguesScreen(navController, runAds, rewardedAds)
        }
    }
}