package com.example.delli3.proyectofinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private Usuario usuario;
    private EditText nombre,apellido,cel,dir,correo,password,confpassword,numeroDocumento;
    private Spinner tipoDoc,rol;
    private CheckBox terminos;
    private Button registrar;
    private String elementos [];
    private ArrayAdapter<String> adapter;
    private FirebaseAuth mAuth;

    private String TAG = "FIREBASE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        mAuth = FirebaseAuth.getInstance();
        showToolbar(getResources().getString(R.string.registrar),true);
        nombre=findViewById(R.id.lblNombre);
        apellido=findViewById(R.id.lblApellido);
        cel=findViewById(R.id.lblCelular);
        dir=findViewById(R.id.lblDireccion);
        correo=findViewById(R.id.lblCorreo);
        password=findViewById(R.id.lblpassword);
        confpassword=findViewById(R.id.lblConfpassword);
        numeroDocumento=findViewById(R.id.lblNumeroDocumento);
        tipoDoc=findViewById(R.id.spnTipoDoc);
        rol= findViewById(R.id.spnRol);
        terminos = findViewById(R.id.check);
        registrar=findViewById(R.id.btnRegistrar);

        elementos=this.getResources().getStringArray(R.array.ListaTipoDoc);
        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,elementos);
        tipoDoc.setAdapter(adapter);
        elementos=this.getResources().getStringArray(R.array.roles);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,elementos);
        rol.setAdapter(adapter);

        if(rol.getSelectedItemPosition()==2){
            apellido.setVisibility(View.INVISIBLE);
        }


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario= new Usuario(nombre.getText().toString(),apellido.getText().toString(),cel.getText().toString(),
                        dir.getText().toString(),correo.getText().toString(),numeroDocumento.getText().toString(),
                        tipoDoc.getSelectedItemPosition(),"",rol.getSelectedItemPosition());
                int sw = 0;
                if(!correoValido(usuario.correo)){
                    sw = 1;
                    Toast.makeText(RegistrarActivity.this, getResources().getString(R.string.email_incorrecto),Toast.LENGTH_LONG).show();
                }
                if(sw == 0 && !isValidLength(usuario.nombre,3)){
                    sw = 1;
                    Toast.makeText(RegistrarActivity.this, R.string.Mensaje_longitd_nombre,Toast.LENGTH_LONG).show();
                }
                if(sw == 0 && !isValidLength(usuario.apellido,3)){
                    sw = 1;
                    Toast.makeText(RegistrarActivity.this, R.string.Mensaje_longitud_apellido,Toast.LENGTH_LONG).show();
                }
                if(sw == 0 && !isValidLength(usuario.cel,10)){
                    sw = 1;
                    Toast.makeText(RegistrarActivity.this, R.string.Mensaje_longitud_celular,Toast.LENGTH_LONG).show();
                }
                if(sw == 0 && !isValidLength(usuario.numeroDocumento,5)){
                    sw = 1;
                    Toast.makeText(RegistrarActivity.this, R.string.Mensaje_longitud_numeroDocumento,Toast.LENGTH_LONG).show();
                }

                if(sw == 0 && !isValidLength(password.getText().toString().trim(),6)){
                    sw = 1;
                    Toast.makeText(RegistrarActivity.this, R.string.Mensaje_longitud_password,Toast.LENGTH_LONG).show();
                }
                if(sw == 0 && !password.getText().toString().trim().equals(confpassword.getText().toString().trim())){
                    sw = 1;
                    Toast.makeText(RegistrarActivity.this, R.string.Mensaje_Password,Toast.LENGTH_LONG).show();
                }
                if(!terminos.isChecked()){
                            sw = 1;
                            Toast.makeText(RegistrarActivity.this, R.string.terminosCond,Toast.LENGTH_LONG).show();
                }

                if(sw == 0){
                    mAuth.createUserWithEmailAndPassword(usuario.correo,password.getText().toString().trim()).addOnCompleteListener(RegistrarActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();
                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference();
                                myRef.child("usuarios/"+uid).setValue(usuario);

                                //myRef.child("usuarios/"+uid).push().setValue(usuario);
                                Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegistrarActivity.this, R.string.Usuario_creado, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(RegistrarActivity.this, R.string.Error_creacion, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    //database = FirebaseDatabase.getInstance();
                    //myRef = database.getReference();
                    //String key = myRef.child("usuarios").push().getKey();
                }
             }
        });
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
    public boolean isValidLength(String campo, int longMin){
        if(campo.length()<longMin){
            return false;
        }else {
            return true;
        }

    }
    private boolean correoValido(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^.+@.+\\..+$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

