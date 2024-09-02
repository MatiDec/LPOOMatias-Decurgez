package clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;

public class TableroGalaga extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private Timer timer;
    private Jugador jugador = new Jugador(640, 600);
    private Timer timerEnemigos;
    private Timer timerKamikaze;
    private Boss boss;
    private BufferedImage texturaBoss;

    
    private Enemigos[] enemigos;
    private int cantToques = 0;
    private boolean direccionderecha = true;
    private int nivel = 1; // para hardcodear
    private int enemigosRestantes;
    private int cantBajadas = 1;
    private int vidaBoss;
    
    private BufferedImage texturaJugador;  // Textura para el jugador
    private BufferedImage texturaEnemigos; // Textura para los enemigos
    private BufferedImage texturaEnemigos2; // Textura para los enemigos
    private BufferedImage texturaEnemigos3;
    private BufferedImage preDisparo;
    private BufferedImage postDisparo;// Textura para los enemigos
    private BufferedImage texturaProyectil; // Textura para los enemigos

    private List<Rectangle2D> disparosEnemigos = new ArrayList<>(); 
    private List<Rectangle2D> kamikazeEnemigos = new ArrayList<>();
    private List<Rectangle2D> gasterEnemigos = new ArrayList<>();
    private List<Enemigos> enemigosDisparando = new ArrayList<>();
    
    private BufferedImage[] framesFondo;
    private int currentFrame = 0;
    
    JPanel pantallaCarga = new JPanel();
    JPanel gameOver = new JPanel();
    JPanel youWin = new JPanel();
    JLabel gifCarga = new JLabel("");
    JLabel progreso = new JLabel("");
    JLabel textoGameOver = new JLabel("");
    JLabel textoYouWin = new JLabel("");
    JButton seguirJugando = new JButton("Seguir");
    JButton noSeguir = new JButton("No seguir");
    JButton seguirJugandoWin = new JButton("Seguir");
    JButton noSeguirWin = new JButton("No seguir");
    
    private JProgressBar barraProgreso = new JProgressBar(0, 100);  // Nueva barra de progreso
    
    public TableroGalaga(Ventana ventana) {
        setBackground(Color.BLACK);
        setLayout(null);
        addKeyListener(this);
        setFocusable(true);
        
        gameOver.setBackground(new Color(0, 0, 0));
        gameOver.setBounds(0, 0, 1280, 720);
        add(gameOver);
        gameOver.setLayout(null);
        gameOver.setVisible(false);
        
        textoGameOver.setBounds(380, 80, 500, 500);
        gameOver.add(textoGameOver);
        
        youWin.setBackground(Color.BLACK);
        youWin.setBounds(0, 0, 1280, 720);
        add(youWin);
        youWin.setLayout(null);
        youWin.setVisible(false);
        
        textoYouWin.setBounds(380, 80, 500, 500);
        youWin.add(textoYouWin);
        
        seguirJugandoWin.setBackground(new Color(0, 0, 0));
        seguirJugandoWin.setForeground(new Color(255, 255, 255));
        seguirJugandoWin.setBounds(380, 540, 89, 23);
        youWin.add(seguirJugandoWin);
        
        noSeguirWin.setBackground(new Color(0,0,0));
        noSeguirWin.setForeground(new Color(255,255,255));
        noSeguirWin.setBounds(800, 540, 89, 23);
        youWin.add(noSeguirWin);
        
        seguirJugandoWin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJuego();
            }
        });

        // Agregar ActionListener para el botón "No seguir"
        noSeguirWin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana y salir
                System.exit(0);
            }
        });
        
        pantallaCarga.setBackground(new Color(0, 0, 0));
        pantallaCarga.setBounds(0, 0, 1280, 720);
        add(pantallaCarga);
        pantallaCarga.setLayout(null);
        pantallaCarga.setVisible(false);
        
        progreso.setFont(new Font("Tahoma", Font.PLAIN, 15));
        progreso.setBounds(640, 600, 100, 100);
        progreso.setForeground(Color.WHITE);
        pantallaCarga.add(progreso);
        
     // Configuración de la barra de progreso
        barraProgreso.setBounds(160, 600, 960, 20);
        barraProgreso.setForeground(Color.WHITE); // Color de la barra (verde lima)
        barraProgreso.setBackground(Color.BLACK); // Color de fondo de la barra (gris oscuro)
        barraProgreso.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        pantallaCarga.add(barraProgreso);
        barraProgreso.setVisible(false);
        
        textoGameOver.setHorizontalAlignment(SwingConstants.CENTER);
        textoGameOver.setIcon(new ImageIcon(TableroGalaga.class.getResource("/images/gameover.png")));
        
        textoYouWin.setHorizontalAlignment(SwingConstants.CENTER);
        textoYouWin.setIcon(new ImageIcon(TableroGalaga.class.getResource("/images/WIN.png")));
        
        seguirJugando.setBackground(new Color(0, 0, 0));
        seguirJugando.setForeground(new Color(255, 255, 255));
        seguirJugando.setBounds(380, 540, 89, 23);
        gameOver.add(seguirJugando);
        
        noSeguir.setBackground(new Color(0,0,0));
        noSeguir.setForeground(new Color(255,255,255));
        noSeguir.setBounds(800, 540, 89, 23);
        gameOver.add(noSeguir);
        
     // Agregar ActionListener para el botón "Seguir jugando"
        seguirJugando.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJuego();
            }
        });

        // Agregar ActionListener para el botón "No seguir"
        noSeguir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana y salir
                System.exit(0);
            }
        });
        
        gifCarga.setHorizontalAlignment(SwingConstants.CENTER);
        gifCarga.setIcon(new ImageIcon(TableroGalaga.class.getResource("/images/loadscreen3.gif")));
        gifCarga.setBounds(0, 0, 1280, 650);
        pantallaCarga.add(gifCarga);
        
        // fixeo de lag
        cargarTexturas();
        cargarFramesFondo();  // Cargar las imágenes de la secuencia
        iniciarNivel();
        timer = new Timer(10, e -> actualizar());
        timer.start();
        reproducirAudio("/sound/fondo.wav");
    }
    
    // para fixear el consumo de memoria a lo loco
    private void cargarTexturas() {
        try {
            texturaJugador = ImageIO.read(getClass().getResource("/images/jugador.png"));
            texturaEnemigos = ImageIO.read(getClass().getResource("/images/enemigosL1.png"));
            texturaEnemigos2 = ImageIO.read(getClass().getResource("/images/enemigosL2.png"));
            texturaEnemigos3 = ImageIO.read(getClass().getResource("/images/enemigosL3.png"));
            preDisparo = ImageIO.read(getClass().getResource("/images/preDisparo.png"));
            postDisparo = ImageIO.read(getClass().getResource("/images/postDisparo.png"));
            if(nivel == 2)
            {
            Enemigos.aplicarTexturaEnemigos(texturaEnemigos2);  
            }
            else if (nivel == 3)
            {
                Enemigos.aplicarTexturaEnemigos(texturaEnemigos3);  
            }
            else
            {	 
            Enemigos.aplicarTexturaEnemigos(texturaEnemigos);       
            }
            texturaProyectil = ImageIO.read(getClass().getResource("/images/proyectil.png"));
            texturaBoss = ImageIO.read(getClass().getResource("/images/boss.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 // Cargar las imágenes de la secuencia
    private void cargarFramesFondo() {
    	
        try {
            framesFondo = new BufferedImage[30]; //
            for (int i = 0; i < framesFondo.length; i++) {
                framesFondo[i] = ImageIO.read(getClass().getResource("/images/images (" + i + ").png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void iniciarNivel() {
        int filas = 4;
        if (nivel <= 3) {
            filas = filas + nivel - 1;
        } else {
            filas = 6;
        }
        int columnas = 15;
        int cantidadEnemigos = filas * columnas;
        enemigos = new Enemigos[cantidadEnemigos];
        if (nivel == 5) {
            enemigosRestantes = cantidadEnemigos + 1;
        } else {
            enemigosRestantes = cantidadEnemigos;
        }
        double velocidadEnemigos = 0;

        if (nivel <= 2) {
            velocidadEnemigos = 2 + nivel * 2;
        } else {
            velocidadEnemigos = 1 + 3 * 2;
        }
        for (int x = 0; x < enemigos.length; x++) {
            int fila = x / columnas;
            int columna = x % columnas;
            enemigos[x] = new Enemigos(50 + columna * 50, 50 + fila * 50, velocidadEnemigos);
        }
        jugador.InicializarDisparos();

        // Detener todos los temporizadores antes de inicializarlos para evitar problemas.
        if (timerEnemigos != null) {
            timerEnemigos.stop();
            
        }
        if(nivel == 2)
        {
        	Enemigos.aplicarTexturaEnemigos(texturaEnemigos2);  
        }
        else if (nivel == 3)
        {
        	Enemigos.aplicarTexturaEnemigos(texturaEnemigos3);  
        }
        else
        {
        	Enemigos.aplicarTexturaEnemigos(texturaEnemigos);  
        }
        if (nivel >= 2) {
            if (timerEnemigos == null) {
                timerEnemigos = new Timer(3000, e -> {
                	
                    dispararEnemigos();
                    if(nivel >=3)
                    {
                    	kamikazeEnemigos();
                    	if(nivel >=4)
                    	{
                    		gasterEnemigos();
                    	}
                    }
                });
            }
            timerEnemigos.start();
            
        }

    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g3 = (Graphics2D) g; // jugador
        Graphics2D g4 = (Graphics2D) g; // enemigos
        Graphics2D g5 = (Graphics2D) g; // proyectiles
        Graphics2D g6 = (Graphics2D) g; // fondo
        Graphics2D g7 = (Graphics2D) g; // kamikazes
        Graphics2D g8 = (Graphics2D) g; // gaster
        Graphics2D g9 = (Graphics2D) g; // boss
     // Dibujar el fondo con la secuencia de imágenes
        if (framesFondo != null && framesFondo.length > 0) {
            BufferedImage fondoActual = framesFondo[currentFrame];
            g6.drawImage(fondoActual, 0, 0, getWidth(), getHeight(), null);
        }
        
        if (texturaJugador != null) {
            Rectangle2D rectJugador = new Rectangle2D.Double(jugador.getX(), jugador.getY(), 40, 40);
            TexturePaint paintJugador = new TexturePaint(texturaJugador, rectJugador);
            g3.setPaint(paintJugador);
            g3.fill(rectJugador);
        }

        for (int i = 0; i < jugador.disparadas; i++) {
            if (jugador.disparos[i] != null) {
            	TexturePaint paintDisparo = new TexturePaint(texturaProyectil, jugador.disparos[i]);
            	g5.setPaint(paintDisparo);
                g5.fill(jugador.disparos[i]);
            }
        }
        
        
        for (Enemigos enemigo : enemigos) {
            enemigo.dibujar(g4);
        }
        
    
        g5.setColor(Color.RED); 
        for (Rectangle2D disparo : disparosEnemigos) {
            g5.fill(disparo);
        }
        
        for (Rectangle2D kamikaze : kamikazeEnemigos) {
        	
        	if(nivel == 2)
        	{
        	TexturePaint paintDisparo = new TexturePaint(texturaEnemigos2, kamikaze);
        	g5.setPaint(paintDisparo);
            g7.fill(kamikaze);
        	}
        	else if(nivel == 3)
        	{
        	TexturePaint paintDisparo = new TexturePaint(texturaEnemigos3, kamikaze);
        	g5.setPaint(paintDisparo);
            g7.fill(kamikaze);
        	}
        	else
        	{
        		TexturePaint paintDisparo = new TexturePaint(texturaEnemigos, kamikaze);
            	g5.setPaint(paintDisparo);
                g7.fill(kamikaze);
        	}
		
        }
        g8.setColor(Color.RED); 
        for (Rectangle2D gaster : gasterEnemigos) {
            g8.fill(gaster);
        }
        if (nivel == 5 && boss != null && boss.esVisible()) {
            boss.dibujar(g9);
        }
    }

    public void actualizar() {
        currentFrame = (currentFrame + 1) % framesFondo.length;

        boolean cambiodireccion = false;
        
        for (Enemigos enemigo : enemigos) {
            if (enemigo.esVisible()) {
                if (direccionderecha && enemigo.getX() + enemigo.getWidth() >= getWidth()) {
                    cambiodireccion = true;
                    break;
                } else if (!direccionderecha && enemigo.getX() <= 0) {
                    cambiodireccion = true;
                    break;
                }
            }
        }

        if (cambiodireccion) {
            direccionderecha = !direccionderecha;
            cantToques++;

            if (cantToques >= 3) {
                for (Enemigos enemigo : enemigos) {
                   enemigo.bajarFila();
                }
                if (!gasterEnemigos.isEmpty()) {
                    for (Rectangle2D gaster : gasterEnemigos) {
                        gaster.setFrame(gaster.getX(), gaster.getY() + 32, gaster.getWidth(), gaster.getHeight());
                    }
                }
                cantBajadas++;
                cantToques = 0;
            }
        }

        if (direccionderecha) {
            for (Rectangle2D gaster : gasterEnemigos) {
                gaster.setFrame(gaster.getX() + 7, gaster.getY(), gaster.getWidth(), gaster.getHeight());
            }
        } else {
            for (Rectangle2D gaster : gasterEnemigos) {
                gaster.setFrame(gaster.getX() - 7, gaster.getY(), gaster.getWidth(), gaster.getHeight());
            }
        }
        if (nivel == 5 && boss != null) {
            boss.mover();
        }
        for (Enemigos enemigo : enemigos) {
            if (enemigo.esVisible()) {
                enemigo.mover(direccionderecha);

                if (enemigo.getY() + enemigo.getWidth() >= jugador.getY() && enemigo.esVisible()) {
                    reproducirAudio("/sound/perdido.wav");
                    Perdio();
                    return;
                }
            }
        }

        if (cantBajadas >= 4 && timerKamikaze != null) {
            timerKamikaze.stop();
        }

        for (int i = 0; i < jugador.disparadas; i++) {
            if (jugador.yDisparos[i] >= -20) {
                jugador.yDisparos[i] -= 6;
                jugador.disparos[i].setFrame(jugador.xDisparos[i], jugador.yDisparos[i], 5, 10);

                for (Enemigos enemigo : enemigos) {
                    if (enemigo.esVisible() && jugador.disparos[i].intersects(enemigo.getEnemigo()) && enemigosDisparando.contains(enemigo) == false) {
                        enemigo.Visibilizar(false);
                        reproducirAudio("/sound/eliminarenemigo.wav");
                        enemigosRestantes--;
                        eliminarBala(i);
                        break;
                    }
                }

            } else {
                eliminarBala(i);
            }
        }
        if (boss != null)
        {
        for (int i = 0; i < jugador.disparadas; i++) {
            if (boss.getBoss().intersects(jugador.disparos[i])) {
                reproducirAudio("/sound/eliminarenemigo.wav");
                vidaBoss--;
                eliminarBala(i);
                if (vidaBoss <= 0) {
                    enemigosRestantes = 0; 
                    boss.setVisible(false);
                    boss = null; 
                }
                break;
            }
        }
        }
        List<Rectangle2D> objetosAEliminar = new ArrayList<>();
        for (Rectangle2D disparo : disparosEnemigos) {
            disparo.setFrame(disparo.getX(), disparo.getY() + 4, disparo.getWidth(), disparo.getHeight());
            if (disparo.getY() > getHeight()) {
                objetosAEliminar.add(disparo);
            }
        }
        disparosEnemigos.removeAll(objetosAEliminar);

        actualizarMovimientoKamikaze();

        for (Rectangle2D disparo : disparosEnemigos) {
            if (disparo.intersects(jugador.getBounds())) {
                reproducirAudio("/sound/perdido.wav");
                Perdio();
                return;
            }
        }

        for (Rectangle2D kamikaze : kamikazeEnemigos) {
            if (kamikaze.intersects(jugador.getBounds())) {
                reproducirAudio("/sound/perdido.wav");
                Perdio();
                return;
            }
        }

        // Nueva sección: Colisión del jugador con gaster
        for (Rectangle2D gaster : gasterEnemigos) {
            if (gaster.intersects(jugador.getBounds())) {
                reproducirAudio("/sound/perdido.wav");
                Perdio();
                return;
            }
        }

        for (int i = 0; i < jugador.disparadas; i++) {
            for (Rectangle2D kamikaze : kamikazeEnemigos) {
                if (kamikaze.intersects(jugador.disparos[i])) {
                    reproducirAudio("/sound/eliminarenemigo.wav");
                    enemigosRestantes--;
                    kamikazeEnemigos.remove(kamikaze);
                    eliminarBala(i);
                    return;
                }
            }
        }

        jugador.mover(getBounds());
        jugador.actualizarCooldown(0.01);
        verificarProximoNivel();
        repaint();
    }

    private void eliminarBala(int index) {
        for (int i = index; i < jugador.disparadas - 1; i++) {
            jugador.xDisparos[i] = jugador.xDisparos[i + 1];
            jugador.yDisparos[i] = jugador.yDisparos[i + 1];
            jugador.disparos[i] = jugador.disparos[i + 1];
        }
        jugador.disparadas--;
        jugador.disparos[jugador.disparadas] = null;
    }

    private void verificarProximoNivel() {
    	if (enemigosRestantes == 0) {
            if (nivel < 5) {
                pantallaCarga.setVisible(true);
                barraProgreso.setVisible(true);
                barraProgreso.setValue(0);  // Reinicia la barra de progreso
                progreso.setText("0%");
                timer.stop();

                Timer cargaTimer = new Timer(50, null); 
                cargaTimer.addActionListener(e -> {
                    int progresoActual = barraProgreso.getValue();
                    if (progresoActual < 100) {
                        barraProgreso.setValue(progresoActual + 1);
                        progreso.setText(barraProgreso.getValue() + "%");
                    } else {
                        cargaTimer.stop(); 
                        pantallaCarga.setVisible(false); 
                        nivel++; 
                        iniciarNivel(); 
                        timer.start(); 
                    }
                });
                cargaTimer.start();

                //JOptionPane.showMessageDialog(this, "Nivel " + nivel + " superado.", "Avanzar de nivel", JOptionPane.INFORMATION_MESSAGE);
            } 
            
            else {
                Ganaste();
            }
        }
    	  else if (nivel == 5 && enemigosRestantes == 1 && boss == null)
          {
          	bossEnemigos();
          }

    
    }

    private void reiniciarJuego() {
        // Limpiar listas de disparos y enemigos
        kamikazeEnemigos.clear();
        disparosEnemigos.clear();
        gasterEnemigos.clear();

        // Reiniciar nivel y enemigos
        nivel = 1;
        enemigosRestantes = 0;
        cantToques = 0;
        cantBajadas = 1;

        // Reiniciar el boss
        boss = null;
        vidaBoss = 0;

        // Inicializar nivel
        iniciarNivel();

        // Reiniciar el temporizador
        if (timer != null) {
            timer.restart();
        }

        // Ocultar paneles de fin de juego
        gameOver.setVisible(false);
        youWin.setVisible(false);
    }

    
    private void Perdio() {
        timer.stop();
        gameOver.setVisible(true);
        //JOptionPane.showMessageDialog(this, "Perdiste sos malisimo.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Ganaste() {
        timer.stop();
        youWin.setVisible(true);
        if (timerEnemigos != null) {
            timerEnemigos.stop(); // Detener el temporizador de disparos de enemigos
        }
        if (timerKamikaze != null) {
            timerKamikaze.stop(); // Detener el temporizador de disparos de enemigos
        }
    }
    
    private void dispararEnemigos() {
        Random rand = new Random();
        for (Enemigos enemigo : enemigos) {
            if (enemigo.esVisible() && rand.nextDouble() < 0.012) { // 2% de probabilidad de disparar
                Rectangle2D disparo = new Rectangle2D.Double(enemigo.getX() + enemigo.getWidth() / 2 - 2, enemigo.getY() + enemigo.getHeight(), 4, 8);
                disparosEnemigos.add(disparo);
                
            }
        }
    }



    private void gasterEnemigos() {
        enemigosDisparando.clear(); // Limpiar la lista antes de agregar nuevos disparos
        List<Enemigos> enemigosUltimaFila = new ArrayList<>();
        int columnas = 15; // Número de columnas de enemigos

        // Obtener la última fila de enemigos visibles
        for (int i = enemigos.length - 1; i >= 0; i--) {
            if (i >= enemigos.length - columnas) { // Considerar solo la última fila
                if (enemigos[i].esVisible()) {
                    enemigosUltimaFila.add(enemigos[i]);
                }
            } 
        }

        // Seleccionar enemigos al azar de la última fila para disparar
        if (!enemigosUltimaFila.isEmpty()) {
            Random rand = new Random();
            int numDisparos = Math.min(1, enemigosUltimaFila.size()); // Número máximo de disparos
            for (int i = 0; i < numDisparos; i++) {
                Enemigos enemigo = enemigosUltimaFila.get(rand.nextInt(enemigosUltimaFila.size()));

                // Aplicar textura de predisparo
                enemigo.aplicarTexturaIndividual(preDisparo); 
                
                // Agregar el enemigo a la lista de disparos
                enemigosDisparando.add(enemigo);

                // Timer para cambiar la textura a postDisparo después de 300 milisegundos
                Timer cambiarTextura = new Timer(700, e -> {
                    if (enemigosDisparando.contains(enemigo)) {
                        enemigo.aplicarTexturaIndividual(postDisparo); // Aplicar textura de postdisparo
                        
                        // Crear y agregar el disparo a la lista
                        Rectangle2D gaster = new Rectangle2D.Double(enemigo.getX() + enemigo.getWidth() / 2 - 2, enemigo.getY() + enemigo.getHeight(), 4, 1238);
                        gasterEnemigos.add(gaster);

                        // Timer para eliminar el disparo y restaurar la textura después de 1500 milisegundos
                        Timer eliminarGaster = new Timer(1500, e1 -> {
                            if (gasterEnemigos.contains(gaster)) {
                                gasterEnemigos.remove(gaster); 
                                enemigo.aplicarTexturaIndividual(texturaEnemigos); // Restaurar la textura compartida
                                repaint(); // Refrescar la pantalla después de eliminar el disparo y restaurar la textura
                            }
                        });
                        eliminarGaster.setRepeats(false);
                        eliminarGaster.start(); 
                    }
                });
                cambiarTextura.setRepeats(false);
                cambiarTextura.start(); 
            }
        }
    }




    
    private void kamikazeEnemigos() {
        Random rand = new Random();
        for (Enemigos enemigo : enemigos) {
            if (enemigo.esVisible() && rand.nextDouble() < 0.010 && enemigosDisparando.contains(enemigo) == false) { // 1.5% de probabilidad de disparar
                Rectangle2D kamikaze = new Rectangle2D.Double(enemigo.getX(), enemigo.getY(), 32, 32);
                kamikazeEnemigos.add(kamikaze);
                enemigo.Visibilizar(false);
            }
        }
    }

    private void actualizarMovimientoKamikaze() {
        List<Rectangle2D> kamikazeAEliminar = new ArrayList<>();
        for (Rectangle2D kamikaze : kamikazeEnemigos) {
            double nuevaX = kamikaze.getX() + Math.sin(kamikaze.getY() * 0.05) * 7; 
            double nuevaY = kamikaze.getY() + 2; 

            

            if (kamikaze.getY() > getHeight()) {
            	if(kamikaze.getX() >= getWidth())
            	{
                kamikaze.setFrame(0, 0, kamikaze.getWidth(), kamikaze.getHeight());
            	}
            	else
            	{
            		kamikaze.setFrame(nuevaX, 0, kamikaze.getWidth(), kamikaze.getHeight());
            	}
            }
            else
            {
            	kamikaze.setFrame(nuevaX, nuevaY, kamikaze.getWidth(), kamikaze.getHeight());
            }
        }
        kamikazeEnemigos.removeAll(kamikazeAEliminar);
    }

    private void bossEnemigos()
    {
    	vidaBoss = 50;
    	boss = new Boss(320, 0, texturaBoss);
    	enemigosRestantes = 1;
    }
    
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();
        double velocidad = 20.0;

        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) {
            jugador.dibujar_en_x(-velocidad);
        }
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) {
            jugador.dibujar_en_x(velocidad);
        }
        if (tecla == KeyEvent.VK_SPACE || tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) {
            if (jugador.disparadas < 20 && jugador.puedeDisparar()) {
                reproducirAudio("/sound/disparo.wav");
                jugador.xDisparos[jugador.disparadas] = (int) (jugador.getX() + jugador.getWidth() / 2 + 2);
                jugador.yDisparos[jugador.disparadas] = (int) jugador.getY();
                jugador.disparos[jugador.disparadas] = new Rectangle2D.Double(jugador.xDisparos[jugador.disparadas], jugador.yDisparos[jugador.disparadas], 5, 10);
                jugador.disparadas++;
            }
        }
    }

    private void reproducirAudio(String nombreArchivo) {
        try {
        	// CARGA EL AUDIO
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(nombreArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

			FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        	control.setValue(-20f);
        } 
		catch (Exception e)
		{
			 e.printStackTrace(); 
		}
	}
    
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_RIGHT || tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_LEFT) {
            jugador.dibujar_en_x(0);
        }
    }

    public void keyTyped(KeyEvent e) {
    }
}
