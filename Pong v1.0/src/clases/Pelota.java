package clases;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;

public class Pelota {
    public double x;
    public double y;
    public int puntuacion1 = 0;
	public int puntuacion2 = 0;
	public final int inicioX = 602;
	public final int inicioY = 312; 
	private boolean colisionIzquierda = false;
	private boolean colisionDerecha = false;
	public Ventana ventana;
    private double velocidadInicial = 0.5;
    private double dx = velocidadInicial, dy = velocidadInicial;
    private final int ancho = 15, alto = 15;
    private int colisiones = 0;
    private double velocidadMultiplicador = 1.2;//Cada 5 colisiones aumenta en este multiplicador la velocidad.

    public boolean enPausa = false;

    
    public Pelota(int x, int y, Ventana ventana){
        this.x = x;
        this.y = y;
        this.ventana = ventana;
    }

    public Rectangle2D getPelota(){
        return new Rectangle2D.Double(x, y, ancho, alto);
    }

    public void mover(Rectangle limites, boolean colisionP1, boolean colisionP2){
        if (enPausa) {
            return; // No hacer nada si el juego está en pausa
        }
        x+=dx;
        y+=dy;
        if(colisionP1)//colision paleta 1
        {
            reproducirAudio("../sonidos/toque.wav");
            dx=-dx;
            x=40;
            colisiones++;
            if(colisiones == 5)
            {
                aumentoVelocidad();
                colisiones = 0;
            }
        }
      
        if(colisionP2)//colision paleta 2
        {
            reproducirAudio("../sonidos/toque.wav");
            dx=-dx;
            x=1200;
            colisiones++;
            if(colisiones == 5)
            {
                aumentoVelocidad();
                colisiones = 0;
            }
        }
        

        if(x>limites.getMaxX()-15)//punto del jugador de la izquierda
        {
            dx=-dx;
            colisionDerecha = true;
            reiniciar();
        }
        if(y>limites.getMaxY()-15)//choque con el techo
        {
            reproducirAudio("../sonidos/toque2.wav");
            dy=-dy;
        }
        if(x<0)//punto del jugador de la derecha
        {
            dx=-dx;
            colisionIzquierda = true;
            reiniciar();
        }
        if(y<0)//choque con el suelo
        {
            reproducirAudio("../sonidos/toque2.wav");
            dy=-dy;
        }
    }
    private void reiniciar() 
    {
        // ESTO HACE QUE LA PELOTA APAREZCA EN EL CENTRO DE LA MANTALLA
        x = inicioX;
        y = inicioY;
        colisiones = 0;
        dx = velocidadInicial;
        dy = velocidadInicial;

        boolean jugador1EnIzquierda = ventana.isJugador1EnIzquierda();
        
        // SI LA COLISION FUE EN LA PARED DE LA DERECHA
        if (colisionDerecha == true) {
            dx = Math.abs(dx);
            colisionDerecha = false; // RESTABLECE EL ESTADO DE LA PARED PARA QUE PUEDA REPETIRSE CONSTANTEMENTE EL CÓDIGO
            
            if (jugador1EnIzquierda) {
                puntuacion1++; // Jugador 1 anota desde la izquierda
                ventana.setPuntuacionJugador1(puntuacion1);
            } else {
                puntuacion2++; // Jugador 2 anota desde la derecha
                ventana.setPuntuacionJugador2(puntuacion2);
            }

            verificarVictoria();
        }
        // SI LA COLISION FUE EN LA PARED DE LA IZQUIERDA
        else if (colisionIzquierda == true) {
            dx = -Math.abs(dx); // SE MOVERÁ A LA DERECHA
            colisionIzquierda = false; // RESTABLECE EL ESTADO DE LA PARED PARA QUE PUEDA REPETIRSE CONSTANTEMENTE EL CÓDIGO
            
            if (jugador1EnIzquierda) {
                puntuacion2++; // Jugador 2 anota desde la derecha
                ventana.setPuntuacionJugador2(puntuacion2);
            } else {
                puntuacion1++; // Jugador 1 anota desde la izquierda
                ventana.setPuntuacionJugador1(puntuacion1);
            }

            verificarVictoria();
        }

    }

    public void aumentoVelocidad() {
        this.dx = 0.5 * velocidadMultiplicador;
        this.dy = 0.5 * velocidadMultiplicador;
    }
        private void verificarVictoria() 
        {
            if (puntuacion1 >= 7 && puntuacion2 <= puntuacion1 - 2) 
            {
                mostrarMensajeVictoria("Jugador 1", puntuacion1, puntuacion2);
            } 
            else if (puntuacion2 >= 7 && puntuacion1 <= puntuacion2 - 2) 
            {
                mostrarMensajeVictoria("Jugador 2", puntuacion2, puntuacion1);
            }
        }

        private void mostrarMensajeVictoria(String jugadorGanador, int puntuacionGanador, int puntuacionPerdedor) {
        enPausa = true; // Pausar el juego
        ventana.temporizador.stop(); // Detener el temporizador
        // Crea el mensaje de victoria
        String mensaje = jugadorGanador + " ganó con un resultado de " + puntuacionGanador + " - " + puntuacionPerdedor + ".\n" +
                        "¿Desea seguir jugando o cerrar la aplicación?";
        
        // Muestra un cuadro de diálogo con opciones
        int respuesta = JOptionPane.showOptionDialog(
            null, 
            mensaje, 
            "Victoria", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            new Object[]{"Seguir Jugando", "Cerrar"}, 
            "Seguir Jugando"
        );
        
        // Verifica la respuesta del usuario
        if (respuesta == JOptionPane.YES_OPTION) {
            reiniciarJuego();
        } else {
            cerrarAplicacion();
        }
    }

    public void reiniciarJuego() 
    {
        // Código para reiniciar el juego
        puntuacion1 = 0;
        puntuacion2 = 0;
        ventana.setPuntuacionJugador1(puntuacion1); 
        ventana.setPuntuacionJugador2(puntuacion2);
        enPausa = false; // Sacar la pausa para que el juego continúe
        ventana.temporizador.start();
        ventana.minutos = 1;
        ventana.segundos = 00;
    }

    private void cerrarAplicacion() {
        System.exit(0);
    }
    private void reproducirAudio(String nombreArchivo) {
        try {
        	// CARGA EL AUDIO
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(nombreArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            //regula el volumen en decibelios
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(-15f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
