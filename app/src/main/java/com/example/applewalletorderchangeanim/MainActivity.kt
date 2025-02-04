package com.example.applewalletorderchangeanim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applewalletorderchangeanim.ui.reorderable.ReorderableItem
import com.example.applewalletorderchangeanim.ui.reorderable.rememberReorderableLazyListState
import com.example.applewalletorderchangeanim.ui.theme.AppleWalletOrderChangeAnimTheme
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppleWalletOrderChangeAnimTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private val CARD_HEIGHT = 500.dp

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var items by remember { mutableStateOf((0..5).map { getRandomColor() }) }

    val lazyListState = rememberLazyListState()

    val cardPctHeight = 0.1f
    val cardLastPctHeight = 0.5f

    // https://github.com/Calvin-LL/Reorderable
    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState = lazyListState,
        shouldItemMoveThreshold = (CARD_HEIGHT * cardPctHeight) / 2f // added new param to reorderable library
    ) { from, to ->
        items = items.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 10.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.toArgb() }
        ) { index, item ->
            ReorderableItem(reorderableLazyListState, key = item.toArgb()) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .draggableHandle()
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            val placeableHeight = if (index != items.lastIndex) {
                                placeable.height * cardPctHeight
                            } else {
                                placeable.height * cardLastPctHeight
                            }
                            layout(placeable.width, placeableHeight.roundToInt()) {
                                placeable.place(0, 0)
                            }
                        }
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .height(CARD_HEIGHT)
                        .background(color = item)
                ) {
                    //
                }
            }
        }
    }
}

private fun getRandomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenContentPreview() {
    AppleWalletOrderChangeAnimTheme {
        ScreenContent()
    }
}
