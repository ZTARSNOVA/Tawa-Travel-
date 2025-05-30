package com.viajes.modelo;

public enum EstadoDocumento {
    PENDING("Pendiente"),
    UPLOADED("Subido"),
    VALIDATED("Validado"),
    REJECTED("Rechazado");
    
    private final String descripcion;
    
    EstadoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}