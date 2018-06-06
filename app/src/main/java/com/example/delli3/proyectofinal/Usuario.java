package com.example.delli3.proyectofinal;

public class Usuario {
    public String nombre,apellido,cel,dir,correo,numeroDocumento, foto,id;
    public Integer tipoDoc,rol;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String cel, String dir, String correo, String numeroDocumento, Integer tipoDoc,
                   String foto, Integer rol) {

        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.cel = cel.trim();
        this.dir = dir.trim();
        this.correo = correo.trim();
        this.numeroDocumento = numeroDocumento.trim();
        this.tipoDoc = tipoDoc;
        this.foto = foto;
        this.rol=rol;
    }
}
