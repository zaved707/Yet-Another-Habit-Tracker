package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import com.zavedahmad.yaHabit.ui.components.MyTopABCommon
import com.zavedahmad.yaHabit.ui.searchScreen.MyTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitPage(viewModel: AddHabitPageViewModel, backStack: NavBackStack) {
    val name = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val setColor = viewModel.selectedColor.collectAsStateWithLifecycle()
    Scaffold(topBar = { MyTopABCommon(backStack, scrollBehavior, "add Habit") }) { innerPadding ->


            val colors = listOf<Color>(
                Color(0xFFFF69B4), // Vibrant Pink (HotPink)
                Color(0xFFFF7F50), // Vibrant Coral (Coral)
                Color(0xFFBA55D3), // Vibrant Orchid (MediumOrchid)
                Color(0xFF9370DB), // Vibrant Medium Purple (MediumPurple)
                Color(0xFF4682B4), // Vibrant Steel Blue (SteelBlue)
                Color(0xFF00BFFF), // Vibrant Deep Sky Blue (DeepSkyBlue)
                Color(0xFF40E0D0), // Vibrant Turquoise (Turquoise)
                Color(0xFF00FA9A), // Vibrant Medium Spring Green (MediumSpringGreen)
                Color(0xFF32CD32), // Vibrant Lime Green (LimeGreen)
                Color(0xFF7FFF00), // Vibrant Chartreuse (Chartreuse)
                Color(0xFFADFF2F), // Vibrant Green Yellow (GreenYellow)
                Color(0xFFFFFF00), // Vibrant Yellow (Yellow)
                Color(0xFFFFD700), // Vibrant Gold (Gold)
                Color(0xFFFFA500), // Vibrant Orange (Orange)
                Color(0xFFFF8C00), // Vibrant Dark Orange (DarkOrange)
                Color(0xFFFF4500), // Vibrant Orange Red (OrangeRed)
                Color(0xFFA0522D), // Vibrant Sienna (Sienna)
                Color(0xFF778899), // Vibrant Light Slate Gray (LightSlateGray)
                Color(0xFF708090)  // Vibrant Slate Gray (SlateGray)
            )
        Column(modifier = Modifier.padding(innerPadding).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            MyTextField(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(0.9f), placeholder = "Title Of Habit",
                onValueChange = { name.value = it }, lineLimits = TextFieldLineLimits.SingleLine, textFieldVerticalAlignment = Alignment.CenterVertically
            )
            Spacer(Modifier.height(20.dp))
            MyTextField(
                cornerRoundness = 10.dp, modifier =
                    Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.9f),
                onValueChange = { description.value = it }, textHorizontalPadding = 10.dp, placeholder ="Description Of Habit"
            )


            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                LazyRow(verticalAlignment = Alignment.CenterVertically) {
                    items(colors) {
                        if (setColor.value == it) {
                            Box(
                                Modifier
                                    .padding(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .size(60.dp)
                                    .background(Color.White)


                                    .clickable(onClick = {})
                            ) {
                                Box(
                                    Modifier
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxSize()
                                        .background(it)
                                )
                            }
                        } else {
                            Box(
                                Modifier
                                    .padding(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .size(50.dp)
                                    .background(it)
                                    .clickable(onClick = { viewModel.setColor(it) })
                            )
                        }
                    }
                }
            }
            Button(onClick = { viewModel.addHabit(name.value) }) { Text("add") }
        }

    }
}