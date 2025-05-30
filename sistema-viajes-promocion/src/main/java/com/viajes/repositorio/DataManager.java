package com.viajes.repositorio;

import com.viajes.modelo.Escuela;
import com.viajes.modelo.Estudiante;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static DataManager instance;
    private List<Escuela> escuelas;
    private Map<Integer, List<Estudiante>> estudiantesPorEscuela;
    private int siguienteIdEscuela;
    private int siguienteIdEstudiante;
    
    private DataManager() {
        this.escuelas = new ArrayList<>();
        this.estudiantesPorEscuela = new HashMap<>();
        this.siguienteIdEscuela = 1;
        this.siguienteIdEstudiante = 1;
        inicializarDatosPrueba();
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    private void inicializarDatosPrueba() {
        // Crear escuelas de prueba
        Escuela escuela1 = new Escuela(siguienteIdEscuela++, "Colegio San José", 
                                      "Av. Principal 123", "555-0001", "info@sanjose.edu");
        Escuela escuela2 = new Escuela(siguienteIdEscuela++, "Instituto Nacional", 
                                      "Calle Central 456", "555-0002", "contacto@nacional.edu");
        Escuela escuela3 = new Escuela(siguienteIdEscuela++, "Colegio Santa María", 
                                      "Jr. Los Andes 789", "555-0003", "admin@santamaria.edu");
        
        escuelas.add(escuela1);
        escuelas.add(escuela2);
        escuelas.add(escuela3);
        
        // Inicializar listas de estudiantes para cada escuela
        estudiantesPorEscuela.put(escuela1.getId(), new ArrayList<>());
        estudiantesPorEscuela.put(escuela2.getId(), new ArrayList<>());
        estudiantesPorEscuela.put(escuela3.getId(), new ArrayList<>());
        
        // Agregar algunos estudiantes de prueba
        agregarEstudiantePrueba("Juan Carlos Pérez López", "12345678", "María López", 
                               "Madre", "987-654-321", escuela1.getId());
        agregarEstudiantePrueba("Ana María González", "87654321", "Carlos González", 
                               "Padre", "987-123-456", escuela1.getId());
        agregarEstudiantePrueba("Luis Fernando Torres", "11223344", "Rosa Torres", 
                               "Madre", "987-789-123", escuela2.getId());
    }
    
    private void agregarEstudiantePrueba(String nombres, String documento, String responsable,
                                        String parentesco, String contacto, int escuelaId) {
        Estudiante estudiante = new Estudiante(siguienteIdEstudiante++, nombres, documento,
                                              responsable, parentesco, contacto, escuelaId);
        estudiantesPorEscuela.get(escuelaId).add(estudiante);
    }
    
    // Métodos para gestión de escuelas
    public List<Escuela> obtenerTodasLasEscuelas() {
        return new ArrayList<>(escuelas);
    }
    
    public boolean guardarEscuela(Escuela escuela) {
        try {
            escuela.setId(siguienteIdEscuela++);
            escuelas.add(escuela);
            estudiantesPorEscuela.put(escuela.getId(), new ArrayList<>());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Escuela obtenerEscuelaPorId(int id) {
        return escuelas.stream()
                      .filter(escuela -> escuela.getId() == id)
                      .findFirst()
                      .orElse(null);
    }
    
    // Métodos para gestión de estudiantes
    public List<Estudiante> obtenerEstudiantesPorEscuela(int escuelaId) {
        List<Estudiante> estudiantes = estudiantesPorEscuela.get(escuelaId);
        return estudiantes != null ? new ArrayList<>(estudiantes) : new ArrayList<>();
    }
    
    public boolean guardarEstudiante(Estudiante estudiante) {
        try {
            estudiante.setId(siguienteIdEstudiante++);
            List<Estudiante> estudiantes = estudiantesPorEscuela.get(estudiante.getEscuelaId());
            if (estudiantes != null) {
                estudiantes.add(estudiante);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Estudiante obtenerEstudiantePorId(int id) {
        for (List<Estudiante> estudiantes : estudiantesPorEscuela.values()) {
            for (Estudiante estudiante : estudiantes) {
                if (estudiante.getId() == id) {
                    return estudiante;
                }
            }
        }
        return null;
    }
    
    public boolean actualizarEstudiante(Estudiante estudiante) {
        Estudiante estudianteExistente = obtenerEstudiantePorId(estudiante.getId());
        if (estudianteExistente != null) {
            // Actualizar los datos del estudiante existente
            estudianteExistente.setNombresCompletos(estudiante.getNombresCompletos());
            estudianteExistente.setNumeroDocumento(estudiante.getNumeroDocumento());
            estudianteExistente.setResponsable(estudiante.getResponsable());
            estudianteExistente.setParentesco(estudiante.getParentesco());
            estudianteExistente.setNumeroContacto(estudiante.getNumeroContacto());
            estudianteExistente.setDocumentos(estudiante.getDocumentos());
            return true;
        }
        return false;
    }
    
    // Método para obtener estadísticas
    public int getTotalEscuelas() {
        return escuelas.size();
    }
    
    public int getTotalEstudiantes() {
        return estudiantesPorEscuela.values().stream()
                                   .mapToInt(List::size)
                                   .sum();
    }
    
    public int getTotalEstudiantesPorEscuela(int escuelaId) {
        List<Estudiante> estudiantes = estudiantesPorEscuela.get(escuelaId);
        return estudiantes != null ? estudiantes.size() : 0;
    }
}