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
public class MgaMetaProject extends Dispatch 
{
	public MgaMetaProject( Dispatch target )
	{
		attach( target );
		changeInterface( "{83BA3243-B758-11D3-ABAE-000000000000}" );
	}

	/*public MgaMetaProject()
	{
		super("Mga.MgaMetaProject");				
	}*/

	public void open( String connection ) 
	{
		call( "Open", connection );
	}

	public void close() 
	{
		call( "Close" );
	}

	public void create( String connection ) 
	{
		call( "Create", connection );
	}

	public void beginTransaction() 
	{
		call( "BeginTransaction" );
	}

	public void commitTransaction() 
	{
		call( "CommitTransaction" );
	}

	public void abortTransaction() 
	{
		call( "AbortTransaction" );
	}

	/*public byte[] getGUID() 
	{
		return Dispatch.get(this, "GUID").toSafeArray().toByteArray();
	}

	public void setGUID(byte[] guid) 
	{
		SafeArray array = new SafeArray(Variant.VariantByte, 16);
		array.fromByteArray(guid);

		Variant v = new Variant();
		v.putSafeArray(array);

		Dispatch.put(this, "GUID", v);
	}*/

	public String getName() 
	{
		return (String)get( "Name" );
	}

	public void setName( String name ) 
	{
		put( "Name", name );
	}

	public String getDisplayedName() 
	{
		return (String)get( "DisplayedName" );
	}

	public void setDisplayedName( String name )
	{
		put( "DisplayedName", name );
	}

	public String getVersion()
	{
		return (String)get( "Version" );
	}
	
	public void setVersion( String version )
	{
		put( "Version", version );
	}

	public String getAuthor()
	{
		return (String)get( "Author" );
	}

	public void setAuthor( String author )
	{
		put( "Author", author );
	}

	public String getComment()
	{
		return (String)get( "Comment" );
	}

	public void setComment( String comment )
	{
		put( "Comment", comment );
	}

	public String getCreatedAt()
	{
		return (String)get( "CreatedAt" );
	}

	public String getModifiedAt()
	{
		return (String)get( "ModifiedAt" );
	}

	public MgaMetaFolder getRootFolder()
	{
		return new MgaMetaFolder( (Dispatch)get("RootFolder") );
	}

	public MgaMetaBase findObject( int metaRef )
	{
		return new MgaMetaBase( (Dispatch)get( "FindObject", new Integer(metaRef) ) );
	}
}
