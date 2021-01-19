/*
 * Copyright (c) 2002, Vanderbilt University
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
 * Date last modified: 09/19/07
 */
package org.isis.gme.mga;

import org.isis.jaut.*;

public class MgaClient extends Dispatch {

	
    // this is temporal, should go to another class (factory)
    public static MgaClient createInstance()
    {
    	MgaClient client = new MgaClient();
    	client.attachNewInstance( "Mga.MgaClient", Dispatch.CLSCTX_INPROC_SERVER );
        return client;
    }
    
    public MgaClient()
    {
    }
    
    public MgaClient( Dispatch d )
    {
        attach( d );
        changeInterface( "{F07EE1A2-2D53-449b-A2DA-45A1A9110E53}" );
    }
    
    public String getName()
    {
        return get( "Name" ).toString();
    }
    
    public Dispatch getOLEServer()
    {
        return (Dispatch)get( "OLEServer" );
    }
    
    public MgaProject getProject()
    {
        return new MgaProject((Dispatch)get( "Project" ));
    }
   
}
