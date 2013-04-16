/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.xml.ws.transport.httpspi.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The JAX-WS dispatcher servlet.
 *
 * <p>
 * It really just forwards processing to {@link com.sun.xml.ws.transport.httpspi.servlet.WSServletDelegate}.
 *
 * @author Jitendra Kotamraju
 */
public class WSSPIServlet extends HttpServlet {
    private transient WSServletDelegate delegate = null;

    private static final Logger LOGGER = Logger.getLogger(WSSPIServlet.class.getName());
    
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        delegate = getDelegate(servletConfig);
    }

    /**
     * Gets the {@link com.sun.xml.ws.transport.httpspi.servlet.WSServletDelegate} that we will be forwarding the requests to.
     *
     * @return
     *      null if the deployment have failed and we don't have the delegate.
     */
    protected WSServletDelegate getDelegate(ServletConfig servletConfig) {
        return (WSServletDelegate) servletConfig.getServletContext().getAttribute(JAXWS_RI_RUNTIME_INFO);
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (delegate != null) {
            delegate.doPost(request,response,getServletContext());
        } else {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "No delegate for {0} to invoke post method.", this);
            }
        }
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        if (delegate != null) {
            delegate.doGet(request,response,getServletContext());
        } else {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "No delegate for {0} to invoke get method.", this);
            }
        }
    }

    @Override
    protected void doPut( HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        if (delegate != null) {
            delegate.doPut(request,response,getServletContext());
        } else {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "No delegate for {0} to invoke put method.", this);
            }
        }
    }

    @Override
    protected void doDelete( HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        if (delegate != null) {
            delegate.doDelete(request,response,getServletContext());
        } else {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "No delegate for {0} to invoke delete method.", this);
            }
        }
    }

    /**
     * {@link com.sun.xml.ws.transport.httpspi.servlet.WSServletDelegate}.
     */
    public static final String JAXWS_RI_RUNTIME_INFO =
        "com.sun.xml.ws.server.http.servletDelegate";
    public static final String JAXWS_RI_PROPERTY_PUBLISH_WSDL =
        "com.sun.xml.ws.server.http.publishWSDL";
    public static final String JAXWS_RI_PROPERTY_PUBLISH_STATUS_PAGE =
        "com.sun.xml.ws.server.http.publishStatusPage";

}
