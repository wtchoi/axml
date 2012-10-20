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
        protected int deep;
        protected Map<String, String> nses;

        public DumpNodeAdapter(NodeVisitor nv) {
            super(nv);
            this.deep = 0;
            this.nses = null;
        }

        public DumpNodeAdapter(NodeVisitor nv, int x, Map<String, String> nses) {
            super(nv);
            this.deep = x;
            this.nses = nses;
        }

        @Override
        public void visitAttr(String ns, String name, int resourceId, int type, Object obj) {
            for (int i = 0; i < deep; i++) {
                System.out.print("  ");
            }
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
            System.out.println();
            super.visitAttr(ns, name, resourceId, type, obj);
        }

        @Override
        public NodeVisitor visitChild(String ns, String name) {
            for (int i = 0; i < deep; i++) {
                System.out.print("  ");
            }
            System.out.print("<");
            if (ns != null) {
                System.out.println(getPrefix(ns) + ":");
            }
            System.out.println(name);
            NodeVisitor nv = super.visitChild(ns, name);
            if (nv != null) {
                return new DumpNodeAdapter(nv, deep + 1, nses);
            }
            return null;
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
        public void visitText(int ln, String value) {
            for (int i = 0; i < deep + 1; i++) {
                System.out.print("  ");
            }
            System.out.println(value);
            super.visitText(ln, value);
        }
    }

    private Map<String, String> nses = new HashMap<String, String>();

    public DumpAdapter() {
    }

    public DumpAdapter(AxmlVisitor av) {
        super(av);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    @Override
    public NodeVisitor visitFirst(String ns, String name) {
        System.out.print("<");
        if (ns != null) {
            System.out.println(nses.get(ns) + ":");
        }
        System.out.println(name);
        NodeVisitor nv = super.visitFirst(ns, name);
        if (nv != null) {
            DumpNodeAdapter x = new DumpNodeAdapter(nv, 1, nses);
            return x;
        }
        return null;
    }

    @Override
    public void visitNamespace(String prefix, String uri, int ln) {
        System.out.println(prefix + "=" + uri);
        this.nses.put(uri, prefix);
        super.visitNamespace(prefix, uri, ln);
    }

}
