package com.example.travelapp.ui.theme.api

import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

fun firebaseAuthWithFacebook(
    accessToken: AccessToken,
    onSuccess: () -> Unit
) {
    val credential = FacebookAuthProvider.getCredential(accessToken.token)
    FirebaseAuth.getInstance()
        .signInWithCredential(credential)
        .addOnSuccessListener { onSuccess() }
}