package com.example.delli3.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class registar_productos extends AppCompatActivity {
    private EditText titulo,precio,referencia;
    private Spinner proovedor;
    private Button registrar;
    private ArrayList<String> usuarios;
    private ArrayAdapter<String> adapter;

    private DatabaseReference databaseReference;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_productos);
       showToolbar(getResources().getString(R.string.registrar_productos),true);

       titulo = findViewById(R.id.Nombre_Producto);
       precio = findViewById(R.id.Precio_Producto);
       referencia = findViewById(R.id.Referencia_Producto_registrar);

       registrar = findViewById(R.id.btnRegistrar_producto);
       usuarios=new ArrayList<String>();
       databaseReference = FirebaseDatabase.getInstance().getReference();
       databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    Usuario u = snapshot.getValue(Usuario.class);

                    if(u.rol==1){
                        u.id = snapshot.getKey();
                        usuarios.add(u.nombre);
                    }

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mAuth = FirebaseAuth.getInstance();

        user= mAuth.getCurrentUser();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Productos p = new Productos(titulo.getText().toString().trim(),precio.getText().toString().trim(),"","",
                    user.getUid(),referencia.getText().toString().trim());
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference();
                String key = myRef.child("productos").push().getKey();

                myRef.child("productos").push().setValue(p);
                Intent intent = new Intent(registar_productos.this,MainActivity.class);
                startActivity(intent);
                registar_productos.this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                Toast.makeText(registar_productos.this, R.string.Producto_creado, Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
