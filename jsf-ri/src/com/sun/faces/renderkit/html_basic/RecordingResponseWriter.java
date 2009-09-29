/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


/*
 This class records all actions against a ResponseWriter for later playback.
 This is necessary for something like MenuRenderer, due to spec bug 637.
*/
public class RecordingResponseWriter extends ResponseWriter {

    private List<Command> record = new ArrayList<Command>();

    private enum CommandType {
        START_DOC,
        END_DOC,
        START_ELE,
        END_ELE,
        WRITE_ATTR,
        WRITE_URI,
        START_CDATA,
        END_CDATA,
        WRITE_COMMENT,
        WRITE_TEXT,
        WRITE_TEXT_ARR,
        WRITE
    }

    public RecordingResponseWriter() {
    }

    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    public void flush() {
        throw new UnsupportedOperationException();
    }

    public void startDocument() {
        record.add(new Command(CommandType.START_DOC));
    }

    public void endDocument() {
        record.add(new Command(CommandType.END_DOC));

    }

    public void startElement(String name, UIComponent component)
            throws IOException {
        record.add(new Command(CommandType.START_ELE, name, component));

    }

    public void endElement(String name) {
        record.add(new Command(CommandType.END_ELE, name));
    }

    public void writeAttribute(String name, Object value,
                               String property) {
        record.add(new Command(CommandType.WRITE_ATTR, name, value, property));
    }

    public void writeURIAttribute(String name, Object value,
                                  String property) {
        record.add(new Command(CommandType.WRITE_URI, name, value, property));
    }

    public void startCDATA() {
        record.add(new Command(CommandType.START_CDATA));
    }

    public void endCDATA() {
        record.add(new Command(CommandType.END_CDATA));
    }

    public void writeComment(Object comment) {
        record.add(new Command(CommandType.WRITE_COMMENT, comment));
    }

    public void writeText(Object text, String property)
            throws IOException {
        record.add(new Command(CommandType.WRITE_TEXT, text, property));
    }

    public void writeText(Object text, UIComponent component, String property)
            throws IOException {
        writeText(text, property);
    }

    public void writeText(char text[], int off, int len)
            throws IOException {
        record.add(new Command(CommandType.WRITE_TEXT_ARR, text, off, len));
    }

    public void write(char text[], int off, int len) {
        record.add(new Command(CommandType.WRITE, text, off, len));
    }

    public void close() {
        throw new UnsupportedOperationException();
    }

    public ResponseWriter cloneWithWriter(Writer writer) {
        throw new UnsupportedOperationException();
    }

    public void replay(ResponseWriter writer) throws IOException {

        for (Command command : record) {
            switch (command.getCommandType()) {
                case START_DOC:
                    writer.startDocument();
                    break;
                case END_DOC:
                    writer.endDocument();
                    break;
                case START_ELE:
                    writer.startElement((String) command.getOne(), (UIComponent) command.getTwo());
                    break;
                case END_ELE:
                    writer.endElement((String) command.getOne());
                    break;
                case START_CDATA:
                    writer.startCDATA();
                    break;
                case END_CDATA:
                    writer.endCDATA();
                    break;
                case WRITE_ATTR:
                    writer.writeAttribute((String) command.getOne(), command.getTwo(), (String) command.getThree());
                    break;
                case WRITE_URI:
                    writer.writeURIAttribute((String) command.getOne(), command.getTwo(), (String) command.getThree());
                    break;
                case WRITE_COMMENT:
                    writer.writeComment(command.getOne());
                    break;
                case WRITE_TEXT:
                    writer.writeText(command.getOne(), (String) command.getTwo());
                    break;
                case WRITE_TEXT_ARR:
                    writer.writeText((char[]) command.getOne(), (Integer) command.getTwo(), (Integer) command.getThree());
                    break;
                case WRITE:
                    writer.write((char[]) command.getOne(), (Integer) command.getTwo(), (Integer) command.getThree());
                    break;
            }
        }
    }

    /*
      Object to be held in the list of recorded commands.
      The object has a command, and up to three parameters, depending on the command.
     */
    class Command {
        CommandType com;
        Object one = null;
        Object two = null;
        Object three = null;

        Command(CommandType com) {
            this.com = com;
        }

        Command(CommandType com, Object one) {
            this.com = com;
            this.one = one;
        }

        Command(CommandType com, Object one, Object two) {
            this.com = com;
            this.one = one;
            this.two = two;
        }

        Command(CommandType com, Object one, Object two, Object three) {
            this.com = com;
            this.one = one;
            this.two = two;
            this.three = three;
        }

        CommandType getCommandType() {
            return com;
        }

        Object getOne() {
            return one;
        }

        Object getTwo() {
            return two;
        }

        Object getThree() {
            return three;
        }
    }

}
