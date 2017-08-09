package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 1000      // Internet Request Id
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private var mFirebaseAuthListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user: FirebaseUser? = firebaseAuth.currentUser

        val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private var mFacebookCallbackManager: CallbackManager? = null
    private var mConnectionFailedListener: GoogleApiClient.OnConnectionFailedListener = GoogleApiClient.OnConnectionFailedListener {

    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth?.addAuthStateListener(mFirebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAuth?.removeAuthStateListener(mFirebaseAuthListener)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Make an object which manages entire firebase login services
        mFirebaseAuth = FirebaseAuth.getInstance()

        // Set google login options (request token, request authentication, ... etc)
        val mGoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, mConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build()

        mFacebookCallbackManager = CallbackManager.Factory.create()

        btn_email_sign_in.setOnClickListener(btnEmailSignInClickListener)
        btn_google_sign_in.setOnClickListener(btnGoogleSignInClickListener)
        btn_facebook_sign_in.setReadPermissions("email", "public_profile")
        btn_facebook_sign_in.registerCallback(mFacebookCallbackManager, object: FacebookCallback<LoginResult> {
            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSuccess(result: LoginResult?) {
                sendFacebookTokenToFirebaseAuth(result?.accessToken)
            }

            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    /**
     * Register email and sign in
     */
    fun registerEmailAndSignIn() {
        mFirebaseAuth?.createUserWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
                ?.addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    } else if (et_password.text.toString().length < 6) {
                        // Error : Password needs 6 characters more
                        Toast.makeText(this@LoginActivity, task.exception?.message, Toast.LENGTH_SHORT).show()
                    } else {
                        // Error : If firebase has same id which user input, sign in email, not occurs error
                        sendEmailInfoToFriebaseAuth()
                    }
                }

    }

    /**
     * Send email information to Firebase
     */
    fun sendEmailInfoToFriebaseAuth() {
        mFirebaseAuth?.signInWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
                ?.addOnCompleteListener(this@LoginActivity) {
                    task ->
                    if (task.isSuccessful) {

                    } else {
                        // Error : Wrong password
                        Toast.makeText(this@LoginActivity, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }

    /**
     * Send information from Google to Firebase
     */
    fun sendGoogleInfoToFirebaseAuth(googleSignInAccount: GoogleSignInAccount?) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(googleSignInAccount?.idToken, null)

        mFirebaseAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this@LoginActivity) { task ->
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

    val btnGoogleSignInClickListener: View.OnClickListener = View.OnClickListener {
        val signInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    val btnEmailSignInClickListener: View.OnClickListener = View.OnClickListener {
        registerEmailAndSignIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mFacebookCallbackManager?.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> {
                val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

                if (result.isSuccess) {
                    val account: GoogleSignInAccount? = result.signInAccount
                    sendGoogleInfoToFirebaseAuth(account)

                }
            }
        }
    }
}
