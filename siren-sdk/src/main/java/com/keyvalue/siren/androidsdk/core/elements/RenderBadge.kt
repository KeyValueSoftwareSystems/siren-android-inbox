package com.keyvalue.siren.androidsdk.core.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.keyvalue.siren.androidsdk.helper.client.CombinedBadgeThemeProps

@Composable
fun RenderBadge(
    unViewedCount: Long,
    badgeStyle: CombinedBadgeThemeProps?,
) {
    Box(
        modifier =
        Modifier
            .size(badgeStyle?.size!!)
            .background(
                color = badgeStyle.color!!,
                shape = badgeStyle.borderShape!!,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (unViewedCount > 99) "99+" else unViewedCount.toString(),
            color = badgeStyle.textColor!!,
            fontSize = badgeStyle.textSize!!,
            textAlign = TextAlign.Center,
        )
    }
}
