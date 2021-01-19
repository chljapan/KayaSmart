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
public class MgaMetaFCO extends MgaMetaBase
{
    protected MgaMetaFCO()
    {
    }
    
    public MgaMetaFCO( Dispatch d )
    {
        attach( d );
        changeInterface( "{83BA3247-B758-11D3-ABAE-000000000000}" );
    }
    
    public MgaMetaBase getDefinedIn()
    {
        return new MgaMetaBase( (Dispatch)get( "DefinedIn") );
    }
    
    public MgaMetaAttributes getDefinedAttributes()
    {
        return new MgaMetaAttributes( (Dispatch)get( "DefinedAttributes") );
    }
    
    public MgaMetaAttribute getDefinedAttributeByName(String name, boolean inScope)
    {
        return new MgaMetaAttribute( (Dispatch)call( "DefinedAttributeByName", name, new Boolean(inScope)) );
    }
    
    public MgaMetaAttribute defineAttribute()
    {
        return new MgaMetaAttribute( (Dispatch)call( "DefineAttribute" ) );
    }
    
    public MgaMetaRoles getUsedInRoles()
    {
        return new MgaMetaRoles( (Dispatch)get( "UsedInRoles" ) );
    }
    
    public MgaMetaFolders getUsedInFolders()
    {
        return new MgaMetaFolders( (Dispatch)get( "UsedInFolders" ) );
    }
    
    public MgaMetaAttributes getAttributes()
    {
        return new MgaMetaAttributes( (Dispatch)get( "Attributes" ) );
    }
    
    public MgaMetaAttribute getAttributeByName( String name )
    {
        return new MgaMetaAttribute( (Dispatch)get( "AttributeByName", name ) );
    }
    
    public MgaMetaAttribute getAttributeByRef( int metaRef )
    {
        return new MgaMetaAttribute( (Dispatch)get( "AttributeByRef", new Integer(metaRef)) );
    }
    
    public boolean getAliasingEnabled()
    {
        return ((Boolean)get( "AliasingEnabled")).booleanValue();
    }
    
    public void setAliasingEnabled( boolean enable )
    {
        put( "AliasingEnabled", new Boolean(enable) );
    }
    
    public void addAttribute( MgaMetaAttribute attribute )
    {
        call( "AddAttribute", attribute );
    }
    
    public void removeAttribute( MgaMetaAttribute attribute )
    {
        call( "RemoveAttribute", attribute );
    }
}
