package com.example.beyzacan.prototip_beyza;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WayActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private View container;
    private BottomNavigationView navigation;
    //yolculuk değişkenleri
    ArrayList<LatLng> konumlar = new ArrayList();
    ArrayList<String> durumlar = new ArrayList();
    ArrayList<Double> ivmeler=new ArrayList<>();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private int veriSayaci=0;
    private ivmeOlcer io;
    android.support.v4.app.Fragment fragment;
    private TextView zaman,hiz;
    private  Thread thread;
    private boolean isTravelPause=false;
    Chronometer mchoronometer;
    private FirebaseModule firebaseModule;
    private String m_text;
    private WayActivity MyClass;
    private  Ways konums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_way);
        navigation = (BottomNavigationView)findViewById(R.id.navigation_menu);
        navigation.setOnNavigationItemSelectedListener(this);
        container=findViewById(R.id.main_view);
        FragmentManager manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.fr_1);
        zaman=(TextView)findViewById(R.id.way_TxtTimer);
        hiz=(TextView)findViewById(R.id.wayTxt_speed);
        firebaseModule=new FirebaseModule();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        MyClass=this;


    }

    private void sonlandir() {
        Snackbar.make(container,"İşlem sonlandırıldı.",Snackbar.LENGTH_SHORT).show();
        //((BlankFragment) getSupportFragmentManager().findFragmentById(id.fr_1)).cizdir();
        if(io != null)
            io.sonlandir();
        if(mchoronometer != null)
            mchoronometer.stop();

        inputDialog();


        ((BlankFragment) getSupportFragmentManager().findFragmentById(R.id.fr_1)).clearMap();
        thread.interrupt();
        thread=null;



    }

    private void duraklat() {
        Snackbar.make(container,"İşlem duraklatıldı.",Snackbar.LENGTH_SHORT).show();
        isTravelPause=true;
        mchoronometer.setTravelPause(true);
    }

    private void baslat() {
        isTravelPause=false;

        Snackbar.make(container,"İşlem başlatıldı.",Snackbar.LENGTH_SHORT).show();
        io=new ivmeOlcer(this);
        if(ivmeler.size() == 0)
            getLastLocation(true);
        else
            getLastLocation(false);

        //Timer başlatıldı
        mchoronometer=new Chronometer(this);
        mchoronometer.setTravelPause(false);
        thread = new Thread(mchoronometer);
        thread.start();
        mchoronometer.start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId) {
            case R.id.baslat: {
                baslat();
                break;
            }
            case R.id.duraklat: {
                duraklat();
                break;
            }

            case R.id.sonlandir: {
                sonlandir();
                break;
            }


        }

        return true;
    }

    public void sensorVerisiGeldi(double z) {
        //Log.i("SAYAC",veriSayaci+" ");
        if(!isTravelPause)
        {
            if(veriSayaci <= 300 )
            {
                ivmeler.add(z);
                veriSayaci++;
            }
            else
            {
                veriSayaci=0;
                getLastLocation(false);
            }
        }

    }

    public void updateTimerText(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                zaman.setText(time);
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // getLastLocation();
            } else {

            }
        }
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(WayActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);


    }
    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            startLocationPermissionRequest();
        } else {
            startLocationPermissionRequest();
        }
    }
    private void getLastLocation(final boolean ilkCagiris) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLastLocation = task.getResult();
                    LatLng location=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    konumlar.add(location);
                    if(!ilkCagiris)
                        hesapla();
                    else
                        ((BlankFragment) getSupportFragmentManager().findFragmentById(R.id.fr_1)).zoom(location);

                }
            }

        });


    }
    //km hesapla
    public void CalculationByDistance(){
        /*PRE: All the input values are in radians!*/
        if(!isTravelPause)
        {
            int i=konumlar.size()-2;
            Double finalLat = konumlar.get(i+1 ).latitude;
            Double finalLong = konumlar.get(i + 1).longitude;
            Double initialLat = konumlar.get(i).latitude;
            Double initialLong = konumlar.get(i).longitude;
            double latDiff = finalLat - initialLat;
            double longDiff = finalLong - initialLong;
            double earthRadius = 6371; //In Km if you want the distance in km

            double distance = 2 * earthRadius * Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff / 2.0), 2) + Math.cos(initialLat) * Math.cos(finalLat) * Math.pow(Math.sin(longDiff / 2), 2)));

            String sure= zaman.getText().toString();
            String[] sureler=sure.split(":");
            double saat=0;
            int sayac=0;
            for(String s:sureler)
            {
                String gecici= (s.charAt(0) == 0) ? String.valueOf(s.charAt(1)) :s;
                if(sayac == 0)
                    saat+=Double.parseDouble(gecici);
                else if(sayac == 1)
                    saat+=(Double.parseDouble(gecici)/60);
                else
                    saat+=(Double.parseDouble(gecici)/60/60);
                sayac++;


            }

            double gelen=(distance/saat);
            hiz.setText(String.format("%.2f",gelen));
        }
        }
    public void hesapla()
    {
        CalculationByDistance();
        konumlar.add(konumlar.get(konumlar.size()-1));
        //çıkarım
        double ortalama=ortalamaHesapla();
        double standartSapma=standartSapmaHesapla(ortalama);
        if(standartSapma > (ortalama /2 ) )
            durumlar.add("kötü");
        else
            durumlar.add("iyi");


        Log.i("Hesapla fonksiyonu içi",durumlar.size()+"");
        ivmeler.clear();
        //burada çizilecek
        ((BlankFragment) getSupportFragmentManager().findFragmentById(R.id.fr_1)).cizdir(konumlar,durumlar);

    }
    public double standartSapmaHesapla(double ort) {
        double kareToplam = 0;
        for (int i = 0; i < ivmeler.size(); i++) {
            kareToplam = kareToplam + (double) ivmeler.get(i) * (double) ivmeler.get(i);            //Formül ile standart sapma bulme işlemi
        }
        return (double) Math.sqrt(kareToplam / ivmeler.size() - ort * ort);    //Sapma değeri alındı

    }
    public double ortalamaHesapla()
    {
        double kareToplam = 0;
        for (int i = 0; i < ivmeler.size(); i++) {
            kareToplam = kareToplam + (double) ivmeler.get(i) ;         //Formül ile standart sapma bulme işlemi
        }
        return (double) (kareToplam/ivmeler.size());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(io != null)
            io.sonlandir();
        if(mchoronometer != null)
            mchoronometer.stop();
        hiz.setText("Hız: 50.0");
        zaman.setText("Sure: 500.0");
        konumlar.clear();
        durumlar.clear();
        ivmeler.clear();
        if(thread != null)
        {

            thread.interrupt();
            thread=null;

        }
        ((BlankFragment) getSupportFragmentManager().findFragmentById(R.id.fr_1)).clearMap();
    }
    public void inputDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Güzergah Yazınız:");
        builder.setCancelable(false);
// I'm using fragment here so I'm using getView() to provide ViewGroup
// but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_guzergah, (ViewGroup)((View)findViewById(R.id.main_view)) , false);
// Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

// Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                m_text = input.getText().toString();
                //veritabanına ekleme
                String konfor_durumu="";
                float iyi=0;
                float kotu=0;
                for(int i=0;i<durumlar.size();i++)
                {
                    if(durumlar.get(i).equals("iyi"))
                        iyi++;
                    else
                        kotu++;
                }
                Log.i("iyi",iyi+"");
                Log.i("kotu",kotu+"");
                float oran=((iyi/(iyi+kotu))*100);
                Log.i("ORAN",oran+"");
                konfor_durumu="%"+String.valueOf(oran);
                Log.i("Sınıfa eklenmeden önce",durumlar.size()+"");
                konums=new Ways(
                        hiz.getText().toString(),
                        zaman.getText().toString(),
                        m_text,
                        conver_latlng_to_string(),
                        durumlar,
                        konfor_durumu
                );

                firebaseModule.yol_adeti_kontrol(MyClass);
                hiz.setText("Hız: 50.0");
                zaman.setText("Sure: 500.0");
                konumlar.clear();
                ivmeler.clear();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                hiz.setText("Hız: 50.0");
                zaman.setText("Sure: 500.0");
                konumlar.clear();
                durumlar.clear();
                ivmeler.clear();
            }
        });

        builder.show();
    }
    public ArrayList<ArrayList<Double>> conver_latlng_to_string()
    {
        ArrayList<ArrayList<Double>> returner=new ArrayList<>();
        for(int i=0;i<konumlar.size();i++)
        {
            ArrayList<Double> temp=new ArrayList<>();
            Double lat=konumlar.get(i).latitude;
            Double lon=konumlar.get(i).longitude;
            temp.add(lat);
            temp.add(lon);
            returner.add(temp);
        }
        return returner;
    }

    public void yol_guncelle_ekle(int adet) {


        Log.i("ADET",adet+"");
        if((adet)%10 == 0 && adet != 0)
        {
            Log.i("Sdet kontrol","10un katı");
            firebaseModule.yol_ekle(konums,true);
            durumlar.clear();


        }
        else
        {


            firebaseModule.yol_ekle(konums,false);
            durumlar.clear();


        }
    }
}