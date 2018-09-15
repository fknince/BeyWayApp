package com.example.beyzacan.prototip_beyza;


import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Harita extends AppCompatActivity {

    private ArrayList<LatLng> konumlar;
    android.support.v4.app.Fragment fragment;
    ArrayList<String> durumlar;
    BlankFragment blankFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harita);
        durumlar=getIntent().getStringArrayListExtra("durumlar");

        double[] konumlar_double=getIntent().getDoubleArrayExtra("konumlar");
        konumlar=new ArrayList<LatLng>();
        for(int i=0;i<konumlar_double.length;i++)
        {
            if((i+1)%2 == 0)
            {
                LatLng konum=new LatLng(konumlar_double[i-1],konumlar_double[i]);
                konumlar.add(konum);

            }

        }
        blankFragment=((BlankFragment) getSupportFragmentManager().findFragmentById(R.id.fr_2));
        blankFragment.setHarita(this);
        FragmentManager manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.fr_2);











    }
    public void ciz()
    {

        blankFragment.zoom(konumlar.get(0));
        blankFragment.cizdir(konumlar,durumlar);
    }

}
