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


package org.isis.gme.mga;

import org.isis.jaut.Dispatch;

public class MgaClients  extends Dispatch
{

    public MgaClients( Dispatch d )
    {
        attach( d );
        /* Object interface: IMgaClients, ver. 0.0,
        GUID={0xc9d8df93,0xc1fd,0x11d3,{0x9a,0xd2,0x00,0xaa,0x00,0xb6,0xfe,0x26}} */

        changeInterface( "{c9d8df93-c1fd-11d3-9ad2-00aa00b6fe26}" );
    }
    
    public int getCount()
    {
        return ((Integer)get( "Count" )).intValue();
    }
    
    public MgaClient getItem( int index )
    {
        return new MgaClient( (Dispatch)get( "Item", new Integer(index+1) ) );
    }
    
    public MgaClient[] getAll()
    {
        int       count = getCount();
        MgaClient[]  all   = new MgaClient[count];

        for( int i=0; i<count; i++ )
            all[i] = getItem(i);
        return all;
    }
}