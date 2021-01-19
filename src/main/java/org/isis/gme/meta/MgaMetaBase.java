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

package org.isis.gme.meta;

import org.isis.jaut.Dispatch;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaMetaBase extends Dispatch
{
    protected MgaMetaBase()
    {
    }
    
    public MgaMetaBase( Dispatch d )
    {
        attach( d );
        changeInterface( "{DB1E0FB7-C9CB-11D3-9AD2-00AA00B6FE26}" );
    }
    
    public int getMetaRef()
    {
        return ((Integer)get( "MetaRef" )).intValue();
    }
    
    public void setMetaRef(int metaRef)
    {
        put( "MetaRef", new Integer(metaRef) );
    }
    
    public MgaMetaProject getMetaProject()
    {
        return new MgaMetaProject( (Dispatch)get( "MetaProject") );
    }
    
    public String getName()
    {
        return (String)get( "Name");
    }
    
    public void setName(String name)
    {
        put( "Name", name );
    }
    
    public String getDisplayedName()
    {
        return (String)get( "DisplayedName" );
    }
    
    public void setDisplayedName(String name)
    {
        put( "DisplayedName", name );
    }
    
    public int getObjType()
    {
        return ((Integer)get( "ObjType")).intValue();
    }
    
    public void delete()
    {
        call( "Delete" );
    }
    
    public MgaMetaRegNodes getRegistryNodes()
    {
        return new MgaMetaRegNodes( (Dispatch)get( "RegistryNodes") );
    }
    
    public MgaMetaRegNode getRegistryNode(String path)
    {
        return new MgaMetaRegNode( (Dispatch)get( "RegistryNode", path ) );
    }
    
    public String getRegistryValue( String path )
    {
        return (String)get("RegistryValue", path );
    }
    
    public void setRegistryValue( String path, String value )
    {
        call( "RegistryValue", path, value );
    }
    
    public MgaConstraints getConstraints()
    {
        return new MgaConstraints( (Dispatch)get( "Constraints") );
    }
    
    public MgaConstraint createConstraint()
    {
        return new MgaConstraint( (Dispatch)call( "CreateConstraint" ) );
    }
}
