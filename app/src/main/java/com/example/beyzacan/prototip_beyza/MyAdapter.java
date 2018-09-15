package com.example.beyzacan.prototip_beyza;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fince on 15.04.2018.
 */

public class MyAdapter extends ArrayAdapter<Ways> {

    Context my_context;
    int resource;
    List<Ways> list;
    FirebaseModule firebaseModule;
    ImageView favoriekle;
    private MyAdapter MyClass;
    private Boolean Fav=false;
    private ArrayList<View> view_lists=new ArrayList<>();


    public MyAdapter(@NonNull Context context, int resource, @NonNull List<Ways> objects) {
        super(context, resource, objects);
        this.my_context=context;
        this.resource=resource;
        this.list=objects;
        firebaseModule=new FirebaseModule();
        MyClass=this;
        view_lists.clear();

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(my_context);
        View view=inflater.inflate(resource,null);
        TextView textLocation=view.findViewById(R.id.textView3);
        TextView textStatus=view.findViewById(R.id.textView4);
        ImageView img=view.findViewById(R.id.imageView2);
        ImageView imgSend=view.findViewById(R.id.goster);
        favoriekle=view.findViewById(R.id.favoriekle);
        final Ways konumlar=list.get(position);
        if(konumlar != null)
        {
            textLocation.setText(konumlar.getGuzergah());
            if(konumlar.getKonfor_durumu() != null)
            {
                String[] dizi=konumlar.getKonfor_durumu().split("\\.");
                textStatus.setText("Konfor durumu: "+dizi[0]);
            }

        }


        //textStatus.setText(konumlar.getDurum());
        img.setImageDrawable(my_context.getResources().getDrawable(R.drawable.ic_yollarim));

        favoriekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ways way=list.get(position);
                if(Fav==false)
                {

                    firebaseModule.favori_ekle(way,position,MyClass);
                }
                else
                {
                    firebaseModule.favori_sil(way,position,MyClass);
                }




            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(my_context,list.get(position).getGuzergah(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),Harita.class);
                Ways way=list.get(position);
                intent.putExtra("durumlar",way.getDurumlar());
                intent.putExtra("hiz",way.getHiz());
                intent.putExtra("zaman",way.getSure());
                intent.putExtra("guzergah",way.getGuzergah());
                Log.i("Adapter",way.getDurumlar().size()+"");
                double[] konumlar=new double[(way.getKonumlar().size()*2)+1];
                int sayac=0;
                for(int i=0;i<(way.getKonumlar().size()*2);i++)
                {
                    konumlar[i]=way.getKonumlar().get(sayac).get((i%2));
                    if((i+1)%2 == 0)
                        sayac++;

                }
                intent.putExtra("konumlar",konumlar);
                getContext().startActivity(intent);


            }
        });
        Ways way=list.get(position);
        if(way.isFav)
            favoriekle.setImageResource(R.drawable.ic_favorite);

        view_lists.add(view);
        return view;

    }
    public void setFavFalse(int position)
    {
        Ways way=list.get(position);
        favoriekle=view_lists.get(position).findViewById(R.id.favoriekle);
        Fav=false;
        favoriekle.setImageResource(R.drawable.ic_unfav);
    }
    public void setFavTrue(int position)
    {
        Ways way=list.get(position);
        favoriekle=view_lists.get(position).findViewById(R.id.favoriekle);
        Fav=true;
        favoriekle.setImageResource(R.drawable.ic_favorite);
    }


}
