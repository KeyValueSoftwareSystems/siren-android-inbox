package com.keyvalue.siren.androidsdk.helper

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyvalue.siren.androidsdk.R
import com.keyvalue.siren.androidsdk.utils.constants.INBOX_ERROR_DESCRIPTION
import com.keyvalue.siren.androidsdk.utils.constants.INBOX_ERROR_TITLE

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InboxErrorState(
    pullRefreshState: PullRefreshState,
    backgroundColor: Color,
    titleColor: Color,
    descriptionColor: Color,
    isDarkMode: Boolean,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier =
            Modifier
                .fillMaxSize()
                .shadow(0.dp)
                .pullRefresh(pullRefreshState)
                .background(backgroundColor),
    ) {
        item {
            Image(painter = painterResource(id = if (isDarkMode) R.drawable.error_state_dark else R.drawable.error_state_light), contentDescription = "Error state", modifier = Modifier.size(150.dp))
            Text(
                text = INBOX_ERROR_TITLE,
                fontWeight = FontWeight.W600,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = titleColor,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
            )
            Text(
                text = INBOX_ERROR_DESCRIPTION,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = descriptionColor,
            )
        }
    }
}
