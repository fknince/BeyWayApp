package com.example.beyzacan.prototip_beyza;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;




public class SplashActivity extends AppCompatActivity {
    TextView tw_splashText;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tw_splashText=(TextView)findViewById(R.id.tw_splashText);
        Animation an = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash_anim);
        image=(ImageView)findViewById(R.id.imageView4);
        image.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(View.INVISIBLE);
                Intent intent=new Intent(getApplication(),StartActivity.class);
                startActivity(intent);
                finish();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }
}
