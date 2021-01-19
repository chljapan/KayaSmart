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
import org.isis.jaut.Dispatch;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaAttribute extends Dispatch 
{
	public MgaAttribute()
	{
	}

	public MgaAttribute( Dispatch d )
	{
		attach( d );
		changeInterface( "{CBF20084-BD43-11D3-9AD2-00AA00B6FE26}" );
	}

	public MgaMetaAttribute getMeta()
	{
		return new MgaMetaAttribute( (Dispatch)get( "Meta" ) );
	}

	public Object getValue()
	{
		return get( "Value" );
	}

	public void setValue( Object val )
	{
		call( "Value", val );
	}

	public MgaFCO getOwner()
	{
		return new MgaFCO( (Dispatch)get( "Owner" ) );
	}

	public int getStatus()
	{
		return ((Integer)get( "Status" )).intValue();
	}

	public Object getOrigValue()
	{
		return get( "OrigValue" );
	}

	public boolean hasChanged()
	{
		return ((Boolean)get( "HasChanged" )).booleanValue();
	}

	public String getStringValue()
	{
		return (String)get( "StringValue" );
	}

	public void setStringValue( String val )
	{
		call( "StringValue", val );
	}

	public int getIntValue()
	{
		return ((Integer)get( "IntValue" )).intValue();
	}

	public void setIntValue( int val )
	{
		call( "IntValue", new Integer(val) );
	}

	public boolean getBoolValue()
	{
		return ((Boolean)get( "BoolValue" )).booleanValue();
	}

	public void setBoolValue( boolean val )
	{
		call( "BoolValue", new Boolean(val) );
	}

	public double getFloatValue()
	{
		return ((Double)get( "FloatValue" )).doubleValue();
	}

	public void setFloatValue( double val )
	{
		call( "FloatValue", new Double(val) );
	}

	public MgaFCO getFCOValue()
	{
		return new MgaFCO( (Dispatch)get( "FCOValue" ) );
	}

	public void setFCOValue( MgaFCO val )
	{
		call( "FCOValue", val );
	}

	public MgaRegNode getRegistryNode( String path )
	{
		return new MgaRegNode( (Dispatch)call( "RegistryNode", path ) );
	}

	public MgaRegNodes getRegistry( boolean virtuals )
	{
		return new MgaRegNodes( (Dispatch)call( "Registry", new Boolean(virtuals)) );
	}

	public MgaRegNodes getRegistry()
	{
		return new MgaRegNodes( (Dispatch)get( "Registry" ) );
	}

	public String getRegistryValue( String path )
	{
		return (String)call( "RegistryValue", path );
	}

	public void setRegistryValue( String path, String val )
	{
		call( "RegistryValue", path, val );
	}

	public void clear()
	{
		call( "Clear" );
	}
}
