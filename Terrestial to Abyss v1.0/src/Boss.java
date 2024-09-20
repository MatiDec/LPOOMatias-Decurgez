import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import java.util.Random;

public class Boss {
    private float x, y;
    private float velocidadX = -2.0f;
    private float velocidadY = 0.0f;
    private final float ancho = 128.0f;
    private final float alto = 64.0f;
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
    private final long intervaloDisparo = 6000; 
    private BufferedImage[] caminarSprites;
    private Map<String, ImageIcon> texturas;
    private int frameActual = 0; 
    private long tiempoCambioFrame = System.currentTimeMillis();
    private final long intervaloCambioFrame = 200;
    private boolean mirrorImage = true;
    
    public Boss(float x, float y, Map<String, ImageIcon> texturas) {
        this.x = x;
        this.y = y;
        this.texturas = texturas;

        caminarSprites = new BufferedImage[2]; 
        caminarSprites[0] = scaleImage(convertirABufferedImage(texturas.get("boss-caminando1.png").getImage()), (int)ancho, (int)alto);
        caminarSprites[1] = scaleImage(convertirABufferedImage(texturas.get("boss-caminando2.png").getImage()), (int)ancho, (int)alto);
        mirrorImage = true; 
    }

    private BufferedImage convertirABufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGraphics = bImage.createGraphics();
        bGraphics.drawImage(img, 0, 0, null);
        bGraphics.dispose();

        return bImage;
    }

    private BufferedImage scaleImage(BufferedImage img, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(img, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }

    public void actualizar(List<Bloque> bloques, List<Killblock> list, List<Enemigo> enemigos, Jugador jugador) {
        if (!eliminado) {
            cambiarDireccionAleatoria();
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
        }
    }

    private void actualizarAnimacion() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoCambioFrame > intervaloCambioFrame) {
            frameActual = (frameActual + 1) % caminarSprites.length;
            tiempoCambioFrame = tiempoActual;
        }
    }
    
    private void realizarEmbestida() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoEmbestida > intervaloEmbestida) {
            velocidadX = (random.nextBoolean() ? 5 : -5); 
            tiempoEmbestida = tiempoActual;
        }
    }

    private void realizarSalto() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoSalto > intervaloSalto) {
            velocidadY = -10.0f; 
            tiempoSalto = tiempoActual;
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
        if (tiempoActual - tiempoDisparo > intervaloDisparo) {
            float direccionX = jugador.getX() - x;
            float direccionY = jugador.getY() - y;
            float longitud = (float) Math.sqrt(direccionX * direccionX + direccionY * direccionY);
            direccionX /= longitud;
            direccionY /= longitud;

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
            } else {
                velocidadY = random.nextFloat() * 4 - 2; 
            }
            mirrorImage = (velocidadX > 0); 
            tiempoCambioDireccion = tiempoActual;
        }
    }

    private void manejarColisiones(List<Bloque> bloques, boolean esHorizontal) {
        Rectangle enemigoRect = new Rectangle((int)x, (int)y, (int)ancho, (int)alto);

        for (Bloque bloque : bloques) {
            Rectangle bloqueRect = new Rectangle((int)(bloque.x * 32), (int)(bloque.y * 32), 32, 32);

            if (enemigoRect.intersects(bloqueRect)) {
                if (esHorizontal) {
                    velocidadX = -velocidadX;
                    x += velocidadX;
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

    public void dibujar(Graphics g, Camara camara, Map<String, ImageIcon> texturas2) {
        if (!eliminado) {
            Graphics2D g2d = (Graphics2D) g;
            BufferedImage spriteParaDibujar = (velocidadX != 0) ? caminarSprites[frameActual] : scaleImage(convertirABufferedImage(texturas.get("boss.png").getImage()), (int)ancho, (int)alto);

            int drawX = (int)(x - camara.getX());
            int drawY = (int)(y - camara.getY());

            if (mirrorImage) {
                g2d.translate(drawX + ancho, drawY);
                g2d.scale(-1, 1);
                g2d.drawImage(spriteParaDibujar, 0, 0, null);
                g2d.scale(-1, 1);
                g2d.translate(-(drawX + ancho), -drawY);
            } else {
                g2d.drawImage(spriteParaDibujar, drawX, drawY, null);
            }
        }

        for (Proyectil proyectil : proyectiles) {
            proyectil.dibujar(g, camara);
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