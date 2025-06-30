package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import sh.calvin.reorderable.ReorderableCollectionItemScope
import java.time.YearMonth

@Composable
fun HabitItemReorderable(
    backStack: SnapshotStateList<NavKey>,
    viewModel: MainPageViewModel,
    habit: HabitEntity,
    reorderableListScope: ReorderableCollectionItemScope,
    isDragging: Boolean
) {
    val color  = if (isDragging){
        CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)

    } else{ CardDefaults.outlinedCardColors() }
    val cardElevation = if (isDragging){
        CardDefaults.outlinedCardElevation(defaultElevation = 10.dp)

    } else{ CardDefaults.outlinedCardElevation() }
    OutlinedCard(
        modifier =
            Modifier
                .fillMaxWidth()

        ,
        elevation = cardElevation,

        colors = color,
        onClick = { backStack.add(Screen.HabitDetailsPageRoute(habit.id)) }) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(modifier = with(reorderableListScope) {Modifier.longPressDraggableHandle().fillMaxWidth()}, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.fillMaxWidth(0.7f)) {
                    Text(
                        habit.name,

                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        habit.description,

                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                }
                /*IconButton(
                    modifier = with(reorderableListScope) {
                        Modifier.draggableHandle(

                        )
                    },
                    onClick = {},
                ) {
                    Icon(
                        Icons.Rounded.DragHandle,
                        contentDescription = "Reorder"
                    )
                }*/


            }
            Spacer(Modifier.height(20.dp))


            val completions = viewModel.getHabitCompletionsByHabitId(habit.id)
                .collectAsStateWithLifecycle(initialValue = emptyList()).value
            var dates by rememberSaveable { mutableStateOf(viewModel.generateInitialDates()) }
            LazyRow(
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                items(dates) { date ->
                    val isCompleted =if(completions == null){false}else {
                        completions.any { it.completionDate == date }
                    }

                    DateItem(viewModel, isCompleted, date, habit, 50.dp)
                }
            }

        }

        HorizontalDivider()

        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column { }
                Row {
                    val currentMonth = YearMonth.now()
                    IconButton(
                        modifier = Modifier,
                        onClick = { backStack.add(Screen.CalenderPageRoute(month  = currentMonth.toString(), habit.id)) }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "")
                    }
                    IconButton(
                        modifier = Modifier,
                        onClick = { backStack.add(Screen.AddHabitPageRoute(habit.id)) }) {
                        Icon(Icons.Default.Edit, contentDescription = "")
                    }
                    IconButton(
                        modifier = Modifier,
                        onClick = { viewModel.deleteHabitById(habit.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "")
                    }
                }
            }
        }
    }
}