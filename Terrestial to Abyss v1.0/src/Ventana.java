import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Ventana extends JFrame {
    private static final long serialVersionUID = 1L;
    private int nivelAnterior = 1;
    private final int ancho = 1280;
    private final int alto = 720;

    private JPanel panelJuego;
    public JPanel panelMenu;
    private Camara camara;
    private Timer timer;
    private Timer timer2;
    private BufferedImage background1, background2, background3, background4, background5;
    private int x1 = 0, x2 = 0, x3 = 0, x4 = 0, x5 = 0;
    private final int SPEED2 = 1;
    private final int SPEED3 = 2;
    private final int SPEED4 = 3;
    private final int SPEED5 = 4;
    private int vidas;
    private Niveles nivel;
    private Jugador jugador;
    private JLabel pwup_super_salto = new JLabel();
    private JLabel pwup_slow_motion = new JLabel();
    public JPanel panelPause;
    private Timer timerVictoria;
    private JLabel contadorVidas = new JLabel();
    private ImageIcon vidaIcon3, vidaIcon2, vidaIcon1;
    public boolean enPausa = false;
    private GameOverPanel gameOverPanel;
    private JPanel panelVictoria;

    
    public Ventana(Jugador jugador, Niveles nivel) {
        this.jugador = jugador;
        this.nivel = nivel;
        nivel.nivelactual = 0;
        
        panelPause = new panelPause(this); 
        panelPause.setVisible(false);
        
        setTitle("Terrestial to Abyss");
        setSize(ancho, alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Inicializar la cámara
        camara = new Camara(0, 0, ancho, alto);
        camara.actualizar(jugador); // Actualizar la cámara para centrarla en el jugador desde el principio
        gameOverPanel = new GameOverPanel(this);
        panelJuego = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Dibujar fondos con efecto parallax
                // Dibujar fondos con efecto parallax
                power_ups();
                dibujarFondosParallax(g);
                nivel.dibujar(g, camara);  
                jugador.dibujar(g, camara); 
                power_ups();
            }
        };
        cargarIconosDeVidas();  
        contadorVidas.setBounds(40, 10, 200, 90);
        actualizarContadorVidas();
        panelJuego.add(contadorVidas);
        // Crear el panel del menú
        panelMenu = new JPanel()
        {
			private static final long serialVersionUID = 1L;

			@Override
             protected void paintComponent(Graphics g) {
                 super.paintComponent(g);

                 dibujarFondosParallax(g);
             }
        };
        panelMenu.setLayout(null);
        
        cargarFondos();
        
        
        ImageIcon tituloImagen = new ImageIcon(getClass().getResource("/Texturas/menu.png"));
        
        JLabel titulo = new JLabel();
        titulo.setBounds(0, 0, ancho, alto);
        
        Image imagenEscalada = tituloImagen.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        titulo.setIcon(new ImageIcon(imagenEscalada));        
        panelMenu.add(titulo);

        // Cargar la imagen para el botón Play
        ImageIcon playImagen = new ImageIcon(getClass().getResource("/Texturas/Playbutton (2).png"));

	    // Escalar la imagen si es necesario
	    imagenEscalada = playImagen.getImage().getScaledInstance(170, 50, Image.SCALE_SMOOTH);
	
	    // Crear el botón sin texto y con la imagen escalada
	    JButton botonPlay = new JButton(new ImageIcon(imagenEscalada));
	    botonPlay.setBounds(570, 430, 170, 50);
	    
	    // Eliminar el borde y el fondo para hacer que el botón parezca solo la imagen
	    botonPlay.setBorderPainted(false);
	    botonPlay.setContentAreaFilled(false);
	    botonPlay.setFocusPainted(false);

        botonPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego(); // Ocultar menú y mostrar el panel de juego
            }
        });
        
        panelMenu.add(botonPlay);
        panelVictoria = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon victoryImage = new ImageIcon(getClass().getResource("/Texturas/WINSCREEN.PNG"));
                g.drawImage(victoryImage.getImage(), 0, 0, ancho-16, alto-38, this); // Eleva la imagen 20 píxeles
            }

        };
        panelVictoria.setLayout(null);
        panelVictoria.setVisible(false);
        getContentPane().add(panelVictoria, BorderLayout.CENTER);

        timerVictoria = new Timer(1000, e -> volverAlMenu());
        timerVictoria.setRepeats(false);
     // Cargar la imagen para el botón exit
        ImageIcon exitImagen = new ImageIcon(getClass().getResource("/Texturas/exitbutton.png"));

	    // Escalar la imagen si es necesario
	    imagenEscalada = exitImagen.getImage().getScaledInstance(170, 50, Image.SCALE_SMOOTH);
	
	    // Crear el botón sin texto y con la imagen escalada
	    JButton botonExit = new JButton(new ImageIcon(imagenEscalada));
	    botonExit.setBounds(570, 500, 170, 50);
	    
	    // Eliminar el borde y el fondo para hacer que el botón parezca solo la imagen
	    botonExit.setBorderPainted(false);
	    botonExit.setContentAreaFilled(false);
	    botonExit.setFocusPainted(false);

	    botonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
        });
        
        panelMenu.add(botonExit);
        
        
        panelJuego.setLayout(null);

        // Para los power ups: 
        // super salto:
        ImageIcon pwup_ss = new ImageIcon(getClass().getResource("/Texturas/pwup-doblesalto.png"));
        
        pwup_super_salto.setVisible(false);
        pwup_super_salto.setBounds(10, alto-100, 125, 50);
        
        imagenEscalada = pwup_ss.getImage().getScaledInstance(125, 50, Image.SCALE_SMOOTH);
        pwup_super_salto.setIcon(new ImageIcon(imagenEscalada));    

        panelJuego.add(pwup_super_salto);
        
        //slow motion:
        ImageIcon pwup_sm = new ImageIcon(getClass().getResource("/Texturas/pwup-slowtime.png"));
        pwup_slow_motion.setVisible(false);
        pwup_slow_motion.setBounds(10, alto-100, 125, 50);

        imagenEscalada = pwup_sm.getImage().getScaledInstance(125, 50, Image.SCALE_SMOOTH);
        pwup_slow_motion.setIcon(new ImageIcon(imagenEscalada));    
        
        panelJuego.add(pwup_slow_motion);
     
        actualizarContadorVidas();


        panelJuego.add(contadorVidas);
        

        getContentPane().add(panelMenu, BorderLayout.CENTER);
        
        
        panelJuego.addKeyListener(jugador.getControladorTeclado());
        // Agregar KeyListener para manejar la tecla Escape
        panelJuego.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (enPausa) {
                        reanudarJuego(); // Reanudar el juego si está pausado
                    } else {
                        pausarJuego(); // Pausar el juego
                    }
                }
            }
        });

        panelJuego.addKeyListener(jugador.getControladorTeclado());

//        add(panelJuego, BorderLayout.CENTER);
        addKeyListener(jugador.getControladorTeclado());
        
        // Timer del menú para el efecto parallax
        timer2 = new Timer(16, e -> actualizarParallax());
        timer2.start();
        
        timer = new Timer(16, e -> {
            jugador.actualizar(nivel.getBloques(), nivel.getEnemigos(), nivel.getKillBlocks(), nivel.getPiedrasCirculares(), nivel.getAguas(), nivel.getBoss());

            for (Enemigo enemigo : nivel.getEnemigos()) {
                enemigo.actualizar(nivel.getBloques(), nivel.getKillBlocks(), jugador);
            }

            // Actualizar la cámara
            camara.actualizar(jugador);
            actualizarParallax();            
            nivel.actualizarEstalactitasYPiedras(jugador);
            if (nivel.nivelactual != nivelAnterior) {
                // Lógica para manejar el cambio de nivel
                cargarFondos(); // Cargar fondos según el nuevo nivel
                nivelAnterior = nivel.nivelactual; // Actualizar el nivel anterior
            }
            
            Boss boss = nivel.getBoss();
            if (boss != null) {
                boss.actualizar(nivel.getBloques(), nivel.getKillBlocks(), nivel.getEnemigos(), jugador);
                
                // Verificar si la vida del boss es 0
                if (boss.getVida() <= 0) { 
                    timer.stop(); 
                    panelVictoria.setVisible(true); 
                    panelJuego.setVisible(false); 
                    getContentPane().add(panelVictoria, BorderLayout.CENTER); 
                    revalidate();
                    repaint();
                    
                    timerVictoria.start(); // Iniciar el timer para volver al menú después de 10 segundos
                }
            }
            actualizarContadorVidas();
            panelJuego.repaint();
        });
        }
    
    private void cargarIconosDeVidas() {
        vidaIcon3 = new ImageIcon(getClass().getResource("/Texturas/3vidas.png"));
        vidaIcon2 = new ImageIcon(getClass().getResource("/Texturas/2vidas.png"));
        vidaIcon1 = new ImageIcon(getClass().getResource("/Texturas/1vida.png"));
    }
    
    private void actualizarContadorVidas() {
       vidas = jugador.getVidas();  
        switch (vidas) {
            case 3:
                contadorVidas.setIcon(vidaIcon3);
                break;
            case 2:
                contadorVidas.setIcon(vidaIcon2);
                break;
            case 1:
                contadorVidas.setIcon(vidaIcon1);
                break;
            default:
                contadorVidas.setIcon(vidaIcon3);
                if (vidas <= 0) {
                    perdio();  
                }
                break;
        }
    }
    
    private void iniciarJuego() {
        panelVictoria.setVisible(false);
        timer2.stop();

        
        nivel.nivelactual = 1;
        jugador.reiniciar(); 
        jugador.x = nivel.getSpawnBlock().x * 32; 
        jugador.y = nivel.getSpawnBlock().y * 32;
        cargarFondos();

        panelMenu.setVisible(false);
        if (panelJuego.getParent() != null) {
            getContentPane().remove(panelJuego);
        }
        getContentPane().add(panelJuego, BorderLayout.CENTER);
        revalidate();
        repaint();

        panelJuego.requestFocusInWindow();
        timer.start(); 
    }



    private void volverAlMenu() {
        timerVictoria.stop(); 
        panelVictoria.setVisible(false); 
        getContentPane().remove(panelVictoria); 
        panelMenu.setVisible(true); 
        getContentPane().add(panelMenu, BorderLayout.CENTER); 
        revalidate();
        repaint();
        nivel.nivelactual = 1; 
        nivel.boss = null; 
        timer2.start();
    }



    
    private void cargarFondos() {
    	
        try {
        	if(panelMenu.isVisible())
        	{
        		background1 = ImageIO.read(getClass().getResource("/Texturas/n5fondo1.png"));
 	            background2 = ImageIO.read(getClass().getResource("/Texturas/n5fondo3.png"));
 	            background3 = ImageIO.read(getClass().getResource("/Texturas/n4fondo5.png"));
        	}
        	
        	if(nivel.nivelactual == 1)
        	{

	            background1 = ImageIO.read(getClass().getResource("/Texturas/n1fondo1.png"));
	            background2 = ImageIO.read(getClass().getResource("/Texturas/n1fondo2.png"));
	            background3 = ImageIO.read(getClass().getResource("/Texturas/n1fondo3.png"));
	            background4 = ImageIO.read(getClass().getResource("/Texturas/n1fondo4.png"));
	            background5 = ImageIO.read(getClass().getResource("/Texturas/n1fondo5.png"));
        	}
        
        	if(nivel.nivelactual == 2) // ACA DUPLICA LOS IF (YO CAMBIO VALORES) Y PONE LA MUSICA QUE VA
        	{

	            background1 = ImageIO.read(getClass().getResource("/Texturas/n2fondo1.png"));
	            background2 = ImageIO.read(getClass().getResource("/Texturas/n2fondo2.png"));
	            background3 = ImageIO.read(getClass().getResource("/Texturas/n2fondo3.png"));
	            background4 = ImageIO.read(getClass().getResource("/Texturas/n2fondo4.png"));
	            background5 = ImageIO.read(getClass().getResource("/Texturas/n2fondo5.png"));
        	}
        	
        	if(nivel.nivelactual == 3) // ACA DUPLICA LOS IF (YO CAMBIO VALORES) Y PONE LA MUSICA QUE VA
        	{

	            background1 = ImageIO.read(getClass().getResource("/Texturas/n3fondo1.png"));
	            background2 = ImageIO.read(getClass().getResource("/Texturas/n3fondo2.png"));
	            background3 = ImageIO.read(getClass().getResource("/Texturas/n3fondo3.png"));
	            background4 = ImageIO.read(getClass().getResource("/Texturas/n3fondo4.png"));
	            background5 = ImageIO.read(getClass().getResource("/Texturas/n3fondo5.png"));
        	}
        	
        	if(nivel.nivelactual == 4) // ACA DUPLICA LOS IF (YO CAMBIO VALORES) Y PONE LA MUSICA QUE VA
        	{

	            background1 = ImageIO.read(getClass().getResource("/Texturas/n4fondo1.png"));
	            background2 = ImageIO.read(getClass().getResource("/Texturas/n4fondo2.png"));
	            background3 = ImageIO.read(getClass().getResource("/Texturas/n4fondo3.png"));
	            background4 = ImageIO.read(getClass().getResource("/Texturas/n4fondo4.png"));
	            background5 = ImageIO.read(getClass().getResource("/Texturas/n4fondo5.png"));
        	}
        	
        	if(nivel.nivelactual == 5) // ACA DUPLICA LOS IF (YO CAMBIO VALORES) Y PONE LA MUSICA QUE VA
        	{

	            background1 = ImageIO.read(getClass().getResource("/Texturas/n5fondo1.png"));
	            background2 = ImageIO.read(getClass().getResource("/Texturas/n5fondo2.png"));
	            background3 = ImageIO.read(getClass().getResource("/Texturas/n5fondo3.png"));
	            background4 = ImageIO.read(getClass().getResource("/Texturas/n5fondo4.png"));
	            background5 = ImageIO.read(getClass().getResource("/Texturas/n5fondo5.png"));
        	}
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void perdio() {
		nivel.detenerAudio();
        timer.stop();
        panelJuego.setVisible(false);
        gameOverPanel.setVisible(true);
        getContentPane().add(gameOverPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    

    
    private void power_ups() // verificacion de los power ups para mostrar el icono del efecto
    {
    	if(jugador.supersalto == true)
    	{
    		pwup_super_salto.setVisible(true);
    	}
    	else
    	{
    		pwup_super_salto.setVisible(false);
    	}
    	if(jugador.slowmotion == true)
    	{
    		pwup_slow_motion.setVisible(true);
    	}
    	else
    	{
    		pwup_slow_motion.setVisible(false);
    	}
   
    }
    
    private void actualizarParallax() {
        x1 -= SPEED2;
        x2 -= SPEED2;
        x3 -= SPEED3;
        x4 -= SPEED4;
        x5 -= SPEED5;

        if (x1 <= -ancho) x1 = 0;
        if (x2 <= -ancho) x2 = 0;
        if (x3 <= -ancho) x3 = 0;
        if (x4 <= -ancho) x4 = 0;
        if (x5 <= -ancho) x5 = 0;

  
        if (panelMenu.isVisible()) {
            panelMenu.repaint();
        }
    }
    
    private void dibujarFondosParallax(Graphics g) 
    {
    	if(panelMenu.isVisible())
    	{
	        g.drawImage(background1, x1, 0, ancho, alto, this);
	        g.drawImage(background1, x1 + ancho, 0, ancho, alto, this);
	
	        g.drawImage(background2, x2, 0, ancho, alto, this);
	        g.drawImage(background2, x2 + ancho, 0, ancho, alto , this);
	
	        g.drawImage(background3, x3, 0, ancho, alto , this);
	        g.drawImage(background3, x3 + ancho, 0, ancho, alto , this);
	
	        g.drawImage(background4, x4, 0, ancho, alto, this);
	        g.drawImage(background4, x4 + ancho, 0, ancho, alto , this);
	
	        g.drawImage(background5, x5, 0, ancho, alto, this);
	        g.drawImage(background5, x5 + ancho, 0, ancho, alto, this);
    	}
    	else
    	{
	        g.drawImage(background1, x1, 0, ancho, alto, this);
	        g.drawImage(background1, x1 + ancho, 0, ancho, alto, this);
	
	        g.drawImage(background2, x2, 0, ancho, alto-200, this);
	        g.drawImage(background2, x2 + ancho, 0, ancho, alto-200 , this);
	
	        g.drawImage(background3, x3, 0, ancho, alto-150 , this);
	        g.drawImage(background3, x3 + ancho, 0, ancho, alto-150 , this);
	
	        g.drawImage(background4, x4, 0, ancho, alto-150, this);
	        g.drawImage(background4, x4 + ancho, 0, ancho, alto-150, this);
	
	        g.drawImage(background5, x5, 0, ancho, alto, this);
	        g.drawImage(background5, x5 + ancho, 0, ancho, alto, this);
    	}
    }

    public void pausarJuego() {
        timer.stop(); 
        enPausa = true; 
        panelPause.setVisible(true); 
        getContentPane().remove(panelJuego); 
        getContentPane().add(panelPause, BorderLayout.CENTER); 
        revalidate();
        repaint();
        jugador.velocidadX = 0;
        jugador.izquierdaPresionada = false;
        jugador.derechaPresionada = false;
        panelPause.requestFocusInWindow();
    }

    public void reanudarJuego() {
        enPausa = false; 
        getContentPane().remove(panelPause); 
        getContentPane().add(panelJuego, BorderLayout.CENTER);
        panelPause.setVisible(false); 
        panelJuego.setVisible(true); 
        revalidate();
        repaint();
        panelJuego.requestFocusInWindow();  
        timer.start(); 
    }

    public void reiniciarGame() {
        nivel.detenerAudio();
        jugador.reiniciar(); 
        nivel.reproducirAudioilimitado("/Sonidos/NIVEL 1.wav"); 
        cargarFondos();  
        nivel.cambiarNivel("level-1.txt");
        nivel.nivelactual = 1;
        jugador.x = (float) nivel.getSpawnBlock().getX() * 32;
        jugador.y = (float) nivel.getSpawnBlock().getY() * 32;
        getContentPane().remove(panelPause); 
        getContentPane().add(panelJuego, BorderLayout.CENTER);  
        gameOverPanel.setVisible(false);  
        panelPause.setVisible(false);  

        panelJuego.setVisible(true);  
        revalidate();  
        repaint();

        panelJuego.requestFocusInWindow(); 
        timer.start();  
    }

    public void reiniciarNivelActual() {
        nivel.detenerAudio();
        nivel.reproducirAudioilimitado("/Sonidos/NIVEL " + nivel.nivelactual + ".wav"); 
        cargarFondos();  
        nivel.reiniciarElementos();
        getContentPane().remove(panelPause); 
        getContentPane().add(panelJuego, BorderLayout.CENTER);  
        gameOverPanel.setVisible(false);  
        panelPause.setVisible(false);  
        jugador.x = nivel.getSpawnBlock().x * 32;
        jugador.y = nivel.getSpawnBlock().y * 32;
        panelJuego.setVisible(true);  
        revalidate();  
        repaint();

        panelJuego.requestFocusInWindow(); 
        timer.start();  
    }
    
    public static void main(String[] args) {
        Niveles nivel = new Niveles("level-1.txt");
		nivel.detenerAudio();
		nivel.reproducirAudioilimitado("/Sonidos/NIVEL 1.wav");
        Bloque spawnBlock = nivel.getSpawnBlock();
        Jugador jugador = new Jugador(spawnBlock.x * 32, spawnBlock.y * 32, nivel); 
        if(spawnBlock != null)
        {
        jugador.x = spawnBlock.x * 32; 
        jugador.y = spawnBlock.y * 32;
   
        }
        
        SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana(jugador, nivel);
            ventana.setVisible(true);
            ventana.setLocationRelativeTo(null);
            ventana.setResizable(false);
        });
    }

	public int getAncho() {
		return ancho;
	}
	public int getAlto() {
		return alto;
	}
}
