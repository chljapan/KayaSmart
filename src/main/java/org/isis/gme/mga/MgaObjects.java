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
 * Author: Gyorgy Balogh
 * Date last modified: 10/23/03
 */

package org.isis.gme.mga;

import org.isis.jaut.*;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaObjects extends Dispatch
{
    public MgaObjects( Dispatch d )
    {
        attach( d );
        changeInterface( "{c5aac2f0-c1fd-11d3-9ad2-00aa00b6fe26}" );
    }
    
    public int getCount()
    {
        return ((Integer)get( "Count" )).intValue();
    }
    
    public MgaObject getItem( int index )
    {
        return new MgaObject( (Dispatch)get( "Item", new Integer(index+1) ) );
    }
    
    // this should be replaced to native implementation
    public MgaObject[] getAll()
    {
        int         count = getCount();
        MgaObject[] all   = new MgaObject[count];
        
        for( int i=0; i<count; i++ )
            all[i] = getItem(i);
        return all;
    }
    
    /*public MgaObject[] getAll()
    {
            int 		count = getCount();
            MgaObject[] all   = new MgaObject[count];
            Variant     v     = new Variant( all );
            
            Variant[] arg = new Variant[2];
            arg[0] = new Variant( new Integer(count) );
            arg[1] = new Variant( all );            
            
            invoke( getIDOfName("GetAll"), DISPATCH_METHOD, arg, null, null );
                        
                 
            return all;
    }*/
    
    public void insert( MgaObject obj, int position )
    {
        //call( "Insert", new Variant(obj), new Integer(position) );
        //call( "Insert", obj, new Integer(position) );
    }
    
    public void append( MgaObject obj )
    {
        //Variant v1 = new Variant( obj );
        //Variant v2 = new Variant( v1 );
        //call( "Append", v2 );
    }
    
    public int find(MgaObject p, int lastParam)
    {
        //return Dispatch.call(this, "Find", p, new Variant(lastParam)).toInt();
        return 0;
    }
    
    public void remove( int pos )
    {
        call( "Remove", new Integer(pos) );
    }
    
    
    
/*	[helpstring("method GetAll")] \
        HRESULT GetAll([in] long count, [out, size_is(count)] ELEM **p); \
\
    [helpstring("method Insert, index starts with 1, element may already be contained")] \
        HRESULT Insert([in] ELEM *p, [in] long at); \
\
        [helpstring("method Append, add to the end, may already be contained")] \
        HRESULT Append([in] ELEM *p); \
\
        [helpstring("method Find, returns 0 if none found after pos start")] \
        HRESULT Find([in] ELEM *p, [in] long start, [out, retval] long *res); \
\
        [helpstring("method Remove, index starts with 1")] \
        HRESULT Remove([in] long n); \*/
    
    //public
    
}
