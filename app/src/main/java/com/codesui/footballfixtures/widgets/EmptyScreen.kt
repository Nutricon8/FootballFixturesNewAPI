package com.codesui.footballfixtures.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codesui.footballfixtures.R

@Composable
fun EmptyScreen(text: String){
    Box(
        modifier = Modifier
            .size(width = 380.dp, height = 400.dp)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_data_array_24),
                contentDescription = "",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "The list of $text is empty!",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}