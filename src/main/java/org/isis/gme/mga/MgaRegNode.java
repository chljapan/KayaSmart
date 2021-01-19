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
public class MgaRegNode extends Dispatch 
{
	public MgaRegNode()
	{
	}

	public MgaRegNode( Dispatch d )
	{
		attach( d );
		changeInterface( "{83BA323D-B758-11D3-ABAE-000000000000}" );
	}

	/*public MgaRegNode(String compName)
	{
		super(compName);
	}*/

	public String getName()
	{
        return (String)get( "Name" );
	}

	public String getPath()
	{
        return (String)get( "Path" );
	}

	public String getValue()
	{
        return (String)get( "Value" );
	}

	/*public void setValue(String lastParam)
	{
		Dispatch.CallSub(this, "Value", lastParam);
	}

	public MgaFCO getFCOValue()
	{
		return new MgaFCO(Dispatch.get(this, "FCOValue").toDispatch());
	}

	public void setFCOValue(MgaFCO lastParam)
	{
		Dispatch.CallSub(this, "FCOValue", lastParam);
	}

	public MgaRegNodes getSubNodes(boolean lastParam)
	{
		return new MgaRegNodes(Dispatch.call(this, "SubNodes", new Variant(lastParam)).toDispatch());
	}

	public MgaRegNodes getSubNodes()
	{
		return new MgaRegNodes(Dispatch.get(this, "SubNodes").toDispatch());
	}

	public MgaRegNodes getSubNodes(boolean lastParam)
	{
		MgaRegNodes result_of_SubNodes = new MgaRegNodes(Dispatch.call(this, "SubNodes", new Variant(lastParam)).toDispatch());


		return result_of_SubNodes;
	}

	public MgaRegNode getSubNodeByName(String lastParam)
	{
		return new MgaRegNode(Dispatch.call(this, "SubNodeByName", lastParam).toDispatch());
	}

	public MgaRegNode getParentNode()
	{
		return new MgaRegNode(Dispatch.get(this, "ParentNode").toDispatch());
	}

	public void getStatus(int lastParam)
	{
		Dispatch.CallSub(this, "Status", new Variant(lastParam));
	}

	public void getStatus(int[] lastParam)
	{
		Variant vnt_lastParam = new Variant();
		if( lastParam == null || lastParam.length == 0 )
			vnt_lastParam.noParam();
		else
			vnt_lastParam.putIntRef(lastParam[0]);

		Dispatch.CallSub(this, "Status", vnt_lastParam);

		if( lastParam != null && lastParam.length > 0 )
			lastParam[0] = vnt_lastParam.toInt();
	}

	public boolean getOpacity()
	{
		return Dispatch.get(this, "Opacity").toBoolean();
	}

	public void setOpacity(boolean lastParam)
	{
		Dispatch.CallSub(this, "Opacity", new Variant(lastParam));
	}

	public void clear()
	{
		Dispatch.CallSub(this, "Clear");
	}

	public void removeTree()
	{
		Dispatch.CallSub(this, "RemoveTree");
	}*/	

  public void removeTree()
  {
    call("RemoveTree");
  }
}
