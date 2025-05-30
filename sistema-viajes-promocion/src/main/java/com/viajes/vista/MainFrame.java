package com.viajes.vista;

import com.viajes.modelo.Escuela;
import com.viajes.modelo.Estudiante;
import com.viajes.modelo.TipoDocumento;
import com.viajes.modelo.EstadoDocumento;
import com.viajes.controlador.SistemaController;
import com.viajes.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainFrame extends JFrame {
    private SistemaController controller;
    private JComboBox<Escuela> escuelaComboBox;
    private JList<Estudiante> estudiantesList;
    private DefaultListModel<Estudiante> estudiantesListModel;
    private JTable documentosTable;
    private DefaultTableModel documentosTableModel;
    private Escuela escuelaSeleccionada;
    private Estudiante estudianteSeleccionado;
    private JLabel estadisticasLabel;
    
    public MainFrame(SistemaController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        setupEvents();
        cargarEscuelas();
        actualizarEstadisticas();
    }
    
    private void initComponents() {
        setTitle("Sistema de Viajes de Promoción");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        escuelaComboBox = new JComboBox<>();
        escuelaComboBox.setPreferredSize(new Dimension(250, 25));
        
        estudiantesListModel = new DefaultListModel<>();
        estudiantesList = new JList<>(estudiantesListModel);
        estudiantesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        estudiantesList.setCellRenderer(new EstudianteListCellRenderer());
        
        // Tabla de documentos
        String[] columnNames = {"Documento", "Estado"};
        documentosTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Solo la columna de estado es editable
            }
        };
        documentosTable = new JTable(documentosTableModel);
        documentosTable.setRowHeight(25);
        
        // ComboBox para estados en la tabla
        JComboBox<EstadoDocumento> estadoComboBox = new JComboBox<>(EstadoDocumento.values());
        estadoComboBox.setRenderer(new EstadoComboBoxRenderer());
        documentosTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(estadoComboBox));
        documentosTable.getColumnModel().getColumn(1).setCellRenderer(new EstadoTableCellRenderer());
        
        estadisticasLabel = new JLabel();
        estadisticasLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        estadisticasLabel.setForeground(Color.GRAY);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con menú
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(new Color(245, 245, 245));
        
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botonesPanel.setBackground(new Color(245, 245, 245));
        
        JButton nuevaEscuelaBtn = new JButton("Nueva Escuela");
        JButton nuevoEstudianteBtn = new JButton("Nuevo Estudiante");
        JButton cerrarSesionBtn = new JButton("Cerrar Sesión");
        
        // Estilo de botones
        estilizarBoton(nuevaEscuelaBtn, new Color(70, 130, 180));
        estilizarBoton(nuevoEstudianteBtn, new Color(60, 179, 113));
        estilizarBoton(cerrarSesionBtn, new Color(220, 20, 60));
        
        botonesPanel.add(nuevaEscuelaBtn);
        botonesPanel.add(nuevoEstudianteBtn);
        
        JPanel derechaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        derechaPanel.setBackground(new Color(245, 245, 245));
        derechaPanel.add(estadisticasLabel);
        derechaPanel.add(Box.createHorizontalStrut(20));
        derechaPanel.add(cerrarSesionBtn);
        
        menuPanel.add(botonesPanel, BorderLayout.WEST);
        menuPanel.add(derechaPanel, BorderLayout.EAST);
        
        add(menuPanel, BorderLayout.NORTH);
        
        // Panel principal con tres secciones
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel de Escuelas
        JPanel escuelasPanel = createTitledPanel("Escuelas", "Selecciona una escuela para ver sus estudiantes");
        escuelasPanel.add(Box.createVerticalStrut(10));
        escuelasPanel.add(new JLabel("Escuela:"));
        escuelasPanel.add(Box.createVerticalStrut(5));
        escuelasPanel.add(escuelaComboBox);
        
        // Información de documentos requeridos
        escuelasPanel.add(Box.createVerticalStrut(20));
        JLabel docReqLabel = new JLabel("Documentos Requeridos por Escuela:");
        docReqLabel.setFont(new Font("Arial", Font.BOLD, 12));
        escuelasPanel.add(docReqLabel);
        
        for (TipoDocumento tipo : TipoDocumento.values()) {
            JLabel docLabel = new JLabel("• " + tipo.getDescripcion());
            docLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            escuelasPanel.add(docLabel);
        }
        
        // Panel de Estudiantes
        JPanel estudiantesPanel = createTitledPanel("Estudiantes", "Selecciona una escuela");
        estudiantesPanel.add(new JScrollPane(estudiantesList));
        
        // Panel de Documentos
        JPanel documentosPanel = createTitledPanel("Documentos para Viaje de Graduación", "Estado de los documentos requeridos");
        documentosPanel.add(new JScrollPane(documentosTable));
        JButton guardarCambiosBtn = new JButton("Guardar Cambios");
        estilizarBoton(guardarCambiosBtn, new Color(255, 140, 0));
        documentosPanel.add(Box.createVerticalStrut(10));
        documentosPanel.add(guardarCambiosBtn);
        
        mainPanel.add(escuelasPanel);
        mainPanel.add(estudiantesPanel);
        mainPanel.add(documentosPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Eventos de botones
        nuevaEscuelaBtn.addActionListener(e -> mostrarDialogoNuevaEscuela());
        nuevoEstudianteBtn.addActionListener(e -> mostrarDialogoNuevoEstudiante());
        cerrarSesionBtn.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea cerrar sesión?", "Confirmar", 
                JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                controller.cerrarSesion();
            }
        });
        guardarCambiosBtn.addActionListener(e -> guardarCambiosDocumentos());
    }
    
    private void estilizarBoton(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 11));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        boton.setFocusPainted(false);
    }
    
    private JPanel createTitledPanel(String title, String subtitle) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(Font.ITALIC, 12f));
        subtitleLabel.setForeground(Color.GRAY);
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    private void setupEvents() {
        escuelaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escuelaSeleccionada = (Escuela) escuelaComboBox.getSelectedItem();
                if (escuelaSeleccionada != null) {
                    cargarEstudiantes();
                    actualizarSubtituloEstudiantes();
                }
            }
        });
        
        estudiantesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                estudianteSeleccionado = estudiantesList.getSelectedValue();
                if (estudianteSeleccionado != null) {
                    cargarDocumentosEstudiante();
                    actualizarSubtituloDocumentos();
                }
            }
        });
    }
    
    private void cargarEscuelas() {
        escuelaComboBox.removeAllItems();
        List<Escuela> escuelas = controller.obtenerTodasLasEscuelas();
        for (Escuela escuela : escuelas) {
            escuelaComboBox.addItem(escuela);
        }
    }
    
    private void cargarEstudiantes() {
        estudiantesListModel.clear();
        if (escuelaSeleccionada != null) {
            List<Estudiante> estudiantes = controller.obtenerEstudiantesPorEscuela(escuelaSeleccionada.getId());
            for (Estudiante estudiante : estudiantes) {
                estudiantesListModel.addElement(estudiante);
            }
        }
        // Limpiar documentos cuando cambia la escuela
        documentosTableModel.setRowCount(0);
        actualizarEstadisticas();
    }
    
    private void cargarDocumentosEstudiante() {
        documentosTableModel.setRowCount(0);
        if (estudianteSeleccionado != null) {
            for (TipoDocumento tipo : TipoDocumento.values()) {
                EstadoDocumento estado = estudianteSeleccionado.getEstadoDocumento(tipo);
                documentosTableModel.addRow(new Object[]{tipo.getDescripcion(), estado});
            }
        }
    }
    
    private void actualizarSubtituloEstudiantes() {
        // Actualizar el subtítulo del panel de estudiantes
        Component[] components = ((JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(1)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            JLabel subtitleLabel = (JLabel) components[0];
            if (escuelaSeleccionada != null) {
                int totalEstudiantes = controller.getTotalEstudiantesPorEscuela(escuelaSeleccionada.getId());
                subtitleLabel.setText("Estudiantes de " + escuelaSeleccionada.getNombre() + " (" + totalEstudiantes + " estudiantes)");
            }
        }
    }
    
    private void actualizarSubtituloDocumentos() {
        // Actualizar el subtítulo del panel de documentos
        Component[] components = ((JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            JLabel subtitleLabel = (JLabel) components[0];
            if (estudianteSeleccionado != null) {
                subtitleLabel.setText("Documentos de " + estudianteSeleccionado.getNombresCompletos());
            }
        }
    }
    
    private void actualizarEstadisticas() {
        int totalEscuelas = controller.getTotalEscuelas();
        int totalEstudiantes = controller.getTotalEstudiantes();
        estadisticasLabel.setText("Total: " + totalEscuelas + " escuelas, " + totalEstudiantes + " estudiantes");
    }
    
    private void mostrarDialogoNuevaEscuela() {
        JDialog dialog = new JDialog(this, "Nueva Escuela", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        JTextField nombreField = new JTextField(25);
        JTextField direccionField = new JTextField(25);
        JTextField telefonoField = new JTextField(25);
        JTextField emailField = new JTextField(25);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(nombreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(direccionField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(telefonoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(emailField, gbc);
        
        JPanel buttonPanel = new JPanel();
        JButton guardarBtn = new JButton("Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        estilizarBoton(guardarBtn, new Color(70, 130, 180));
        estilizarBoton(cancelarBtn, new Color(128, 128, 128));
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);
        
        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio");
                return;
            }
            
            if (controller.crearEscuela(nombre, direccionField.getText().trim(), 
                                      telefonoField.getText().trim(), emailField.getText().trim())) {
                cargarEscuelas();
                actualizarEstadisticas();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Escuela creada exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear la escuela");
            }
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void mostrarDialogoNuevoEstudiante() {
        if (escuelaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una escuela primero");
            return;
        }
        
        JDialog dialog = new JDialog(this, "Nuevo Estudiante - " + escuelaSeleccionada.getNombre(), true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        JTextField nombresField = new JTextField(25);
        JTextField documentoField = new JTextField(25);
        JTextField responsableField = new JTextField(25);
        JTextField parentescoField = new JTextField(25);
        JTextField contactoField = new JTextField(25);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nombres completos:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(nombresField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Número de documento:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(documentoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Responsable:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(responsableField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Parentesco:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(parentescoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Número de contacto:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(contactoField, gbc);
        
        JPanel buttonPanel = new JPanel();
        JButton guardarBtn = new JButton("Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        estilizarBoton(guardarBtn, new Color(60, 179, 113));
        estilizarBoton(cancelarBtn, new Color(128, 128, 128));
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);
        
        guardarBtn.addActionListener(e -> {
            String nombres = nombresField.getText().trim();
            String documento = documentoField.getText().trim();
            
            if (nombres.isEmpty() || documento.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nombres y número de documento son obligatorios");
                return;
            }
            
            if (controller.crearEstudiante(nombres, documento, responsableField.getText().trim(),
                                         parentescoField.getText().trim(), contactoField.getText().trim(),
                                         escuelaSeleccionada.getId())) {
                cargarEstudiantes();
                actualizarEstadisticas();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Estudiante creado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear el estudiante");
            }
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void guardarCambiosDocumentos() {
        if (estudianteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante primero");
            return;
        }
        
        boolean cambiosGuardados = true;
        TipoDocumento[] tipos = TipoDocumento.values();
        
        for (int i = 0; i < documentosTableModel.getRowCount(); i++) {
            EstadoDocumento nuevoEstado = (EstadoDocumento) documentosTableModel.getValueAt(i, 1);
            TipoDocumento tipo = tipos[i];
            
            if (!controller.actualizarEstadoDocumento(estudianteSeleccionado.getId(), tipo, nuevoEstado)) {
                cambiosGuardados = false;
            }
        }
        
        if (cambiosGuardados) {
            JOptionPane.showMessageDialog(this, "Cambios guardados exitosamente");
            // Recargar estudiantes para actualizar los datos
            cargarEstudiantes();
            // Reseleccionar el estudiante
            for (int i = 0; i < estudiantesListModel.size(); i++) {
                if (estudiantesListModel.get(i).getId() == estudianteSeleccionado.getId()) {
                    estudiantesList.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar algunos cambios");
        }
    }
    
    // Renderer personalizado para la lista de estudiantes
    private class EstudianteListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) value;
                setText("<html><b>" + estudiante.getNombresCompletos() + "</b><br/>" +
                       "Doc: " + estudiante.getNumeroDocumento() + "<br/>" +
                       "Responsable: " + estudiante.getResponsable() + "</html>");
            }
            
            return this;
        }
    }
    
    // Renderer para ComboBox de estados
    private class EstadoComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof EstadoDocumento) {
                EstadoDocumento estado = (EstadoDocumento) value;
                setText(estado.getDescripcion());
                
                if (!isSelected) {
                    // Colores según el estado
                    switch (estado) {
                        case PENDING:
                            setForeground(Color.ORANGE);
                            break;
                        case UPLOADED:
                            setForeground(Color.BLUE);
                            break;
                        case VALIDATED:
                            setForeground(new Color(0, 128, 0));
                            break;
                        case REJECTED:
                            setForeground(Color.RED);
                            break;
                    }
                }
            }
            
            return this;
        }
    }
    
    // Renderer para celdas de tabla
    private class EstadoTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof EstadoDocumento) {
                EstadoDocumento estado = (EstadoDocumento) value;
                setText(estado.getDescripcion());
                
                if (!isSelected) {
                    // Colores según el estado
                    switch (estado) {
                        case PENDING:
                            setForeground(Color.ORANGE);
                            break;
                        case UPLOADED:
                            setForeground(Color.BLUE);
                            break;
                        case VALIDATED:
                            setForeground(new Color(0, 128, 0));
                            break;
                        case REJECTED:
                            setForeground(Color.RED);
                            break;
                    }
                } else {
                    setForeground(table.getSelectionForeground());
                }
            }
            
            return this;
        }
    }
}