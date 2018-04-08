package telephone;

import javax.sound.sampled.*;

public class AudioRecorder {
    private TargetDataLine targetDataLine;
    private boolean recording=true;

    public void captureAudio( ) throws LineUnavailableException {
        AudioFormat audioFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        int bufferSize = (int) (audioFormat.getSampleRate()*audioFormat.getFrameSize());
        final byte buffer[] = new byte[bufferSize];
        targetDataLine.open(audioFormat);
        targetDataLine.start();
        System.out.println("recording...");
        while (recording) {
            int count = targetDataLine.read(buffer, 0, buffer.length);
            if (count > 0) {
                try {
                    DataWrap data = new DataWrap();
                    data.buffer=buffer;
                    data.count=count;
                    //System.out.println("recorded: data size:"+data.count);
                    //FileUtils.writeByteArrayToFile(new File("E:\\temp\\send.pcm"),data.buffer,true);
                    UdpCommunicate.getInstance().send(data);
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


    public static AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
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