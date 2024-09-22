import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

public class Enemigo {
    private float x, y;
    public float velocidadX = -2.0f;
    public float velocidadY = 0.0f;
    private final float ancho = 32.0f;
    private final float alto = 32.0f;
    private boolean eliminado = false;
    private String tipoEnemigo;
    private Map<String, ImageIcon> texturas;
    private int frameActual = 0;
    private long ultimoTiempoAnimacion;
    private long duracionFrame = 100; 
    private boolean salto = false;
 
    private boolean animacionActiva = false; 
    private String texturaIdle = "mimic-idle.png"; 
    
    public Enemigo(float x, float y, String tipoEnemigo, Map<String, ImageIcon> texturas) {
        this.x = x;
        this.y = y;
        this.tipoEnemigo = tipoEnemigo;
        this.texturas = texturas;
        this.ultimoTiempoAnimacion = System.currentTimeMillis();
        
    }

    public void actualizar(List<Bloque> bloques, List<Killblock> list, Jugador jugador) {
        if (!eliminado) {
            float distancia = distanciaConJugador(jugador); 

      
            if (distancia < 800) {
                velocidadY += 1.0f;  

                if (tipoEnemigo.equals("slime")) {
                    duracionFrame = 200;
                    if (salto && frameActual == 2) {
                        velocidadY -= 8f;
                        y += velocidadY;
                        salto = false;
                    } else {
                        velocidadY += 8f;
                        y += velocidadY;
                        if (frameActual == 1) {
                            x += velocidadX;
                        }
                        salto = true;
                    }

                } else if (tipoEnemigo.equals("skull")) {
                    duracionFrame = 300;
                    
                    x += velocidadX; 
                    manejarColisiones(bloques, true); 
                    
                    if (salto && frameActual == 1) {
                        velocidadY -= 8f;  
                        y += velocidadY;
                        salto = false;
                    }
                    else if (salto && frameActual == 2) {
                        velocidadY += 2f;  
                        y += velocidadY;
                        salto = false;
                    }
                    else {
                        velocidadY += 8f;  
                        y += velocidadY;
                        salto = true;
                    }

                } else if (tipoEnemigo.equals("mimic")) {
                    duracionFrame = 300;

                    if (distancia < 200) {  
                        animacionActiva = true;
                        x += velocidadX;
                        manejarColisiones(bloques, true);
                    } else {
                        animacionActiva = false;  
                    }
                } else {
                    x += velocidadX;
                    manejarColisiones(bloques, true); 
                }

                y += velocidadY;
                manejarColisiones(bloques, false);  
                verificarKillBlocks(list);    
                actualizarAnimacion();              
            }
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

    private void verificarKillBlocks(List<Killblock> kills) {
        Rectangle enemigoRect = new Rectangle((int)x, (int)y, (int)ancho, (int)alto);
        for (Killblock killblock : kills) {
            Rectangle killblockRect = new Rectangle((int)(killblock.x * 32), (int)(killblock.y * 32), 32, 32);
            if (enemigoRect.intersects(killblockRect)) {
                eliminar();
            }
        }
    }


    private void actualizarAnimacion() {
        long tiempoActual = System.currentTimeMillis();

        if (tipoEnemigo.equals("mimic") && !animacionActiva) {
          
            frameActual = 0;
        } else {
            
            if (tiempoActual - ultimoTiempoAnimacion >= duracionFrame) {
                frameActual = (frameActual + 1) % 3;  
                ultimoTiempoAnimacion = tiempoActual;
            }
        }
    }

    public void eliminar() {
        eliminado = true;
    }

    public void dibujar(Graphics g, Camara camara) {
        if (!eliminado) {
            String texturaClave;

            if (tipoEnemigo.equals("mimic") && !animacionActiva) {
               
                texturaClave = texturaIdle;
            } else {
                
                texturaClave = tipoEnemigo + frameActual + ".png";  
            }

            ImageIcon icono = texturas.get(texturaClave);
            if (icono != null) {
                Image imagen = icono.getImage();
                if (velocidadX < 0) {
                    g.drawImage(imagen, (int)(x - camara.getX()), (int)(y - camara.getY()), null);
                } else {
                   
                    AffineTransform at = AffineTransform.getTranslateInstance(x - camara.getX(), y - camara.getY());
                    at.scale(-1, 1);
                    at.translate(-imagen.getWidth(null), 0);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.drawImage(imagen, at, null);
                    g2d.dispose();
                }
            } else {
                g.setColor(Color.RED); 
                g.fillRect((int)(x - camara.getX()), (int)(y - camara.getY()), (int)ancho, (int)alto);
            }
        }
    }

    private float distanciaConJugador(Jugador jugador) {
        float dx = jugador.getX() - this.x;
        float dy = jugador.getY() - this.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    
    public void slow(boolean slow)
    {
	      	if(slow == true) 
	      	{	
	      		if(velocidadX < 0) 
	      		{
	      			velocidadX = -1.0f;
	      		}
	      		else if(velocidadX > 0) 
	      		{
	      			velocidadX = 1.0f;
	      		}
	        }
	      	else
	      	{
	      		if(velocidadX < 0)
	      		{
	      			velocidadX = -2.0f;
	      		}
	      		else if(velocidadX > 0)
	      		{
	      			velocidadX = 2.0f;
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

	public String getTipo() {
		return tipoEnemigo;
	}
}
