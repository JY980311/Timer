package com.example.timer.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon








import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.R
import com.example.timer.ui.theme.MyColor

@Composable
fun MyTopBar(
    modifier : Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription ="logo",
            tint = MyColor
        )
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp , Alignment.CenterHorizontally)
        ) {
            IconButton(
                modifier = Modifier
                    .size(35.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
            IconButton(
                modifier = Modifier
                    .size(35.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
            IconButton(
                modifier = Modifier
                    .size(35.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun cataegory(
    modifier : Modifier = Modifier
) {
    val items = remember { listOf("전체", "게임" , "음악","전체", "게임" , "음악","전체", "게임" , "음악")}

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp , Alignment.CenterHorizontally)
    ) {
        itemsIndexed(items) {index , item ->

            var isClick by remember { mutableStateOf(false) }

            CatagoryItem(
                title = item,
                isClick = isClick,
                onClick = {

                }
            )

        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
private fun CatagoryItem(
    modifier: Modifier = Modifier,
    title: String,
    isClick: Boolean,
    onClick: () -> Unit

) {

    val backgroundColor = if (isClick) Color.Black else Color.LightGray
    val textColor = if (isClick) Color.White else Color.Black

    Box(
        modifier = modifier
            .background(Color.Gray, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp , vertical = 3.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Light,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyTopBarPreview() {
    MyTopBar()
}
