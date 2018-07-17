package myrecoder;

import org.apache.commons.io.FileUtils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Myrecoder1 {

    public static void main(String[] args) throws IOException, LineUnavailableException {
        new Myrecoder1().captureAudio();
    }

    public void captureAudio( ) throws LineUnavailableException, IOException {
        AudioFormat audioFormat = getFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        final byte buffer[] = new byte[160];
        targetDataLine.open(audioFormat);
        targetDataLine.start();
        System.out.println("recording...");
        while (true) {
            int count = targetDataLine.read(buffer, 0, buffer.length);
            FileUtils.writeByteArrayToFile(new File("my1.pcm"),buffer,0,count,true);
        }
    }


    private AudioFormat getFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 8;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                bigEndian);//notice here!
    }
}
