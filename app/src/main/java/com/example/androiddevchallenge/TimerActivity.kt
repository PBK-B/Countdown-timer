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
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.components.CircularProgressIndicator
import com.example.androiddevchallenge.ui.theme.MyTheme

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerActivityMyApp()
        }
    }
}

@Composable
fun TimerActivityMyApp() {
    val _context = BaseApplication.getContext()

    val animatedProgress = animateFloatAsState(
        targetValue = 0.2f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Surface(color = MaterialTheme.colors.background) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            // 标题栏
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = "Timing…",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            // 内容区
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.fillMaxSize()) {

                    CircularProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .width(280.dp)
                            .height(280.dp)
                            .align(
                                Alignment.Center
                            ),

                    )

                    Column(modifier = Modifier.align(Alignment.Center).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                        Row() {
                            Text(
                                text = "13",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF0000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp
                            )

                            Text(
                                text = ":",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF0000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp
                            )

                            Text(
                                text = "36",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF0000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp
                            )

                            Text(
                                text = ":",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF0000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp
                            )

                            Text(
                                text = "57",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF0000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Total time 24H 56M 00S")
                        }
                    }
                }
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
                        painter = painterResource(id = R.drawable.ic_action_stop),
                        contentDescription = "",
                        Modifier
                            .width(65.dp)
                            .height(65.dp)
                    )
                    Text(
                        text = "Stop",
                        modifier = Modifier.padding(top = 5.dp),
                        color = Color(0x66000000)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_action_pause),
                        contentDescription = "",
                        Modifier
                            .width(85.dp)
                            .height(85.dp)
                    )
                    Text(
                        text = "Pause",
                        modifier = Modifier.padding(top = 5.dp),
                        color = Color(0x66000000)
                    )
                }
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun TimerActivityLightPreview() {
    MyTheme {
        TimerActivityMyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun TimerActivityDarkPreview() {
    MyTheme(darkTheme = true) {
        TimerActivityMyApp()
    }
}
