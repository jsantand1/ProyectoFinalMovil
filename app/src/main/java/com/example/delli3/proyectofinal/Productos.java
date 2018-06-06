package com.example.delli3.proyectofinal;

public class Productos {

    public String id;
    public String titulo;
    public String precio;
    public String foto;
    public String idCliente;
    public String provedor;
    public String referencia;

    public Productos(){}

    public Productos( String titulo, String precio, String foto, String idCliente,String provedor,String referencia){
        this.titulo = titulo;
        this.precio = precio;
        this.foto = foto;
        this.idCliente=idCliente;
        this.provedor=provedor;
        this.referencia=referencia;
    }

}
