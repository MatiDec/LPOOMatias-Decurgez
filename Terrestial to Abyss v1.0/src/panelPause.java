import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class panelPause extends JPanel 
{
	
	private static final long serialVersionUID = 1L;
	private final int ancho = 1280;
	private final int alto = 720;
	public panelPause(Ventana ventana) 
    {
        setLayout(new BorderLayout());

        ImageIcon tituloImagen = new ImageIcon(getClass().getResource("/Texturas/pause.png"));
        
        JLabel pause = new JLabel();
        pause.setBounds(0, 0, 1280, 720);
        
        Image imagenEscalada = tituloImagen.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        setLayout(null);
        pause.setIcon(new ImageIcon(imagenEscalada));        
        add(pause, BorderLayout.NORTH);

        JButton buttonSalir = new JButton("");
        buttonSalir.setBounds(ancho/2-60, alto-305, 120, 40);
       
        buttonSalir.setBorderPainted(false);
        buttonSalir.setContentAreaFilled(false);
        buttonSalir.setFocusPainted(false);
        
        buttonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        add(buttonSalir);
        
        JButton restartButton = new JButton(); 
        restartButton.setBounds(ventana.getAncho() / 2 - 160, 340, 320, 50);
        restartButton.setOpaque(true); 
        restartButton.setContentAreaFilled(false); 
        restartButton.setBorder(BorderFactory.createEmptyBorder()); 
        restartButton.setUI(new BasicButtonUI()); 
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.reiniciarNivelActual(); 
            }
        });
        
        restartButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        add(restartButton);
        
        JButton buttonReanudar = new JButton("");
        buttonReanudar.setBounds(ancho/2-100, alto-445, 200, 40);
       
        buttonReanudar.setBorderPainted(false);
        buttonReanudar.setContentAreaFilled(false);
        buttonReanudar.setFocusPainted(false);
        
        buttonReanudar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.reanudarJuego();
            }
        });
        add(buttonReanudar);
        
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (!ventana.enPausa) {
                        ventana.pausarJuego(); 
                    } else {
                        ventana.reanudarJuego(); 
                    }
                }
            }
        });
        
        setFocusable(true); 

    }
}