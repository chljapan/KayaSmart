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
import org.isis.jaut.*;

/**
 * @author bogyom
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MgaFolder extends MgaObject
{
    public MgaFolder( Dispatch d )
    {
        attach( d );
        changeInterface( "{270B4F96-B17C-11D3-9AD1-00AA00B6FE26}" );
    }
    
    public MgaMetaBase getMetaBase()
    {
        return new MgaMetaBase( (Dispatch)get( "MetaBase") );
    }
    
/*	public objtype_enum getObjType()
        {
                return new objtype_enum(get( "ObjType").toDispatch());
        }*/
    
    public MgaProject getProject()
    {
        return new MgaProject( (Dispatch)get( "Project" ) );
    }
    
    public MgaTerritory getTerritory()
    {
        return new MgaTerritory( (Dispatch)get( "Territory" ) );
    }
    
    public boolean getIsEqual( MgaObject obj )
    {
        return ((Boolean)call( "IsEqual", obj )).booleanValue();
    }
    
        /*public void getParent(VT_PTR pVal, objtype_enum lastParam)
        {
                Dispatch.CallSub(this, "GetParent", pVal, lastParam);
        }
         
        public void getParent(VT_PTR pVal)
        {
                Dispatch.CallSub(this, "GetParent", pVal);
        }
         
        public void getParent(VT_PTR[] pVal, objtype_enum[] lastParam)
        {
                Variant vnt_pVal = new Variant();
                if( pVal == null || pVal.length == 0 )
                        vnt_pVal.noParam();
                else
                        vnt_pVal.putVT_PTRRef(pVal[0]);
         
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putobjtype_enumRef(lastParam[0]);
         
                Dispatch.CallSub(this, "GetParent", vnt_pVal, vnt_lastParam);
         
                if( pVal != null && pVal.length > 0 )
                        pVal[0] = vnt_pVal.toVT_PTR();
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toobjtype_enum();
        }*/
    
    public void checkProject( MgaProject proj )
    {
        call( "CheckProject", proj );
    }
    
    public void destroyObject()
    {
        call( "DestroyObject" );
    }
    
        /*public void open(int lastParam)
        {
                Dispatch.CallSub(this, "Open", new Variant(lastParam));
        }*/
    
    public void open()
    {
        call( "Open" );
    }
    
        /*public void open(int lastParam)
        {
                Dispatch.CallSub(this, "Open", new Variant(lastParam));
        }*/
    
    public void close()
    {
        call( "Close" );
    }
    
        /*public void associate(Variant lastParam)
        {
                Dispatch.CallSub(this, "Associate", lastParam);
        }
         
        public Variant getCurrentAssociation()
        {
                return get( "CurrentAssociation");
        }
         
        public void sendEvent(int lastParam)
        {
                Dispatch.CallSub(this, "SendEvent", new Variant(lastParam));
        }
         
        public int getRelID()
        {
                return get( "RelID").toInt();
        }
         
        public void setRelID(int lastParam)
        {
                Dispatch.CallSub(this, "RelID", new Variant(lastParam));
        }
         
        public boolean getIsLibObject()
        {
                return get( "IsLibObject").toBoolean();
        }
         
        public void check()
        {
                Dispatch.CallSub(this, "Check");
        }
         
        public void checkTree()
        {
                Dispatch.CallSub(this, "CheckTree");
        }
         
        public void setExempt(boolean lastParam)
        {
                Dispatch.CallSub(this, "Exempt", new Variant(lastParam));
        }
         
        public boolean getExempt()
        {
                return get( "Exempt").toBoolean();
        }
         
        public MgaObjects getChildObjects()
        {
                return new MgaObjects(get( "ChildObjects").toDispatch());
        }
         
        public MgaObject getChildObjectByRelID(int lastParam)
        {
                return new MgaObject(call( "ChildObjectByRelID", new Variant(lastParam)).toDispatch());
        }
         
        public MgaObject getObjectByPath(String lastParam)
        {
                return new MgaObject(call( "ObjectByPath", lastParam).toDispatch());
        }*/
         
    public MgaMetaFolder getMetaFolder()
    {
        return new MgaMetaFolder((Dispatch)get( "MetaFolder"));
    }
         
    public MgaFolder getParentFolder()
    {
        Dispatch d = (Dispatch)get( "ParentFolder");
        if( d.isNull() )
            return null;
        else 
            return new MgaFolder(d);
    }
         
    public MgaFolders getChildFolders()
    {
        return new MgaFolders((Dispatch)get( "ChildFolders"));
    }
         
    public MgaFolder createFolder( MgaMetaFolder meta )
    {               
        Variant arguments[] = new Variant[1];
        arguments[0]        = new Variant(meta);

        Variant retval = new Variant();
        retval.allocate(Variant.VT_DISPATCH);

        invoke( getIDOfName("CreateFolder"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaFolder( retval.getDispatch() );
    }
         
    public MgaFCO createRootObject( MgaMetaFCO meta )    
    {        
        Variant arguments[] = new Variant[1];
        arguments[0]        = new Variant(meta);
        
        Variant retval = new Variant();
        retval.allocate(Variant.VT_DISPATCH);

        invoke( getIDOfName("CreateRootObject"), DISPATCH_METHOD, arguments, null, retval );
        
        return new MgaFCO( retval.getDispatch() ); 
    }
         
        
         
        /*public void deriveRootObject(MgaFCO base, boolean instance, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "DeriveRootObject", base, new Variant(instance), lastParam);
        }
         
        public void deriveRootObject(MgaFCO base, boolean instance, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "DeriveRootObject", base, new Variant(instance), vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }*/
    
    public MgaRegNode getRegistryNode(String path)
    {
        return new MgaRegNode((Dispatch) get( "RegistryNode", path));
    }
    
        /*public MgaRegNode getRegistryNode(String lastParam)
        {
                return new MgaRegNode(call( "RegistryNode", lastParam).toDispatch());
        }
         
        public MgaRegNodes getRegistry(boolean lastParam)
        {
                return new MgaRegNodes(call( "Registry", new Variant(lastParam)).toDispatch());
        }
         
        public MgaRegNodes getRegistry()
        {
                return new MgaRegNodes(get( "Registry").toDispatch());
        }
         
        public MgaRegNodes getRegistry(boolean lastParam)
        {
                MgaRegNodes result_of_Registry = new MgaRegNodes(call( "Registry", new Variant(lastParam)).toDispatch());
         
         
                return result_of_Registry;
        }*/

    public String getRegistryValue(String path)
    {
        return get( "RegistryValue", path).toString();
    }
     
    public void setRegistryValue(String path, String value)
    {
        put("RegistryValue", path, value);
    }

        /*public String getRegistryValue(String lastParam)
        {
                return call( "RegistryValue", lastParam).toString();
        }
         
        public void setRegistryValue(String path, String lastParam)
        {
                Dispatch.CallSub(this, "RegistryValue", path, lastParam);
        }
         
        public MgaConstraints getConstraints(boolean lastParam)
        {
                return new MgaConstraints(call( "Constraints", new Variant(lastParam)).toDispatch());
        }
         
        public void addConstraint(MgaConstraint lastParam)
        {
                Dispatch.CallSub(this, "AddConstraint", lastParam);
        }
         
        public void defineConstraint(String name, int mask, String expr, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "DefineConstraint", name, new Variant(mask), expr, lastParam);
        }
         
        public void defineConstraint(String name, int mask, String expr, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "DefineConstraint", name, new Variant(mask), expr, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public void moveFCOs(MgaFCOs to_copy, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "MoveFCOs", to_copy, lastParam);
        }
         
        public void moveFCOs(MgaFCOs to_copy, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "MoveFCOs", to_copy, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public void copyFCOs(MgaFCOs to_move, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "CopyFCOs", to_move, lastParam);
        }
         
        public void copyFCOs(MgaFCOs to_move, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "CopyFCOs", to_move, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }*/
         
    public MgaFCOs getChildFCOs()
    {
        return new MgaFCOs( (Dispatch)get( "ChildFCOs") );
    }
         
        /*public MgaFCO getChildFCO(String lastParam)
        {
                return new MgaFCO(call( "ChildFCO", lastParam).toDispatch());
        }*/
         
    public MgaFCOs getDescendantFCOs(MgaFilter filter)
    {
      return new MgaFCOs( (Dispatch)call("GetDescendantFCOs", filter) );
    }
    
        /*public void getDescendantFCOs(MgaFilter filter, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "GetDescendantFCOs", filter, lastParam);
        }
         
        public void getDescendantFCOs(MgaFilter filter, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "GetDescendantFCOs", filter, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public void getChildrenOfKind(String kindname, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "GetChildrenOfKind", kindname, lastParam);
        }
         
        public void getChildrenOfKind(String kindname, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "GetChildrenOfKind", kindname, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public int getChildRelIDCounter()
        {
                return get( "ChildRelIDCounter").toInt();
        }
         
        public void setChildRelIDCounter(int lastParam)
        {
                Dispatch.CallSub(this, "ChildRelIDCounter", new Variant(lastParam));
        }
         
        public void attachLibrary(String connstring, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "AttachLibrary", connstring, lastParam);
        }
         
        public void attachLibrary(String connstring, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "AttachLibrary", connstring, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public void refreshLibrary(String lastParam)
        {
                Dispatch.CallSub(this, "RefreshLibrary", lastParam);
        }
         
        public String getLibraryName()
        {
                return get( "LibraryName").toString();
        }
         
        public void setLibraryName(String lastParam)
        {
                Dispatch.CallSub(this, "LibraryName", lastParam);
        }*/
    
    
}
