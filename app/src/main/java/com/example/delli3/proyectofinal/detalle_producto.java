package com.example.delli3.proyectofinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detalle_producto extends AppCompatActivity {
    private TextView nombre,precio,referencia,proovedor;
    private ImageView foto;
    private FloatingActionButton enviar;
    private static String db = "productos";
    private String id,idusuario;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        showToolbar(getResources().getString(R.string.detalle_producto), true);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user=mAuth.getCurrentUser();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        id = b.getString("id");
        idusuario=b.getString("idCliente");
        nombre = findViewById(R.id.tvDtNombre_producto);
        precio = findViewById(R.id.tvDtprecio_producto);
        referencia = findViewById(R.id.tvDtreferencia_producto);
        proovedor = findViewById(R.id.tvDtproovedor_producto);
        enviar = findViewById(R.id.fabAgregar);


        nombre.setText(b.getString("titulo"));
        precio.setText(b.getString("precio"));
        referencia.setText(b.getString("referencia"));
        proovedor.setText(b.getString("proovedor"));

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(detalle_producto.this);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setMessage(getResources().getString(R.string.enviar_producto));

                builder.setPositiveButton(getResources().getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        databaseReference.child("productos/"+id).child("idCliente").setValue(user.getUid());
                        Toast.makeText(detalle_producto.this, R.string.confirmacion_envio,Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(detalle_producto.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.text_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

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
