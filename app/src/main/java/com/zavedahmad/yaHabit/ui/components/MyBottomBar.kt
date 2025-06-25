package com.zavedahmad.yaHabit.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.Screen.FavouritePageRoute
import com.zavedahmad.yaHabit.Screen.MainPageRoute
import com.zavedahmad.yaHabit.Screen.SearchPageRoute

data class ActivePageTracker(
    var homePage: Boolean,
    var profilePage: Boolean,
    var favouritesPage: Boolean,
    var searchPage: Boolean

)

val BottomBarItems = listOf<Screen>(
    MainPageRoute,
    SearchPageRoute,

    FavouritePageRoute,

)
val icons = listOf<ImageVector>(
    Icons.Outlined.Home,
    Icons.Default.Search,
    Icons.Default.FavoriteBorder,


)

@Composable
fun MyBottomBar(backStack: SnapshotStateList<NavKey>) {

    NavigationBar {
        BottomBarItems.forEachIndexed { index, element ->

            NavigationBarItem(
                onClick = {
                    if(backStack.lastOrNull() != element){
                        if(backStack.lastOrNull() in BottomBarItems){
                            backStack.removeAt(backStack.lastIndex)
                        }
                        backStack.add(
                            element
                        )
                    }

                },
                icon = { Icon(icons[index], contentDescription = "desp") },
                selected = backStack.lastOrNull() == element
            )
        }
    }
}