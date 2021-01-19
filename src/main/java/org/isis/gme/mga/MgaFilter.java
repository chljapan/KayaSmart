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
public class MgaFilter extends Dispatch 
{
	public MgaFilter()
	{
	}

	public MgaFilter( Dispatch d )
	{
		attach( d );
		changeInterface( "{c8f6e970-c1fd-11d3-9ad2-00aa00b6fe26}" );
	}

	/*public MgaFilter(String compName)
	{
		super(compName);
	}

	public String getName()
	{
		return Dispatch.get(this, "Name").toString();
	}

	public void setName(String lastParam)
	{
		Dispatch.CallSub(this, "Name", lastParam);
	}

	public String getKind()
	{
		return Dispatch.get(this, "Kind").toString();
	}

	public void setKind(String lastParam)
	{
		Dispatch.CallSub(this, "Kind", lastParam);
	}

	public String getrole()
	{
		return Dispatch.get(this, "role").toString();
	}

	public void setrole(String lastParam)
	{
		Dispatch.CallSub(this, "role", lastParam);
	}

	public String getObjType()
	{
		return Dispatch.get(this, "ObjType").toString();
	}

	public void setObjType(String lastParam)
	{
		Dispatch.CallSub(this, "ObjType", lastParam);
	}

	public String getLevel()
	{
		return Dispatch.get(this, "Level").toString();
	}

	public void setLevel(String lastParam)
	{
		Dispatch.CallSub(this, "Level", lastParam);
	}

	public MgaProject getProject()
	{
		return new MgaProject(Dispatch.get(this, "Project").toDispatch());
	}*/
}
