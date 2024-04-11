package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularLoader(color: Color? = null) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            strokeWidth = 2.dp,
            color = color ?: Color(0xFF4D2E6B),
        )
    }
}
