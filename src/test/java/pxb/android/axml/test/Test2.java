package pxb.android.axml.test;

import com.googlecode.dex2jar.reader.io.ArrayDataIn;
import org.junit.Test;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlWriter;
import pxb.android.axml.DumpAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Test2 {
    @Test
    public void test() throws Exception {
        for (File file : new File("src/test/resources/").listFiles()) {
            if (file.getName().endsWith(".axml")) {
                System.out.println("======= test " + file);
                InputStream is = new FileInputStream(file);
                byte[] xml = new byte[is.available()];
                is.read(xml);
                is.close();
                AxmlReader rd = new AxmlReader(ArrayDataIn.le(xml));
                AxmlWriter wr = new AxmlWriter();
                System.out.println("=== A ");
                //rd.accept(new DumpAdapter());
                rd.accept(new DumpAdapter(wr));
                System.out.println("=== B ");
                new AxmlReader(ArrayDataIn.le(wr.toByteArray())).accept(new DumpAdapter());
            }
        }
    }

}
