package com.example.timer

import TimerViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.ui.theme.TimerTheme
import java.lang.StringBuilder
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : ComponentActivity() {

    private val timerviewModel by viewModels<TimerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // setContent는 화면에 그려질 내용을 지정하는 함수

            TimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    timer(timerviewModel)
                }
            }
        }
    }
}

@Composable
fun timer(timerViewModel: TimerViewModel) {

    // animateFloatAsState는 애니메이션을 적용할 값을 지정하는 targetValue와 애니메이션을 적용하는데 걸리는 시간을 지정하는 animationSpec을 인자로 받는다.
    // tween(durationMillis = 1000, easing = LinearEasing) : 1초동안 선형적으로 증가 (LinearEasing)
    // easing의 종류 : https://developer.android.com/reference/kotlin/androidx/compose/animation/core/package-summary#easing


    val animatedSweepAngle by animateFloatAsState(
        targetValue = (timerViewModel.sec.value.toFloat() / 60) * 360, // 1초에 6도씩 증가
        animationSpec = tween(durationMillis = 400, easing = LinearEasing), label = ""
    )

    val record_text = StringBuilder()
    timerViewModel.recordList.forEachIndexed { index, s -> record_text.append("${index + 1} : $s\n") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 30.sp,
            fontWeight = FontWeight(1000),
            text = "스톱워치"

        )

        Spacer(modifier = Modifier.size(20.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(220.dp)
                //.clip(CircleShape) // 모양 자르기
                .border(1.dp, Color.Transparent, CircleShape) // 테두리
                .background(color = Color.Cyan, shape = CircleShape)
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize(),
            ) {
                // Box 크기만큼 원 태두리
                drawIntoCanvas { canvas ->
                    val canvasSize = drawContext.size
                    drawArc(
                        color = Color.LightGray,
                        startAngle = -90f, // 0도
                        sweepAngle = 360f, // 360도
                        useCenter = false, // 원의 중심을 사용할 것인지
                        topLeft = Offset(0f, 0f), // 테두리 위치
                        size = canvasSize,// 테두리 크기
                        style = Stroke(width = 8f) // 테두리 두께
                    )
                    // 원 그리기
                    drawArc(
                        color = Color.Black,
                        startAngle = -90f,
                        sweepAngle = animatedSweepAngle,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        size = canvasSize,
                        style = Stroke(width = 15f , cap =  Round)
                    )

                    val radius = canvasSize.width / 2 // 반지름
                    val angleInRadian =
                        Math.toRadians((animatedSweepAngle - 90).toDouble()) // 각도를 라디안으로 변환
                    val circleX = (radius + radius * Math.cos(angleInRadian)).toFloat()
                    val circleY = (radius + radius * Math.sin(angleInRadian)).toFloat()
                    if (timerViewModel.button.value == "멈추기") {
                        drawCircle(
                            color = Color.Black, center = Offset(circleX, circleY), radius = 20f
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                //text = time.value, // time하고 time2를 합쳐서 표시하기
                text = timerViewModel.min.value + ':' + timerViewModel.sec.value + '.' + timerViewModel.msec.value, // time하고 time2를 합쳐서 표시하기
                fontSize = 30.sp,
                fontWeight = FontWeight(1000),
            )
        }

        Spacer(modifier = Modifier.size(10.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Button(
                modifier = Modifier.size(130.dp, 50.dp),
                onClick = {
                timerViewModel.timer()
            }
            ) {
                Text(
                    text = timerViewModel.button.value,
                    fontSize = 20.sp,
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                modifier = Modifier.size(130.dp, 50.dp),
                onClick = {
                timerViewModel.record()
            }
            ) {
                Text(
                    text = timerViewModel.reset_button.value,
                    fontSize = 20.sp,
                )
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), // 스크롤 가능하게
        ) {
            Text(
                text = record_text.toString(),

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewDark() {
    TimerTheme(darkTheme = true) {
        timer(timerViewModel = TimerViewModel())
    }
}