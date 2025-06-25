package com.zavedahmad.yaHabit.ui.searchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(backStack: SnapshotStateList<NavKey>) {






    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        val basicTextFiledState = rememberTextFieldState()
        LaunchedEffect(basicTextFiledState.text) {
            { }

        }

        Icon(
            Icons.Default.Search, contentDescription = "search for movie you want "
        )
        Spacer(modifier = Modifier.width(10.dp))
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer),
            textStyle = TextStyle(
                fontSize = 20.sp,

                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            state = basicTextFiledState,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            lineLimits = TextFieldLineLimits.SingleLine,
            onKeyboardAction = {   }
        )
    }


}
