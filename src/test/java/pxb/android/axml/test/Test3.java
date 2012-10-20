package pxb.android.axml.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlVisitor;
import pxb.android.axml.DumpAdapter;

import com.googlecode.dex2jar.reader.io.ArrayDataIn;

public class Test3 {
    @Test
    public void test0() throws Exception {
        for (File file : new File("src/test/resources/").listFiles()) {
            if (file.getName().endsWith(".axml")) {
                System.out.println("======= test " + file);
                InputStream is = new FileInputStream(file);
                byte[] xml = new byte[is.available()];
                is.read(xml);
                is.close();
                AxmlReader rd = new AxmlReader(ArrayDataIn.le(xml));
                rd.accept(null);
            }
        }
    }

    @Test
    public void test1() throws Exception {
        for (File file : new File("src/test/resources/").listFiles()) {
            if (file.getName().endsWith(".axml")) {
                System.out.println("======= test " + file);
                InputStream is = new FileInputStream(file);
                byte[] xml = new byte[is.available()];
                is.read(xml);
                is.close();
                AxmlReader rd = new AxmlReader(ArrayDataIn.le(xml));
                rd.accept(new AxmlVisitor(new DumpAdapter()) {

                    @Override
                    public NodeVisitor first(String ns, String name) {
                        return null;
                    }
                });
            }
        }
    }

    @Test
    public void test2() throws Exception {
        for (File file : new File("src/test/resources/").listFiles()) {
            if (file.getName().endsWith(".axml")) {
                System.out.println("======= test " + file);
                InputStream is = new FileInputStream(file);
                byte[] xml = new byte[is.available()];
                is.read(xml);
                is.close();
                AxmlReader rd = new AxmlReader(ArrayDataIn.le(xml));
                rd.accept(new AxmlVisitor(new DumpAdapter()) {

                    @Override
                    public NodeVisitor first(String ns, String name) {
                        return new NodeVisitor(super.first(ns, name)) {

                            @Override
                            public NodeVisitor child(String ns, String name) {
                                return null;
                            }
                        };
                    }
                });
            }
        }
    }
}
