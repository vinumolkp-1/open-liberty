/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.cdi.beansxml.fat.apps.classexclusion.packageprotectedbyclass;

import javax.enterprise.context.RequestScoped;

import com.ibm.ws.cdi.beansxml.fat.apps.classexclusion.interfaces.IProtectedByClassBean;

@RequestScoped
public class ProtectedByClassBean implements IProtectedByClassBean {

    @Override
    public String getOutput() {
        return "ProtectedByClassBean was correctly injected";
    }

}
