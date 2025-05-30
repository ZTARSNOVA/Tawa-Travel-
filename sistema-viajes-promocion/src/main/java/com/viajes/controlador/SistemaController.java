package com.viajes.controlador;

import com.viajes.modelo.Administrador;
import com.viajes.modelo.Escuela;
import com.viajes.modelo.Estudiante;
import com.viajes.modelo.TipoDocumento;
import com.viajes.modelo.EstadoDocumento;
import com.viajes.modelo.*;
import com.viajes.repositorio.DataManager;
import com.viajes.vista.LoginFrame;
import com.viajes.vista.MainFrame;
import java.util.List;

public class SistemaController {
    private DataManager dataManager;
    private Administrador administrador;
    private LoginFrame loginFrame;
    private MainFrame mainFrame;
    
    public SistemaController() {
        this.dataManager = DataManager.getInstance();
        this.administrador = new Administrador("admin", "admin123");
        iniciarSistema();
    }
    
    private void iniciarSistema() {
        loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
    }
    
    public boolean autenticarUsuario(String usuario, String contraseña) {
        if (administrador.validarCredenciales(usuario, contraseña)) {
            loginFrame.setVisible(false);
            mainFrame = new MainFrame(this);
            mainFrame.setVisible(true);
            return true;
        }
        return false;
    }
    
    public void cerrarSesion() {
        if (mainFrame != null) {
            mainFrame.setVisible(false);
            mainFrame.dispose();
        }
        loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
    }
    
    // Métodos para gestión de escuelas
    public List<Escuela> obtenerTodasLasEscuelas() {
        return dataManager.obtenerTodasLasEscuelas();
    }
    
    public boolean crearEscuela(String nombre, String direccion, String telefono, String email) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        Escuela escuela = new Escuela(0, nombre.trim(), direccion, telefono, email);
        return dataManager.guardarEscuela(escuela);
    }
    
    public Escuela obtenerEscuelaPorId(int id) {
        return dataManager.obtenerEscuelaPorId(id);
    }
    
    // Métodos para gestión de estudiantes
    public List<Estudiante> obtenerEstudiantesPorEscuela(int escuelaId) {
        return dataManager.obtenerEstudiantesPorEscuela(escuelaId);
    }
    
    public boolean crearEstudiante(String nombres, String numeroDocumento, String responsable, 
                                  String parentesco, String numeroContacto, int escuelaId) {
        if (nombres == null || nombres.trim().isEmpty() || 
            numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
            return false;
        }
        
        Estudiante estudiante = new Estudiante(0, nombres.trim(), numeroDocumento.trim(), 
                                             responsable, parentesco, numeroContacto, escuelaId);
        return dataManager.guardarEstudiante(estudiante);
    }
    
    public boolean actualizarEstadoDocumento(int estudianteId, TipoDocumento tipo, EstadoDocumento estado) {
        Estudiante estudiante = dataManager.obtenerEstudiantePorId(estudianteId);
        if (estudiante != null) {
            estudiante.actualizarEstadoDocumento(tipo, estado);
            return dataManager.actualizarEstudiante(estudiante);
        }
        return false;
    }
    
    public Estudiante obtenerEstudiantePorId(int id) {
        return dataManager.obtenerEstudiantePorId(id);
    }
    
    // Métodos para estadísticas
    public int getTotalEscuelas() {
        return dataManager.getTotalEscuelas();
    }
    
    public int getTotalEstudiantes() {
        return dataManager.getTotalEstudiantes();
    }
    
    public int getTotalEstudiantesPorEscuela(int escuelaId) {
        return dataManager.getTotalEstudiantesPorEscuela(escuelaId);
    }
}