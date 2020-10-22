package com.thestudyguide.activities.onboarding

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.tedpark.tedpermission.rx2.TedRx2Permission
import com.thestudyguide.R
import com.thestudyguide.adapters.SliderAdapter
import com.thestudyguide.databinding.ActivityOnboardingBinding

@Suppress("DEPRECATION")
class OnBoardingActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var viewPagerAdapter: SliderAdapter
    private lateinit var binding: ActivityOnboardingBinding
    private val handler by lazy { Handler(Looper.myLooper()!!) }

    private val dots = arrayOfNulls<ImageView>(4) // Number of onBoarding images

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)


        viewPagerAdapter = SliderAdapter(this)
        binding.onboardViewPager.adapter = viewPagerAdapter
        //Initialize onBoarding image's counter
        for (i in 0..4){
            dots[i] = ImageView(this)
        }
        addDot(0)
        binding.onboardViewPager.addOnPageChangeListener(listener)

        binding.buttonProceed.setOnClickListener(this)
    }

    private val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            addDot(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    private fun addDot(position: Int) {
        for (i in 0..4){
            dots[i]!!.setNonSelectedDot()
            binding.layoutDots.addView(dots[i])
        }
        dots[position]!!.setSelectedDot()
    }

    private fun ImageView.setNonSelectedDot() {
        this.setImageDrawable(
            ContextCompat.getDrawable(
                this@OnBoardingActivity,
                R.drawable.non_selected_dot
            )
        )
    }

    private fun ImageView.setSelectedDot() {
        this.setImageDrawable(
            ContextCompat.getDrawable(
                this@OnBoardingActivity,
                R.drawable.selected_dot
            )
        )
    }

    private fun askPermission() {
        TedRx2Permission.with(this)
            .setRationaleMessage(R.string.rationale_message)
            .setDeniedCloseButtonText(android.R.string.cancel)
            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .setDeniedMessage(R.string.message_permission_denied)
            .request()
            .subscribe({ tedPermissionResult ->
                if (tedPermissionResult.isGranted) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    handler.postDelayed({
                        startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }, 200)
                } else {
                    Toast.makeText(
                        this, "Permission Denied\n" + tedPermissionResult.deniedPermissions.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, { })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        askPermission()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.buttonProceed -> askPermission()
        }
    }
}