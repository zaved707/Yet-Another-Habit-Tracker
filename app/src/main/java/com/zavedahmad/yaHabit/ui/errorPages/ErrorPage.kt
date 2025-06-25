package com.zavedahmad.yaHabit.ui.errorPages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorPage(paddingValues: PaddingValues,error : String, isLoading: Boolean , onReload : () -> Unit, backStack : SnapshotStateList<NavKey>){
    when(error){

        "NoApi"-> {
              val pagerState = rememberPagerState(pageCount = {
                  1
              })
              PullToRefreshBox(
              modifier = Modifier
                  .padding(paddingValues)
                  .fillMaxSize(),
              isRefreshing = isLoading,
              onRefresh = { onReload() }
          ) {
              VerticalPager(state = pagerState) {
                  Column(
                      modifier = Modifier.fillMaxSize(),
                      verticalArrangement = Arrangement.Center,
                      horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                      Icon(
                          Icons.Default.KeyOff,
                          modifier = Modifier.size(200.dp),
                          contentDescription = "placeholderr",
                          tint = MaterialTheme.colorScheme.onSecondaryContainer
                      )
                      Text("Either you Api is Not set Or is Invalid",textAlign = TextAlign.Center)
                      Button(onClick = { backStack.add(Screen.SettingsPageRoute) }) {
                          Text( text = "Go to settings to change it")
                      }
                  }
              }
          }}
        else -> {
            val pagerState = rememberPagerState(pageCount = {
                1
            })
            PullToRefreshBox(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                isRefreshing = isLoading,
                onRefresh = { onReload() }
            ) {
                VerticalPager(state = pagerState) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CloudOff,
                            modifier = Modifier.size(200.dp),
                            contentDescription = "placeholderr",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text("Either you are Not Connected To Internet Or Server Is Not Responding",textAlign = TextAlign.Center)

                    }
                }
            }
        }
    }
}