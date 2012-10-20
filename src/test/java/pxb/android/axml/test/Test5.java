package pxb.android.axml.test;


import com.googlecode.dex2jar.reader.io.ArrayDataIn;
import org.junit.Test;
import pxb.android.axml.*;

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
                AxmlWriter wr = new AxmlWriter();
                AxmlDecoder av = new AxmlDecoder(new DumpAdapter(null, wr));
                rd.accept(av);

                System.out.println(av.getMainActivityName());
            }
        }
    }
}

class AxmlDecoder extends AxmlVisitor{

    private static String androidNS = "http://schemas.android.com/apk/res/android";

    public AxmlDecoder(AxmlVisitor visitor){
        super(visitor);
    }

    public static class NodeDecoder extends NodeVisitor{
        private AxmlDecoder root;
        private NodeDecoder parent;
        private String namespace;
        private String name;
        private Object aux;

        private static boolean permissionHandled = false;
        private static boolean screeDensityHandled = false;

        private boolean hasActionMainAttr = false;
        private boolean hasCategoryLauncherAttr = false;

        public NodeDecoder(String namespace, String name, NodeDecoder parent, AxmlDecoder root, NodeVisitor nv){
            super(nv);
            this.namespace = namespace;
            this.name = name;
            this.parent = parent;
            this.root = root;
        }

        @Override
        public NodeVisitor visitChild(String namespace, String name){
            NodeVisitor nv = super.visitChild(namespace, name);
            return new NodeDecoder(namespace, name, this, root, nv);
        }

        @Override
        public void visitContentAttr(String namespace, String name, int resourceId, int type, Object obj){
            if(this.name.compareTo("supports-screens") == 0
                    && namespace.compareTo(androidNS) == 0
                    && name.compareTo("anyDensity") == 0){
                super.visitContentAttr(namespace, name, resourceId, type, true);
                screeDensityHandled = true;
            }
            else{
                super.visitContentAttr(namespace, name, resourceId, type, obj);
                if(this.name.compareTo("uses-permission") == 0
                        && namespace.compareTo(androidNS) == 0
                        && name.compareTo("name") == 0
                        && ((String)obj).compareTo("android.permission.INTERNET") == 0) {
                    permissionHandled = true;
                }
            }

            //check main activity
            if(this.parent != null && this.parent.name.compareTo("intent-filter") == 0){
                if(this.name.compareTo("action") == 0
                        && namespace.compareTo(androidNS) == 0
                        && name.compareTo("name") == 0
                        && ((String) obj).compareTo("android.intent.action.MAIN") == 0){
                    parent.parent.hasActionMainAttr = true;
                }
                if(this.name.compareTo("category") == 0
                        && namespace.compareTo(androidNS) == 0
                        && name.compareTo("name") == 0
                        && ((String) obj).compareTo("android.intent.category.LAUNCHER") == 0){
                    parent.parent.hasCategoryLauncherAttr = true;
                }
            }

            if(this.name.compareTo("activity") == 0
                    && namespace.compareTo(androidNS) == 0
                    && name.compareTo("name") == 0){
                aux = obj;
            }

            if(this.name.compareTo("manifest") == 0
                    && name.compareTo("package") == 0){
                root.setPackage((String)obj);
            }
        }

        @Override
        public void visitContentEnd(){
            if(this.name.compareTo("supports-screens") == 0 && !screeDensityHandled){
                super.visitContentAttr("android", "anyDensity", 0x0101026c, AxmlVisitor.TYPE_INT_BOOLEAN, true);
                screeDensityHandled = true;
            }
            if(this.name.compareTo("uses-permission")== 0 && !permissionHandled){
                super.visitContentAttr("android", "name", 0x01010003, AxmlVisitor.TYPE_STRING, "android.permission.INTERNET");
                permissionHandled = true;
            }
            super.visitContentEnd();
        }


        @Override
        public void visitEnd(){
            if(name.compareTo("manifest") == 0 ){
                if(!screeDensityHandled){
                    screeDensityHandled = true;
                    NodeVisitor nv = visitChild(null,"supports-screens");
                    if(nv != null){
                        nv.visitBegin();
                        nv.visitContentAttr("android","anyDensity", 0x0101026c, AxmlVisitor.TYPE_INT_BOOLEAN, true);
                        nv.visitContentEnd();
                        nv.visitEnd();
                    }
                }
                if(!permissionHandled){
                    permissionHandled = true;
                    NodeVisitor nv = visitChild(null, "uses-permission");
                    if(nv != null){
                        nv.visitBegin();
                        nv.visitContentAttr("android","name", 0x01010003, AxmlVisitor.TYPE_STRING, "android.permission.INTERNET");
                        nv.visitContentEnd();
                        nv.visitEnd();
                    }
                }
            }
            super.visitEnd();

            //check
            if(this.name.compareTo("activity") == 0 && hasActionMainAttr && hasCategoryLauncherAttr){
                root.setMainActivity((String)this.aux);
            }
        }
    }

    private String mainActivity;
    private String packageName;

    @Override
    public NodeVisitor visitFirst(String namespace, String name){
        NodeVisitor nv = super.visitFirst(namespace, name);
        return new NodeDecoder(namespace, name, null, this, nv);
    }

    public void setMainActivity(String name){
        mainActivity = name;
    }

    public void setPackage(String name){
        packageName = name;
    }

    public String getMainActivityName(){
        return packageName + mainActivity;
    }
}
