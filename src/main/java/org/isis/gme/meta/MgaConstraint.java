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
public class MgaConstraint extends Dispatch 
{
	//protected
	public MgaConstraint()
	{
	}
	
	public MgaConstraint( Dispatch d )
	{
		attach( d );
		changeInterface( "{83BA3241-B758-11D3-ABAE-000000000000}" );			
	}	

	public String getName()
	{
		return (String)get( "Name" );
	}

	public void setName(String name)
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

	public String getExpression()
	{
		return (String)get( "Expression" );
	}

	public void setExpression( String expr )
	{
		put( "Expression", expr );
	}

	public int getEventMask()
	{
		return ((Integer)get( "EventMask" )).intValue();
	}

	public void setEventMask( int mask )
	{
		put( "EventMask", new Integer(mask) );
	}

	public int getDepth()
	{
		return ((Integer)get( "Depth" )).intValue();
	}

	public void setDepth( int depth )
	{
		put( "Depth", new Integer(depth) );
	}

	public int getPriority()
	{
		return ((Integer)get( "Priority" )).intValue();
	}

	public void setPriority( int priority )
	{
		put( "Priority", new Integer(priority) );
	}

	public int getType()
	{
		return ((Integer)get( "Type" )).intValue();
	}

	public void setType( int type )
	{
		put( "Type", new Integer(type) );
	}

	public void remove()
	{
		call( "Remove" );
	}
}
