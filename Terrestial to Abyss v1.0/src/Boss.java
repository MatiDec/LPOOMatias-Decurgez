import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Boss {
    private float x, y;
    private float velocidadX = -2.0f;
    private float velocidadY = 0.0f;
    private float ancho = 128.0f;
    private float alto = 64.0f;
    private boolean eliminado = false;
    private int vida = 6; 
    private long tiempoCambioDireccion = System.currentTimeMillis();
    private final long intervaloCambioDireccion = 2000; 
    private long tiempoEmbestida = System.currentTimeMillis();
    private final long intervaloEmbestida = 10000; 
    private long tiempoSalto = System.currentTimeMillis();
    private final long intervaloSalto = 5000; 
    private final Random random = new Random();
    private long tiempoInvocacion = System.currentTimeMillis();
    private final long intervaloInvocacion = 19000; 
    private List<Proyectil> proyectiles = new ArrayList<>();
    private long tiempoDisparo = System.currentTimeMillis();
    private final long intervaloDisparo = 16000; 
    private boolean embistiendo = false;
    private boolean mirandoALaDerecha = false;
    private BufferedImage[] sprites;
    private BufferedImage[] spritesPreEmbestida;
    private BufferedImage[] spritesSalto; 
    private int animacionIndex = 0;
    private int contadorFrames = 0;
    private final int framesPorSprite = 6;
    private long tiempoInicioPreAtaque = 0;
    private boolean preAtaqueIniciado = false;
    

    private Map<String, ImageIcon> texturas;

    private enum EstadoBoss {
        CAMINANDO, PRE_ATAQUE, ATACANDO, SALTANDO
    }
    
    private EstadoBoss estadoActual = EstadoBoss.CAMINANDO; 
    
    public Boss(float x, float y, Map<String, ImageIcon> texturas) {
        this.x = x;
        this.y = y;
        this.texturas = texturas;
        this.ancho = 128.0f; 
        this.alto = 64.0f;   
        cargarSprites();
    }
   
    private void cargarSprites() {
        sprites = new BufferedImage[4]; 
        spritesPreEmbestida = new BufferedImage[2]; 
        spritesSalto = new BufferedImage[3];
        
        String[] spriteNombres = {"boss-walk1.png", "boss-walk2.png", "boss-walk3.png", "boss-walk4.png"};
        String[] preEmbestidaNombres = {"boss-preattack.png", "boss-attack.png"};
        String[] saltoNombres = {"boss-prejump.png", "boss-jump.png", "boss-postjump.png"};
        
        for (int i = 0; i < spriteNombres.length; i++) {
            BufferedImage originalSprite = cargarImagenDirectamente(spriteNombres[i]);
            if (originalSprite != null) {
                sprites[i] = escalarImagenPixelPerfect(originalSprite, (int) ancho, (int) alto);

            } else {
                System.err.println("No se pudo cargar el sprite: " + spriteNombres[i]);
            }
        }
        


        for (int i = 0; i < saltoNombres.length; i++) {
            BufferedImage originalSprite = cargarImagenDirectamente(saltoNombres[i]);
            if (originalSprite != null) {
                spritesSalto[i] = escalarImagenPixelPerfect(originalSprite, (int) ancho, (int) alto);

            }
        }
        
        for (int i = 0; i < preEmbestidaNombres.length; i++) {
            BufferedImage originalSprite = cargarImagenDirectamente(preEmbestidaNombres[i]);
            if (originalSprite != null) {
                spritesPreEmbestida[i] = escalarImagenPixelPerfect(originalSprite, (int) ancho, (int) alto);

            } else {
                System.err.println("No se pudo cargar el sprite: " + preEmbestidaNombres[i]);
            }
        }
    }

    private BufferedImage voltearImagen(BufferedImage imagen) {
        BufferedImage imagenVolteada = new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenVolteada.createGraphics();
        g2d.drawImage(imagen, 0, 0, imagen.getWidth(), imagen.getHeight(), imagen.getWidth(), 0, 0, imagen.getHeight(), null);
        g2d.dispose();
        return imagenVolteada;
    }
    
    private BufferedImage cargarImagenDirectamente(String nombreArchivo) {
        try {
            File file = new File("src/Texturas/" + nombreArchivo);
            return ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + nombreArchivo);
            e.printStackTrace();
            return null;
        }
    }
    
    private BufferedImage escalarImagenPixelPerfect(BufferedImage original, int nuevoAncho, int nuevoAlto) {
        BufferedImage escalada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = escalada.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(original, 0, 0, nuevoAncho, nuevoAlto, null);
        g2d.dispose();
        return escalada;
    }

 

    private void actualizarDireccionJugador(Jugador jugador) {
        if (jugador.getX() < this.x) {
            mirandoALaDerecha = false; 
        } else if (jugador.getX() > this.x) {
            mirandoALaDerecha = true;  
        } else if (jugador.getY() < this.y) {
            mirandoALaDerecha = false; 
        }
    }
    
    public void actualizar(List<Bloque> bloques, List<Killblock> list, List<Enemigo> enemigos, Jugador jugador) {
        if (!eliminado) {
            if (estadoActual == EstadoBoss.CAMINANDO) {
                cambiarDireccionAleatoria();
            }

            invocarEnemigos(enemigos);
            disparar(jugador);
            realizarEmbestida();
            realizarSalto();

            velocidadY += 1.0f;
            x += velocidadX;
            manejarColisiones(bloques, true);
            y += velocidadY;
            manejarColisiones(bloques, false);
            verificarKillBlocks(list);

            for (Proyectil proyectil : proyectiles) {
                proyectil.actualizar();
            }

            verificarColisionesConJugador(jugador);
            actualizarAnimacion(); 


            actualizarDireccionJugador(jugador);
        }
    }


    private void actualizarAnimacion() {
        contadorFrames++;


        if (estadoActual == EstadoBoss.SALTANDO) {
            if (contadorFrames >= framesPorSprite) {
                contadorFrames = 0;
                animacionIndex = Math.min(animacionIndex, spritesSalto.length - 1);
            }
        } else {
 
            if (contadorFrames >= framesPorSprite) {
                contadorFrames = 0;
                animacionIndex++;
                if (animacionIndex >= sprites.length) {
                    animacionIndex = 0;
                }
            }
        }
    }

    
    private void realizarEmbestida() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoEmbestida > intervaloEmbestida && estadoActual == EstadoBoss.CAMINANDO) {
            estadoActual = EstadoBoss.PRE_ATAQUE; 
            embistiendo = true;
            tiempoEmbestida = tiempoActual;

            new Thread(() -> {
                try {
                    Thread.sleep(framesPorSprite * 2 * 100); 
                    velocidadX = (random.nextBoolean() ? 5 : -5);
                    embistiendo = false;
                    estadoActual = EstadoBoss.CAMINANDO;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    private void realizarSalto() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoSalto > intervaloSalto && estadoActual == EstadoBoss.CAMINANDO) {
            estadoActual = EstadoBoss.SALTANDO;
            animacionIndex = 0;
            tiempoSalto = tiempoActual;
        }

        if (estadoActual == EstadoBoss.SALTANDO) {
            if (animacionIndex == 0 && tiempoActual - tiempoSalto > 500) {
                animacionIndex = 1;
                velocidadY = -18.0f;  
            }

            if (animacionIndex == 1 && velocidadY > 0) {
                animacionIndex = 2;
            }

            if (animacionIndex == 2 && velocidadY == 0) {
                estadoActual = EstadoBoss.CAMINANDO;
            }
        }
    }


    private void verificarColisionesConJugador(Jugador jugador) {
        for (Proyectil proyectil : proyectiles) {
            if (proyectil.getBounds().intersects(jugador.getBounds())) {
                jugador.perderVida(); 
                proyectil.eliminar(); 
            }
        }
        proyectiles.removeIf(Proyectil::estaEliminado);
    }

    private void disparar(Jugador jugador) {
        long tiempoActual = System.currentTimeMillis();
        float distanciaMinima = 10.0f; 


        float distanciaX = jugador.getX() - x;
        float distanciaY = jugador.getY() - y;
        float distancia = (float) Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);


        if (distancia > distanciaMinima && tiempoActual - tiempoDisparo > intervaloDisparo) {

            float direccionX = distanciaX / distancia;
            float direccionY = distanciaY / distancia;

            proyectiles.add(new Proyectil(x + ancho / 2, y + alto / 2, direccionX, direccionY));
            tiempoDisparo = tiempoActual; 
        }
    }


    private void invocarEnemigos(List<Enemigo> enemigos) {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoInvocacion > intervaloInvocacion) {
            float xEnemigo = x + ancho / 2;  
            float yEnemigo = y + alto / 2;
            enemigos.add(new Enemigo(xEnemigo, yEnemigo, "espectro", texturas)); 
            tiempoInvocacion = tiempoActual;
        }
    }

    private void cambiarDireccionAleatoria() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoCambioDireccion > intervaloCambioDireccion) {
            if (random.nextBoolean()) {
                velocidadX = random.nextFloat() * 4 - 2;
                mirandoALaDerecha = velocidadX > 0;
            } else {
                velocidadY = random.nextFloat() * 4 - 2;
            }
            tiempoCambioDireccion = tiempoActual;
        }
    }

    private void manejarColisiones(List<Bloque> bloques, boolean esHorizontal) {
        Rectangle enemigoRect = new Rectangle((int)x, (int)y, (int)ancho, (int)alto);

        for (Bloque bloque : bloques) {
            Rectangle bloqueRect = new Rectangle((int)(bloque.x * 32), (int)(bloque.y * 32), 32, 32);

            if (enemigoRect.intersects(bloqueRect)) {
                if (esHorizontal) {
                 
                    if (velocidadX > 0) { 
                        x = bloqueRect.x - ancho; 
                    } else if (velocidadX < 0) { 
                        x = bloqueRect.x + bloqueRect.width; 
                    }
                    velocidadX = 0; 
                } else {
                    
                    if (velocidadY > 0) { 
                        y = bloqueRect.y - alto; 
                        velocidadY = 0; 
                    } else if (velocidadY < 0) { 
                        y = bloqueRect.y + bloqueRect.height; 
                        velocidadY = 0; 
                    }
                }
            }
        }
    }


    private void verificarKillBlocks(List<Killblock> list) {
        Rectangle enemigoRect = new Rectangle((int)x, (int)y, (int)ancho, (int)alto);

        for (Killblock killblock : list) {
            Rectangle killblockRect = new Rectangle((int)(killblock.x * 32), (int)(killblock.y * 32), 32, 32);

            if (enemigoRect.intersects(killblockRect)) {
                eliminar();
            }
        }
    }

    public void recibirDanio(int danio) {
        if (!eliminado) { 
            vida -= danio;
            if (vida <= 0) {
                eliminar();
            }
        }
    }

    public void eliminar() {
        if (!eliminado) {
            eliminado = true;
        }
    }

    public void dibujar(Graphics g, Camara camara) {
        if (!eliminado) {
            int drawX = (int) (x - camara.getX());
            int drawY = (int) (y - camara.getY());
            BufferedImage spriteActual;

            if (estadoActual == EstadoBoss.SALTANDO) {
                spriteActual = spritesSalto[animacionIndex];
            } else {
                spriteActual = sprites[animacionIndex];
            }

            if (mirandoALaDerecha) {
                spriteActual = voltearImagen(spriteActual);
            }

            g.drawImage(spriteActual, drawX, drawY, null);

            for (Proyectil proyectil : proyectiles) {
                proyectil.dibujar(g, camara);
            }
        }
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

    public boolean estaEliminado() {
        return eliminado;
    }

    public int getVida() {
        return vida;
    }


}
