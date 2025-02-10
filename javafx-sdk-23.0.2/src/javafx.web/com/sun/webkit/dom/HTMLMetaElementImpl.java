/*
 * Copyright (c) 2013, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLMetaElement;

public class HTMLMetaElementImpl extends HTMLElementImpl implements HTMLMetaElement {
    HTMLMetaElementImpl(long peer) {
        super(peer);
    }

    static HTMLMetaElement getImpl(long peer) {
        return (HTMLMetaElement)create(peer);
    }


// Attributes
    @Override
    public String getContent() {
        return getContentImpl(getPeer());
    }
    native static String getContentImpl(long peer);

    @Override
    public void setContent(String value) {
        setContentImpl(getPeer(), value);
    }
    native static void setContentImpl(long peer, String value);

    @Override
    public String getHttpEquiv() {
        return getHttpEquivImpl(getPeer());
    }
    native static String getHttpEquivImpl(long peer);

    @Override
    public void setHttpEquiv(String value) {
        setHttpEquivImpl(getPeer(), value);
    }
    native static void setHttpEquivImpl(long peer, String value);

    @Override
    public String getName() {
        return getNameImpl(getPeer());
    }
    native static String getNameImpl(long peer);

    @Override
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }
    native static void setNameImpl(long peer, String value);

    @Override
    public String getScheme() {
        return getSchemeImpl(getPeer());
    }
    native static String getSchemeImpl(long peer);

    @Override
    public void setScheme(String value) {
        setSchemeImpl(getPeer(), value);
    }
    native static void setSchemeImpl(long peer, String value);

}

