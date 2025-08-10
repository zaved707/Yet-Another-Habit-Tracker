package com.zavedahmad.yaHabit.ui.mainPage

import android.widget.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.database.utils.getShowArchive

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomSheetForFiltersAndSorting(viewModel: MainPageViewModel) {
    val isVisible = viewModel.isBottomSheetVisible.collectAsStateWithLifecycle(
    )
    val preferences = viewModel.allPreferences.collectAsStateWithLifecycle()
    if (isVisible.value) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setBottomSheetVisibility(false) }
        ) {
            ToggleButton(
                onCheckedChange = {viewModel.setArchivedFilter(it)},
                checked = preferences.value.getShowArchive(),
                content = {Text("Archived")}
            )
        }
    }
}