package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.database.PreferenceEntity
import com.zavedahmad.yaHabit.database.utils.getShowActive
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
            Column (Modifier.padding(horizontal = 20.dp)){
                FilterSection(preferences, viewModel)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun FilterSection(
    preferences: State<List<PreferenceEntity>>,
    viewModel: MainPageViewModel,
) {
    val onlyOneFilterIsLeft by remember { derivedStateOf { preferences.value.getShowActive() xor preferences.value.getShowArchive() } }
    val archiveChecked = preferences.value.getShowArchive()
    val activeChecked = preferences.value.getShowActive()
    FilterSectionHeading("INCLUDE" , )
    Row (horizontalArrangement = Arrangement.spacedBy(10.dp)){
        Spacer(Modifier.Companion.width(5.dp))
        OutlinedToggleButton(
            colors = ToggleButtonDefaults.outlinedToggleButtonColors(checkedContainerColor = MaterialTheme.colorScheme.primary),
            onCheckedChange = { viewModel.setArchivedFilter(it) },
            checked = archiveChecked,
            content = {
                AnimatedVisibility(archiveChecked) { Icon(Icons.Default.Check, "included") }
                Text("Archived")
            },
            enabled = !(onlyOneFilterIsLeft && preferences.value.getShowArchive())
        )
        OutlinedToggleButton(
            colors = ToggleButtonDefaults.outlinedToggleButtonColors(checkedContainerColor = MaterialTheme.colorScheme.primary),
            onCheckedChange = { viewModel.setActiveFilter(it) },
            checked = activeChecked,
            content = {
                AnimatedVisibility(activeChecked) { Icon(Icons.Default.Check, "included") }
                Text("Active")
            },
            enabled = !(onlyOneFilterIsLeft && preferences.value.getShowActive())
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilterSectionHeading(text: String, bottomPadding: Boolean = true) {

    Row {
        Text(
            text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Companion.Medium,
            letterSpacing = 3.sp
        )
    }
    if (bottomPadding) {
        Spacer(Modifier.Companion.height(20.dp))
    }

}