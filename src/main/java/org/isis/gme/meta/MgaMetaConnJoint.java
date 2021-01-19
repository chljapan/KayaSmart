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
public class MgaMetaConnJoint extends Dispatch 
{
	protected MgaMetaConnJoint()
	{
	}
	
	public MgaMetaConnJoint(Dispatch d)
	{
		attach( d );
		changeInterface( "{83BA3257-B758-11D3-ABAE-000000000000}" );
	}	

	public MgaMetaConnection getParent()
	{
		return new MgaMetaConnection( (Dispatch)get( "Parent" ) );
	}

	public MgaMetaPointerSpecs getPointerSpecs()
	{
		return new MgaMetaPointerSpecs( (Dispatch)get( "PointerSpecs" ) );
	}

	public MgaMetaPointerSpec getPointerSpecByName(String name)
	{
		return new MgaMetaPointerSpec( (Dispatch)call( "PointerSpecByName", name ) );
	}

	public MgaMetaPointerSpec createPointerSpec()
	{
		return new MgaMetaPointerSpec( (Dispatch)call( "CreatePointerSpec" ) );
	}
}