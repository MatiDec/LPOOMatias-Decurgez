import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class Jugador {
    public float x, y;
    public float velocidadX = 0;
    public float velocidadY = 0;
    private final float ancho = 32;
    private final float alto = 64;
    private boolean pisando = false;
    public int vidas = 3;  
    public boolean izquierdaPresionada = false;
    public boolean derechaPresionada = false;
    private Niveles niveles;  
    
    private BufferedImage idleSprite;
    private BufferedImage[] caminarSprites;
    private int animacionCaminarIndex = 0;
    private int contadorFrames = 0; 
    private final int framesPorSprite = 6;
    private boolean mirandoIzquierda = false;

    public boolean supersalto = false;
    public boolean slowmotion = false;
    public boolean pw_up_active = false;
    Random random = new Random();
    private int randomInt;
	private Timer timer = new Timer();
	public TimerTask slowMotion;
	private TimerTask superSalto;
	public boolean pause = false;
    public Jugador(float x, float y, Niveles niveles) {
        this.x = x;
        this.y = y;
        this.niveles = niveles;  
        
        cargarSprites();
    }
    
    private void cargarSprites() {
        try {
            
            idleSprite = ImageIO.read(getClass().getResource("/texturas/intermedio.png"));

            
            caminarSprites = new BufferedImage[3]; 
            caminarSprites[0] = ImageIO.read(getClass().getResource("/texturas/caminando1.png"));
            caminarSprites[1] = ImageIO.read(getClass().getResource("/texturas/intermedio.png"));
            caminarSprites[2] = ImageIO.read(getClass().getResource("/texturas/caminando2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar(List<Bloque> bloques, List<Enemigo> enemigos, List<Killblock> killBlocks, List<Bloque> piedrasCirculares, List<Bloque> aguas, Boss boss) {
    	if (izquierdaPresionada) {
    	    velocidadX = -5;
    	    mirandoIzquierda = true;  
    	} else if (derechaPresionada) {
    	    velocidadX = 5;
    	    mirandoIzquierda = false; 
    	} else {
    	    velocidadX = 0;
    	}
    	
    	 if(supersalto==true)
         {
         	velocidadY += 0.6f;	
         }
         else
         {
         	velocidadY += 1;	
         }
         
         if(slowmotion == true)
         {
         	for(Enemigo enemigo : enemigos)
         	{
         		enemigo.slow(true);
         	}
         }

        x += velocidadX;
        manejarColisiones(bloques, true);

        y += velocidadY;
        manejarColisiones(bloques, false);

        verificarColisionConEnemigos(enemigos);
        verificarColisionConPiedras(piedrasCirculares);
        verificarColisionConBoss(boss); 
        verificarColisionConAgua(aguas);
        
        actualizarAnimacion();
    }


    private void manejarColisiones(List<Bloque> bloques, boolean esHorizontal) {
        Rectangle2D.Float jugadorRect = new Rectangle2D.Float(x, y, ancho, alto);
        pisando = false;

        for (Bloque bloque : bloques) {
            Rectangle2D.Float bloqueRect = new Rectangle2D.Float(bloque.x * 32, bloque.y * 32, 32, 32);

            if (jugadorRect.intersects(bloqueRect)) {
                if (esHorizontal) {

                    if (velocidadX > 0) { 
                        x = bloqueRect.x - ancho;
                    } else if (velocidadX < 0) { 
                        x = bloqueRect.x + bloqueRect.width;
                    }
                } else {

                    if (velocidadY > 0) {
                        y = bloqueRect.y - alto;
                        velocidadY = 0;
                        pisando = true; 
                    } else if (velocidadY < 0) { 
                        y = bloqueRect.y + bloqueRect.height;
                        velocidadY = 0;
                    }
                }
                jugadorRect.setRect(x, y, ancho, alto);
            }
        }
    }
    
    private void actualizarAnimacion() {
        contadorFrames++;
        if (contadorFrames >= framesPorSprite) {
            contadorFrames = 0;
            animacionCaminarIndex++;
            if (animacionCaminarIndex >= caminarSprites.length) {
                animacionCaminarIndex = 0;
            }
        }
    }
    
    private void verificarColisionConEnemigos(List<Enemigo> enemigos) {
        Rectangle2D.Float jugadorRect = new Rectangle2D.Float(x, y, ancho, alto);


        List<Enemigo> copiaEnemigos = new ArrayList<>(enemigos);

        for (Enemigo enemigo : copiaEnemigos) {
            if (!enemigo.estaEliminado()) {
                Rectangle2D.Float enemigoRect = new Rectangle2D.Float(enemigo.getX(), enemigo.getY(), enemigo.getAncho(), enemigo.getAlto());

                if (jugadorRect.intersects(enemigoRect)) {

                    if (y + alto <= enemigo.getY() + enemigo.getAlto() / 2) {
                        enemigos.remove(enemigo); 
                        randomInt = random.nextInt(7); 
                        // Posibles valores: 0,1,2;
                        //Posibilidad de 0,33 de no obtener un power up al matar un enemigo (se puede cambiar);
                    
                        
                        if(pw_up_active == false)
                        {
	                        //El jugador solo va a poder tener 1 efecto activo al mismo tiempo
	                        if(randomInt == 2) // Super salto solo se va a activar si sale el numero 2
	                        {
	                        	desactivarPowerUps(enemigos);
	                        	
	                        	supersalto = true;
	                        	slowmotion = false;
	                        	pw_up_active = true;
	                        	
	                        	superSalto = new TimerTask() // Timer para sacar el power up, "Tiempo de 20 segundos" (cambiable)
	                        	{
	                                @Override
	                                public void run() {
	                                	supersalto = false;
	    	                        	pw_up_active = false;

	                                }
	                            };
	                        	timer.schedule(superSalto, 20000);
	                        }
	                        
	                     // Slow motion solo se va a dar al jugador cuando el numero random al matar a un enemigo sea = 1
	                        else if(randomInt == 1)
	                        {
	                        	
	                        	desactivarPowerUps(enemigos);
	                        	pw_up_active = true;
	                        	slowmotion = true;
	                        	supersalto = false;
	                        	
	                        	slowMotion = new TimerTask() // Timer para sacar el power up, "Tiempo de 20 segundos" (cambiable)
	                        	{
	                                @Override
	                                public void run() {
	                                	slowmotion = false;
	                                	pw_up_active = false;
	                                	for(Enemigo enemigo : enemigos)
	                                	{
	                                		enemigo.slow(false); 
	                                	}
	                                }
	                            };
	                        	
	                        	timer.schedule(slowMotion, 20000);
	                        }
                        
	                        //si sale 0 no se da power up;
                        }
                        
                    } else {
                        perderVida(); 
                    }
                }
            }
        }
    }
    
    public void detenerMovimiento() {
        this.izquierdaPresionada = false;
        this.derechaPresionada = false;
        this.velocidadX = 0;
    }

    public void reiniciar() {
        this.x = 50;
        this.y = 50;
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.vidas = 3;
        this.supersalto = false;
        this.slowmotion = false;
        this.pw_up_active = false;

        this.izquierdaPresionada = false;
        this.derechaPresionada = false;

        if (slowMotion != null) {
            slowMotion.cancel();
            slowMotion = null;
        }
        if (superSalto != null) {
            superSalto.cancel();
            superSalto = null;
        }

    }


    
    private void desactivarPowerUps(List<Enemigo> enemigos) {
        if (slowmotion) {
            slowmotion = false;
            if (slowMotion != null) {
                slowMotion.cancel();
            }
            for(Enemigo enemigo : enemigos) {
                enemigo.slow(false);
            }
        }

        if (supersalto) {
            supersalto = false;
            if (superSalto != null) {
                superSalto.cancel();
            }
        }
    }


    private void verificarColisionConBoss(Boss boss) {
        if (boss != null) {
            Rectangle2D.Float jugadorRect = new Rectangle2D.Float(x, y, ancho, alto);
            Rectangle2D.Float bossRect = new Rectangle2D.Float(boss.getX(), boss.getY(), boss.getAncho(), boss.getAlto());

            if (jugadorRect.intersects(bossRect)) {
             
                if (y + alto <= boss.getY() + boss.getAlto() / 2 && velocidadY > 0) {
                    boss.recibirDanio(1);
                    velocidadY = -15; 
                    pisando = true; 
                } else {
                    
                    if (velocidadY >= 0) {
                        perderVida(); 
                    }
                }
            }
        }
    }





   
    
    private boolean en_agua = false;
    private void verificarColisionConAgua(List<Bloque> aguas) {
        Rectangle2D.Float jugadorRect = new Rectangle2D.Float(x, y, ancho, alto);

        for (Bloque agua : aguas) {
            Rectangle2D.Float aguaRect = new Rectangle2D.Float(agua.x* 32, agua.y * 32, 32, 32);

            if (jugadorRect.intersects(aguaRect) && en_agua == false) {
            	
                velocidadX *= 0.5;  
                velocidadY *= 0.85;  
                en_agua = true;
            }
            else
            {
            	en_agua = false;
            }
        }
    }


    private void verificarColisionConPiedras(List<Bloque> piedrasCirculares) {
        Rectangle2D.Float jugadorRect = new Rectangle2D.Float(x, y, ancho, alto);


        List<Bloque> copiaPiedras = new ArrayList<>(piedrasCirculares);

        for (Bloque piedra : copiaPiedras) {
            Rectangle2D.Float piedraRect = new Rectangle2D.Float(piedra.x * 32, piedra.y * 32, 32, 32);

            if (jugadorRect.intersects(piedraRect)) {
                perderVida(); 
            }
        }
    }
 


    public void perderVida() {
        vidas--;
        if (vidas <= 0) {
           
        } else {
            niveles.reiniciarElementos();  
            

            Bloque spawnBlock = niveles.getSpawnBlock();
            if (spawnBlock != null) {
                x = spawnBlock.x * 32;  
                y = spawnBlock.y * 32;
            }
            else
            {
            	System.err.print("No se encontro el spawnblock");
            	x = 50;
            	y = 50;
            }
            velocidadY = 0; 
        }
    }


    public void dibujar(Graphics g, Camara camara) {
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage spriteActual;


        if (velocidadX == 0) {
            spriteActual = idleSprite;  
        } else {
            spriteActual = caminarSprites[animacionCaminarIndex];  
        }

        g2d.translate((int)(x - camara.getX()), (int)(y - camara.getY()));

        if (mirandoIzquierda) {
            g2d.scale(-1, 1);  
            g2d.drawImage(spriteActual, -(int) ancho, 0, (int) ancho, (int) alto, null);  //para dibujarlo invertido
        } else {
            g2d.drawImage(spriteActual, 0, 0, (int) ancho, (int) alto, null);
        }


        g2d.setTransform(new AffineTransform()); // invertir el sprite al original
    }


    public KeyAdapter getControladorTeclado() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                        izquierdaPresionada = true;

                    }
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                        derechaPresionada = true;

                    }
                    case KeyEvent.VK_SPACE, KeyEvent.VK_W, KeyEvent.VK_UP -> {
                        if (pisando) {
                            velocidadY = -15;

                        }
                    }
                    case KeyEvent.VK_ESCAPE -> {
                pause = true; 
                }
            }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                        izquierdaPresionada = false;

                    }
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                        derechaPresionada = false;

                    }
                }
            }
        };
    }




    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }

    public int getVidas() {
        return vidas;
    }
    
    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(x, y, ancho, alto);
    }

	public void setY(float y2) {
		y = y2;
		
	}

	public void setX(float x2) {
		x = x2;
	}
	
    public float getVelocidadX() {
        return velocidadX;
    }

    public void setVelocidadX(float velocidadX) {
        this.velocidadX = velocidadX;
    }

	public void setVelocidadY(float f) {
		this.velocidadY = f;
		
	}
}
