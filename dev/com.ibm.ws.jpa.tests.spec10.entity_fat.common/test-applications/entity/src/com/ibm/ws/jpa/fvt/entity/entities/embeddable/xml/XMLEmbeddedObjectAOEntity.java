/*******************************************************************************
 * Copyright (c) 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.ibm.ws.jpa.fvt.entity.entities.embeddable.xml;

import com.ibm.ws.jpa.fvt.entity.entities.IEmbeddedObjectEntity;
import com.ibm.ws.jpa.fvt.entity.entities.embeddable.SimpleEmbeddableObject;

public class XMLEmbeddedObjectAOEntity implements IEmbeddedObjectEntity {
    private int id;

    private int localIntVal;
    private String localStrVal;

    private SimpleEmbeddableObject embeddedObj;

    public XMLEmbeddedObjectAOEntity() {
        embeddedObj = new SimpleEmbeddableObject();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getLocalIntVal() {
        return localIntVal;
    }

    @Override
    public void setLocalIntVal(int localIntVal) {
        this.localIntVal = localIntVal;
    }

    @Override
    public String getLocalStrVal() {
        return localStrVal;
    }

    @Override
    public void setLocalStrVal(String localStrVal) {
        this.localStrVal = localStrVal;
    }

    @Override
    public SimpleEmbeddableObject getEmbeddedObj() {
        return embeddedObj;
    }

    @Override
    public void setEmbeddedObj(SimpleEmbeddableObject embeddedObj) {
        this.embeddedObj = embeddedObj;
    }

    @Override
    public String toString() {
        return "XMLEmbeddedObjectAOEntity [id=" + id + "]";
    }
}
