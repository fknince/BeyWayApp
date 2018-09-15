package com.example.beyzacan.prototip_beyza;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseModule {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<Ways> list;
    private SearchActivity searchActivity;
    private FirebaseAuth mAuth;
    private long counter=1;
    private  int sayac_durum;
    private  int adet=0;
    HashMap< ArrayList<Double>,Integer[]> noktalar=new HashMap<>();
    ArrayList<String> durumlar = new ArrayList();
    public FirebaseModule()
    {
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Yol").child("Yollar");
        mAuth = FirebaseAuth.getInstance();
        
        list=new ArrayList<>();


    }




    public void yol_ekle(Ways way, final Boolean kontrol_guncelleme)
    {
        Log.i("yol_ekle kısmı",way.getDurumlar().size()+"");
        ref=FirebaseDatabase.getInstance().getReference("Tum_Yollar");
        String guzergah=way.getGuzergah();
        String hiz=way.getHiz();
        String sure=way.getSure();
        final ArrayList<ArrayList<Double>> konumlar=way.getKonumlar();
        String yol_key="key";
        HashMap<String,Object> gecici=new HashMap<String,Object>();
        gecici.put("guzergah",guzergah);
        gecici.put("hiz",hiz);
        gecici.put("sure",sure);
        gecici.put("yol_key",yol_key);
        gecici.put("durumlar",way.getDurumlar());
        gecici.put("konumlar",konumlar);
        gecici.put("konfor_durumu",way.getKonfor_durumu());
        ref=ref.child(getCurrentUser().getUid());
        ref.push().setValue(gecici).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    if(kontrol_guncelleme)
                    {
                        yol_guncelleme();
                    }
                }
            }
        });


    }

    private void yol_guncelleme() {
        adet=0;
        sayac_durum=0;
        noktalar.clear();
        counter=0;
        ref=FirebaseDatabase.getInstance().getReference("Tum_Yollar");
        ref.addValueEventListener(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data_uid:dataSnapshot.getChildren())
                {
                    counter++;
                    for(DataSnapshot data_yolid:data_uid.getChildren())
                    {
                        ArrayList<ArrayList<Double>> list_konumlar= (ArrayList<ArrayList<Double>>) data_yolid.child("konumlar").getValue();
                        ArrayList<String> durumlar=(ArrayList<String>)data_yolid.child("durumlar").getValue();
                        adet=0;
                        sayac_durum=0;
                        for(ArrayList<Double> konum:list_konumlar)
                        {

                            //dizi[i,k]
                            if(!noktalar.containsKey(konum))
                            {



                                    Integer dizi[]=new Integer[2];
                                    if(durumlar.get(sayac_durum).equals("iyi"))
                                    {
                                        dizi[0]=1;
                                        dizi[1]=0;
                                    }

                                    else
                                    {
                                        dizi[1]=1;
                                        dizi[0]=0;
                                    }
                                    noktalar.put(konum,dizi);
                                    adet++;
                                    if(adet%2 == 0)
                                        sayac_durum++;







                                }


                            else
                            {


                                if(sayac_durum >= durumlar.size())
                                {
                                    sayac_durum=durumlar.size()-1;
                                    Integer dizi[]=new Integer[2];
                                    if(durumlar.get(sayac_durum).equals("iyi"))
                                    {
                                        dizi[0]=1;
                                        dizi[1]=0;
                                        noktalar.replace(konum,dizi);
                                    }

                                    else
                                    {
                                        dizi[1]=1;
                                        dizi[0]=0;
                                        noktalar.replace(konum,dizi);
                                    }
                                }
                                else
                                {
                                    Integer dizi[]=new Integer[2];
                                    if(durumlar.get(sayac_durum).equals("iyi"))
                                    {
                                        dizi[0]=noktalar.get(konum)[0]+1;
                                        dizi[1]=noktalar.get(konum)[1];
                                        noktalar.replace(konum,dizi);
                                    }

                                    else
                                    {
                                        dizi[1]=noktalar.get(konum)[1]+1;
                                        dizi[0]=noktalar.get(konum)[0];
                                        noktalar.replace(konum,dizi);
                                    }
                                }
                                adet++;
                                if(adet%2 == 0)
                                    sayac_durum++;


                            }




                        }


                    }
                    if(counter == dataSnapshot.getChildrenCount())
                    {
                        Log.i("verileri anlamdır","çağrıldı");
                        verileri_anlamlandir();
                        return;
                    }
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void verileri_anlamlandir() {

       sayac_durum=0;
        final ArrayList<String> guncellenecek_yollar=new ArrayList<String>();
        final ArrayList<ArrayList<String>> guncellenecek_durumlar=new ArrayList<>();
       ref=FirebaseDatabase.getInstance().getReference("Tum_Yollar");
       ref.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot data_user:dataSnapshot.getChildren())
               {
                   sayac_durum++;
                   for(DataSnapshot data_way:data_user.getChildren())
                   {
                       ArrayList<ArrayList<Double>> konumlar=(ArrayList<ArrayList<Double>>) data_way.child("konumlar").getValue();
                       ArrayList<String> durumlar=(ArrayList<String>)data_way.child("durumlar").getValue();
                       int sayac=0;
                       int durum_sayac=0;
                       boolean degisiklik_oldu=false;
                       for(ArrayList<Double> konum:konumlar)
                       {

                           Double lan=konum.get(0);
                           Double lon=konum.get(1);
                           for(Map.Entry<ArrayList<Double>, Integer[]> entry : noktalar.entrySet()) {
                               ArrayList<Double> key = entry.getKey();
                               Integer[] value = entry.getValue();
                               if (lan == key.get(0) && lon == key.get(1) && (value[0] + value[1]) > 1) {
                                   String durum="iyi";
                                   durum=value[0] > value[1] ? "iyi" : "kötü";
                                   if(durumlar.get(durum_sayac) != durum)
                                   {

                                       durumlar.set(durum_sayac,durum);
                                       degisiklik_oldu=true;
                                   }

                               }
                           }
                           sayac++;
                           if(sayac % 2 == 0)
                               durum_sayac++;



                       }
                       if(degisiklik_oldu)
                       {
                           guncellenecek_yollar.add(data_user.getKey()+"/"+data_way.getKey());
                           guncellenecek_durumlar.add(durumlar);
                       }



                   }

                   if(sayac_durum == dataSnapshot.getChildrenCount() && guncellenecek_durumlar.size() > 0)
                   {



                           nokta_degistir(guncellenecek_durumlar,guncellenecek_yollar);
                           return;

                   }
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });



    }

    private void nokta_degistir(ArrayList<ArrayList<String>> guncellenecek_durumlar, ArrayList<String> guncellenecek_yollar) {
        DatabaseReference yollar_ref=FirebaseDatabase.getInstance().getReference("Tum_Yollar");
        for(int i=0;i<guncellenecek_durumlar.size();i++)
        {

            String[] array=guncellenecek_yollar.get(i).split("/");
                    String key=array[0];
                    String key1=array[1];
                yollar_ref.child(key).child(key1).child("durumlar").setValue(guncellenecek_durumlar.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("BİLGİ","veriler güncellendi");
                    }
                });
        }

    }


    public FirebaseUser getCurrentUser()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        return currentUser;
    }
    public void logOut() {
        mAuth.signOut();
    }

    public void logIn(String email, String password, final LoginActivity myClass) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    if(myClass.getmLoginProgress().isShowing())
                        myClass.getmLoginProgress().dismiss();

                    myClass.gecisYap();
                }
                else
                {
                    if(myClass.getmLoginProgress()!=null)
                        myClass.getmLoginProgress().hide();
                    Snackbar.make(myClass.getLogin_activity(),"Hesabınıza giriş yaparken bir hata ile karşılaşıldı.",Snackbar.LENGTH_SHORT).show();
                    Log.d("HATA",task.getException().getMessage());
                }



            }
        });
    }


    public void createNewUser(String email, String password, final RegisterActivity registerActivity) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    registerActivity.gecisYap();
                }
                else
                {
                    if(registerActivity.getmRegProgress().isShowing())
                        registerActivity.getmRegProgress().dismiss();
                    Log.i("HATA",task.getException().toString());
                    Snackbar.make(registerActivity.getRegister_activity(),"Hesap oluşturulurken bir hata ile karşılaşıldı.",Snackbar.LENGTH_SHORT).show();

                }
            }
        });

    }
    public void push_name(String uid, String name) {
        ref = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid);
        ref.keepSynced(true);
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", name);

        ref.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Log.i("push user information", "başarılı");

                } else {

                }

            }
        });


    }
    public void yollari_getir(final SearchActivity searchActivity)
    {
        list.clear();
        ref=FirebaseDatabase.getInstance().getReference().child("Tum_Yollar");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               for(DataSnapshot ds:dataSnapshot.getChildren())
               {
                  for(DataSnapshot data_way:ds.getChildren())
                  {
                      final Ways way=new Ways((String)data_way.child("hiz").getValue(),
                              (String)data_way.child("sure").getValue(),
                              (String)data_way.child("guzergah").getValue(),
                              (ArrayList<ArrayList<Double>>) data_way.child("konumlar").getValue(),
                              (ArrayList<String>)data_way.child("durumlar").getValue(),
                              (String)data_way.child("konfor_durumu").getValue()





                      );
                      way.setKey(data_way.getKey());


                      list.add(way);
                      if(list.size() == ds.getChildrenCount())
                          kontrol_favori(searchActivity);

                  }
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void kontrol_favori(final SearchActivity searchActivity) {
        DatabaseReference ref_favori_yollar=FirebaseDatabase.getInstance().getReference("Kullanicilar")
                .child(getCurrentUser().getUid()).child("Favori_Yollar");

        counter=1;

        ref_favori_yollar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() > 0)
            {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String key=ds.getKey();
                    int sonuc=indexBul(key);
                    if(sonuc != -1)
                        list.get(sonuc).setFav(true);



                    if(counter==dataSnapshot.getChildrenCount())
                        searchActivity.listViewYazdir(list);
                    else
                        counter++;

                }

            }
            else
                searchActivity.listViewYazdir(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void favori_ekle(Ways way, final int position, final MyAdapter MyClass) {
        ref=FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(getCurrentUser().getUid())
                .child("Favori_Yollar").child(way.getKey());
        HashMap<String,Object> gecici=new HashMap<String,Object>();
        gecici.put("guzergah",way.getGuzergah());
        gecici.put("hiz",way.getHiz());
        gecici.put("sure",way.getSure());
        gecici.put("durumlar",way.getDurumlar());
        gecici.put("konumlar",way.getKonumlar());
        ref.setValue(gecici).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                MyClass.setFavTrue(position);
            }
        });




    }

    public void favori_sil(Ways way, final int position, final MyAdapter MyClass) {
        ref=FirebaseDatabase.getInstance().getReference("Kullanicilar").child(getCurrentUser().getUid()).
                child("Favori_Yollar").child(way.getKey());
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                MyClass.setFavFalse(position);
            }
        });
    }
    public int indexBul(String key)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getKey().equals(key))
                return i;
        }
        return -1;
    }
    public void favorileri_getir(final FavorilerActivity favorilerActivity)
    {
        list.clear();
        ref=FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(getCurrentUser().getUid())
        .child("Favori_Yollar");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                    for(DataSnapshot data_way:dataSnapshot.getChildren())
                    {
                        final Ways way=new Ways((String)data_way.child("hiz").getValue(),
                                (String)data_way.child("sure").getValue(),
                                (String)data_way.child("guzergah").getValue(),
                                (ArrayList<ArrayList<Double>>) data_way.child("konumlar").getValue(),
                                (ArrayList<String>)data_way.child("durumlar").getValue(),
                                (String)data_way.child("konfor_durumu").getValue()





                        );
                        way.setKey(data_way.getKey());
                        way.setFav(true);
                        list.add(way);
                        if(list.size() == dataSnapshot.getChildrenCount())
                            favorilerActivity.listViewYazdir(list);

                    }
                }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void yol_adeti_kontrol(final WayActivity wayActivity)
    {
        adet=0;
        counter=1;

        ref=FirebaseDatabase.getInstance().getReference("Tum_Yollar");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.getChildrenCount()>0)
                {
                    for(DataSnapshot datasnapshot_uid:dataSnapshot.getChildren())
                    {
                        for(DataSnapshot datasnapshot_yol:datasnapshot_uid.getChildren())
                        {
                            adet++;

                        }
                        if(counter == dataSnapshot.getChildrenCount())
                        {

                            wayActivity.yol_guncelle_ekle(adet);
                            return;
                        }
                        else
                        {
                            counter++;

                        }



                    }

                }
                else
                {
                    wayActivity.yol_guncelle_ekle(adet);
                    return;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void tum_yollari_ciz(final HaritaGlobal haritaGlobal)
    {
        final ArrayList<ArrayList<ArrayList<Double>>> konumlar=new ArrayList<>();
        final ArrayList<ArrayList<String>> durumlar=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tum_Yollar");
        sayac_durum=0;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data_uid:dataSnapshot.getChildren())
                {
                    sayac_durum++;
                    for(DataSnapshot data_way:data_uid.getChildren())
                    {
                        ArrayList<ArrayList<Double>> yol_konumlar= (ArrayList<ArrayList<Double>>) data_way.child("konumlar").getValue();
                        ArrayList<String> yol_durumlar= (ArrayList<String>) data_way.child("durumlar").getValue();

                        konumlar.add(yol_konumlar);
                        durumlar.add(yol_durumlar);
                    }
                    if(sayac_durum == dataSnapshot.getChildrenCount())
                    {

                        haritaGlobal.ciz(konumlar,durumlar);
                        return;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
