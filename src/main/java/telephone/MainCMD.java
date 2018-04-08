package telephone;

import javax.sound.sampled.LineUnavailableException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainCMD {
    private static ExecutorService executorService= Executors.newCachedThreadPool();

    public static void main(String args[]) throws IOException {
        //sxxx set host
        //UdpCommunicate.getInstance().setHost(args[0]);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        AudioRecorder recorder =null;
        while ((line = reader.readLine()) != null) {
            if(recorder!=null){
                recorder.stopThread();
                recorder=null;
            }else{
                recorder= new AudioRecorder();
                AudioRecorder finalLast = recorder;
                executorService.submit(()->{
                    try {
                        finalLast.captureAudio();
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
