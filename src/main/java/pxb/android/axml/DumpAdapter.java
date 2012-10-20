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

import java.io.PrintWriter;
import java.io.Writer;
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
        protected PrintWriter writer;

        public DumpNodeAdapter(PrintWriter w, NodeVisitor nv, String namespace, String name) {
            super(nv);
            this.depth = 0;
            this.nses = null;
            this.namespace = namespace;
            this.name = name;
            this.writer = w;
        }

        public DumpNodeAdapter(PrintWriter w, NodeVisitor nv, int x, Map<String, String> nses, String namespace, String name) {
            super(nv);
            this.depth = x;
            this.nses = nses;
            this.namespace = namespace;
            this.name = name;
            this.writer = w;
        }

        @Override
        public void visitBegin(){
            indent();
            writer.print("<");
            if (namespace != null) {
                writer.println(getPrefix(namespace) + ":");
            }
            writer.print(name);
            super.visitBegin();
        }


        @Override
        public void visitContentAttr(String ns, String name, int resourceId, int type, Object obj) {
            writer.println();

            indentContent();
            if (ns != null) {
                writer.print(String.format("%s:", getPrefix(ns)));
            }
            writer.print(name);
            if (resourceId != -1) {
                writer.print(String.format("(%08x)", resourceId));
            }
            if (obj instanceof String) {
                writer.print(String.format("=[%08x]\"%s\"", type, obj));
            } else if (obj instanceof Boolean) {
                writer.print(String.format("=[%08x]\"%b\"", type, obj));
            } else {
                writer.print(String.format("=[%08x]%08x", type, obj));
            }

            //writer.println();
            super.visitContentAttr(ns, name, resourceId, type, obj);
        }

        @Override
        public void visitContentEnd(){
            writer.println(">");
            super.visitContentEnd();
        }

        @Override
        public NodeVisitor visitChild(String namespace, String name) {
            NodeVisitor nv = super.visitChild(namespace, name);
            return new DumpNodeAdapter(writer, nv, depth + 1, nses, namespace, name);
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
            writer.println();

            indentContent();
            writer.print(value);
            super.visitContentText(ln, value);
        }

        @Override
        public void visitEnd(){
            indent();
            writer.println("</"+name+">");
            super.visitEnd();
        }

        private void indent(){
            for (int i = 0; i < depth; i++) {
                writer.print("  ");
            }
        }

        private void indentContent(){
            indent();
            writer.print("  ");
        }
    }

    private Map<String, String> nses = new HashMap<String, String>();
    private PrintWriter writer = null;

    public DumpAdapter() {
        writer = new PrintWriter(System.out);
    }

    public DumpAdapter(Writer w) {
        writer = new PrintWriter(w);
    }

    public DumpAdapter(Writer w, AxmlVisitor av) {
        super(av);
        writer = new PrintWriter(w);
    }

    @Override
    public void visitBegin(){
        writer.println("<xml document>");
        super.visitBegin();
    }

    @Override
    public NodeVisitor visitFirst(String namespace, String name) {
        NodeVisitor nv = super.visitFirst(namespace, name);
        //if (nv != null) {
            DumpNodeAdapter x = new DumpNodeAdapter(writer, nv, 1, nses, namespace, name);
            return x;
        //}
        //else{
        //    return new DumpNodeAdapter(null, 1, nses);
        //}
    }


    @Override
    public void visitNamespace(String prefix, String uri, int ln) {
        writer.println("xmlns:" + prefix + "=" + uri);
        this.nses.put(uri, prefix);
        super.visitNamespace(prefix, uri, ln);
    }

    @Override
    public void visitEnd(){
        writer.println("</xml document>");
        super.visitEnd();
    }

}
