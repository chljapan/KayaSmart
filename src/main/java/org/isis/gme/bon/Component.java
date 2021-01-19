/*
 * Created on Feb 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.isis.gme.bon;

import org.isis.gme.mga.MgaFCO;
import org.isis.gme.mga.MgaFCOs;
import org.isis.gme.mga.MgaProject;

public interface Component
{
    public void invokeEx(MgaProject project, MgaFCO currentObj, MgaFCOs selectedObjs, int param);    
}
