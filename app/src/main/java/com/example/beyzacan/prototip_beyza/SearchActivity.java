package com.example.beyzacan.prototip_beyza;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.beyzacan.prototip_beyza.FirebaseModule;
import com.example.beyzacan.prototip_beyza.MyAdapter;
import com.example.beyzacan.prototip_beyza.R;
import com.example.beyzacan.prototip_beyza.Ways;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    MaterialSearchView searchView;
    ListView lstView;
    FirebaseModule firebaseModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Güzergah Ara");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView=(MaterialSearchView)findViewById(R.id.search_view);
        lstView=(ListView)findViewById(R.id.lstView);
        firebaseModule=new FirebaseModule();
        firebaseModule.yollari_getir(this);







    }
    public void listViewYazdir(ArrayList<Ways> list)
    {

        MyAdapter adapter=new MyAdapter(this,R.layout.listele_layout,list);
        lstView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item =menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}
