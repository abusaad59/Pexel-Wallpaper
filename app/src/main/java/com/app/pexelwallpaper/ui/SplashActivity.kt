package com.app.pexelwallpaper.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.pexelwallpaper.MainActivity
import com.app.pexelwallpaper.R

class SplashActivity : AppCompatActivity() {
    private lateinit var ivLogo:ImageView
    private lateinit var tvAppName:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ivLogo=findViewById(R.id.ivLogo)
        tvAppName=findViewById(R.id.tvAppName)

        ivLogo.alpha = 0f
        tvAppName.alpha = 0f

        val logoAnimatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(ivLogo, "translationX", 500f, 0f).apply {
                    duration = 2000
                },
                ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f).apply {
                    duration = 2000
                }
            )
        }

        val textAnimatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(tvAppName, "translationY", 500f, 0f).apply {
                    duration = 1500
                },
                ObjectAnimator.ofFloat(tvAppName, "alpha", 0f, 1f).apply {
                    duration = 1500
                }
            )
        }

        AnimatorSet().apply {
            playSequentially(logoAnimatorSet, textAnimatorSet)
            start()

            doOnEnd {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }, 1000)
            }
        }
    }
}