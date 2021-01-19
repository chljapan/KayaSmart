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
import org.isis.jaut.Variant;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaTerritory extends Dispatch
{
	public MgaTerritory()
	{
	}
	
	public MgaTerritory( Dispatch target )
	{
		attach( target );
		changeInterface( "{32D1F3A3-D276-11D3-9AD5-00AA00B6FE26}" );
	}
	
/*	[ helpstring("method Flush: empty the territory")] HRESULT Flush();
	[ helpstring("method Destroy: empty and destroy the territory")] HRESULT Destroy();
	[ propput, helpstring("property EventMask (defaults to ALL)")] HRESULT EventMask([in] unsigned long eventmask);
	[ propput, helpstring("property RWEventMask (defaults to NONE)")] HRESULT RWEventMask([in] unsigned long eventmask);
	[ helpstring("Associate object with user data")] HRESULT Associate([in] IMgaObject *obj, [in] VARIANT userdata);
	[ helpstring("property OpenFCOs")] HRESULT OpenFCOs([in] IMgaFCOs *obj, [in, out] IMgaFCOs **newobj);
	[ helpstring("property CloseObj")] HRESULT CloseObj([in] IMgaObject *obj);

	[ propget,  helpstring("property Project")] HRESULT Project([out, retval] IMgaProject **pVal);
	[ helpstring("Check if object belongs to a project")]	HRESULT CheckProject([in] IMgaProject *project);*/
	
	public void flush()
	{
		call( "Flush" );
	}
	
	public MgaFCO openFCO( MgaFCO fco )
    {
		Variant returned_fco_variant = new Variant();
		returned_fco_variant.allocate(Variant.VT_DISPATCH);
	   
		Variant arguments[] = new Variant[2];
		arguments[0]        = new Variant(fco);
		arguments[1]        = new Variant(returned_fco_variant);
		
        Variant retval = new Variant();
        retval.allocate(Variant.VT_EMPTY);

        invoke( getIDOfName("OpenFCO"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaFCO( returned_fco_variant.getDispatch() );
    }
    
    public MgaObject openObj( MgaObject obj )
    {
    	Variant returned_obj_variant = new Variant();
		returned_obj_variant.allocate(Variant.VT_DISPATCH);
	   
		Variant arguments[] = new Variant[2];
		arguments[0]        = new Variant(obj);
		arguments[1]        = new Variant(returned_obj_variant);
		
        Variant retval = new Variant();
        retval.allocate(Variant.VT_EMPTY);

        invoke( getIDOfName("OpenObj"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaObject( returned_obj_variant.getDispatch() );
  
    }
	

}
