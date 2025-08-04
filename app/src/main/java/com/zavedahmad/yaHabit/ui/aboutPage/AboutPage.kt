package com.zavedahmad.yaHabit.ui.aboutPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.R
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutPage() {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {LargeFlexibleTopAppBar(title = { Text("YA Habit Tracker") }, scrollBehavior = scrollBehavior)

        }
    )
    { innerPadding ->
        Column(
            Modifier
                .fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(512.dp),
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = " Image"
            )
            Image(
                modifier = Modifier.size(512.dp),
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = " Image"
            )
            Image(
                modifier = Modifier.size(512.dp),
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = " Image"
            )
        }
    }
}