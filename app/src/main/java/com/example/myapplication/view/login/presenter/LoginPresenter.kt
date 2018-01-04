package com.example.myapplication.view.login.presenter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.view.MainActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*

/**
 * Created by Owner on 2017-08-10.
 */
open class LoginPresenter(override val mView: LoginContract.View, private val mActivity: AppCompatActivity) : LoginContract.Presenter {

    private val mFirebaseAuth: FirebaseAuth by lazy {
        // Make an object which manages entire firebase login services
         FirebaseAuth.getInstance()
    }

    override val mFacebookCallbackManager: CallbackManager? by lazy {
        CallbackManager.Factory.create()
    }

    override val mFacebookCallback: FacebookCallback<LoginResult> = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            sendFacebookTokenToFirebaseAuth(result?.accessToken)
        }

        override fun onError(error: FacebookException?) {

        }

        override fun onCancel() {

        }
    }

    override val mGoogleApiClient: GoogleApiClient? by lazy {
        // Set google login options (request token, request authentication, ... etc)
        val mGoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, mConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build()
    }


    private var mFirebaseAuthListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user: FirebaseUser? = firebaseAuth.currentUser



    }


    private var mConnectionFailedListener: GoogleApiClient.OnConnectionFailedListener = GoogleApiClient.OnConnectionFailedListener {

    }

    override fun addFirebaseAuthStateListener() {
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener)
    }

    override fun removeFirebaseAuthStateListener() {
        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener)    }

    /**
     * Register email and sign in
     */
    override fun registerEmailAndSignIn() {
        Log.d("LoginPresenter", "registerEmailAndSignIn, mail:${mView.getUserMail()}, password: ${mView.getUserPassword()}")

        mFirebaseAuth.createUserWithEmailAndPassword(mView.getUserMail(), mView.getUserPassword())
                .addOnCompleteListener(mActivity) { task ->
                    Log.d("LoginPresenter", "registerEmailAndSignIn, isSuccessful? ${task.isSuccessful}")
                    when {
                        task.isSuccessful -> Toast.makeText(mActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        mView.getUserPassword().length < 6 -> //                    Error : Password needs 6 characters more
                            Toast.makeText(mActivity, task.exception?.message, Toast.LENGTH_SHORT).show()
                        else -> //                    Error : If firebase has same id which user input, sign in email, not occurs error
                            sendEmailInfoToFirebaseAuth()
                    }

                }.addOnSuccessListener {
            Log.d("LoginPresenter", "OnSuccessListener, Success Login!")
        }.addOnFailureListener {
            it.printStackTrace()
            Log.e("LoginPresenter", "OnFailureListener, Fail Login, error: ${it.message}")
            if (it.message.equals("The email address is already in use by another account."))
                sendEmailInfoToFirebaseAuth()
        }

    }

    /**
     * Send email information to Firebase
     */
    private fun sendEmailInfoToFirebaseAuth() {
        mFirebaseAuth.signInWithEmailAndPassword(mView.getUserMail(), mView.getUserPassword())
                .addOnCompleteListener(mActivity) {
                    task ->
                    Log.d("LoginPresenter", "sendEmailInfoToFirebaseAuth, isSuccessful? ${task.isSuccessful}")
                    if (task.isSuccessful) {
                        Toast.makeText(mActivity, "signInWithEmailAndPassword is successful", Toast.LENGTH_SHORT).show()
                    } else {
                        // Error : Wrong password
                        Toast.makeText(mActivity, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

    }

    /**
     * Send information from Google to Firebase
     */
    override fun sendGoogleInfoToFirebaseAuth(googleSignInAccount: GoogleSignInAccount?) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(googleSignInAccount?.idToken, null)

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity) { task ->
                    if (!task.isSuccessful) {

                    } else {
                        val intent: Intent = Intent(mActivity, MainActivity::class.java)
                        mActivity.startActivity(intent)
                        mActivity.finish()
                    }
                }
    }

    /**
     * Send a Facebook token to Firebase
     */
    fun sendFacebookTokenToFirebaseAuth(token: AccessToken?) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token?.token!!)
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {

                    }
                }
    }

}