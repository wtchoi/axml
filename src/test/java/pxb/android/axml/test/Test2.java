package pxb.android.axml.test;

import com.googlecode.dex2jar.reader.io.ArrayDataIn;
import org.junit.Test;
import pxb.android.axml.Axml;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlWriter;
import pxb.android.axml.DumpAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

public class Test2 {
    @Test
    public void test() throws Exception {
        for (File file : new File("src/test/resources/").listFiles()) {
            if (file.getName().endsWith(".axml")) {
                System.out.println("======= test0 " + file);
                InputStream is = new FileInputStream(file);
                byte[] xml = new byte[is.available()];
                is.read(xml);
                is.close();
                AxmlReader rd = new AxmlReader(ArrayDataIn.le(xml));
                AxmlWriter wr = new AxmlWriter();

                //initial parsed
                System.out.println("==== A");
                StringWriter writerA = new StringWriter();
                Axml axml1 = new Axml();
                rd.accept(axml1);
                axml1.accept(new DumpAdapter(writerA));
                System.out.println(writerA);

                //write and re-parsed
                System.out.println("==== B");
                axml1.accept(wr);
                StringWriter writerB = new StringWriter();
                Axml axml2 = new Axml();
                new AxmlReader(ArrayDataIn.le(wr.toByteArray())).accept(axml2);
                axml2.accept(new DumpAdapter(writerB));
                System.out.println(writerB);

               // Assert.assertTrue(writerA.toString().compareTo(writerB.toString()) == 0);

            }
        }
    }
}
