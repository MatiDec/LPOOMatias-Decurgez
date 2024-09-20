import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import java.util.concurrent.CopyOnWriteArrayList;
public class Niveles {
	
	
	

	
	private CopyOnWriteArrayList<Bloque> areasVictoria = new CopyOnWriteArrayList<>();

    private List<Bloque> bloques = new ArrayList<>();
    private List<Bloque> bloquesSinColision = new ArrayList<>();
    private List<Killblock> killBlocks = new ArrayList<>();
    private List<Spike> spikes = new ArrayList<>();
    private List<Bloque> estalactitas = new ArrayList<>();
    private List<Bloque> piedrasCirculares = new ArrayList<>();
    private List<Enemigo> enemigos = new ArrayList<>();
    private Map<String, ImageIcon> texturas;
    private List<String> texturasSinColision;
    public int nivelactual = 1;
    public Boss boss;
    private List<Bloque> estalactitasOriginales = new ArrayList<>();
    private List<Bloque> piedrasCircularesOriginales = new ArrayList<>();
    private List<Enemigo> enemigosOriginales = new ArrayList<>();
    private Bloque spawnBlock;
    private List<Bloque> bloquesAgua = new ArrayList<>();
    private List<Lava> lavaBloques = new ArrayList<>();
    private Ventana ventana;
    private Clip clip;
    
    
    public Niveles(String nombreArchivo) {
        texturasSinColision = List.of("lava-top.png", "troncoarbol-var1.png", "troncoarbol-var2.png", "troncoarbol-var3.png", "hojas-lados.png", "hojas-relleno.png", "cartel-sup.png", "cartel-inf.png", "concreto-var1.png", "concreto-var2.png", "concreto-var3.png", "cristal-cueva-var1.png", "cristal-cueva-var2.png", "cristal-cueva-var3.png", "cumulopolvo-var1.png", "cumulopolvo-var2.png", "cumulopolvo-var3.png", "enredadera-var1.png", "enredadera-var2.png", "hojas-lados.png", "hojas-relleno.png", "hongo-var1.png", "hongo-var2.png", "hongo-var3.png", "palo.png", "palopodrido.png", "papel-roto-var1.png", "papel-roto-var2.png", "papel-roto-var3.png", "piedrarota-var1.png", "piedrarota-var2.png", "piedrarota-var3.png", "puerta-rota.png", "rail.png", "troncoarbol-var1.png", "troncoarbol-var2.png", "troncoarbol-var3.png", "troncocortado-var1.png", "troncocortado-var2.png", "troncoenredadera-var1.png", "troncoenredadera-var2.png", "troncoraiz.png", "viga-var1.png", "viga-var2.png" );
        texturas = cargarTexturas("src/Texturas");
        cargarNivel(nombreArchivo);
        guardarPosicionesOriginales();
    }

    public void cambiarNivel(String nuevoNombreArchivo) {
        bloques.clear();
        bloquesAgua.clear();
        bloquesSinColision.clear();
        killBlocks.clear();
        spikes.clear();
        spawnBlock = null;
        estalactitas.clear();
        piedrasCirculares.clear();
        enemigos.clear();
        lavaBloques.clear();
        System.out.println("Cambiando de nivel...");
        cargarNivel(nuevoNombreArchivo);
        guardarPosicionesOriginales();
        spawnBlock = getSpawnBlock();
   
    }

    private Map<String, ImageIcon> cargarTexturas(String rutaCarpeta) {
        Map<String, ImageIcon> texturas = new HashMap<>();
        try {
            Files.walk(Paths.get(rutaCarpeta)).forEach(rutaArchivo -> {
                if (Files.isRegularFile(rutaArchivo)) {
                    try {
                        String nombreTextura = rutaArchivo.getFileName().toString().toLowerCase();
                        BufferedImage imagenTextura = ImageIO.read(rutaArchivo.toFile());
                        if (imagenTextura != null) {
                            ImageIcon iconoImagen = new ImageIcon(imagenTextura.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                            texturas.put(nombreTextura, iconoImagen);
                        } else {
                            System.err.println("Error al cargar la imagen: " + rutaArchivo.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texturas;
    }



    private void cargarNivel(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                float x = Float.parseFloat(partes[0]);
                float y = Float.parseFloat(partes[1]);
                String textura = partes[2].toLowerCase();

                if (textura.startsWith("spike")) {
                    Spike spike = new Spike(x, y, textura);
                    spikes.add(spike);
                } else {
                    Bloque bloque = new Bloque(x, y, textura);

                    if (textura.startsWith("spawnblock")) {
                        spawnBlock = bloque;
                    } else if (textura.startsWith("killblock")) {
                    	Killblock killblock = new Killblock(x, y, textura);
                    	killBlocks.add(killblock);

                    } else if (textura.startsWith("spawnenemigo")) {
                        String tipoEnemigo = textura.substring("spawnenemigo".length()).toLowerCase();
                        tipoEnemigo = tipoEnemigo.replace(".png", "");  // bug que hacia que mandara un .png extra
                        Enemigo enemigo = new Enemigo((int) (x * 32), (int) (y * 32), tipoEnemigo, texturas);
                        enemigos.add(enemigo);
                    } else if (textura.startsWith("estalactita")) {
                        estalactitas.add(bloque);
                    } else if (textura.startsWith("piedra_circular")) {
                        piedrasCirculares.add(bloque);
                    } else if (textura.startsWith("area-victoria")) {
                        areasVictoria.add(bloque);
                    } else if (textura.startsWith("spawnboss")) {
                        boss = new Boss((x * 32), (y * 32), texturas);
                    } else if (textura.startsWith("agua")) {
                        bloquesAgua.add(bloque);
                        
                    } else if (textura.startsWith("lava-relleno")) {
                            
                            Lava lava = new Lava(x, y, textura);
                            lavaBloques.add(lava);
                        
                    } else {
                        if (texturasSinColision.contains(textura)) {
                            bloquesSinColision.add(bloque);
                        } else {
                            bloques.add(bloque);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void guardarPosicionesOriginales() {
        estalactitasOriginales.clear();
        piedrasCircularesOriginales.clear();
        enemigosOriginales.clear();

        
        for (Bloque estalactita : estalactitas) {
            estalactitasOriginales.add(new Bloque(estalactita.x, estalactita.y, estalactita.Textura));
        }

        for (Bloque piedra : piedrasCirculares) {
            piedrasCircularesOriginales.add(new Bloque(piedra.x, piedra.y, piedra.Textura));
        }

        for (Enemigo enemigo : enemigos) {
            Enemigo enemigoOriginal = new Enemigo(enemigo.getX(), enemigo.getY(), enemigo.getTipo(), texturas);
            enemigosOriginales.add(enemigoOriginal);
        }
    }

    public void reiniciarElementos() {
        estalactitas.clear();
        for (Bloque estalactitaOriginal : estalactitasOriginales) {
            estalactitas.add(new Bloque(estalactitaOriginal.x, estalactitaOriginal.y, estalactitaOriginal.Textura));
        }

        piedrasCirculares.clear();
        for (Bloque piedraOriginal : piedrasCircularesOriginales) {
            piedrasCirculares.add(new Bloque(piedraOriginal.x, piedraOriginal.y, piedraOriginal.Textura));
        }

        enemigos.clear();
        for (Enemigo enemigoOriginal : enemigosOriginales) {
            Enemigo enemigo = new Enemigo(enemigoOriginal.getX(), enemigoOriginal.getY(), enemigoOriginal.getTipo(), texturas);
            enemigos.add(enemigo);
        }
    }

    public void actualizarEstalactitasYPiedras(Jugador jugador) {
        List<Bloque> estalactitasParaEliminar = new ArrayList<>();
        List<Bloque> piedrasParaEliminar = new ArrayList<>();
        

        for (Bloque estalactita : estalactitas) {
            if (Math.abs(jugador.getX() / 32.0f - estalactita.x) <= 9) {
                estalactita.y += 0.3f;
                Rectangle2D.Float estalactitaRect = new Rectangle2D.Float(estalactita.x * 32, estalactita.y * 32, 32, 32);
                boolean colisionada = false;

                for (Bloque bloque : bloques) {
                    if (!bloque.Textura.startsWith("estalactita")) {
                        Rectangle2D.Float bloqueRect = new Rectangle2D.Float(bloque.x * 32, bloque.y * 32, 32, 32);
                        if (estalactitaRect.intersects(bloqueRect)) {
                            estalactitasParaEliminar.add(estalactita);
                            colisionada = true;
                            break;
                        }
                    }
                }

                if (!colisionada) {
                    Rectangle2D.Float jugadorRect = new Rectangle2D.Float(jugador.getX(), jugador.getY(), jugador.getAncho(), jugador.getAlto());
                    if (estalactitaRect.intersects(jugadorRect)) {
                        jugador.perderVida();
                        reiniciarElementos();
                        estalactitasParaEliminar.add(estalactita);
                    }
                }
            }
        }



        
        for (Lava lava : lavaBloques) {
        	Rectangle2D.Float jugadorRect = new Rectangle2D.Float(jugador.getX(), jugador.getY(), jugador.getAncho(), jugador.getAlto());
            Rectangle2D.Float lavaRect = lava.getAreaColision();
            if (lavaRect.intersects(jugadorRect)) {
                jugador.perderVida(); 
                reiniciarElementos();
                break; 
            }
        }
        
        for (Killblock killblock : killBlocks) {
        	Rectangle2D.Float jugadorRect = new Rectangle2D.Float(jugador.getX(), jugador.getY(), jugador.getAncho(), jugador.getAlto());
            Rectangle2D.Float killRect = killblock.getAreaColision();
            if (killRect.intersects(jugadorRect)) {
                jugador.perderVida(); 
                reiniciarElementos();
                break; 
            }
        }
        for (Bloque bloque : bloques) {
            if (bloque.Textura.startsWith("lava-relleno")) {
                continue; 
            }

            Rectangle2D.Float bloqueRect = new Rectangle2D.Float(bloque.x * 32, bloque.y * 32, 32, 32);
            Rectangle2D.Float jugadorRect = new Rectangle2D.Float(jugador.getX(), jugador.getY(), jugador.getAncho(), jugador.getAlto());

            if (bloqueRect.intersects(jugadorRect)) {
                
                jugador.perderVida();
                reiniciarElementos();
                break; 
            }
        }


        
        for (Spike spike : spikes) {
            Rectangle2D.Float spikeRect = spike.getAreaColision();
            Rectangle2D.Float jugadorRect = new Rectangle2D.Float(jugador.getX(), jugador.getY(), jugador.getAncho(), jugador.getAlto());

            if (spikeRect.intersects(jugadorRect)) {
                jugador.perderVida();  
                reiniciarElementos();  
            }
        }


        for (Bloque piedra : piedrasCirculares) {
            boolean puedeCaer = true;
            Rectangle2D.Float piedraRect = new Rectangle2D.Float(piedra.x * 32, piedra.y * 32, 32, 32);

            for (Bloque bloque : bloques) {
                if (!bloque.Textura.startsWith("piedra_circular")) {
                    Rectangle2D.Float bloqueDebajoRect = new Rectangle2D.Float(bloque.x * 32, (bloque.y - 1) * 32, 32, 32);
                    if (piedraRect.intersects(bloqueDebajoRect)) {
                        piedra.y = bloque.y - 1;
                        piedraRect = new Rectangle2D.Float(piedra.x * 32, piedra.y * 32, 32, 32);
                        puedeCaer = false;
                        break;
                    }
                }
            }

            if (Math.abs(jugador.getX() / 32.0f - piedra.x) <= 16) {
                if (puedeCaer) {
                    piedra.y += 0.2f;
                } else {
                    boolean puedeMoverseHaciaLaIzquierda = true;
                    Rectangle2D.Float nuevaPosicionPiedraRect = new Rectangle2D.Float((piedra.x - 0.1f) * 32, piedra.y * 32, 32, 32);

                    for (Bloque bloque : bloques) {
                        if (!bloque.Textura.startsWith("piedra_circular")) {
                            Rectangle2D.Float bloqueRect = new Rectangle2D.Float(bloque.x * 32, bloque.y * 32, 32, 32);

                            if (nuevaPosicionPiedraRect.intersects(bloqueRect)) {
                                puedeMoverseHaciaLaIzquierda = false;
                                break;
                            }
                        }
                    }

                    if (puedeMoverseHaciaLaIzquierda) {
                        piedra.x -= 0.1f;
                        piedra.rotacion -= 20;
                        if (piedra.rotacion == 360) {
                            piedra.rotacion = 0;
                        }
                    }
                }
            }

            Rectangle2D.Float jugadorRect = new Rectangle2D.Float(jugador.getX(), jugador.getY(), jugador.getAncho(), jugador.getAlto());
            if (piedraRect.intersects(jugadorRect)) {
                jugador.perderVida();
                reiniciarElementos();
                piedrasParaEliminar.add(piedra);
            }

            
        }

        estalactitas.removeAll(estalactitasParaEliminar);
        piedrasCirculares.removeAll(piedrasParaEliminar);

        List<Bloque> areasAEliminar = new ArrayList<>();
        for (Bloque area : areasVictoria) {
            Rectangle2D.Float bloqueRect = new Rectangle2D.Float(area.x * 32, area.y * 32, 32, 32);
            Rectangle2D.Float jugadorRect = new Rectangle2D.Float(jugador.getX(), jugador.getY(), jugador.getAncho(), jugador.getAlto());
            if (bloqueRect.intersects(jugadorRect)) {
                reiniciarElementos();
                if (nivelactual == 1) {
                    cambiarNivel("level-3.txt");
                    detenerAudio();

                    if (spawnBlock != null) {
                        jugador.x = (float) spawnBlock.getX() * 32;
                        jugador.y = (float) spawnBlock.getY() * 32;
                    } else {
                        jugador.x = 50;
                        jugador.y = 50;
                    }
                    reproducirAudioilimitado("/Sonidos/NIVEL 2.wav"); // nivel 2
                    nivelactual++;
                } else if (nivelactual == 2) {
                    cambiarNivel("level-3.txt"); 
                    detenerAudio();
                    if (spawnBlock != null) {
                        jugador.x = (float) spawnBlock.getX() * 32;
                        jugador.y = (float) spawnBlock.getY() * 32;
                    } else {
                        jugador.x = 50;
                        jugador.y = 50;
                    }
                    reproducirAudioilimitado("/Sonidos/NIVEL 3.wav"); // nivel 1
                    nivelactual++;
                }
                else if (nivelactual == 3) {
                    cambiarNivel("level-4.txt"); 
                    detenerAudio();
                    reproducirAudioilimitado("/Sonidos/NIVEL 4.wav");
                    if (spawnBlock != null) {
                        jugador.x = (float) spawnBlock.getX() * 32;
                        jugador.y = (float) spawnBlock.getY() * 32;
                    } else {
                        jugador.x = 50;
                        jugador.y = 50;
                    }// nivel 1
                    nivelactual++;
                }
                else if (nivelactual == 4) {
                    cambiarNivel("level-5.txt"); 
                    detenerAudio();
                    reproducirAudioilimitado("/Sonidos/NIVEL 5.wav");
                    if (spawnBlock != null) {
                        jugador.x = (float) spawnBlock.getX() * 32;
                        jugador.y = (float) spawnBlock.getY() * 32;
                    } else {
                        jugador.x = 50;
                        jugador.y = 50;
                    }// nivel 1
                    nivelactual++;
                }
                areasAEliminar.add(area); 
            }
        }
        areasVictoria.removeAll(areasAEliminar); 




        
    }




    public void dibujar(Graphics g, Camara camara) {
        
        for (Bloque b : bloques) {
            ImageIcon icono = texturas.get(b.Textura);
            if (icono != null) {
                g.drawImage(icono.getImage(), (int)((b.x * 32) - camara.getX()), (int)((b.y * 32) - camara.getY()), null);
            }
        }


        if (boss != null) {
            boss.dibujar(g, camara, texturas);
        }

        for (Bloque spike : spikes)
        {
        	ImageIcon icono = texturas.get(spike.Textura);
            if (icono != null) {
                g.drawImage(icono.getImage(), (int)((spike.x * 32) - camara.getX()), (int)((spike.y * 32) - camara.getY()), null);
            }
        }
        for (Bloque b : bloquesSinColision) {
            ImageIcon icono = texturas.get(b.Textura);
            if (icono != null) {
                g.drawImage(icono.getImage(), (int)((b.x * 32) - camara.getX()), (int)((b.y * 32) - camara.getY()), null);
            }
        }

        for (Enemigo enemigo : enemigos) {
            enemigo.dibujar(g, camara);
        }

        for (Bloque estalactita : estalactitas) {
            ImageIcon icono = texturas.get(estalactita.Textura);
            if (icono != null) {
                g.drawImage(icono.getImage(), (int)((estalactita.x * 32) - camara.getX()), (int)((estalactita.y * 32) - camara.getY()), null);
            }
        }
        
        for (Bloque agua : bloquesAgua) {
            ImageIcon icono = texturas.get(agua.Textura);
            if (icono != null) {
                g.drawImage(icono.getImage(), (int)((agua.x * 32) - camara.getX()), (int)((agua.y * 32) - camara.getY()), null);
            }
        }
        for (Lava lava : lavaBloques) {
            lava.dibujar(g, camara, texturas);
        }
        for (Bloque piedra : piedrasCirculares) {
            ImageIcon icono = texturas.get(piedra.Textura);
            if (icono != null) {
                Graphics2D g2d = (Graphics2D) g.create();  
                int posX = (int)((piedra.x * 32) - camara.getX());
                int posY = (int)((piedra.y * 32) - camara.getY());

               
                g2d.translate(posX + 16, posY + 16);
                g2d.rotate(Math.toRadians(piedra.rotacion));  

               
                g2d.drawImage(icono.getImage(), -16, -16, 32, 32, null);

                g2d.dispose(); 
            }
        }


        for (Bloque area : areasVictoria) {
            ImageIcon icono = texturas.get(area.Textura);
            if (icono != null) {
                g.drawImage(icono.getImage(), (int)((area.x * 32) - camara.getX()), (int)((area.y * 32) - camara.getY()), null);
            }
        }
    }
    
    public void reproducirAudioilimitado(String nombreArchivo) {
        AudioInputStream audioInputStream = null;
        try {
            
            audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(nombreArchivo));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(-15f);

            
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (audioInputStream != null) {
                try {
                    audioInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void detenerAudio() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close(); 
        }
    }
    
    private void reproducirAudioUnaVez(String nombreArchivo) {
        AudioInputStream audioInputStream = null;
        try {
            // CARGA EL AUDIO
            audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(nombreArchivo));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(-15f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (audioInputStream != null) {
                try {
                    audioInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public List<Bloque> getBloques() {
        return bloques;
    }

    public List<Bloque> getBloquesSinColision() {
        return bloquesSinColision;
    }

    public List<Killblock> getKillBlocks() {
        return killBlocks;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public List<Bloque> getEstalactitas() {
        return estalactitas;
    }

    public List<Bloque> getPiedrasCirculares() {
        return piedrasCirculares;
    }

	public Boss getBoss() {

		return boss;
	}
	public Bloque getSpawnBlock() {
        return spawnBlock;
    }

	public List<Bloque> getAguas() {
		return bloquesAgua;
	}
    
}
