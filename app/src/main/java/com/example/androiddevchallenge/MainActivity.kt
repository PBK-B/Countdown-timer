/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.LinkedList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun SlidingTimeSelection(
    modifier: Modifier?,
    text: String = "",
    minValue: Int = 0,
    maxValue: Int = 60,
    callBack: (value: Int) -> Unit? = {}
) {

    val TAG = "tzmax"
    val initValue = 3

    val listState = LazyListState()
    var listIndex = remember {
        mutableStateOf(minValue + initValue)
    }
    var lists = remember {
        mutableStateOf(LinkedList<Int>((minValue..maxValue).toList()))
    }

    Row(
        modifier = modifier!!,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {

        LazyColumn(
            state = listState,
        ) {

            items(
                items = lists.value,
                itemContent = { item ->

                    DisposableEffect(listState) {
                        onDispose {
                            GlobalScope.launch {
                                listState.run {
                                    val info = this.layoutInfo
                                    val i = info.visibleItemsInfo
                                    listIndex.value = i[initValue].index
                                    listState.animateScrollToItem(i[0].index)

                                    callBack(listIndex.value)

                                    Log.d(
                                        TAG,
                                        "SlidingTimeSelection() returned: $item ${listIndex.value} ${i[0].index}"
                                    )
                                }
                            }
                        }
                    }

                    when (item) {
                        listIndex.value -> {
                            Text(
                                text = if (item > 9) item.toString() else "0$item",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF0000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp
                            )
                        }
                        else -> {
                            Text(
                                text = if (item > 9) item.toString() else "0$item",
                                textAlign = TextAlign.End,
                                color = Color(0x660000000),
                                fontWeight = FontWeight.W300,
                                fontSize = 35.sp
                            )
                        }
                    }
                }
            )
        }

        Text(text = text, modifier = Modifier.padding(start = 5.dp))
    }
}

// Start building your app here!
@Composable
fun MyApp() {

    val _context = BaseApplication.getContext()

    var _timer_s = remember {
        mutableStateOf(0)
    }
    var _timer_m = remember {
        mutableStateOf(3)
    }
    var _timer_h = remember {
        mutableStateOf(3)
    }

    Surface(color = MaterialTheme.colors.background) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = _context.getString(R.string.app_name),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                SlidingTimeSelection(
                    modifier = Modifier
                        .weight(1f)
                        .height(280.dp),
                    text = "H",
//                    text = "Hour",
                    maxValue = 23,
                    callBack = fun(it: Int) {
                        _timer_h.value = it
                        // Log.d("tzmax", "选中的小时: $it")
                    }
                )

                SlidingTimeSelection(
                    modifier = Modifier
                        .weight(1f)
                        .height(280.dp),
                    text = "M",
//                    text = "Minute",
                    callBack = fun(it: Int) {
                        _timer_m.value = it
                        // Log.d("tzmax", "选中的分钟: $it")
                    }
                )

                SlidingTimeSelection(
                    modifier = Modifier
                        .weight(1f)
                        .height(280.dp),
                    text = "S",
//                    text = "Second",
                    callBack = fun(it: Int) {
                        _timer_s.value = it
                        // Log.d("tzmax", "选中的秒钟: $it")
                    }
                )
            }

            // 控制栏
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_action_play),
                        contentDescription = "",
                        Modifier
                            .width(85.dp)
                            .height(85.dp)
                            .clip(RoundedCornerShape((42.5).dp))
                            .clickable {
                                TimerActivity.actionStart(_timer_h.value, _timer_m.value, _timer_s.value)
                            }
                    )
                    Text(
                        text = "Run timer",
                        modifier = Modifier.padding(top = 5.dp),
                        color = Color(0x66000000),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
