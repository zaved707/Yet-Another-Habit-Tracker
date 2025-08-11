package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.R
import com.zavedahmad.yaHabit.Screen

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPageTopAppBar(viewModel: MainPageViewModel,backStack: SnapshotStateList<NavKey>,scrollBehavior: TopAppBarScrollBehavior) {
    val isReorderableMode = viewModel.isReorderableMode.collectAsStateWithLifecycle()
    val colorForBorder = MaterialTheme.colorScheme.outlineVariant.copy(0.5f)
    MediumFlexibleTopAppBar(
        modifier = Modifier.drawWithContent() {
            drawContent()
            drawLine(
                color = colorForBorder,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx()
            )
        },
        title = {
            Text( // Add border around this
                "Habits",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {

            if (isReorderableMode.value
            ) {
                Button(
                    onClick = {


                        viewModel.changeReorderableMode(false)

                    },
                    modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "turn off reorderable mode"
                    )
                }
            }

            if (!isReorderableMode.value
            ) {
                Row {


                    IconButton(onClick = {viewModel.setBottomSheetVisibility(true)} , shapes = IconButtonDefaults.shapes(
                        IconButtonDefaults.extraSmallRoundShape)) {
                        Icon(
                            painterResource(R.drawable.list_status),
                            contentDescription = "Sort and filter"
                        )
                    }
                    MainPageMenu(viewModel, backStack)
                }
            }


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),

        scrollBehavior = scrollBehavior
    )

}