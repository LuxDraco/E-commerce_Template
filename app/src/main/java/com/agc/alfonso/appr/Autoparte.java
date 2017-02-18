package com.agc.alfonso.appr;

import android.net.Uri;

import java.net.URI;

/**
 * Created by Alfonso on 18/10/2016.
 */

public class Autoparte implements Comparable<Autoparte>
{
    private double precio;
    private int year;
    private String marca;
    private String modelo;
    private String motor;
    private String nombre;
    private int cantidad;
    private String descripcion;
    private int idImage;
    private String nombre_vendedor;
    private String direccion_vendedor;
    private double rating;
    private int visitas;
    private String ID;
    private String path;

    public Autoparte(){

    }
/*
    public Autoparte(double precio, int year, String marca, String modelo, String nombre, String motor, int cantidad, String descripcion, int idImage, String nombre_vendedor, String direccion_vendedor, double rating, int visitas)
    {
        this.precio = precio;
        this.year = year;
        this.marca = marca;
        this.modelo = modelo;
        this.nombre = nombre;
        this.motor = motor;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.idImage = idImage;
        this.nombre_vendedor = nombre_vendedor;
        this.direccion_vendedor = direccion_vendedor;
        this.rating = rating;
        this.visitas = visitas;
    }*/

    public Autoparte(double precio, int year, String marca, String modelo, String nombre, String motor, int cantidad, String descripcion, String path, String nombre_vendedor, String direccion_vendedor, double rating, int visitas)
    {
        this.precio = precio;
        this.year = year;
        this.marca = marca;
        this.modelo = modelo;
        this.nombre = nombre;
        this.motor = motor;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.path = path;
        this.nombre_vendedor = nombre_vendedor;
        this.direccion_vendedor = direccion_vendedor;
        this.rating = rating;
        this.visitas = visitas;
    }

    public double getPrecio(){return precio; }

    public int getYear() {
        return year;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMotor() {
        return motor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIdImage() {
        return idImage;
    }

    public String getNombre_vendedor() {
        return nombre_vendedor;
    }

    public String getDireccion_vendedor() {
        return direccion_vendedor;
    }

    public double getRating(){ return rating; }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPath() {
        return path;
    }


    @Override
    public int compareTo(Autoparte a) {
        if (visitas > a.visitas) {
            return -1;
        }
        if (visitas < a.visitas) {
            return 1;
        }
        return 0;
    }
}
