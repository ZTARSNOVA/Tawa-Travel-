package com.viajes.modelo;

import java.util.ArrayList;
import java.util.List;

public class Escuela {
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private List<Estudiante> estudiantes;
    
    public Escuela() {
        this.estudiantes = new ArrayList<>();
    }
    
    public Escuela(int id, String nombre, String direccion, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.estudiantes = new ArrayList<>();
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }
    
    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }
    
    public void agregarEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}