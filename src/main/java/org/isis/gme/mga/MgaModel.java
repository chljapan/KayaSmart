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
import org.isis.gme.meta.MgaMetaModel;
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
public class MgaModel extends MgaFCO
{
    public MgaModel()
    {
    }
    
    public MgaModel( Dispatch d )
    {
        attach( d );
        changeInterface( "{270B4F9A-B17C-11D3-9AD1-00AA00B6FE26}" );
    }
    
    public MgaMetaModel getMetaModel()
    {
        return new MgaMetaModel( getMeta() );
    }
    
    /*public Vector getAspectNames( MgaModel model )
    {
        //MgaMetaModel metaModel = new MgaMetaModel( getMeta() );
        
        MgaMetaAspects aspects = metaModel.getAspects();
        for( int i=0; i<aspects.getCount(); ++i )
        {
            System.out.println( aspects.getItem(i).getName() );
        }
    }*/
    
        /*public MgaModel(String compName)
        {
                super(compName);
        }*/
    
        /*public int getStatus()
        {
                return Dispatch.get(this, "Status").toInt();
        }
         
        public boolean getIsWritable()
        {
                return Dispatch.get(this, "IsWritable").toBoolean();
        }
         
        public String getID()
        {
                return Dispatch.get(this, "ID").toString();
        }
         
        public String getName()
        {
                return Dispatch.get(this, "Name").toString();
        }
         
        public void setName(String lastParam)
        {
                Dispatch.CallSub(this, "Name", lastParam);
        }
         
        public MgaMetaBase getMetaBase()
        {
                return new MgaMetaBase(Dispatch.get(this, "MetaBase").toDispatch());
        }
         
        public objtype_enum getObjType()
        {
                return new objtype_enum(Dispatch.get(this, "ObjType").toDispatch());
        }
         
        public MgaProject getProject()
        {
                return new MgaProject(Dispatch.get(this, "Project").toDispatch());
        }
         
        public MgaTerritory getTerritory()
        {
                return new MgaTerritory(Dispatch.get(this, "Territory").toDispatch());
        }
         
        public boolean getIsEqual(MgaObject lastParam)
        {
                return Dispatch.call(this, "IsEqual", lastParam).toBoolean();
        }
         
        public void getParent(VT_PTR pVal, objtype_enum lastParam)
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
        }
         
        public Variant getCurrentAssociation()
        {
                return Dispatch.get(this, "CurrentAssociation");
        }
         
        public void sendEvent(int lastParam)
        {
                Dispatch.CallSub(this, "SendEvent", new Variant(lastParam));
        }
         
        public int getRelID()
        {
                return Dispatch.get(this, "RelID").toInt();
        }
         
        public void setRelID(int lastParam)
        {
                Dispatch.CallSub(this, "RelID", new Variant(lastParam));
        }
         
        public boolean getIsLibObject()
        {
                return Dispatch.get(this, "IsLibObject").toBoolean();
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
                return Dispatch.get(this, "Exempt").toBoolean();
        }
         
        public MgaObjects getChildObjects()
        {
                return new MgaObjects(Dispatch.get(this, "ChildObjects").toDispatch());
        }
         
        public MgaObject getChildObjectByRelID(int lastParam)
        {
                return new MgaObject(Dispatch.call(this, "ChildObjectByRelID", new Variant(lastParam)).toDispatch());
        }
         
        public MgaObject getObjectByPath(String lastParam)
        {
                return new MgaObject(Dispatch.call(this, "ObjectByPath", lastParam).toDispatch());
        }
         
        public MgaMetaFCO getMeta()
        {
                return new MgaMetaFCO(Dispatch.get(this, "Meta").toDispatch());
        }
         
        public MgaMetaRole getMetaRole()
        {
                return new MgaMetaRole(Dispatch.get(this, "MetaRole").toDispatch());
        }
         
        public MgaModel getParentModel()
        {
                return new MgaModel(Dispatch.get(this, "ParentModel").toDispatch());
        }
         
        public MgaFolder getParentFolder()
        {
                return new MgaFolder(Dispatch.get(this, "ParentFolder").toDispatch());
        }
         
        public MgaParts getParts()
        {
                return new MgaParts(Dispatch.get(this, "Parts").toDispatch());
        }
         
        public MgaPart getPart(MgaMetaAspect lastParam)
        {
                return new MgaPart(Dispatch.call(this, "Part", lastParam).toDispatch());
        }
         
        public MgaPart getPartByMetaPart(MgaMetaPart lastParam)
        {
                return new MgaPart(Dispatch.call(this, "PartByMetaPart", lastParam).toDispatch());
        }
         
        public MgaAttributes getAttributes()
        {
                return new MgaAttributes(Dispatch.get(this, "Attributes").toDispatch());
        }
         
        public MgaAttribute getAttribute(MgaMetaAttribute lastParam)
        {
                return new MgaAttribute(Dispatch.call(this, "Attribute", lastParam).toDispatch());
        }
         
        public Variant getAttributeByName(String lastParam)
        {
                return Dispatch.call(this, "AttributeByName", lastParam);
        }
         
        public void setAttributeByName(String name, Variant lastParam)
        {
                Dispatch.CallSub(this, "AttributeByName", name, lastParam);
        }
         
        public String getStrAttrByName(String lastParam)
        {
                return Dispatch.call(this, "StrAttrByName", lastParam).toString();
        }
         
        public void setStrAttrByName(String name, String lastParam)
        {
                Dispatch.CallSub(this, "StrAttrByName", name, lastParam);
        }
         
        public int getIntAttrByName(String lastParam)
        {
                return Dispatch.call(this, "IntAttrByName", lastParam).toInt();
        }
         
        public void setIntAttrByName(String name, int lastParam)
        {
                Dispatch.CallSub(this, "IntAttrByName", name, new Variant(lastParam));
        }
         
        public double getFloatAttrByName(String lastParam)
        {
                return Dispatch.call(this, "FloatAttrByName", lastParam).toDouble();
        }
         
        public void setFloatAttrByName(String name, double lastParam)
        {
                Dispatch.CallSub(this, "FloatAttrByName", name, new Variant(lastParam));
        }
         
        public boolean getBoolAttrByName(String lastParam)
        {
                return Dispatch.call(this, "BoolAttrByName", lastParam).toBoolean();
        }
         
        public void setBoolAttrByName(String name, boolean lastParam)
        {
                Dispatch.CallSub(this, "BoolAttrByName", name, new Variant(lastParam));
        }
         
        public MgaFCO getRefAttrByName(String lastParam)
        {
                return new MgaFCO(Dispatch.call(this, "RefAttrByName", lastParam).toDispatch());
        }
         
        public void setRefAttrByName(String name, MgaFCO lastParam)
        {
                Dispatch.CallSub(this, "RefAttrByName", name, lastParam);
        }
         
        public void clearAttrByName(String lastParam)
        {
                Dispatch.CallSub(this, "ClearAttrByName", lastParam);
        }
         
        public MgaRegNode getRegistryNode(String lastParam)
        {
                return new MgaRegNode(Dispatch.call(this, "RegistryNode", lastParam).toDispatch());
        }
         
        public MgaRegNodes getRegistry(boolean lastParam)
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
        }
         
        public MgaConstraints getConstraints(boolean lastParam)
        {
                return new MgaConstraints(Dispatch.call(this, "Constraints", new Variant(lastParam)).toDispatch());
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
         
        public MgaFCO getDerivedFrom()
        {
                return new MgaFCO(Dispatch.get(this, "DerivedFrom").toDispatch());
        }
         
        public MgaFCOs getDerivedObjects()
        {
                return new MgaFCOs(Dispatch.get(this, "DerivedObjects").toDispatch());
        }
         
        public MgaFCO getType()
        {
                return new MgaFCO(Dispatch.get(this, "Type").toDispatch());
        }
         
        public MgaFCO getBaseType()
        {
                return new MgaFCO(Dispatch.get(this, "BaseType").toDispatch());
        }
         
        public MgaFCO getArcheType()
        {
                return new MgaFCO(Dispatch.get(this, "ArcheType").toDispatch());
        }
         
        public boolean getIsInstance()
        {
                return Dispatch.get(this, "IsInstance").toBoolean();
        }
         
        public boolean getIsPrimaryDerived()
        {
                return Dispatch.get(this, "IsPrimaryDerived").toBoolean();
        }
         
        public void attachToArcheType(MgaFCO newtype, boolean lastParam)
        {
                Dispatch.CallSub(this, "AttachToArcheType", newtype, new Variant(lastParam));
        }
         
        public void detachFromArcheType()
        {
                Dispatch.CallSub(this, "DetachFromArcheType");
        }
         
        public void getAbsMetaPath(String lastParam)
        {
                Dispatch.CallSub(this, "GetAbsMetaPath", lastParam);
        }
         
        public void getAbsMetaPath(String[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putStringRef(lastParam[0]);
         
                Dispatch.CallSub(this, "GetAbsMetaPath", vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toString();
        }
         
        public void getRelMetaPath(MgaFCO begfco, String relpath, MgaFCOs lastParam)
        {
                Dispatch.CallSub(this, "GetRelMetaPath", begfco, relpath, lastParam);
        }
         
        public void getRelMetaPath(MgaFCO begfco, String relpath)
        {
                Dispatch.CallSub(this, "GetRelMetaPath", begfco, relpath);
        }
         
        public void getRelMetaPath(MgaFCO begfco, String[] relpath, MgaFCOs lastParam)
        {
                Variant vnt_relpath = new Variant();
                if( relpath == null || relpath.length == 0 )
                        vnt_relpath.noParam();
                else
                        vnt_relpath.putStringRef(relpath[0]);
         
                Dispatch.CallSub(this, "GetRelMetaPath", begfco, vnt_relpath, lastParam);
         
                if( relpath != null && relpath.length > 0 )
                        relpath[0] = vnt_relpath.toString();
        }
         
        public MgaConnPoints getPartOfConns()
        {
                return new MgaConnPoints(Dispatch.get(this, "PartOfConns").toDispatch());
        }
         
        public MgaFCOs getMemberOfSets()
        {
                return new MgaFCOs(Dispatch.get(this, "MemberOfSets").toDispatch());
        }
         
        public MgaFCOs getReferencedBy()
        {
                return new MgaFCOs(Dispatch.get(this, "ReferencedBy").toDispatch());
        }
         
        public void createCollection(VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "CreateCollection", lastParam);
        }
         
        public void createCollection(VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "CreateCollection", vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public MgaFCO getRootFCO()
        {
                return new MgaFCO(Dispatch.get(this, "RootFCO").toDispatch());
        }*/
         
    public MgaFCO createChildObject( MgaMetaRole meta )
    {
        Variant arguments[] = new Variant[1];
        arguments[0]        = new Variant(meta);

        Variant retval = new Variant();
        retval.allocate(Variant.VT_DISPATCH);

        invoke( getIDOfName("CreateChildObject"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaFCO( retval.getDispatch() );
    }
                                       
         
        /*public void deriveChildObject(MgaFCO base, MgaMetaRole role, boolean instance, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "DeriveChildObject", base, role, new Variant(instance), lastParam);
        }
         
        public void deriveChildObject(MgaFCO base, MgaMetaRole role, boolean instance, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "DeriveChildObject", base, role, new Variant(instance), vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public MgaFCO getChildDerivedFrom(MgaFCO lastParam)
        {
                return new MgaFCO(Dispatch.call(this, "ChildDerivedFrom", lastParam).toDispatch());
        }*/
         
    //public MgaParts getAspectParts(MgaMetaAspect asp, int lastParam)
    public MgaParts getAspectParts(MgaMetaAspect asp)
    {
        return new MgaParts( (Dispatch)invoke("AspectParts", DISPATCH_PROPERTYGET, 
            new Object[] { asp, new Integer(0) }, null) );
    }
         
    public MgaFCO createSimpleConn( MgaMetaRole meta, MgaFCO src, MgaFCO dst, MgaFCOs srcrefs, MgaFCOs dstrefs )
    {
        Variant arguments[] = new Variant[5];
        arguments[0]        = new Variant(meta);
        arguments[1]        = new Variant(src);
        arguments[2]        = new Variant(dst);
        arguments[3]        = new Variant(srcrefs);
        arguments[4]        = new Variant(dstrefs);

        Variant retval = new Variant();
        retval.allocate(Variant.VT_DISPATCH);

        invoke( getIDOfName("CreateSimpleConn"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaFCO( retval.getDispatch() );
    }    
        
    public MgaFCO createReference( MgaMetaRole meta, MgaFCO target )
    {
        Variant arguments[] = new Variant[2];
        arguments[0]        = new Variant(meta);
        arguments[1]        = new Variant(target);

        Variant retval = new Variant();
        retval.allocate(Variant.VT_DISPATCH);

        invoke( getIDOfName("CreateReference"), DISPATCH_METHOD, arguments, null, retval );

        return new MgaFCO( retval.getDispatch() );
    }
         
        /*public void createReference(MgaMetaRole meta, MgaFCO target, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "CreateReference", meta, target, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public void addInternalConnections(MgaFCOs inobjs, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "AddInternalConnections", inobjs, lastParam);
        }
         
        public void addInternalConnections(MgaFCOs inobjs, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "AddInternalConnections", inobjs, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public void moveFCOs(MgaFCOs to_copy, MgaMetaRoles destroles, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "MoveFCOs", to_copy, destroles, lastParam);
        }
         
        public void moveFCOs(MgaFCOs to_copy, MgaMetaRoles destroles, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "MoveFCOs", to_copy, destroles, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }
         
        public void copyFCOs(MgaFCOs to_move, MgaMetaRoles destroles, VT_PTR lastParam)
        {
                Dispatch.CallSub(this, "CopyFCOs", to_move, destroles, lastParam);
        }
         
        public void copyFCOs(MgaFCOs to_move, MgaMetaRoles destroles, VT_PTR[] lastParam)
        {
                Variant vnt_lastParam = new Variant();
                if( lastParam == null || lastParam.length == 0 )
                        vnt_lastParam.noParam();
                else
                        vnt_lastParam.putVT_PTRRef(lastParam[0]);
         
                Dispatch.CallSub(this, "CopyFCOs", to_move, destroles, vnt_lastParam);
         
                if( lastParam != null && lastParam.length > 0 )
                        lastParam[0] = vnt_lastParam.toVT_PTR();
        }*/
         
    public MgaFCOs getChildFCOs()
    {
        return new MgaFCOs((Dispatch)get("ChildFCOs"));
    }
         
        /*public MgaFCO getChildFCO(String lastParam)
        {
                return new MgaFCO(Dispatch.call(this, "ChildFCO", lastParam).toDispatch());
        }
         
        public void getDescendantFCOs(MgaFilter filter, VT_PTR lastParam)
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
                return Dispatch.get(this, "ChildRelIDCounter").toInt();
        }
         
        public void setChildRelIDCounter(int lastParam)
        {
                Dispatch.CallSub(this, "ChildRelIDCounter", new Variant(lastParam));
        }*/
}
