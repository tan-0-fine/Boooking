package com.example.travelapp.ui.theme.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.travelapp.R
import com.example.travelapp.ui.theme.api.firebaseAuthWithGoogle
import com.example.travelapp.ui.theme.api.getGoogleSignInClient
import com.example.travelapp.ui.theme.component.InputField
import com.example.travelapp.ui.theme.component.PassWordField
import com.example.travelapp.ui.theme.component.PrimaryButton
import com.example.travelapp.ui.theme.component.PrimaryofButton
import com.example.travelapp.ui.theme.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(navController: NavHostController) {
    val context = LocalContext.current
    val googleSignInClient = remember {
        getGoogleSignInClient(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken!!

                firebaseAuthWithGoogle(idToken) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    val callbackManager = remember {
        CallbackManager.Factory.create()
    }

    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val credential =
                        FacebookAuthProvider.getCredential(result.accessToken.token)

                    FirebaseAuth.getInstance()
                        .signInWithCredential(credential)
                        .addOnSuccessListener {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {
                    error.printStackTrace()
                }
            }
        )
        onDispose { }
    }

    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("") }

    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        LoginIllustration()

        Text(
            text = "Login To Your Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.offset(y = (-70).dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFC9EEFF)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-70).dp)
                .padding(horizontal = 50.dp)
                .fillMaxHeight(0.6f)

        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                InputField(
                    value = email,
                    onValueChange = {email = it},
                    placeholder = "Email"
                )
                PassWordField(
                    value = password,
                    onValueChange = {password = it},
                    placeholder = "Password",
                    isPassword = true
                )
                Text(
                    text = "Forgot Password?",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            showForgotDialog = true
                        }
                )
                if (showForgotDialog) {
                    ForgotPasswordDialog(
                        email = forgotEmail,
                        onEmailChange = { forgotEmail = it },
                        onDismiss = {
                            showForgotDialog = false
                        },
                        onSend = {
                            // TODO: gọi API gửi mail reset
                            showForgotDialog = false
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    PrimaryButton(
                        text = "Login",
                        onClick = { navController.navigate(Screen.Home.route)},
                        modifier = Modifier
                            .fillMaxWidth(1f),
                    )
                }
            }
        }
        OrDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            SocialIcon(
                icon = R.drawable.facebook,
                onClick = {
                    LoginManager.getInstance()
                        .logInWithReadPermissions(
                            context as Activity,
                            listOf("email", "public_profile")
                        )
                }
            )

            SocialIcon(
                icon = R.drawable.google,
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            )

            SocialIcon(
                icon = R.drawable.apple,
                onClick = {
                    // TODO: Apple login (demo)
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Don't have an account? ",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Text(
                text = "Sign Up",
                fontSize = 13.sp,
                color = Color(0xFF000000),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }

    }
}

@Composable
fun LoginIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.sun),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
        )

        Image(
            painter = painterResource(R.drawable.cloud),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopStart)
                .offset(x = -20.dp, y = 80.dp)
        )

        Image(
            painter = painterResource(R.drawable.cloud_big),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
                .offset(x = 90.dp,y = -30.dp)
        )
    }
}
@Composable
fun SocialIcon(
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Card(
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() }
            .background(Color.White)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
@Composable
fun OrDivider(
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            text = "or continue with",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(modifier = Modifier.weight(1f))
    }
}
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0B3C6F),
            disabledContainerColor = Color(0xFF9FB3C8)
        )
    ) {
        Text(text, color = Color.White)
    }
}
