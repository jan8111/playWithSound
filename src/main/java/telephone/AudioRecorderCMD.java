package telephone;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioRecorderCMD {
    private TargetDataLine targetDataLine;
    private boolean recording=true;
    private int secondTime=5;//recording time in 秒

  private static ExecutorService executorService= Executors.newCachedThreadPool();

    public static void main(String args[]) throws LineUnavailableException, IOException {
        //sxxx set host
        //UdpCommunicate.getInstance().setHost(args[0]);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        AudioRecorderCMD last =null;
        while ((line = reader.readLine()) != null) {
            if(last!=null){
                last.stopThread();
                last=null;
            }else{
                last= new AudioRecorderCMD();
                AudioRecorderCMD finalLast = last;
                executorService.submit(()->{
                    try {
                        finalLast.captureAudio();
                    } catch (LineUnavailableException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void captureAudio( ) throws LineUnavailableException, IOException {
        AudioFormat audioFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

        int bufferSize = (int) (audioFormat.getSampleRate()*audioFormat.getFrameSize());
        final byte buffer[] = new byte[bufferSize];
        targetDataLine.open(audioFormat);
        //stopThread();
        targetDataLine.start();
        System.out.println("recording...");
        while (recording) {
            int count = targetDataLine.read(buffer, 0, buffer.length);
            if (count > 0) {
                try {
                    DataWrap data = new DataWrap();
                    data.buffer=buffer;
                    data.count=count;
                    boolean flag1 = UdpCommunicate.getInstance().send(data);
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