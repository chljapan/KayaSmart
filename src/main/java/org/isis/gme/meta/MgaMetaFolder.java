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

import org.isis.jaut.*;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaMetaFolder extends MgaMetaBase
{
	protected MgaMetaFolder()
	{
	}
	
	public MgaMetaFolder( Dispatch target )
	{
		attach( target );
		changeInterface( "{83BA3245-B758-11D3-ABAE-000000000000}" );
	}	

	public MgaMetaFolder getDefinedIn()
	{
		return new MgaMetaFolder( (Dispatch)get( "DefinedIn" ) );
	}

	public MgaMetaFolders getDefinedFolders()
	{
		return new MgaMetaFolders( (Dispatch)get("DefinedFolders") );
	}

	public MgaMetaFolder getDefinedFolderByName( String name, boolean inScope )
	{
		return new MgaMetaFolder( (Dispatch)call( "DefinedFolderByNameDisp", name, new Variant(inScope)) );
	}

	public MgaMetaFCOs getDefinedFCOs()
	{
		return new MgaMetaFCOs( (Dispatch)get("DefinedFCOs") );
	}

	public MgaMetaFCO getDefinedFCOByName(String name, boolean inScope)
	{
		return new MgaMetaFCO( (Dispatch)call( "DefinedFCOByNameDisp", name, new Boolean(inScope) ) );
	}

	public MgaMetaAttributes getDefinedAttributes()
	{
		return new MgaMetaAttributes( (Dispatch)get("DefinedAttributes") );
	}

	public MgaMetaAttribute getDefinedAttributeByName(String name, boolean inScope)
	{
		return new MgaMetaAttribute( (Dispatch)call( "DefinedAttributeByNameDisp", name, new Boolean(inScope)) );
	}

	public MgaMetaFolder defineFolder()
	{
		return new MgaMetaFolder( (Dispatch)call( "DefineFolder") );
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

	public MgaMetaAttribute defineAttribute()
	{
		return new MgaMetaAttribute( (Dispatch)call( "DefineAttribute" ) );
	}

	public MgaMetaFolders getLegalChildFolders()
	{
		return new MgaMetaFolders( (Dispatch)get("LegalChildFolders" ) );
	}

	public MgaMetaFolder getLegalChildFolderByName(String name)
	{
		return new MgaMetaFolder( (Dispatch)get( "LegalChildFolderByName", name ) );
	}

	public MgaMetaFCOs getLegalRootObjects()
	{
		return new MgaMetaFCOs( (Dispatch)get( "LegalRootObjects" ) );
	}

	public MgaMetaFCO getLegalRootObjectByName( String name )
	{
		return new MgaMetaFCO( (Dispatch)get( "LegalRootObjectByName", name ) );
	}

	public MgaMetaFolders getUsedInFolders()
	{
		return new MgaMetaFolders( (Dispatch)get( "UsedInFolders" ) );
	}

	public void addLegalChildFolder( MgaMetaFolder folder )
	{
		call( "AddLegalChildFolder", folder );
	}

	public void removeLegalChildFolder( MgaMetaFolder folder )
	{
		call( "RemoveLegalChildFolder", folder );
	}

	public void addLegalRootObject( MgaMetaFCO fco )
	{
		call( "AddLegalRootObject", fco );
	}

	public void removeLegalRootObject( MgaMetaFCO fco )
	{
		call( "RemoveLegalRootObject", fco );
	}
}
