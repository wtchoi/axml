package pxb.android.axml.test;

import org.junit.Test;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlWriter;
import pxb.android.axml.DumpAdapter;
import pxb.android.axml.NodeVisitor;

import java.io.IOException;

public class Test4 {
    @Test
    public void test() throws IOException {
        AxmlWriter aw = new AxmlWriter();
        NodeVisitor nv = aw.visitFirst("http://abc.com", "abc");
        nv.visitEnd();
        nv = aw.visitFirst("http://efg.com", "efg");
        nv.visitEnd();
        aw.visitEnd();
        AxmlReader ar = new AxmlReader(aw.toByteArray());
        ar.accept(new DumpAdapter());
    }
}
