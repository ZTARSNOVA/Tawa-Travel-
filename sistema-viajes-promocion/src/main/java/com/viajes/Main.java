package com.viajes;

import com.viajes.controlador.SistemaController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Usar el Look and Feel por defecto para evitar problemas de compatibilidad
                    UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName()
                    );
                } catch (Exception e) {
                    // Si falla, continuar con el Look and Feel por defecto
                    System.out.println("Usando Look and Feel por defecto");
                }
                
                // Iniciar el sistema
                new SistemaController();
            }
        });
    }
}