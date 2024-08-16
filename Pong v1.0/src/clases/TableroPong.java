package clases;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TableroPong extends JPanel implements KeyListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Pelota pelota;
    Paleta paleta1 = new Paleta(20, 280); 
    Paleta paleta2 = new Paleta(1220, 280);
    
    private Timer timer;
    public TableroPong(Ventana ventana) {

		pelota = new Pelota(602, 312, ventana);
        setBackground(Color.BLACK);
        setLayout(null);
        addKeyListener(this); 
        setFocusable(true);
        reproducirAudioilimitado("../sonidos/marchaturca8bit.wav");
        timer = new Timer(200, e -> actualizar());
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        dibujar(g2); // pelota
        actualizar();

        Graphics2D g3 = (Graphics2D) g; // paleta 1
        g3.setColor(Color.WHITE);
        g3.fill(new Rectangle2D.Double(paleta1.getX(), paleta1.getY(), 20, 100));

        Graphics2D g4 = (Graphics2D) g; // paleta 2
        g4.setColor(Color.WHITE);
        g4.fill(new Rectangle2D.Double(paleta2.getX(), paleta2.getY(), 20, 100));

        Graphics2D g5 = (Graphics2D) g; // Linea Punteada del centro
        g5.setColor(Color.WHITE);
        float guiones1[] = {12, 12};
        int ancho = 2;
        g5.setStroke(new BasicStroke(ancho, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, guiones1, 0));
        g5.drawLine(610, 710, 610, 0);

        // Bordes de la cancha:
        Graphics2D g6 = (Graphics2D) g;
        g6.setColor(Color.WHITE);
        g6.fill(new Rectangle2D.Double(0, 0, 1280, 5)); // superior

        Graphics2D g7 = (Graphics2D) g;
        g7.setColor(Color.WHITE);
        g7.fill(new Rectangle2D.Double(0, 0, 5, 720)); // izquierdo

        Graphics2D g8 = (Graphics2D) g;
        g8.setColor(Color.WHITE);
        g8.fill(new Rectangle2D.Double(0, 678, 1280, 6)); // inferior

        Graphics2D g9 = (Graphics2D) g;
        g9.setColor(Color.WHITE);
        g9.fill(new Rectangle2D.Double(1260, 0, 5, 720)); // derecho
    }

    public void dibujar(Graphics2D g) {
        g.fill(pelota.getPelota());
    }

    public void actualizar() {
        pelota.mover(getBounds(), colision(paleta1.getPaleta()), colision(paleta2.getPaleta()));
        paleta1.mover(getBounds());
        paleta2.mover(getBounds());
        repaint();
    }

	//colision entre la pelota y las paletas

	public boolean colision(Rectangle2D r)
	{
		return pelota.getPelota().intersects(r);
	}

    // de aca para abajo es todo lo del teclado
    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();
        double velocidad = 1.0; // Velocidad por defecto
        if (tecla == KeyEvent.VK_W) {
            paleta1.dibujar_en_y(-velocidad);
        }
        if (tecla == KeyEvent.VK_S) {
            paleta1.dibujar_en_y(velocidad);
        }
        if (tecla == KeyEvent.VK_UP) {
            paleta2.dibujar_en_y(-velocidad);
        }
        if (tecla == KeyEvent.VK_DOWN) {
            paleta2.dibujar_en_y(velocidad);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();
        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_S) {
            paleta1.dibujar_en_y(0);
        }
        if (tecla == KeyEvent.VK_UP || tecla == KeyEvent.VK_DOWN) {
            paleta2.dibujar_en_y(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void reproducirAudioilimitado(String nombreArchivo) {
        AudioInputStream audioInputStream = null;
        try {
            // CARGA EL AUDIO
            audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(nombreArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(-15f);

            // ENTRA EN BUCLE
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (audioInputStream != null) {
                try {
                    audioInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }}}
        }
}