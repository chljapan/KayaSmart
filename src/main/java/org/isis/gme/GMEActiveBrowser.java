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

public class GMEActiveBrowser  extends Dispatch{
	
    public GMEActiveBrowser()
    {
    }
    
    public GMEActiveBrowser( Dispatch d )
    {
        attach( d );
        changeInterface( "{9D345706-0DE7-4A6A-B32A-DD1BB2F8B81F}" );
        
    }
    
    public Dispatch getSelected(){
    	return (Dispatch)get("GetSelectedMgaObjects");
    }
    
}
