package pxb.android.axml.test;


import com.googlecode.dex2jar.reader.io.ArrayDataIn;
import org.junit.Test;
import org.w3c.dom.Document;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlVisitor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: wtchoi
 * Date: 10/19/12
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test5 {
    @Test
    public void test() throws Exception{
        System.out.println("TEST5");
        for(File file: new File("src/test/resources/").listFiles()){
            if(file.getName().compareTo("manifest1.axml") == 0){
                InputStream is = new FileInputStream(file);
                byte[] xml = new byte[is.available()];
                is.read(xml);
                is.close();
                AxmlReader rd = new AxmlReader(ArrayDataIn.le(xml));
                //AxmlWriter wr = new AxmlWriter();
                AxmlVisitor av = new AxmlDecoder();
                rd.accept(av);
            }
        }
    }
}

class AxmlDecoder extends AxmlVisitor{
    private Document doc;

    public AxmlDecoder() throws Exception{
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        doc = docBuilder.newDocument();
    }


    @Override
    public NodeVisitor visitFirst(String namespace, String name){
        System.out.println("visit First");
        return null;
    }

    @Override
    public void visitNamespace(String prefix, String uri, int ln) {
        System.out.println("visit Namespace");

    }

    @Override
    public void visitEnd(){
        System.out.println("visit End");
    }


    public Document getXML(){
        return doc;
    }

}
