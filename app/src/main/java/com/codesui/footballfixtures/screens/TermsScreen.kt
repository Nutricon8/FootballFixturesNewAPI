package com.codesui.footballfixtures.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.widgets.Topbar
import com.codesui.powerkingtips.ads.AdmobBanner
import com.farimarwat.composenativeadmob.nativead.BannerAdAdmobMedium
import com.farimarwat.composenativeadmob.nativead.BannerAdAdmobSmall
import com.farimarwat.composenativeadmob.nativead.rememberNativeAdState

@Composable
fun TermsScreen (navController: NavController, runAds :() -> Unit){
    val context = LocalContext.current
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        val adState = rememberNativeAdState(context = context, adUnitId = context.getString(R.string.Native_Ad_Id))
        Topbar(stringResource(id = R.string.terms_of_service)){
            navController.popBackStack()
            runAds .invoke()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, false)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardItem(
                modifier = Modifier,
                title = "1. Introduction",
                description = "Welcome to "+ stringResource(id = R.string.app_name) + " ! By using our football live features and streaming app, you agree to comply with and be bound by the following Terms of Service. Please read these terms carefully before using the app."
            )

            CardItem(
                modifier = Modifier,
                title = "2. Use of the App",
                description = "1. Eligibility\n" +
                        " You must be at least 13 years old to use the app. If you are under 18, you must have parental or guardian consent to use the app.\n" +
                        "\n" +
                        "2. Personal Use\n" +
                        " The app is for personal, non-commercial use only. You agree not to modify, distribute, or reproduce any content without permission."
            )
            BannerAdAdmobSmall(loadedAd = adState)
            CardItem(
                modifier = Modifier,
                title = "3. Content and Streaming",
                description = "1. Live Features\n" +
                        "The app provides access to live football events, updates, and streaming. We do not guarantee the accuracy or availability of these services at all times.\n" +
                        "\n" +
                        "2. Third-Party Services\n" +
                        "Some content, including live streaming, may be provided by third parties. We are not responsible for the content or functionality of third-party services integrated into the app."
            )

            CardItem(
                modifier = Modifier,
                title = "4. Advertisements",
                description = "1. Ad-Supported Service\n" +
                        "The app is supported by advertisements. By using the app, you agree to the display of ads, which may be personalized based on your device’s advertisement ID.\n" +
                        "\n" +
                        "2. Opting Out\n" +
                        "You may opt out of personalized ads by adjusting your device’s settings, but non-personalized ads will still be displayed."
            )


            CardItem(
                modifier = Modifier,
                title = "5. User Conduct",
                description = "You agree not to:\n" +
                        "Use the app for any illegal activities or to promote any unlawful actions.\n" +
                        "\n" +
                        "Attempt to hack, disrupt, or interfere with the app’s functionality or security.\n"
            )
            Spacer(modifier = Modifier.fillMaxWidth())
            BannerAdAdmobMedium(loadedAd = adState)
            CardItem(
                modifier = Modifier,
                title = "6. Intellectual Property",
                description = "All content, including images, text, logos, and trademarks, is owned by or licensed to [App Name]. You may not use, copy, or distribute any content from the app without our prior written consent."
            )

            CardItem(
                modifier = Modifier,
                title = "7. Limitation of Liability",
                description = "We strive to provide a seamless and uninterrupted experience; however, [App Name] is provided on an \"as-is\" basis. We do not guarantee the app will be free from errors, bugs, or interruptions, and we are not liable for any damages that may result from the use or inability to use the app."
            )

            CardItem(
                modifier = Modifier,
                title = "8. Termination",
                description = "We reserve the right to suspend or terminate your access to the app at any time, without prior notice, if you violate these Terms of Service or engage in unlawful activities."
            )

            CardItem(
                modifier = Modifier,
                title = "9. Changes to the Terms",
                description = "We may update these Terms of Service from time to time to reflect changes in our practices or legal obligations. Continued use of the app after updates constitutes acceptance of the revised terms."
            )

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "By accessing or using "+ stringResource(id = R.string.app_name) + " you acknowledge that you have read, understood, and agree to these Terms of Service. If you do not agree, you may not use the app.",
                fontSize = 18.sp,
                fontWeight =  FontWeight.Normal,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(5.dp),
            )
        }
        AdmobBanner()
    }
}

@Composable
fun CardItem(modifier: Modifier, title : String, description: String){
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val bgColor = if (isDarkTheme) Color.DarkGray else Color.White
    Card(
        modifier
            .padding(5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight =  FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = modifier.height(5.dp))
                Text(
                    text = description,
                    fontSize = 18.sp,
                    fontWeight =  FontWeight.Normal,
                    color = textColor
                )
            }
        }
    }
}