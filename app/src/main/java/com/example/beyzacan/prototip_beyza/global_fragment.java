package com.example.beyzacan.prototip_beyza;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class global_fragment extends Fragment  implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    private static final int LOCATION_REQUEST=500;
    private HaritaGlobal harita;



    public HaritaGlobal getHarita() {
        return harita;
    }

    public void setHarita(HaritaGlobal harita) {
        this.harita = harita;
    }

    public  ArrayList<ArrayList<ArrayList<Double>>> yollar;
    public  ArrayList<ArrayList<String>> yol_durumlari;

    public ArrayList<ArrayList<ArrayList<Double>>> getYollar() {
        return yollar;
    }

    public void setYollar(ArrayList<ArrayList<ArrayList<Double>>> yollar) {
        this.yollar = yollar;
    }

    public ArrayList<ArrayList<String>> getYol_durumlari() {
        return yol_durumlari;
    }

    public void setYol_durumlari(ArrayList<ArrayList<String>> yol_durumlari) {
        this.yol_durumlari = yol_durumlari;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_global_fragment, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            android.support.v4.app.FragmentManager fm = getFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();

        }
        mapFragment.getMapAsync(this);
        return v;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("yol durumlari",(yol_durumlari == null)+"");
        Log.i("yollar",(yollar == null)+"");
        if(harita != null && yol_durumlari != null && yollar != null)
        {
            harita.ciz(yollar,yol_durumlari);
            Log.i("Harita çiz çağrıldı","Çağrıldı");
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);

            return;
        }
        mMap.setMyLocationEnabled(true);
        harita.harbiden_ciz();





    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void cizdir(ArrayList<ArrayList<LatLng>> yollar, ArrayList<ArrayList<String>> yol_durumlari)
    {
        if(mMap!=null)
            mMap.clear();

       for(int j=0;j<yollar.size();j++) {
           ArrayList<LatLng> konumlar=yollar.get(j);
           ArrayList<String> durumlar=yol_durumlari.get(j);
           MarkerOptions markerOptions=new MarkerOptions();
           markerOptions.position(konumlar.get(0));
           markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
           mMap.addMarker(markerOptions);
           MarkerOptions markerOptions2=new MarkerOptions();
           markerOptions2.position(konumlar.get(konumlar.size()-1));
           markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
           mMap.addMarker(markerOptions2);
           PolylineOptions polylineOptions=null;
           ArrayList<PolylineOptions> pointOptionslar=new ArrayList<>();

           for(int i=1;i<=konumlar.size();i++)
           {
               if(i%2 == 0)
               {
                   polylineOptions=new PolylineOptions();
                   polylineOptions.add(konumlar.get(i-2),konumlar.get(i-1));

                   polylineOptions.width(15);
                   if(durumlar.get((i/2)-1).equals("iyi"))
                       polylineOptions.color(Color.GREEN);
                   else
                       polylineOptions.color(Color.RED);
                   pointOptionslar.add(polylineOptions);

               }
           }
           for(int i=0;i<pointOptionslar.size();i++)
           {

               mMap.addPolyline(pointOptionslar.get(i));
           }
       }






    }
    public void zoom(LatLng konum)
    {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(konum,15),3000,null);
    }
    public void clearMap()
    {
        mMap.clear();
    }

}



