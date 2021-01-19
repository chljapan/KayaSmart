package org.isis.gme.bon;

import javax.swing.JOptionPane;

import org.isis.gme.mga.MgaFCO;
import org.isis.gme.mga.MgaFCOs;
import org.isis.gme.mga.MgaProject;

public class TestComponent implements Component
{
    public void invokeEx(MgaProject project, MgaFCO currentObj, MgaFCOs selectedObjs, int param)
    {
        String msg = new String();        
        msg = "Project name: " + project.getName() + "\n";
        if(currentObj!=null)
            msg += "Current object: " + currentObj.getName() + "\n";
        if(selectedObjs!=null)
            msg += "Number of selected objects: " + selectedObjs.getCount() + "\n";
        JOptionPane.showMessageDialog(null, msg, "Java Interpreter Test", 
            JOptionPane.ERROR_MESSAGE);
    }
}
