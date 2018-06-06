package com.example.delli3.proyectofinal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Articulos_vendidos extends AppCompatActivity implements AdaptadorProductos.OnProductosClickListener {

    private RecyclerView lstventas;
    private ArrayList<Productos> ventas;
    private AdaptadorProductos adapter;
    private LinearLayoutManager llm;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String bd = "productos";
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos_vendidos);
       showToolbar(getResources().getString(R.string.ventas),true);


        lstventas = findViewById(R.id.lstArticulos_vendidos);
        ventas = new ArrayList<>();

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdaptadorProductos(this,ventas,this);

        lstventas.setLayoutManager(llm);
        lstventas.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();

        user= mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(bd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ventas.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Productos p = snapshot.getValue(Productos.class);
                        if((user.getUid().equals(p.provedor)) && !p.idCliente.equals("")) {
                            p.id = snapshot.getKey();
                            ventas.add(p);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override

            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onProductoClick(Productos e) {

    }
}
