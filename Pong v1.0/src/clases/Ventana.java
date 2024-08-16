package clases;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Ventana extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int ancho = 1280, alto = 720;
	private TableroPong tablero;
	JLabel Jugador_1 = new JLabel("0");
	JLabel Jugador_2 = new JLabel("0");
	JLabel Tiempo = new JLabel("1:00");
	JLabel mensajeCambioLado = new JLabel("Cambio de lado...");
	Timer temporizador;
	int minutos=1, segundos=0;
	public int fin_primer_tiempo=0;
	Pelota pelota;
	public int cambioLado = 0;

	public Ventana() {
		setTitle("Pong");
		setSize(ancho, alto);
		setLocationRelativeTo(null);
		setResizable(false);
		tablero = new TableroPong(this);
		add(tablero);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Marcador de puntos:
		//Jugador 1:
		
		Jugador_1.setFont(new Font("Segoe UI", Font.BOLD, 40));
		Jugador_1.setHorizontalAlignment(SwingConstants.CENTER);
		Jugador_1.setForeground(new Color(255, 255, 255));
		Jugador_1.setBounds(560, 10, 50, 90);
		tablero.add(Jugador_1);
		
		//Jugador 2
		
		Jugador_2.setFont(new Font("Segoe UI", Font.BOLD, 40));
		Jugador_2.setHorizontalAlignment(SwingConstants.CENTER);
		Jugador_2.setForeground(new Color(255, 255, 255));
		Jugador_2.setBounds(610, 10, 50, 90);
		tablero.add(Jugador_2);
		
		Tiempo.setFont(new Font("Segoe UI", Font.BOLD, 40));
		Tiempo.setHorizontalAlignment(SwingConstants.CENTER);
		Tiempo.setForeground(new Color(255, 255, 255));
		Tiempo.setBounds(200, 10, 80, 90);
		tablero.add(Tiempo);

		// Mensaje de Cambio de Lado:
        mensajeCambioLado.setFont(new Font("Segoe UI", Font.BOLD, 60));
        mensajeCambioLado.setHorizontalAlignment(SwingConstants.CENTER);
        mensajeCambioLado.setForeground(new Color(255, 255, 255));
        mensajeCambioLado.setBounds(0, 0, ancho, alto);
        mensajeCambioLado.setVisible(false); // Inicialmente oculto
        tablero.add(mensajeCambioLado);

		ActionListener tarea = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (minutos > 0 && segundos == 0) 
                {
                    minutos--;
                    segundos = 59;
                    Tiempo.setText(minutos + ":" + String.format("%02d", segundos));
                } 
                else if (minutos >= 0 && segundos > 0) 
                {
                    segundos--;
                    Tiempo.setText(minutos + ":" + String.format("%02d", segundos));
                } 
                
                else if(fin_primer_tiempo == 1 && segundos == 0 && minutos == 0)
                {
					mensajeCambioLado.setVisible(false);
                	fin_primer_tiempo=0;
                	reiniciarJuego();
                }
                
                else if (segundos == 0 && minutos == 0) 
                {
					mensajeCambioLado.setVisible(true);
                	// Intercambiar posiciones de los jugadores	
                	intercambiarPosicionesJugadores();
                	fin_primer_tiempo = 1;
                	segundos = 15;
                	minutos= 0;
					tablero.pelota.enPausa = true;
                }

            }
        };

        // Iniciar el temporizador Swing
        temporizador = new Timer(1000, tarea);  // Dispara la tarea cada 1000 ms (1 segundo)
        temporizador.start();
	}

	public void setPuntuacionJugador1(int puntuacion) {
        Jugador_1.setText(String.valueOf(puntuacion));
        reproducirAudio("../sonidos/punto.wav");
    }

    public void setPuntuacionJugador2(int puntuacion) {
    	Jugador_2.setText(String.valueOf(puntuacion));
    	reproducirAudio("../sonidos/punto.wav");
    }

	   public boolean isJugador1EnIzquierda() {
        return Jugador_1.getX() < Jugador_2.getX();
    }

	private void reproducirAudio(String nombreArchivo) {
        try {
        	// CARGA EL AUDIO
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(nombreArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

			FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        	control.setValue(-10f);
        } 
		catch (Exception e)
		{
			 e.printStackTrace(); 
		}
	}

	private void reiniciarJuego() {
        minutos = 1;
        segundos = 00;
        Tiempo.setText("1:00");

        // Resetear puntuaciones
		cambioLado = 1;
        tablero.pelota.reiniciarJuego();

        intercambiarPosicionesJugadores(); 
    }
    
    private void intercambiarPosicionesJugadores() {
        // Guardar temporalmente las posiciones actuales
        int tempXJugador1 = Jugador_1.getX();
        int tempXJugador2 = Jugador_2.getX();

        // Intercambiar las posiciones
        Jugador_1.setBounds(tempXJugador2, Jugador_1.getY(), Jugador_1.getWidth(), Jugador_1.getHeight());
        Jugador_2.setBounds(tempXJugador1, Jugador_2.getY(), Jugador_2.getWidth(), Jugador_2.getHeight());
        
        tablero.pelota.x = tablero.pelota.inicioX;
        tablero.pelota.y = tablero.pelota.inicioY;
        
    }
}
