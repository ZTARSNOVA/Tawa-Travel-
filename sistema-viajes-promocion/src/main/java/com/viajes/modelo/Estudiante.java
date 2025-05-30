package com.viajes.modelo;

import java.util.HashMap;
import java.util.Map;

public class Estudiante {
    private int id;
    private String nombresCompletos;
    private String numeroDocumento;
    private String responsable;
    private String parentesco;
    private String numeroContacto;
    private int escuelaId;
    private Map<TipoDocumento, EstadoDocumento> documentos;
    
    public Estudiante() {
        this.documentos = new HashMap<>();
        inicializarDocumentos();
    }
    
    public Estudiante(int id, String nombresCompletos, String numeroDocumento, 
                     String responsable, String parentesco, String numeroContacto, int escuelaId) {
        this.id = id;
        this.nombresCompletos = nombresCompletos;
        this.numeroDocumento = numeroDocumento;
        this.responsable = responsable;
        this.parentesco = parentesco;
        this.numeroContacto = numeroContacto;
        this.escuelaId = escuelaId;
        this.documentos = new HashMap<>();
        inicializarDocumentos();
    }
    
    private void inicializarDocumentos() {
        for (TipoDocumento tipo : TipoDocumento.values()) {
            documentos.put(tipo, EstadoDocumento.PENDING);
        }
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombresCompletos() {
        return nombresCompletos;
    }
    
    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }
    
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    public String getResponsable() {
        return responsable;
    }
    
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    
    public String getParentesco() {
        return parentesco;
    }
    
    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
    
    public String getNumeroContacto() {
        return numeroContacto;
    }
    
    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }
    
    public int getEscuelaId() {
        return escuelaId;
    }
    
    public void setEscuelaId(int escuelaId) {
        this.escuelaId = escuelaId;
    }
    
    public Map<TipoDocumento, EstadoDocumento> getDocumentos() {
        return documentos;
    }
    
    public void setDocumentos(Map<TipoDocumento, EstadoDocumento> documentos) {
        this.documentos = documentos;
    }
    
    public void actualizarEstadoDocumento(TipoDocumento tipo, EstadoDocumento estado) {
        documentos.put(tipo, estado);
    }
    
    public EstadoDocumento getEstadoDocumento(TipoDocumento tipo) {
        return documentos.get(tipo);
    }
    
    @Override
    public String toString() {
        return nombresCompletos + " (" + numeroDocumento + ")";
    }
}