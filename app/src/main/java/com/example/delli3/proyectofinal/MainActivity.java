package com.example.delli3.proyectofinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdaptadorProductos.OnProductosClickListener{

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private RecyclerView lstProductos;
    private ArrayList<Productos> productos;
    private AdaptadorProductos adapter;
    private LinearLayoutManager llm;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private String bd = "productos";
    private String bdu = "usuarios";
    private Integer rol;

    private String nombre;
    private String correo;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstProductos = findViewById(R.id.lstProductos);
        productos = new ArrayList<>();

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdaptadorProductos(this,productos,this);

        lstProductos.setLayoutManager(llm);
        lstProductos.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();

        user= mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(bd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Productos p = snapshot.getValue(Productos.class);
                        p.id = snapshot.getKey();
                        productos.add(p);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //final TextView correo,nombre;
        //correo=findViewById(R.id.ttvCorreo);
        //nombre=findViewById(R.id.ttvUsuario);

        databaseReference.child(bdu+"/"+user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Usuario u = dataSnapshot.getValue(Usuario.class);
                        //if(u.id.equals(user.getUid()) ){
                    //correo.setText(u.correo);
                    //nombre.setText(u.nombre+" "+u.apellido);
                    nombre = u.nombre+" "+u.apellido;
                    correo = u.correo;
                    rol=u.rol;
                    showToolbar(getResources().getString(R.string.title_main).toString(),true);

                       // }

                   // }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);



        drawerLayout = findViewById(R.id.drawer_layout);

        navView = findViewById(R.id.navview);

        View headerView = navView.getHeaderView(0);
        TextView ttvNombre = headerView.findViewById(R.id.ttvUsuario);
        TextView ttvCorreo = headerView.findViewById(R.id.ttvCorreo);

        ttvNombre.setText(nombre);
        ttvCorreo.setText(correo);

        if(rol==0){

            navView.getMenu().findItem(R.id.menu_repuestos).setVisible(false);
            navView.getMenu().findItem(R.id.menu_ventas).setVisible(false);

        }

        if(rol==1){

            navView.getMenu().findItem(R.id.menu_pedidos).setVisible(false);
            }


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_profile:
                       Intent intent  = new Intent(MainActivity.this, ProfileActivity.class);
                       startActivity(intent);
                        MainActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        break;
                    case R.id.menu_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(getResources().getString(R.string.app_name));
                        builder.setMessage(getResources().getString(R.string.message_logout));

                        builder.setPositiveButton(getResources().getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        builder.setNegativeButton(getResources().getString(R.string.text_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    case R.id.menu_pedidos:
                        Intent i  = new Intent(MainActivity.this, MisPedidos.class);
                        startActivity(i);
                        MainActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;

                    case R.id.menu_repuestos:
                        Intent b  = new Intent(MainActivity.this, Repuestos.class);
                        startActivity(b);
                        MainActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    case R.id.menu_ventas:
                        Intent a  = new Intent(MainActivity.this, Articulos_vendidos.class);
                        startActivity(a);
                        MainActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductoClick(Productos p) {
        Intent intent = new Intent(MainActivity.this,detalle_producto.class);
        Bundle b = new Bundle();
        b.putString("titulo",p.titulo);
        b.putString("precio",p.precio);
        b.putString("referencia",p.referencia);
        b.putString("proovedor",p.provedor);
        b.putString("id",p.id);
        b.putString("idCliente",p.idCliente);
        intent.putExtras(b);
        startActivity(intent);
        MainActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}
