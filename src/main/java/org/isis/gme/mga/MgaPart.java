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

import org.isis.gme.meta.MgaMetaAspect;
import org.isis.gme.meta.MgaMetaPart;
import org.isis.gme.meta.MgaMetaRole;
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
public class MgaPart extends Dispatch
{
    public class GmeAttrs
    {
        public String icon = null;
        public int    x    = 0;
        public int    y    = 0;
        
        public GmeAttrs() 
        {
        }
        
        public GmeAttrs( String icon, int x, int y ) 
        {
            this.icon = icon;
            this.x    = x;
            this.y    = y;            
        }                     
    }
    
    public MgaPart()
    {
    }
    
    public MgaPart( Dispatch d )
    {
        attach( d );
        changeInterface( "{270B4F9E-B17C-11D3-9AD1-00AA00B6FE26}" );
    }    
    
    public MgaModel getModel()
    {
        return new MgaModel( (Dispatch)get( "Model" ) );
    }
     
    public MgaMetaPart getMeta()
    {
        return new MgaMetaPart( (Dispatch)get( "Meta" ) );
    }
         
    public MgaMetaAspect getMetaAspect()
    {
        return new MgaMetaAspect( (Dispatch)get( "MetaAspect" ) );
    }
         
    public MgaMetaRole getMetaRole()
    {
        return new MgaMetaRole( (Dispatch)get( "MetaRole" ) );
    }
     
    public MgaFCO getFCO()
    {
        return new MgaFCO( (Dispatch)get( "FCO" ) );        
    }
     
    public int getAccessMask()
    {
        return ((Integer)get("AccessMask")).intValue();
    }
     
    /*public boolean getRegistryMode()
    {
            return Dispatch.get(this, "RegistryMode").toBoolean();
    }
     
    public void setRegistryMode(boolean lastParam)
    {
            Dispatch.CallSub(this, "RegistryMode", new Variant(lastParam));
    }
    
    public MgaRegNode getRegistryNode(String path)
    {
        return new MgaRegNode( (Dispatch)get( "RegistryNode", path ) );
    }
     
    /*public MgaRegNodes getRegistry(boolean lastParam)
    {
            return new MgaRegNodes(Dispatch.call(this, "Registry", new Variant(lastParam)).toDispatch());
    }
     
    public MgaRegNodes getRegistry()
    {
            return new MgaRegNodes(Dispatch.get(this, "Registry").toDispatch());
    }
     
    public MgaRegNodes getRegistry(boolean lastParam)
    {
            MgaRegNodes result_of_Registry = new MgaRegNodes(Dispatch.call(this, "Registry", new Variant(lastParam)).toDispatch());
     
     
            return result_of_Registry;
    }
     
    public String getRegistryValue(String lastParam)
    {
            return Dispatch.call(this, "RegistryValue", lastParam).toString();
    }
     
    public void setRegistryValue(String path, String lastParam)
    {
            Dispatch.CallSub(this, "RegistryValue", path, lastParam);
    }*/
           
    public GmeAttrs getGmeAttrs()
    {
        GmeAttrs ret = new GmeAttrs();
        
        Variant var_icon = new Variant(new String());
        Variant var_x    = new Variant((int)0);
        Variant var_y    = new Variant((int)0);
        
        Variant arguments[] = new Variant[3];
        arguments[0] = new Variant(var_icon);
        arguments[1] = new Variant(var_x);
        arguments[2] = new Variant(var_y);
        
        Variant retval = new Variant();
        retval.allocate(Variant.VT_EMPTY);
        
        invoke( getIDOfName("GetGmeAttrs"), DISPATCH_METHOD, arguments, null, retval );
        
        ret.icon = var_icon.getString();
        ret.x    = var_x.getInt();
        ret.y    = var_y.getInt();
        return ret;
    }
    
    public void setGmeAttrs( GmeAttrs attrs )
    {
        call( "SetGmeAttrs", attrs.icon, new Variant(attrs.x), new Variant(attrs.y) );
    }
}
