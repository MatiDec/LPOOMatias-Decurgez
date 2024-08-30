package clases;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sonido {
	private Map<String, Clip> clips;

    public Sonido() {
        clips = new HashMap<>();
    }

    // Método para cargar un archivo de audio y asignarle un identificador
    public void loadAudio(String id, String filePath) {
        try {
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clips.put(id, clip); // Asigna el clip a un identificador
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para reproducir el audio asociado a un identificador
    public void playAudio(String id) {
        Clip clip = clips.get(id);
        if (clip != null) {
            clip.start();
        }
    }

    public void playAudioReiterado(String id) {
    	Clip clip = clips.get(id);
    	if(clip != null) {
        	clip.setFramePosition(0); // Reinicia el clip al principio
        	clip.start();
    	}
    }
    
    // Método para detener el audio asociado a un identificador
    public void stopAudio(String id) {
        Clip clip = clips.get(id);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
    // Método para ajustar el volumen de un sonido específico
    public void setVolume(String id, float volume) {
        Clip clip = clips.get(id);
        if (clip != null) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);
        }
    }

    // Método para cerrar todos los clips cuando ya no se necesitan
    public void closeAll() {
        for (Clip clip : clips.values()) {
            clip.close();
        }
    }
}
