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
public class MgaMetaModel extends MgaMetaFCO
{
    protected MgaMetaModel()
    {
    }
    
    public MgaMetaModel(Dispatch d)
    {
        attach( d );
        changeInterface( "{83BA3249-B758-11D3-ABAE-000000000000}" );
    }
    
    public MgaMetaFCOs getDefinedFCOs()
    {
        return new MgaMetaFCOs( (Dispatch)get( "DefinedFCOs" ) );
    }
    
    public MgaMetaFCO getDefinedFCOByName( String name, boolean inScope )
    {
        return new MgaMetaFCO( (Dispatch)call( "DefinedFCOByName", name, new Boolean(inScope) ) );
    }
    
    public MgaMetaModel defineModel()
    {
        return new MgaMetaModel( (Dispatch)call( "DefineModel" ) );
    }
    
    public MgaMetaAtom defineAtom()
    {
        return new MgaMetaAtom( (Dispatch)call( "DefineAtom" ) );
    }
    
    public MgaMetaReference defineReference()
    {
        return new MgaMetaReference( (Dispatch)call( "DefineReference" ) );
    }
    
    public MgaMetaSet defineSet()
    {
        return new MgaMetaSet( (Dispatch)call( "DefineSet" ) );
    }
    
    public MgaMetaConnection defineConnection()
    {
        return new MgaMetaConnection( (Dispatch)call( "DefineConnection" ) );
    }
    
    public MgaMetaRoles getRoles()
    {
        return new MgaMetaRoles( (Dispatch)get( "Roles" ) );
    }
    
    public MgaMetaRole getRoleByName( String name )
    {
        return new MgaMetaRole( (Dispatch)get( "RoleByName", name ) );
    }
    
    public MgaMetaAspects getAspects()
    {
        return new MgaMetaAspects( (Dispatch)get( "Aspects" ) );
    }
    
    public MgaMetaAspect getAspectByName( String name )
    {
        return new MgaMetaAspect( (Dispatch)get( "AspectByName", name ) );
    }
    
    public MgaMetaRole createRole( MgaMetaFCO kind )
    {
        return new MgaMetaRole( (Dispatch)call( "CreateRole", kind ) );
    }
    
    public MgaMetaAspect createAspect()
    {
        return new MgaMetaAspect( (Dispatch)call( "CreateAspect" ) );
    }
    
    public MgaMetaRoles legalConnectionRoles( String paths )
    {
        return new MgaMetaRoles( (Dispatch)call( "LegalConnectionRoles", paths ) );
    }
    
    public MgaMetaRoles legalReferenceRoles( String path )
    {
        return new MgaMetaRoles( (Dispatch)call( "LegalReferenceRoles" ) );
    }
    
    public MgaMetaRoles legalSetRoles( String path )
    {
        return new MgaMetaRoles( (Dispatch)call( "LegalSetRoles", path ) );
    }
    
    public MgaMetaRoles legalRoles( MgaMetaFCO kind )
    {
        return new MgaMetaRoles( (Dispatch)call( "LegalRoles", kind ) );
    }
}
