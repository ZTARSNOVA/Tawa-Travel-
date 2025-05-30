package com.viajes.vista;

import com.viajes.controlador.SistemaController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private SistemaController controller;
    private JTextField usuarioField;
    private JPasswordField contraseñaField;
    private JButton loginButton;
    
    public LoginFrame(SistemaController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        setupEvents();
    }
    
    private void initComponents() {
        setTitle("Sistema de Viajes de Promoción - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        usuarioField = new JTextField(15);
        contraseñaField = new JPasswordField(15);
        loginButton = new JButton("Iniciar Sesión");
        
        // Valores por defecto para facilitar las pruebas
        usuarioField.setText("admin");
        contraseñaField.setText("admin123");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel titleLabel = new JLabel("Sistema de Viajes de Promoción");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Gestión de Documentos para Viajes de Graduación");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);
        
        // Espacio
        gbc.gridy = 2;
        mainPanel.add(Box.createVerticalStrut(20), gbc);
        
        // Usuario
        gbc.gridwidth = 1; gbc.gridy = 3;
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usuarioField, gbc);
        
        // Contraseña
        gbc.gridy = 4;
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(contraseñaField, gbc);
        
        // Botón
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(loginButton, gbc);
        
        // Información de credenciales
        JLabel infoLabel = new JLabel("<html><center>Credenciales por defecto:<br/>Usuario: admin<br/>Contraseña: admin123</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(infoLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void setupEvents() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // Enter para login
        getRootPane().setDefaultButton(loginButton);
        
        // Enter en los campos de texto
        usuarioField.addActionListener(e -> contraseñaField.requestFocus());
        contraseñaField.addActionListener(e -> realizarLogin());
    }
    
    private void realizarLogin() {
        String usuario = usuarioField.getText().trim();
        String contraseña = new String(contraseñaField.getPassword());
        
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese usuario y contraseña", "Campos Vacíos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (controller.autenticarUsuario(usuario, contraseña)) {
            JOptionPane.showMessageDialog(this, 
                "¡Bienvenido al sistema!", "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos", "Error de Login", 
                JOptionPane.ERROR_MESSAGE);
            contraseñaField.setText("");
            usuarioField.requestFocus();
        }
    }
}