package com.zavedahmad.yaHabit.ui.searchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onValueChange: (text: String) -> Unit,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    cornerRoundness  : Dp = 50.dp
    ,textHorizontalPadding : Dp = 20.dp,
    textFieldHorizontalArrangement : Arrangement.Horizontal = Arrangement.Center,
    textFieldVerticalAlignment: Alignment.Vertical = Alignment.Top,


) {



    Row(horizontalArrangement = textFieldHorizontalArrangement, verticalAlignment = textFieldVerticalAlignment,
        modifier = modifier

            .clip(RoundedCornerShape(cornerRoundness))
            .background(MaterialTheme.colorScheme.secondaryContainer).padding(horizontal = textHorizontalPadding)


    ) {
        val basicTextFiledState = rememberTextFieldState()
        LaunchedEffect(basicTextFiledState.text) {
            onValueChange(basicTextFiledState.text.toString())

        }

        BasicTextField(
            modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer),
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            state = basicTextFiledState,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            lineLimits = lineLimits,
//            onKeyboardAction = { onClick()  },
            decorator = { innerTextField ->
                if (basicTextFiledState.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                        fontSize = 20.sp
                    )
                }
                innerTextField()
            })
    }


}
