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
public class MgaComponent extends Dispatch 
{
	public MgaComponent()// String progid )
	{
        //project.attachNewInstance( "Mga.MgaProject", Dispatch.CLSCTX_INPROC_SERVER );
        //return project;
	}
	
	public MgaComponent( Dispatch d )
	{
		attach( d );
		changeInterface( "{11BB02D9-2E2C-11D3-B36D-0060082DF884}" );
	}
    
    
    
    //mgaConstMgr->ObjectsInvokeEx(mgaProject, NULL, NULL, NULL);
	
	
/*	[helpstring ("method Invoke, selectedobjs may be NULL")]
		HRESULT Invoke([in] IMgaProject *project, [in] IMgaFCOs *selectedobjs, [in] long param);
	[helpstring("method Initialize")] 
		HRESULT Initialize(IMgaProject *p);
	[helpstring("method Enable")] 
		HRESULT Enable(VARIANT_BOOL newVal);
	[propget, helpstring("property InteractiveMode")] 
		HRESULT InteractiveMode([out, retval] VARIANT_BOOL * enabled);
	[propput, helpstring("property InteractiveMode")] 
		HRESULT InteractiveMode([in] VARIANT_BOOL enabled);
	[propget, helpstring("property ComponentName")] 
		HRESULT ComponentName([out, retval] BSTR *pVal);
	[propget, helpstring("property ComponentType")] 
		HRESULT ComponentType([out, retval] componenttype_enum *t);
	[propget, helpstring("property Paradigm")] 
		HRESULT Paradigm([out, retval] BSTR *pVal);*/
}
