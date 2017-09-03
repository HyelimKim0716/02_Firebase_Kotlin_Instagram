package com.example.myapplication.view.login.presenter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
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
class LoginPresenter(override val view: LoginContract.View, val activity: AppCompatActivity) : LoginContract.Presenter {

    private var mFirebaseAuth: FirebaseAuth? = null

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
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, mConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build()
    }


    private var mFirebaseAuthListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user: FirebaseUser? = firebaseAuth.currentUser

        if (user != null) {
            val intent: Intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }


    private var mConnectionFailedListener: GoogleApiClient.OnConnectionFailedListener = GoogleApiClient.OnConnectionFailedListener {

    }

    init {
        // Make an object which manages entire firebase login services
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    override fun addFirebaseAuthStateListener() {
        mFirebaseAuth?.addAuthStateListener(mFirebaseAuthListener)
    }

    override fun removeFirebaseAuthStateListener() {
        mFirebaseAuth?.removeAuthStateListener(mFirebaseAuthListener)    }

    /**
     * Register email and sign in
     */
    override fun registerEmailAndSignIn() {
        mFirebaseAuth?.createUserWithEmailAndPassword(view.getUserMail(), view.getUserPassword())
                ?.addOnCompleteListener(activity) { task ->
                                        if (task.isSuccessful) {
                        Toast.makeText(activity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    } else if (view.getUserPassword().length < 6) {
//                    Error : Password needs 6 characters more
                        Toast.makeText(activity, task.exception?.message, Toast.LENGTH_SHORT).show()
                    } else {
//                    Error : If firebase has same id which user input, sign in email, not occurs error
                        sendEmailInfoToFirebaseAuth()
                    }

                }

    }

    /**
     * Send email information to Firebase
     */
    fun sendEmailInfoToFirebaseAuth() {
        mFirebaseAuth?.signInWithEmailAndPassword(view.getUserMail(), view.getUserPassword())
                ?.addOnCompleteListener(activity) {
                    task ->
                    if (task.isSuccessful) {

                    } else {
                        // Error : Wrong password
                        Toast.makeText(activity, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

    }

    /**
     * Send information from Google to Firebase
     */
    override fun sendGoogleInfoToFirebaseAuth(googleSignInAccount: GoogleSignInAccount?) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(googleSignInAccount?.idToken, null)

        mFirebaseAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(activity) { task ->
                    if (!task.isSuccessful) {

                    }
                }
    }

    /**
     * Send a Facebook token to Firebase
     */
    fun sendFacebookTokenToFirebaseAuth(token: AccessToken?) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token?.token!!)
        mFirebaseAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {

                    }
                }
    }

}