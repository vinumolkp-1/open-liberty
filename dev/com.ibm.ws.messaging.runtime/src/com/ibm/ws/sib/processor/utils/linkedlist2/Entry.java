/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
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
package com.ibm.ws.sib.processor.utils.linkedlist2;

import com.ibm.websphere.ras.TraceComponent;
import com.ibm.ejs.ras.TraceNLS;
import com.ibm.websphere.sib.exception.SIErrorException;
import com.ibm.ws.ffdc.FFDCFilter;
import com.ibm.ws.sib.processor.SIMPConstants;
import com.ibm.ws.sib.utils.ras.SibTr;

/**
 * An entry in the linked list. Many of the methods on Entry are unsynchronized because
 * the entire linked list needs to be synchronized by the user before calling them.
 * 
 * @author tevans
 */
public class Entry
{
  //the list to which this entry is a part
  protected LinkedList parentList = null;
  //The previous entry in the list
  protected Entry previous = null;
  //The next entry in the list
  protected Entry next = null;
  
  private static final TraceComponent tc =
    SibTr.register(
      Entry.class,
      SIMPConstants.MP_TRACE_GROUP,
      SIMPConstants.RESOURCE_BUNDLE);

  
  //NLS for component
   private static final TraceNLS nls =
     TraceNLS.getTraceNLS(SIMPConstants.RESOURCE_BUNDLE);
  
  /**
   * Unsynchronized. Get the previous entry in the list.
   * 
   * @return the previous entry in the list
   */
  public Entry getPrevious()
  {
    checkEntryParent();

    Entry entry = null;

    if(!isFirst())
    {
      entry = previous;
    }

    return entry;
  }
  
  /**
   * Unsynchronized. Get the next entry in the list.
   * 
   * @return the next entry in the list
   */
  public Entry getNext()
  {
    checkEntryParent();
    
    Entry entry = null;
    
    if(!isLast())
    {
      entry = next;
    }

    return entry;
  }
  
  /**
   * Unsynchronized. Insert a new entry in after this one.
   * 
   * @param newEntry The entry to be inserted after this one
   */
  Entry insertAfter(Entry newEntry)
  {
    if (tc.isEntryEnabled())
      SibTr.entry(
        tc,
        "insertAfter",
        new Object[] { newEntry });
    
    checkEntryParent();
    
    //make sure that the new entry is not already in a list
    if(newEntry.parentList == null)
    {
      //get the next entry in the list
      Entry nextEntry = getNext();
      //link the new entry to the next one
      newEntry.previous = this;
      //link the new entry to the this one
      newEntry.next = nextEntry;
      newEntry.parentList = parentList;
      
      if(nextEntry != null)
      {
        //link the next entry to the new one
        nextEntry.previous = newEntry;
      }      
      //link this entry to the new one
      next = newEntry;
      //if this entry was the last one in the list
      if(isLast())
      {
        //mark the new one as the last entry
        parentList.last = newEntry;                       
      }
   
      if (tc.isEntryEnabled())
        SibTr.exit(tc, "insertAfter", newEntry);

      return newEntry;
    }
    
    //if the new entry was already in a list then throw a runtime exception
    SIErrorException e = new SIErrorException(
      nls.getFormattedMessage(
        "INTERNAL_MESSAGING_ERROR_CWSIP0001",
        new Object[] {
          "com.ibm.ws.sib.processor.utils.linkedlist.Entry",
          "1:154:1.3" },
        null));
      
    FFDCFilter.processException(
      e,
      "com.ibm.ws.sib.processor.utils.linkedlist.Entry.insertAfter",
      "1:160:1.3",
      this);
        
    SibTr.exception(tc, e);  
    SibTr.error(tc, "INTERNAL_MESSAGING_ERROR_CWSIP0001",
      new Object[] {
        "com.ibm.ws.sib.processor.utils.linkedlist.Entry",
        "1:167:1.3" });    

    if (tc.isEntryEnabled())
      SibTr.exit(tc, "insertAfter", e);
      
    throw e;
  }
  
  /**
   * Unsynchronized. Removes this entry from the list.
   */
  Entry remove()
  {
    if (tc.isEntryEnabled())
      SibTr.entry(tc, "remove");

    checkEntryParent();
    
    Entry removedEntry = null;    

    Entry prevEntry = getPrevious();
    if(prevEntry != null)
    {
      //link the previous entry to the next one
      prevEntry.next = next;
    }
    Entry nextEntry = getNext();
    if(nextEntry != null)
    {
      //link the next entry to the previous one
      nextEntry.previous = prevEntry;
    }
    if(isFirst())
    {
      //if this entry was the first in the list,
      //mark the next one as the first entry
      parentList.first = nextEntry;              
    }
    if(isLast())
    {
      //if this entry was the last in the list,
      //mark the previous one as the last entry
      parentList.last = prevEntry;              
    }
    
    //set all of this entry's fields to null to make it absolutely
    //sure that it is no longer in the list
    next = null;
    previous = null;
    parentList = null;
    
    removedEntry = this;
    
    if (tc.isEntryEnabled())
      SibTr.exit(tc, "remove", removedEntry);
      
    return removedEntry;
  }
  
  /**
   * Unsynchronized. Check that the parent list is not null and therefore the entry
   * is still valid. Otherwise, throw a runtime exception
   */
  void checkEntryParent()
  {
    if(parentList == null)
    {
      SIErrorException e = new SIErrorException(
        nls.getFormattedMessage(
          "INTERNAL_MESSAGING_ERROR_CWSIP0001",
          new Object[] {
            "com.ibm.ws.sib.processor.utils.linkedlist.Entry",
            "1:239:1.3" },
          null));
          
      // FFDC
      FFDCFilter.processException(
        e,
        "com.ibm.ws.sib.processor.utils.linkedlist.Entry.checkEntryParent",
        "1:246:1.3",
        this);

      SibTr.exception(tc, e);   
      SibTr.error(tc, "INTERNAL_MESSAGING_ERROR_CWSIP0001",
        new Object[] {
          "com.ibm.ws.sib.processor.utils.linkedlist.Entry",
          "1:253:1.3" });   

      if (tc.isEntryEnabled())
        SibTr.exit(tc, "checkEntryParent", e);

      throw e;
    }
  }
  
  /**
   * A convience method to check if this entry is the first in the list.
   * It is assumed that the entry is a valid one in the list.
   * 
   * @return true if this is the first entry
   */
  boolean isFirst()
  {
    return parentList.first == this;
  }
  
  /**
   * A convience method to check if this entry is the last in the list.
   * It is assumed that the entry is a valid one in the list.
   * 
   * @return true if this is the last in the list
   */
  boolean isLast()
  {
    return parentList.last == this;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return toString("");
  }
  
  /**
   * Return a string representation of this entry and any cursors
   * what are pointing to it.
   * 
   * @param indent
   * @return string representation of this entry
   */
  public String toString(String indent)
  {    
    StringBuffer buffer = new StringBuffer();
    
    if(parentList == null)
    {
      buffer.append("Entry not in list - ");
      buffer.append(super.toString());
    }
    else
    {
      buffer.append(indent);
      buffer.append(super.toString());    
    }
  
    return buffer.toString();
  }
}
