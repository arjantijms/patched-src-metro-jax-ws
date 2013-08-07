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
package com.sun.xml.ws.handler;

import com.sun.xml.ws.api.message.AttachmentSet;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.message.EmptyMessageImpl;
import com.sun.xml.ws.message.source.PayloadSourceMessage;
import javax.xml.transform.Source;

import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalMessageContext;

/**
 * Implementation of LogicalMessageContext. This class is used at runtime
 * to pass to the handlers for processing logical messages.
 *
 * <p>This Class delegates most of the fuctionality to Packet
 *
 * @see Packet
 *
 * @author WS Development Team
 */
class LogicalMessageContextImpl extends MessageUpdatableContext implements LogicalMessageContext {
    private LogicalMessageImpl lm;
    private WSBinding binding;

    public LogicalMessageContextImpl(WSBinding binding, Packet packet) {
        super(packet);
        this.binding = binding;
    }

    public LogicalMessage getMessage() {
        if(lm == null)
            lm = new LogicalMessageImpl(packet);
        return lm;
    }

    void setPacketMessage(Message newMessage){
        if(newMessage != null) {
            packet.setMessage(newMessage);
            lm = null;
        }
    }


    protected void updateMessage() {
        //If LogicalMessage is not acccessed, its not modified.
        if(lm != null) {
            //Check if LogicalMessageImpl has changed, if so construct new one
            //TODO: Attachments are not used
            // Packet are handled through MessageContext
            if(lm.isPayloadModifed()){
                Message msg = packet.getMessage();
                HeaderList headers = msg.getHeaders();
                AttachmentSet attachments = msg.getAttachments();
                Source modifiedPayload = lm.getModifiedPayload();
                if(modifiedPayload == null){
                    packet.setMessage(new EmptyMessageImpl(headers,attachments,binding.getSOAPVersion()));
                } else {
                    packet.setMessage(new PayloadSourceMessage(headers,modifiedPayload, attachments, binding.getSOAPVersion()));
                }
            }
            lm = null;
        }

    }

}