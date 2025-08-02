package com.zavedahmad.yaHabit.ui.settingsScreen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.zavedahmad.yaHabit.ui.components.ConfirmationDialog
import com.zavedahmad.yaHabit.ui.mainPage.MainPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ExportDatabaseSettingsItem(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val exportLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("application/x-sqlite3")
        ) { uri: Uri? ->
            uri?.let {
                viewModel.exportDatabase(context, it) { result ->
                    result.onSuccess {
                        makeToast(context, "databaseExported")
                    }.onFailure { e ->
                        makeToast(context, "failure")
                    }
                }
            }


        }
    SettingsItem(
        icon = Icons.Default.FileUpload,
        title = "Export Database",
        description = "Export the Entire Database File (Including Settings)",
        task = { exportLauncher.launch("main_database.db") }
    )

}


fun makeToast(context: Context, text: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

    }
}

@Composable
fun ImportDatabaseSettingsItem(viewModel: SettingsViewModel, onDatabaseImport: (Boolean) -> Unit) {
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) {
            coroutineScope.launch {
                makeToast(context, "Import canceled")
            }
        } else {
            viewModel.importDatabase(context, uri) { result ->
                coroutineScope.launch {
                    result.onSuccess {
                        onDatabaseImport(true)


//                        makeToast(context, "Database imported successfully")

                    }.onFailure { e ->
                        onDatabaseImport(false)
//                        makeToast(context, "Import failed: ${e.message}")
                    }
                }
            }
        }
    }

        ConfirmationDialog(
            visible = showDialog.value,
            title = "Import Data?",
            text = "Are you sure you want to import the database? This will overwrite the current database.",
            confirmAction = {  importLauncher.launch(
                arrayOf("application/vnd.sqlite3", "application/octet-stream")
            )},
            onDismiss ={ showDialog.value = false}
        )

    SettingsItem(
        icon = Icons.Default.Download,
        title = "Import Database",
        description = "Import a Database file. Please Note this will delete all existing Data",
        task = {showDialog.value = true

        }
    )
}

@Composable
fun DialogForImportConfirm() {
    AlertDialog(onDismissRequest = {}, confirmButton = { Text("i want to confirm") })

}