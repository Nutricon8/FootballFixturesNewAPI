package com.codesui.footballfixtures.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codesui.footballfixtures.R
import com.codesui.footballfixtures.api.Requests.RetrofitClient
import com.codesui.footballfixtures.widgets.IndeterminateCircularIndicator
import com.codesui.footballfixtures.widgets.NoInternetDialog
import com.codesui.footballfixtures.widgets.isInternetAvailable
import com.codesui.footballfixtures.widgets.Fixture
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun FixturesScreen(navController: NavController, runAds: () -> Unit, rewardedAds: () -> Unit) {
    Column (modifier = Modifier.fillMaxSize()){
        val fixtures = remember { mutableStateOf<List<JsonObject>?>(null) }
        val isLoading = remember { mutableStateOf(true) }
        val error = remember { mutableStateOf<String?>(null) }
        val params = mapOf("action" to "get_events", "from" to getCurrentDate(), "to" to getCurrentDate(),"match_live" to "0","timezone" to "Africa/Nairobi", "APIkey" to stringResource(id = R.string.api_key))
        var isButtonClicked by remember { mutableStateOf(true) }
        LaunchedEffect(isButtonClicked) {
            if (isButtonClicked) {
                try {
                    val response = RetrofitClient.apiService.getFixtures(params)
                    fixtures.value = response.map { it.asJsonObject }
                } catch (e: Exception) {
                    error.value = e.message
                } finally {
                    isLoading.value = false
                }
                isButtonClicked = false
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
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            text = "Fixtures Not Available!",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            fixtures.value != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items = fixtures.value!!) { fixture ->
                        when{
                            fixture.get("match_live").asString.equals("0") && !fixture.get("match_status").asString.equals("Finished") -> {
                                Fixture(fixture, navController, runAds)
                            }
                        }
                    }
                }
            }
        }
    }
}
private fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val currentDate = Date()
    return dateFormat.format(currentDate)
}