package com.example.beyzacan.prototip_beyza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }
    public void yolculuk_tiklandi(View view)
    {
        Intent way_intent=new Intent(getBaseContext(),WayActivity.class);
        startActivity(way_intent);

    }
    public void yollarim_tiklandi(View view)
    {
        Intent way_intent=new Intent(getBaseContext(), SearchActivity.class);
        startActivity(way_intent);

    }
    public void favorilerim_tiklandi(View view)
    {
        Intent way_intent=new Intent(getBaseContext(),FavorilerActivity.class);
        startActivity(way_intent);
    }
    public void bildirimler_tiklandi(View view)
    {
       Intent intent=new Intent(getBaseContext(),HaritaGlobal.class);
       startActivity(intent);

    }
}
