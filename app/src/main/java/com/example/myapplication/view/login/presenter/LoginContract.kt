package com.example.myapplication.view.login.presenter

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by Owner on 2017-08-10.
 */
interface LoginContract {

    interface View {
        fun getUserMail() : String

        fun getUserPassword() : String
    }

    interface Presenter {
        val mView: View

        val mFacebookCallbackManager: CallbackManager?

        val mFacebookCallback: FacebookCallback<LoginResult>?

        val mGoogleApiClient : GoogleApiClient?

        fun addFirebaseAuthStateListener()

        fun removeFirebaseAuthStateListener()

        fun registerEmailAndSignIn()

        fun sendGoogleInfoToFirebaseAuth(googleSignInAccount: GoogleSignInAccount?)

    }
}