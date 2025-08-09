package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetForFiltersAndSorting(viewModel: MainPageViewModel) {
    val isVisible = viewModel.isBottomSheetVisible.collectAsStateWithLifecycle(
    )
    if (isVisible.value){
    ModalBottomSheet(
        onDismissRequest = {  viewModel.setBottomSheetVisibility(false)}
    ) { }}
}