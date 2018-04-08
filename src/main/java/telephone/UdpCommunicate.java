package telephone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UdpCommunicate {

    public static final int PORT = 8888;
    private String host = "127.0.0.1";
    private static UdpCommunicate instance = new UdpCommunicate();

    public static UdpCommunicate getInstance() {
        return instance;
    }

    AudioPlayer playSounds = new AudioPlayer();
    DatagramSocket socket;

    private UdpCommunicate() {
        try {
            init();
            playSounds.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void init() throws Exception {
        socket = new DatagramSocket(PORT);

        new Thread(() -> {
            try {
                while (true) {
                    byte[] buf = new byte[playSounds.getBuffsize()];
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    socket.receive(dp);
                    //System.out.println("Receive: "+dp.getAddress().getHostAddress() + ":" + dp.getData().length);
                    //FileUtils.writeByteArrayToFile(new File("E:\\temp\\Receive1.pcm"),dp.getData(),true);
                    playSounds.write(dp.getData(), 0, dp.getData().length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                playSounds.close();
            }
        }).start();
   }


    public void send(byte buf[], int offset, int length) throws IOException {
        DatagramPacket dp = new DatagramPacket(buf,offset,length, InetAddress.getByName(host), PORT);
        socket.send(dp);
    }


}


