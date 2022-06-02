package com.app.smartwatchapplication.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Activities.Login.LoginActivity;
import com.app.smartwatchapplication.Activities.OnBoardingScreens.OnBoardingScreen1Activity;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.SharedPreferences.SharedPref;

public class LoadingScreenActivity extends AppCompatActivity {
    ImageView ivLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPref.init(LoadingScreenActivity.this);
        setContentView(R.layout.activity_loading_screen);
        init();
        addAnimation();
    }

    private void init() {
        ivLogo = findViewById(R.id.iv_logo);
    }

    private void addAnimation() {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000);
        animator.setInterpolator(new DecelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivLogo.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivLogo.startAnimation(anim);

        new Handler().postDelayed(() -> {
            if (!SharedPref.readBoolean(Constants.ON_BOARDING_SHOWN, Constants.IS_ON_BOARDING_SHOWN)) {
                startActivity(new Intent(LoadingScreenActivity.this, OnBoardingScreen1Activity.class));
            }
            else {
                startActivity(new Intent(LoadingScreenActivity.this, LoginActivity.class));
            }
            overridePendingTransition(0,0);
            finish();
        },4500);
    }
}