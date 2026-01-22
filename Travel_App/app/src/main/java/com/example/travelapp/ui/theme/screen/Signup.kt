package com.example.travelapp.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.travelapp.R
import com.example.travelapp.ui.theme.component.InputField
import com.example.travelapp.ui.theme.component.PassWordField
import com.example.travelapp.ui.theme.component.PrimaryButton
import com.example.travelapp.ui.theme.component.PrimaryofButton
import com.example.travelapp.ui.theme.navigation.Screen

@Composable
fun Signup(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    val isPasswordMismatch =
        confirm.isNotEmpty() && password != confirm

    val isFormValid =
        name.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirm.isNotBlank() &&
                !isPasswordMismatch

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SigupIllustration()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Create Your Account",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0B3C6F)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFC9EEFF)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                InputField(name, { name = it }, "Full Name")
                InputField(email, { email = it }, "Email")
                PassWordField(password, { password = it }, "Password", true)
                PassWordField(confirm, { confirm = it }, "Confirm Password", true)

                if (isPasswordMismatch) {
                    Text(
                        text = "Password does not match",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                PrimaryofButton(
                    text = "Sign Up",
                    onClick = { navController.navigate("login") },
                    enabled = isFormValid
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text(
                text = "Already have an account? ",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Text(
                text = "Login",
                fontSize = 13.sp,
                color = Color(0xFF000000),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}
@Composable
fun SigupIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.sun_2),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-30).dp, y = (-40).dp)
        )

        Image(
            painter = painterResource(R.drawable.cloud),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopStart)
                .offset(x = 30.dp, y = 80.dp)
        )
        Image(
            painter = painterResource(R.drawable.cloud),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopStart)
                .offset(x = 140.dp, y = (20).dp)
        )
        Image(
            painter = painterResource(R.drawable.cloud),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = 0.dp)
        )

        Image(
            painter = painterResource(R.drawable.cloud_big),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
                .offset(x = 130.dp,y = -30.dp)
        )
    }
}