/*
 * Copyright (c) 2009-2012 Panxiaobo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pxb.android.axml;

import java.util.HashMap;
import java.util.Map;

/**
 * dump axml to stdout
 * 
 * @author <a href="mailto:pxb1988@gmail.com">Panxiaobo</a>
 * 
 */
public class DumpAdapter extends AxmlVisitor {

    /**
     * dump a node to stdout
     * 
     * @author <a href="mailto:pxb1988@gmail.com">Panxiaobo</a>
     * 
     */
    public static class DumpNodeAdapter extends NodeVisitor {
        protected final int depth;
        protected Map<String, String> nses;
        protected String namespace;
        protected String name;

        public DumpNodeAdapter(NodeVisitor nv, String namespace, String name) {
            super(nv);
            this.depth = 0;
            this.nses = null;
            this.namespace = namespace;
            this.name = name;
        }

        public DumpNodeAdapter(NodeVisitor nv, int x, Map<String, String> nses, String namespace, String name) {
            super(nv);
            this.depth = x;
            this.nses = nses;
            this.namespace = namespace;
            this.name = name;
        }

        @Override
        public void visitBegin(){
            indent();
            System.out.print("<");
            if (namespace != null) {
                System.out.println(getPrefix(namespace) + ":");
            }
            System.out.print(name);
            super.visitBegin();
        }


        @Override
        public void visitContentAttr(String ns, String name, int resourceId, int type, Object obj) {
            System.out.println();

            indentContent();
            if (ns != null) {
                System.out.print(String.format("%s:", getPrefix(ns)));
            }
            System.out.print(name);
            if (resourceId != -1) {
                System.out.print(String.format("(%08x)", resourceId));
            }
            if (obj instanceof String) {
                System.out.print(String.format("=[%08x]\"%s\"", type, obj));
            } else if (obj instanceof Boolean) {
                System.out.print(String.format("=[%08x]\"%b\"", type, obj));
            } else {
                System.out.print(String.format("=[%08x]%08x", type, obj));
            }

            //System.out.println();
            super.visitContentAttr(ns, name, resourceId, type, obj);
        }

        @Override
        public void visitContentEnd(){
            System.out.println(">");
            super.visitContentEnd();
        }

        @Override
        public NodeVisitor visitChild(String namespace, String name) {
            NodeVisitor nv = super.visitChild(namespace, name);
            return new DumpNodeAdapter(nv, depth + 1, nses, namespace, name);
        }

        protected String getPrefix(String uri) {
            if (nses != null) {
                String prefix = nses.get(uri);
                if (prefix != null) {
                    return prefix;
                }
            }
            return uri;
        }

        @Override
        public void visitContentText(int ln, String value) {
            System.out.println();

            indentContent();
            System.out.print(value);
            super.visitContentText(ln, value);
        }

        @Override
        public void visitEnd(){
            indent();
            System.out.println("</"+name+">");
            super.visitEnd();
        }

        private void indent(){
            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }
        }

        private void indentContent(){
            indent();
            System.out.print("  ");
        }
    }

    private Map<String, String> nses = new HashMap<String, String>();

    public DumpAdapter() {
    }

    public DumpAdapter(AxmlVisitor av) {
        super(av);
    }

    @Override
    public void visitBegin(){
        System.out.println("<xml document>");
        super.visitBegin();
    }

    @Override
    public NodeVisitor visitFirst(String namespace, String name) {
        NodeVisitor nv = super.visitFirst(namespace, name);
        //if (nv != null) {
            DumpNodeAdapter x = new DumpNodeAdapter(nv, 1, nses, namespace, name);
            return x;
        //}
        //else{
        //    return new DumpNodeAdapter(null, 1, nses);
        //}
    }


    @Override
    public void visitNamespace(String prefix, String uri, int ln) {
        System.out.println("xmlns:" + prefix + "=" + uri);
        this.nses.put(uri, prefix);
        super.visitNamespace(prefix, uri, ln);
    }

    @Override
    public void visitEnd(){
        System.out.println("</xml document>");
        super.visitEnd();
    }

}
