package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.handwriting.handwritingHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp.R
import com.example.travelapp.ui.theme.component.PrimaryButton
import com.example.travelapp.ui.theme.navigation.Screen

@Composable
fun Wellcome(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC9EEFF))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // 🎨 Tranh minh họa
        WelcomeIllustration()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Travel the world\nwith us",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Get best deals on all your online\n" +
                    "travel bookings. Book hotels, flights,\n" +
                    "bus, trains and cabs.",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            PrimaryButton(
                text = "Let's Go!",
                onClick = { navController.navigate(Screen.Login.route)},
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun WelcomeIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {

        // 1️⃣ Mặt trời
        Image(
            painter = painterResource(R.drawable.sun),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
        )

        // 2️⃣ Mây xa (nhỏ)
        Image(
            painter = painterResource(R.drawable.cloud),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopStart)
                .offset(x = -20.dp, y = 80.dp)
        )

        // 3️⃣ Trái đất
        Image(
            painter = painterResource(R.drawable.earth),
            contentDescription = null,
            modifier = Modifier
                .size(190.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-61).dp, y = 40.dp)
        )

        // 4️⃣ Mây lớn phía sau máy bay (MỚI)
        Image(
            painter = painterResource(R.drawable.cloud_big),
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopCenter)
                .offset(x = 70.dp,y = -30.dp)
        )

        // 5️⃣ Máy bay
        Image(
            painter = painterResource(R.drawable.plane),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.Center)
                .offset(x = 10.dp, y =70.dp)
        )

        // 6️⃣ Mây trước máy bay (che nhẹ)
        Image(
            painter = painterResource(R.drawable.cloud_front),
            contentDescription = null,
            modifier = Modifier
                .size(270.dp)
                .align(Alignment.Center)
                .offset(x = 132.dp, y = 100.dp)
        )
    }
}
