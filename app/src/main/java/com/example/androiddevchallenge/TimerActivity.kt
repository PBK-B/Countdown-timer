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

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.core.content.ContextCompat
import com.example.androiddevchallenge.components.CircularProgressIndicator
import com.example.androiddevchallenge.ui.theme.MyTheme

class TimerActivity : AppCompatActivity() {

    companion object {
        fun actionStart(
            totalH: Int = 0,
            totalM: Int = 0,
            totalS: Int = 60,
        ) {

            val context = BaseApplication.getContext()
            val intent = Intent(context, TimerActivity::class.java)
            intent.putExtra("totalH", totalH)
            intent.putExtra("totalM", totalM)
            intent.putExtra("totalS", totalS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val totalH = intent.getIntExtra("totalH", 0)
        val totalM = intent.getIntExtra("totalM", 0)
        val totalS = intent.getIntExtra("totalS", 60)

        setContent {
            TimerActivityMyApp(
                totalH = totalH,
                totalM = totalM,
                totalS = totalS,
                onBackPressed = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition()
                    } else {
                        finish()
                    }
                }
            )
        }
    }
}

@Composable
fun TimerActivityMyApp(
    totalH: Int = 0,
    totalM: Int = 0,
    totalS: Int = 0,
    onBackPressed: () -> Unit = {}
) {

    val TAG = "TzMax"
    val _context = BaseApplication.getContext()

    val _timer_t_second = (totalH * 60 * 60) + (totalM * 60) + totalS
    var _timer_val_second = remember {
        mutableStateOf(0)
    }

    var _timer_s = remember {
        mutableStateOf(totalS)
    }
    var _timer_m = remember {
        mutableStateOf(totalM)
    }
    var _timer_h = remember {
        mutableStateOf(totalH)
    }

    var _timer_run_status = remember {
        mutableStateOf(true)
    }

    var _progress = remember {
        mutableStateOf(0f)
    }
    val animatedProgress = animateFloatAsState(
        targetValue = _progress.value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    val mRunnable = Runnable {
        run {
            while (_timer_run_status.value) {
                _timer_val_second.value += 1

                if (_timer_s.value > 0) {
                    _timer_s.value -= 1 // 计算秒数
                }

                if (_timer_s.value <= 0 && _timer_m.value >= 1) {
                    _timer_s.value = 60
                    _timer_m.value -= 1
                }

                if (_timer_m.value <= 0 && _timer_h.value >= 1) {
                    _timer_m.value = 60
                    _timer_h.value -= 1
                }

                if (_timer_h.value <= 0 && _timer_m.value <= 0 && _timer_s.value <= 0) {
                    _timer_run_status.value = false
                }

                val t_s = (_timer_t_second).toFloat()
                val v_s = (_timer_val_second.value).toFloat()
                _progress.value = v_s / t_s

                Log.d(
                    TAG,
                    "TimerActivityMyApp: p=${_progress.value} ts=${_timer_val_second.value} s=${_timer_s.value} m=${_timer_m.value}"
                )
                Thread.sleep(1000)
            }
        }
    }

    LaunchedEffect(_timer_t_second) {
        Thread(mRunnable).start()
    }

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

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row() {
                            Text(
                                text = "${if (_timer_h.value <= 9) "0" + _timer_h.value else _timer_h.value}",
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
                                text = "${if (_timer_m.value <= 9) "0" + _timer_m.value else _timer_m.value}",
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
                                text = "${if (_timer_s.value <= 9) "0" + _timer_s.value else _timer_s.value}",
                                textAlign = TextAlign.Center,
                                color = Color(0xFF0000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Total time ${totalH}H ${totalM}M ${totalS}S")
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
                            .clip(RoundedCornerShape((32.5).dp))
                            .clickable {
                                _timer_run_status.value = false // 结束计时
                                onBackPressed()
                            }
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
                        painter = if (_timer_run_status.value) {
                            painterResource(id = R.drawable.ic_action_pause)
                        } else {
                            painterResource(
                                id = R.drawable.ic_action_play
                            )
                        },
                        contentDescription = "",
                        Modifier
                            .width(85.dp)
                            .height(85.dp)
                            .clip(RoundedCornerShape((42.5).dp))
                            .clickable {
                                _timer_run_status.value = !_timer_run_status.value
                                if (_timer_run_status.value) {
                                    Thread(mRunnable).start()
                                }
                            }
                    )
                    Text(
                        text = if (_timer_run_status.value) "Pause" else "Start",
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
