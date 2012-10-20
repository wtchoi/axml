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

/**
 * visitor to visit an axml
 * 
 * @author <a href="mailto:pxb1988@gmail.com">Panxiaobo</a>
 * 
 */
public class AxmlVisitor {

    public static abstract class NodeVisitor {
        protected NodeVisitor nv;

        public NodeVisitor() {
            super();
        }

        public NodeVisitor(NodeVisitor nv) {
            super();
            this.nv = nv;
        }

        /**
         * add attribute to the node
         * 
         * @param ns
         * @param name
         * @param resourceId
         * @param type
         *            {@link AxmlVisitor#TYPE_STRING} or others
         * @param obj
         *            a string for {@link AxmlVisitor#TYPE_STRING} ,and Integer for others
         */
        public void visitAttr(String ns, String name, int resourceId, int type, Object obj) {
            if (nv != null) {
                nv.visitAttr(ns, name, resourceId, type, obj);
            }
        }

        /**
         * create a child node
         * 
         * @param ns
         * @param name
         * @return
         */
        public NodeVisitor visitChild(String ns, String name) {
            if (nv != null) {
                return nv.visitChild(ns, name);
            }
            return null;
        }

        /**
         * end the visit
         */
        public void visitEnd() {
            if (nv != null) {
                nv.visitEnd();
            }
        }

        /**
         * line number in the .xml
         * 
         * @param ln
         */
        public void visitLine(int ln) {
            if (nv != null) {
                nv.visitLine(ln);
            }
        }

        /**
         * the node text
         * 
         * @param value
         */
        public void visitText(int lineNumber, String value) {
            if (nv != null) {
                nv.visitText(lineNumber, value);
            }
        }
    }
    public static final int TYPE_FIRST_INT = 0x10;
    public static final int TYPE_INT_BOOLEAN = 0x12;
    public static final int TYPE_INT_HEX = 0x11;
    public static final int TYPE_REFERENCE = 0x01;

    public static final int TYPE_STRING = 0x03;

    protected AxmlVisitor av;

    public AxmlVisitor() {
        super();

    }

    public AxmlVisitor(AxmlVisitor av) {
        super();
        this.av = av;
    };

    /**
     * end the visit
     */
    public void visitEnd() {
        if (av != null) {
            av.visitEnd();
        }
    };

    /**
     * create the first node
     * 
     * @param ns
     * @param name
     * @return
     */
    public NodeVisitor visitFirst(String ns, String name) {
        if (av != null) {
            return av.visitFirst(ns, name);
        }
        return null;
    }

    /**
     * create a ns
     * 
     * @param prefix
     * @param uri
     * @param ln
     */
    public void visitNamespace(String prefix, String uri, int ln) {
        if (av != null) {
            av.visitNamespace(prefix, uri, ln);
        }
    }

}
