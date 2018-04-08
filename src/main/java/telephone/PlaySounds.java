package telephone;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlaySounds {

    public static void main(String[] args) throws LineUnavailableException, InterruptedException, IOException {
        PlaySounds ps =new PlaySounds();
        ps.init();
        byte[] bb = Files.readAllBytes(Paths.get("E:\\workSource\\audios\\ok-wav\\347.wav"));
        ps.write(bb,0,bb.length);
        ps.close();
    }



    SourceDataLine auline = null;

    public void init() throws LineUnavailableException {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        auline = (SourceDataLine) AudioSystem.getLine(info);
        auline.open(format);
        auline.start();
    }

    public void write(byte[] b, int off, int len) {
        auline.write(b, off, len);
    }

    public void close() {
        auline.drain();
        auline.close();
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        // 8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        // 8,16
        int channels = 1;
        // 1,2
        boolean signed = true;
        // true,false
        boolean bigEndian = false;
        // true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}