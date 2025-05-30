package com.viajes.modelo;

public enum TipoDocumento {
    CONTRATO_AGENCIA("Contrato de servicio de agencia de viajes"),
    PERMISO_DIRECCION("Permiso de Dirección"),
    SEGURO_MEDICO("Seguro médico vigente"),
    CERTIFICADO_SALUD("Certificado de Salud"),
    PERMISO_NOTARIAL("Permiso Notarial");
    
    private final String descripcion;
    
    TipoDocumento(String descripcion) {
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