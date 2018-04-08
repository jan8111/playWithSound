package telephone;

import javax.sound.sampled.*;

public class AudioRecorder {
    private TargetDataLine targetDataLine;
    private boolean recording=true;

    public void captureAudio( ) throws LineUnavailableException {
        AudioFormat audioFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        //int bufferSize = (int) (audioFormat.getSampleRate()*audioFormat.getFrameSize());
        final byte buffer[] = new byte[getBuffzie()];
        targetDataLine.open(audioFormat);
        targetDataLine.start();
        System.out.println("recording...");
        while (recording) {
            int count = targetDataLine.read(buffer, 0, buffer.length);
            if (count > 0) {
                try {
                    //System.out.println("recorded: data size:"+count);
                    UdpCommunicate.getInstance().send(buffer,0,count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("recording end.");
    }

    public void stopThread( ) {
        recording=false;
        targetDataLine.stop();
        targetDataLine.close();
    }

    public static int getBuffzie() {
        return 160;
    }

    public static AudioFormat getAudioFormat() {
        float sampleRate = 6000.0F;
        // 8000,11025,16000,22050,44100
        int sampleSizeInBits = 8;
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