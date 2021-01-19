/*
 * Support.java
 *
 * Created on December 11, 2003, 2:00 PM
 */

package org.isis.gme.bon;

public class Support
{	
	public static void Assert(boolean expression, String message)
	{	if(!expression)
		{	System.out.println("Assert Failure\n"+message);
			System.exit(0);
		}
	}
	
	/* public static void MessageBox(String message)
	{	com.ms.wfc.ui.MessageBox.show(message,"MessageBox",com.ms.wfc.ui.MessageBox.OK);
		
	} */
}
