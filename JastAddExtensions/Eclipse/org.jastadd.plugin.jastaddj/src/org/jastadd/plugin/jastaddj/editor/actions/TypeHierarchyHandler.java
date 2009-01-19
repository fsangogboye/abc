package org.jastadd.plugin.jastaddj.editor.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.jastadd.plugin.compiler.ast.IJastAddNode;
import org.jastadd.plugin.jastaddj.Activator;
import org.jastadd.plugin.jastaddj.view.JastAddJTypeHierarchyView;
import org.jastadd.plugin.ui.AbstractBaseActionDelegate;

public class TypeHierarchyHandler extends AbstractBaseActionDelegate {

	@Override
	public void run(IAction action) {
		final IJastAddNode selectedNode = selectedNode();
		if (selectedNode != null)
			try {
				JastAddJTypeHierarchyView.activate(selectedNode);
			} catch (CoreException e) {
				String message = "Failed to show type hierarchy"; 
				IStatus status = new Status(IStatus.ERROR, 
						Activator.JASTADDJ_PLUGIN_ID,
						IStatus.ERROR, message, e);
				Activator.INSTANCE.getLog().log(status);
			}	
	}
}
