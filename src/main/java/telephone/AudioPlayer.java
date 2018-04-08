package telephone;

import javax.sound.sampled.*;

import static telephone.AudioRecorder.getAudioFormat;

public class AudioPlayer {

    private SourceDataLine auline = null;

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

}