package com.app.smartwatchapplication.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Activities.OnBoardingScreens.OnBoardingScreen1Activity;
import com.app.smartwatchapplication.R;

public class LoadingScreenActivity extends AppCompatActivity {
    ImageView ivLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        init();
        addAnimation();
    }

    private void init() {
        ivLogo = findViewById(R.id.iv_logo);
    }

    private void addAnimation() {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3000);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000);
        animator.setInterpolator(new DecelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationEnd(Animation animation) {
                ivLogo.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivLogo.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingScreenActivity.this, OnBoardingScreen1Activity.class));
                overridePendingTransition(0,0);
                finish();
            }
        },2500);
    }
}