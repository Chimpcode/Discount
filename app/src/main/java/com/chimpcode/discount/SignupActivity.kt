package com.chimpcode.discount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.view.animation.DecelerateInterpolator

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowAnimations()
        setContentView(R.layout.activity_signup)

    }

    private fun setupWindowAnimations() {
        val fade = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade_in)
        window.enterTransition = fade
//        val fade = Fade(Fade.IN)
//        fade.duration = 1000
//        fade.interpolator = DecelerateInterpolator()
    }
}
