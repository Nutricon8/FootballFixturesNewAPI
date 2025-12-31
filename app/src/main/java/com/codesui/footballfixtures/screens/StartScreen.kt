package com.codesui.footballfixtures.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.navigation.Routes
import com.codesui.powerkingtips.ads.AdmobBanner

@Composable
fun StartScreen(navController: NavController, runAds: () -> Unit, openAds: () -> Unit) {
    LaunchedEffect(Unit) {
        openAds.invoke()
    }
    Box (
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.w1),
                contentScale = ContentScale.FillBounds
            )
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column (
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✅ LIVE SCORES & LIVESTREAM",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "✅ LEAGUE TABLES & STANDINGS",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "✅ FIXTURES & RESULTS",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    ,
                onClick = {
                    navController.navigate(
                        route = Routes.mainScreen
                    )
                    runAds.invoke()
                }) {
                Text(
                    text = "LIVE FOOTBALL ➤",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .background(Color.Transparent)
                        .wrapContentHeight(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyLarge
                )

            }
            Spacer(modifier = Modifier.height(20.dp))
            AdmobBanner()
        }
    }
}