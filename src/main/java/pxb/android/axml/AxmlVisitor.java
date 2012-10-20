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

/*
    AXMLVisitor visiting sequence : visitBegin [visitNamespace]* visitFirstNode visitEnd
    NodeVisitor visiting sequence : visitBegin [visitContentAttr | visitContentText]* visitContentEnd visitChildNoe visitEnd
 */

/**
 * visitor to visit an axml
 * 
 * @author <a href="mailto:pxb1988@gmail.com">Panxiaobo</a>
 * 
 */
public class AxmlVisitor {

    public static class NodeVisitor {
        protected NodeVisitor nv;

        public NodeVisitor() {
            super();
        }

        public NodeVisitor(NodeVisitor nv) {
            super();
            this.nv = nv;
        }

        public void visitBegin(){
            if( nv != null ){
                nv.visitBegin();
            }
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
        public void visitContentAttr(String ns, String name, int resourceId, int type, Object obj) {
            if (nv != null) {
                nv.visitContentAttr(ns, name, resourceId, type, obj);
            }
        }

        public void visitContentEnd(){
            if(nv != null){
                nv.visitContentEnd();
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
        public void visitContentText(int lineNumber, String value) {
            if (nv != null) {
                nv.visitContentText(lineNumber, value);
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

    public void visitBegin(){
        if (av != null){
            av.visitBegin();
        }
    }

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
     * @param namespace
     * @param name
     * @return
     */
    public NodeVisitor visitFirst(String namespace, String name) {
        if (av != null) {
            return av.visitFirst(namespace, name);
        }
        return null;
    }

    /**
     * create a namespace
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
