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

import org.isis.gme.meta.*;
import org.isis.jaut.*;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaObject extends Dispatch
{
    public static final int OBJTYPE_NULL        = 0;
    public static final int OBJTYPE_MODEL       = 1;
    public static final int OBJTYPE_ATOM        = 2;
    public static final int OBJTYPE_REFERENCE   = 3;
    public static final int OBJTYPE_CONNECTION  = 4;
    public static final int OBJTYPE_SET         = 5;
    public static final int OBJTYPE_FOLDER      = 6;
    public static final int OBJTYPE_ASPECT      = 7;
    public static final int OBJTYPE_ROLE        = 8;
    public static final int OBJTYPE_ATTRIBUTE   = 9;
    public static final int OBJTYPE_PART        = 10;
    
    public static final int ATTVAL_NULL         = 0;
    public static final int ATTVAL_STRING       = 1;
    public static final int ATTVAL_INTEGER      = 2;
    public static final int ATTVAL_DOUBLE       = 3;
    public static final int ATTVAL_BOOLEAN      = 4;
    public static final int ATTVAL_REFERENCE    = 5;
    public static final int ATTVAL_ENUM         = 6;
    public static final int ATTVAL_DYNAMIC      = 7;
        
    public MgaObject()
    {
    }
    
    public MgaObject( Dispatch d )
    {
        attach( d );
        changeInterface( "{32D1F3A7-D276-11D3-9AD5-00AA00B6FE26}" );
    }
    
    public int getStatus()
    {
        return ((Integer)get( "Status" )).intValue();
    }
    
    public boolean getIsWritable()
    {
        return ((Boolean)get( "IsWritable" )).booleanValue();
    }
    
    public String getID()
    {
        return get( "ID" ).toString();
    }
    
    public String getName()
    {
        return get( "Name" ).toString();
    }
    
    public void setName( String name )
    {
        put( "Name", name );
    }
    
    public MgaMetaBase getMetaBase()
    {
        return new MgaMetaBase( (Dispatch)get( "MetaBase" ) );
    }
    
    public int getObjType()
    {
        return ((Integer)get("ObjType")).intValue();
    }
    
    public MgaProject getProject()
    {
        return new MgaProject( (Dispatch)get("Project") );
    }
    
    public MgaTerritory getTerritory()
    {
        return new MgaTerritory( (Dispatch)get("Territory") );
    }
    
    public boolean getIsEqual( MgaObject obj )
    {
        return ((Boolean)call( "IsEqual", obj)).booleanValue();
    }
    
	public MgaObject getParent()
    {                       
        Variant parent     = new Variant( new Dispatch() );
        Variant parent_ref = new Variant( parent );                  
        call( "GetParent", parent_ref, 0 );                
        if( parent.getDispatch().isNull() )
            return null;
        else      
            return new MgaObject( parent.getDispatch() );
    }
    
    public void checkProject( MgaProject proj )
    {
        call( "CheckProject", proj );
    }
    
    public void destroyObject()
    {
        call( "DestroyObject" );
    }
    
    public void open( int mode )
    {
        call( "Open", new Integer(mode) );
    }
    
    public void open()
    {
        call( "Open" );
    }
    
    public void close()
    {
        call( "Close" );
    }
    
    public void associate( Variant userdata )
    {
        call( "Associate", userdata );
    }
    
/*	public Variant getCurrentAssociation()
        {
                // EZ TENYLEG KERDESES
 
                //Variant userdata     = new Variant( new Integer(132) );
                //Variant userdata_ref = new Variant(userdata);
 
                Variant v = Variant.create( Variant.VT_EMPTY );
 
                System.out.println( v.getVartype() );
                System.out.println( v.toLong() );
 
                invoke( getIDOfName( "CurrentAssociation" ), DISPATCH_PROPERTYGET, null, null, v );
 
                System.out.println( v.getVartype() );
                System.out.println( v.toLong() );
 
                //Variant v2 = new Variant( Variant.VT_EMPTY );
 
                return null;
        }*/
    
    public void sendEvent( int mask )
    {
        call( "SendEvent", new Integer(mask) );
    }
    
    public int getRelID()
    {
        return ((Integer)get( "RelID" )).intValue();
    }
    
    public void setRelID( int newVal )
    {
        put( "RelID", new Integer(newVal) );
    }
    
    public boolean getIsLibObject()
    {
        return ((Boolean)get( "IsLibObject" )).booleanValue();
    }
    
    public void check()
    {
        call( "Check" );
    }
    
    public void checkTree()
    {
        call( "CheckTree" );
    }
    
    public void setExempt( boolean newVal )
    {
        put( "Exempt", new Boolean(newVal));
    }
    
    public boolean getExempt()
    {
        return ((Boolean)get( "Exempt" )).booleanValue();
    }
    
    public MgaObjects getChildObjects()
    {
        return new MgaObjects( (Dispatch)get( "ChildObjects" ) );
    }
    
    public MgaObject getChildObjectByRelID( int id )
    {
        return new MgaObject( (Dispatch)call( "ChildObjectByRelID", new Integer(id) ) );
    }
    
    public MgaObject getObjectByPath( String path )
    {
        return new MgaObject( (Dispatch)call( "ObjectByPath", path ) );
    }
}
