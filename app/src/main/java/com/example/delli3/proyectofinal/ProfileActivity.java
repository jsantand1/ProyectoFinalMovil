package com.example.delli3.proyectofinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    private TextView nombre,apellido,direccion,correo,numeroDocumento,tipoDocumento,celular;
    private ImageView foto;
    private FloatingActionButton camera;
    private DatabaseReference databaseReference;
    private static String dbb = "usuarios";
    private String idb;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        showToolbar(getResources().getString(R.string.perfil),true);


        nombre = findViewById(R.id.tvDtNombre);
        apellido = findViewById(R.id.tvDtApellido);
        direccion = findViewById(R.id.tvDtDireccion);
        correo = findViewById(R.id.tvDtCorreo);
        numeroDocumento=findViewById(R.id.tvDtNumeroDocumento);
        tipoDocumento=findViewById(R.id.tvDtTipoDocumento);
        celular = findViewById(R.id.tvDtCelular);
        camera = findViewById(R.id.fabCamera);
        foto=findViewById(R.id.img_perfil);

        final String [] elementos = this.getResources().getStringArray(R.array.ListaTipoDoc);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user=mAuth.getCurrentUser();
        databaseReference.child(dbb+"/"+user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                nombre.setText(u.nombre);
                apellido.setText(u.apellido);
                direccion.setText(u.dir);
                correo.setText(u.correo);
                numeroDocumento.setText(u.numeroDocumento);
                tipoDocumento.setText(elementos[u.tipoDoc]);
                celular.setText(u.cel);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            final CharSequence[] opciones = {"Tomar foto","Elegir foto","Cancelar"};
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle(getResources().getString(R.string.elegirfoto));
                    builder.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int op) {
                            switch(op){
                                case 0:
                                    //opencamera();
                                case 1:
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("images/*");
                                    // startActivityForResult(Intent.createChooser());
                            }
                        }
                    });
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

