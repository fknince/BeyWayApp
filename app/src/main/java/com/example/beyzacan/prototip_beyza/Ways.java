package com.example.beyzacan.prototip_beyza;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class  Ways {
    String hiz,sure,guzergah;
    ArrayList<ArrayList<Double>> konumlar;
    ArrayList<String> durumlar;
    String key;
    String user_key;
    String konfor_durumu;

    public String getKonfor_durumu() {
        return konfor_durumu;
    }

    public void setKonfor_durumu(String konfor_durumu) {
        this.konfor_durumu = konfor_durumu;
    }

    boolean isFav;

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHiz() {
        return hiz;
    }

    public String getSure() {
        return sure;
    }

    public String getGuzergah() {
        return guzergah;
    }

    public ArrayList<ArrayList<Double>> getKonumlar() {
        return konumlar;
    }

    public ArrayList<String> getDurumlar() {
        return durumlar;
    }
    public Ways() {
    }

    public Ways(String hiz, String sure, String guzergah, ArrayList<ArrayList<Double>> konumlar, ArrayList<String> durumlar, String konfor_durumu) {
        this.hiz = hiz;
        this.sure = sure;
        this.guzergah = guzergah;
        this.konumlar = konumlar;
        this.durumlar = durumlar;
        this.konfor_durumu = konfor_durumu;
    }
}
