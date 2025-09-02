package com.zavedahmad.yaHabit.ui.aboutPage

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.R
import com.zavedahmad.yaHabit.ui.settingsScreen.SettingsItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutPage(backStack: SnapshotStateList<NavKey>, viewModel: AboutPageViewModel) {
    val context = LocalContext.current
    val sourceCodeUrl = stringResource(R.string.github_repo)
    val issueTrackerUrl = stringResource(R.string.issue_tracker)
    val developerEmail = stringResource(R.string.developer_address)
    val privacyPolicyUrl = stringResource(R.string.privacy_policy)
    val licenseLink = stringResource(R.string.license)
    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val allPreferences = viewModel.allPreferences.collectAsStateWithLifecycle().value

    if (allPreferences.isEmpty()) {
        Text("FuckYou")

    } else {


        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("About") },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(onClick = { backStack.removeLastOrNull() }) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "go back"
                            )
                        }
                    })

            }
        )
        { innerPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier

                        .size(150.dp),
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = " Image"
                )
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("YA Habit Tracker", style = MaterialTheme.typography.titleLargeEmphasized)
                    Text(
                        "Version " + stringResource(R.string.app_version),
                        style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(10.dp))

                SettingsItem(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.source_branch),
                            contentDescription = "Show Source Code"
                        )
                    },
                    title = "View Source Code", description = sourceCodeUrl,
                    task = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            sourceCodeUrl.toUri()
                        )
                        context.startActivity(intent)
                    },
                )
                SettingsItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.BugReport,
                            contentDescription = "Issue Tracker"
                        )
                    },
                    title = "Issue Tracker", description = issueTrackerUrl,
                    task = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            issueTrackerUrl.toUri()
                        )
                        context.startActivity(intent)
                    },
                )
                SettingsItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = "contact Developer"
                        )
                    },
                    title = "Contact Me", description = developerEmail,
                    task = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data =
                                ("mailto:$developerEmail").toUri() // Replace with desired email
                        }
                        context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
                    },
                )

                SettingsItem(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.shield_lock_open_outline),
                            contentDescription = "contact Developer"
                        )
                    },
                    title = "Privacy Policy", description = privacyPolicyUrl,
                    task = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            privacyPolicyUrl.toUri()
                        )
                        context.startActivity(intent)
                    },
                )

                SettingsItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Balance,
                            contentDescription = "Apache-2.0"
                        )
                    },
                    title = "License", description = "Apache License 2.0",
                    task = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            licenseLink.toUri()
                        )
                        context.startActivity(intent)
                    },
                )

            }
        }
    }
}