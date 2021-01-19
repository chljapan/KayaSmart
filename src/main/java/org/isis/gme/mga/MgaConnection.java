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

import org.isis.jaut.Dispatch;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaConnection extends MgaFCO
{
	public MgaConnection()
	{
	}

	public MgaConnection( Dispatch d )
	{
		attach( d );
		changeInterface( "{270B4FA0-B17C-11D3-9AD1-00AA00B6FE26}" );
	}
	
	/*public MgaConnPoints getConnPoints()
	{
		return new MgaConnPoints(Dispatch.get(this, "ConnPoints").toDispatch());
	}

	public void compareToBase(MgaConnPoint connpoint, short lastParam)
	{
		Dispatch.CallSub(this, "CompareToBase", connpoint, new Variant(lastParam));
	}

	public void compareToBase()
	{
		Dispatch.CallSub(this, "CompareToBase");
	}

	public void compareToBase(MgaConnPoint connpoint, short[] lastParam)
	{
		Variant vnt_lastParam = new Variant();
		if( lastParam == null || lastParam.length == 0 )
			vnt_lastParam.noParam();
		else
			vnt_lastParam.putShortRef(lastParam[0]);

		Dispatch.CallSub(this, "CompareToBase", connpoint, vnt_lastParam);

		if( lastParam != null && lastParam.length > 0 )
			lastParam[0] = vnt_lastParam.toShort();
	}

	public void revertToBase(MgaConnPoint lastParam)
	{
		Dispatch.CallSub(this, "RevertToBase", lastParam);
	}

	public void revertToBase()
	{
		Dispatch.CallSub(this, "RevertToBase");
	}

	public void revertToBase(MgaConnPoint lastParam)
	{
		Dispatch.CallSub(this, "RevertToBase", lastParam);

	}*/
}
