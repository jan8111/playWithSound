package telephone;

import javax.sound.sampled.*;

import static telephone.AudioRecorder.getAudioFormat;

public class AudioPlayer {

/*    public static void main(String[] args) throws LineUnavailableException, InterruptedException, IOException {
        AudioPlayer ps =new AudioPlayer();
        ps.init();
        byte[] bb = Files.readAllBytes(Paths.get("E:\\workSource\\audios\\ok-wav\\347.wav"));
        ps.write(bb,0,bb.length);
        ps.close();
    }*/

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

    public int getBuffsize(){
        AudioFormat audioFormat = getAudioFormat();
        return (int) (audioFormat.getSampleRate()*audioFormat.getFrameSize());
    }


}