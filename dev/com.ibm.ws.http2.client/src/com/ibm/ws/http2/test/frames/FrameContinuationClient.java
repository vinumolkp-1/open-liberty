/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
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
package com.ibm.ws.http2.test.frames;

import java.util.ArrayList;
import java.util.List;

import com.ibm.ws.http.channel.h2internal.hpack.H2HeaderField;
import com.ibm.ws.http2.test.helpers.HeaderEntry;

/**
 *
 */
public class FrameContinuationClient extends com.ibm.ws.http.channel.h2internal.frames.FrameContinuation {

    private List<HeaderEntry> headerEntries = new ArrayList<HeaderEntry>();
    private List<H2HeaderField> headerFields = new ArrayList<H2HeaderField>();

    /**
     * @param streamId
     * @param headerBlockFragment
     * @param endHeaders
     * @param endStream
     * @param reserveBit
     */
    public FrameContinuationClient(int streamId, byte[] headerBlockFragment, boolean endHeaders, boolean endStream, boolean reserveBit) {
        super(streamId, headerBlockFragment, endHeaders, endStream, reserveBit);
        // TODO Auto-generated constructor stub
    }

    public List<HeaderEntry> getHeaderEntries() {
        return headerEntries;
    }

    /**
     * Use this to send headers from the client to the server.
     */
    public void setHeaderEntries(List<HeaderEntry> headerEntries) {
        this.headerEntries = headerEntries;

        //we create a new one every time as we don't want to add the new header fields with the previous ones
        //this method should only be called once, but just in case.
        headerFields = new ArrayList<H2HeaderField>();

        for (HeaderEntry headerEntry : headerEntries)
            headerFields.add(headerEntry.getH2HeaderField());

    }

    /**
     * Use this for comparison in the test framework.
     */
    public void setHeaderFields(List<H2HeaderField> headerFields) {
        this.headerFields = headerFields;
    }

    public List<H2HeaderField> getHeaderFields() {
        return headerFields;
    }

    @Override
    public boolean equals(Object receivedFrame) {
        if (receivedFrame instanceof FrameContinuationClient) {

            FrameContinuationClient frameToCompare = (FrameContinuationClient) receivedFrame;

            if (this.flagEndHeadersSet() != frameToCompare.flagEndHeadersSet()) {
                System.out.println("this.flagEndHeadersSet() = " + this.flagEndHeadersSet() + " frameToCompare.flagEndHeadersSet() = " + frameToCompare.flagEndHeadersSet());
                return false;
            }
            if (this.getFrameType() != frameToCompare.getFrameType()) {
                System.out.println("getFrameType is false");
                return false;
            }
            if (this.getFrameReserveBit() != frameToCompare.getFrameReserveBit()) {
                System.out.println("getFrameReserveBit is false");
                return false;
            }
            if (this.getStreamId() != frameToCompare.getStreamId()) {
                System.out.println("getStreamId is false");
                return false;
            }

            //we need to check if the expected frames are in receivedFrame
            //we won't compare for extra frame in receivedFrame as some headers might not be expected
            List<H2HeaderField> receivedHeaderFields = frameToCompare.getHeaderFields();
            for (H2HeaderField headerField : getHeaderFields())
                if (!receivedHeaderFields.contains(headerField)) {
                    System.out.println("headerField mismatch. headerField: " + headerField);
                    return false;
                }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder(super.toString());

        toString.append('\n');
        toString.append("Header fields: ").append('\n');
        for (H2HeaderField headerField : getHeaderFields())
            toString.append(" ").append(headerField).append('\n');

        return toString.toString();
    }

}
