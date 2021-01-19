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

import org.isis.jaut.*;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaReference extends MgaFCO 
{
	public MgaReference()
	{
	}

	public MgaReference( Dispatch d )
	{
		attach( d );
		changeInterface( "{83BA3233-B758-11D3-ABAE-000000000000}" );
	}
	
	/*[propget, helpstring("property Referred")] HRESULT Referred([out, retval] IMgaFCO * *pVal);
	[propput, helpstring("property Referred")] HRESULT Referred([in] IMgaFCO * newVal);
	[propget, helpstring("property RefAspect: allways NULL for ref's to non-models, and may also be NULL for model refs")] 
												HRESULT RefAspect([out, retval] IMgaMetaAspect **pVal);
	[propput, helpstring("property RefAspect: NOP for non-model refs")] 
												HRESULT RefAspect([in] IMgaMetaAspect * newVal);

	[propget, helpstring("property UsedByConns")]	HRESULT UsedByConns([out, retval] IMgaConnPoints **pVal);

	[id(0x580), helpstring("method CompareToBase: returns 0 if match found")] 
		HRESULT CompareToBase([out] short *status);
	[id(0x581), helpstring("method RevertToBase")] 
		HRESULT RevertToBase();*/
		
	public MgaFCO getReferred()
	{
        return new MgaFCO( (Dispatch)get( "Referred" ) );
	}

/*	public void setReferred(MgaFCO lastParam)
	{
		Dispatch.CallSub(this, "Referred", lastParam);
	}

	public MgaMetaAspect getRefAspect()
	{
		return new MgaMetaAspect(Dispatch.get(this, "RefAspect").toDispatch());
	}

	public void setRefAspect(MgaMetaAspect lastParam)
	{
		Dispatch.CallSub(this, "RefAspect", lastParam);
	}

	public MgaConnPoints getUsedByConns()
	{
		return new MgaConnPoints(Dispatch.get(this, "UsedByConns").toDispatch());
	}

	public void compareToBase(short lastParam)
	{
		Dispatch.CallSub(this, "CompareToBase", new Variant(lastParam));
	}

	public void compareToBase(short[] lastParam)
	{
		Variant vnt_lastParam = new Variant();
		if( lastParam == null || lastParam.length == 0 )
			vnt_lastParam.noParam();
		else
			vnt_lastParam.putShortRef(lastParam[0]);

		Dispatch.CallSub(this, "CompareToBase", vnt_lastParam);

		if( lastParam != null && lastParam.length > 0 )
			lastParam[0] = vnt_lastParam.toShort();
	}

	public void revertToBase()
	{
		Dispatch.CallSub(this, "RevertToBase");
	}*/
}
