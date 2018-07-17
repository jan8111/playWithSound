package flac;

import org.apache.commons.io.FileUtils;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.frame.Frame;
import org.kc7bfi.jflac.metadata.Metadata;
import org.kc7bfi.jflac.util.ByteData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class flac1 {

    public static void main(String[] args) throws IOException {
        FLACDecoder decoder = new FLACDecoder(new FileInputStream(new File("E:\\temp\\9_short.flac")));
        Metadata[] meta = decoder.readMetadata();
        Frame frame = null;
        while ((frame = decoder.readNextFrame()) != null) {
            ByteData data = new ByteData(0);
            data = decoder.decodeFrame(frame, data);
            FileUtils.writeByteArrayToFile(new File("pcm1.pcm"), data.getData(),0,data.getLen(), true);
        }
    }

}
