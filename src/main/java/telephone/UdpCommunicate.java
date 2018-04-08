package telephone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UdpCommunicate {

    private String host = "127.0.0.1";
    private static UdpCommunicate instance = new UdpCommunicate();

    private BlockingQueue<DataWrap> queue = new LinkedBlockingQueue<>( );

    public boolean send(DataWrap data) {
        return this.queue.offer(data);
    }

    public static UdpCommunicate getInstance() {
        return instance;
    }

    AudioPlayer playSounds = new AudioPlayer();

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
        DatagramSocket socket = new DatagramSocket(8888);

        new Thread(() -> {
            try {
                while (true) {
                    byte[] buf = new byte[playSounds.getBuffsize()];
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    socket.receive(dp);
                    //System.out.println("Receive: "+dp.getAddress().getHostAddress() + ":" + dp.getData().length);
                    //FileUtils.writeByteArrayToFile(new File("E:\\temp\\Receive1.pcm"),dp.getData(),true);
                    playSounds.write(dp.getData(),0,dp.getData().length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                playSounds.close();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Send: " + host);
                DataWrap dataWrap1 = null;
                while ((dataWrap1 = queue.take()) != null) {
                    DatagramPacket dp = new DatagramPacket(dataWrap1.buffer, dataWrap1.count, InetAddress.getByName(host), 8888);
                    socket.send(dp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }


}


