/*
 * Copyright (c) 2008, Institute for Software Integrated Systems Vanderbilt University
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 *
 * IN NO EVENT SHALL THE VANDERBILT UNIVERSITY BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE VANDERBILT
 * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE VANDERBILT UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE VANDERBILT UNIVERSITY HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 * Author: Andras Nadas
 * Contact: Andras Nadas (nadand@isis.vanderbilt.edu)
 */ 

package org.isis.gme;

import org.isis.jaut.*;

public class GMEOLEColl  extends Dispatch{
    
	
    public GMEOLEColl()
    {
    }
    
    public GMEOLEColl( Dispatch d )
    {
        attach( d );
        changeInterface( "{36C7B797-6BDE-46d0-8870-70189000EDF9}" );
        
    }
    
    public Integer getCount(){
    	return (Integer)get("Count");
    }
    
    public Dispatch getItem(Integer nIndex){
    	return (Dispatch)get("Item",nIndex);
    }
   
}
