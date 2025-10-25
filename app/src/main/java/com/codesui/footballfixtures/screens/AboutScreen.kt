package com.codesui.footballfixtures.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.resources.Routes
import com.codesui.footballfixtures.widgets.Topbar
import com.codesui.footballfixtures.resources.UrlManager
import com.codesui.powerkingtips.ads.AdmobBanner
import com.codesui.powerkingtips.resources.RateManager
import com.codesui.powerkingtips.resources.ShareManager

@Composable
fun AboutScreen(navController: NavController, runAds :() -> Unit) {
    val ctx = LocalContext.current
    val rateManager  =  RateManager()
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Topbar("About Us"){
            navController.popBackStack()
            runAds .invoke()
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            ColumnItem(modifier = Modifier, name = "More Apps", icon = Icons.Rounded.MoreVert, runFunction = {
                UrlManager(ctx.getString(R.string.more_apps_url), ctx)
            })
            ColumnItem(modifier = Modifier, name = "Share", icon = Icons.Rounded.Share, runFunction = {
                ShareManager(ctx)
            })
            ColumnItem(modifier = Modifier, name = "Rate Us", icon = Icons.Rounded.Star, runFunction = {
                rateManager.rateApp(ctx)
            })
            ColumnItem(modifier = Modifier, name = "Terms of service", icon = Icons.Rounded.List, runFunction = {
                navController.navigate(Routes.termsScreen)
                runAds.invoke()
            })
        }
        Spacer(modifier = Modifier.weight(1f))
        AdmobBanner()
    }
}

@Composable
fun ColumnItem(modifier: Modifier, name : String, icon: ImageVector, runFunction: () -> Unit){
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.DarkGray else Color.White
    Card(
        modifier
            .padding(3.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .aspectRatio(4f)
            .clickable {
                runFunction.invoke()
            },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier
                .padding(5.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontSize = 22.sp,
                    fontWeight =  FontWeight.Bold,
                    color = textColor
                )
                Icon(imageVector = icon, contentDescription = name, tint = textColor)
            }
        }
    }
}