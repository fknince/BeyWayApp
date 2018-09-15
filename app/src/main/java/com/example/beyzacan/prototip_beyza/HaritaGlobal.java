package com.example.beyzacan.prototip_beyza;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class HaritaGlobal extends AppCompatActivity {


    ArrayList<ArrayList<LatLng>> yollar;
    ArrayList<ArrayList<String>> yol_durumlari;
    FirebaseModule firebaseModule=new FirebaseModule();
    global_fragment blankFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harita_global);
        yollar=new ArrayList<>();
        yol_durumlari=new ArrayList<>();
        blankFragment=((global_fragment) getSupportFragmentManager().findFragmentById(R.id.fr_3));
        blankFragment.setHarita(this);





        firebaseModule.tum_yollari_ciz(this);

    }

    public void ciz(ArrayList<ArrayList<ArrayList<Double>>> konumlar, ArrayList<ArrayList<String>> durumlar) {

        for (int i = 0; i < konumlar.size(); i++) {
            ArrayList<LatLng> gecici=new ArrayList<>();
            ArrayList<ArrayList<Double>> way_konumlar=konumlar.get(i);
            double[] konumlar_double=new double[(way_konumlar.size()*2)+1];
            int sayac=0;
            for(int j=0;j<(way_konumlar.size()*2);j++)
            {
                konumlar_double[j]=way_konumlar.get(sayac).get((j%2));
                if((j+1)%2 == 0)
                    sayac++;

            }
            for(int j=0;j<konumlar_double.length;j++)
            {
                if ((j + 1) % 2 == 0) {
                    LatLng konum=new LatLng(konumlar_double[j-1],konumlar_double[j]);
                    gecici.add(konum);
                }





            }
            yollar.add(gecici);

        }
        yol_durumlari=durumlar;





    }
    public void harbiden_ciz()
    {   if(yollar != null && yol_durumlari != null)
            blankFragment.cizdir(yollar,yol_durumlari);
    }
}
