package com.chimpcode.discount.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.transition.Slide
import android.transition.TransitionInflater
import android.util.Log
import android.view.Gravity
import com.chimpcode.discount.GointApplication
import com.chimpcode.discount.R
import com.chimpcode.discount.data.User
import com.chimpcode.discount.viewmodels.LoginViewInteractor
import com.chimpcode.discount.viewmodels.LoginViewModel
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoginViewInteractor {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private val TAG = "LoginActivity ***"
    private val TAG_FACEBOOK = "Facebook ***"
    private var  callbackManager : CallbackManager? = null

    var loginViewModel : LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowAnimations()
        setContentView(R.layout.activity_login)


        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_background)
        video_background.setVideoURI(uri)
        video_background.setOnPreparedListener {
            it.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            it.isLooping = true
            it.setScreenOnWhilePlaying(false)
        }
        video_background.start()

        val apolloClient = (application as GointApplication).apolloClient()

        callbackManager = CallbackManager.Factory.create()
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.d(TAG_FACEBOOK, "Already logged with fb login")
            goToHome()
        }

        loginViewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]

        val loginButton = findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions("public_profile", "email")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d(TAG_FACEBOOK, "...login result success")
                Log.d(TAG_FACEBOOK, "result token" + result.accessToken)

                val request : GraphRequest = GraphRequest.newMeRequest(
                        result.accessToken,
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(jsonobject: JSONObject, response: GraphResponse?) {

                                Log.d(TAG_FACEBOOK, response.toString())
                                Log.d(TAG_FACEBOOK, jsonobject.toString())

                                val profileId : String = jsonobject["id"] as String
                                loginViewModel!!.facebookId = profileId
                                loginViewModel!!.apolloClient = apolloClient
                                loginViewModel!!.login(apolloClient!!, profileId, this@LoginActivity, jsonobject)
                            }
                        }
                )
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,age_range")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                Log.d(TAG_FACEBOOK, "...login failed")
            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG_FACEBOOK, "result error" + error?.localizedMessage )
            }
        })

//        FOR TEST
        email_sign_in_button.setOnClickListener { goToHome() }
//        sign_up_button.setOnClickListener { goToSignup() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupWindowAnimations() {
        val slide= TransitionInflater.from(this).inflateTransition(R.transition.activity_slide) as Slide
        slide.slideEdge = Gravity.START
        window.exitTransition = slide

    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCompleteLogin(user: User) {
        val sharedPref =  getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(getString(R.string.facebook_user_id), loginViewModel!!.facebookId)
            putString(getString(R.string.user_id), user.id)
            putString(getString(R.string.fullname), user.fullName)
            putString(getString(R.string.email_user), user.email)
            commit()
        }
        goToHome()
    }

    override fun onFailedLogin(message: String) {

    }


//    private fun goToSignup() {
//
//        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
//        val intent = Intent(this, SignupActivity::class.java)
//        startActivity(intent, options.toBundle())
//    }

}
