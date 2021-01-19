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

import org.isis.gme.bon.JBuilder;
import org.isis.gme.mga.MgaClient;
import org.isis.gme.mga.MgaClients;
import org.isis.gme.mga.MgaProject;
import org.isis.jaut.*;

public class GMEOLEApp  extends Dispatch{
    
	public static final int MSG_NORMAL = 0;
	public static final int MSG_INFO = 1;
	public static final int MSG_WARNING = 2;
	public static final int MSG_ERROR =3;
	
    public GMEOLEApp()
    {
    	
    }
    
    public GMEOLEApp(JBuilder builder)
    {
		this(builder.getProject());
    }
	
    public GMEOLEApp(MgaProject project)
    {
    	MgaClient client = null;
    	Object clients = project.get("Clients");
		MgaClients mgaClients = new MgaClients((Dispatch)clients);
		for(MgaClient c: mgaClients.getAll()){
			if(c.getName().equals("GME.Application")){
				client=c;
				break;
			}			
		}
        attach( client.getOLEServer() );
        changeInterface( "{81191A44-B898-4143-BF8B-CA7501FEC19A}" );
    }
    
    public GMEOLEApp( Dispatch d )
    {
        attach( d );
        changeInterface( "{81191A44-B898-4143-BF8B-CA7501FEC19A}" );
        
    }
    
    public String getVersion()
    {
        return get( "Version" ).toString();
    }
    
    public MgaProject getProject()
    {
        return new MgaProject((Dispatch)get( "MgaProject" ));
    }
    
    public boolean consoleMessage(String message, int message_type){
    	call("ConsoleMessage",message,message_type);
    	return true;
    }
    
    public void consoleClear(){
    	//call("ConsoleClear");
    	put("ConsoleContents","");
    }
    
    public GMEOLEColl getPanels()
    {
        return new GMEOLEColl((Dispatch)get( "Panels" ));
    }
    
}
