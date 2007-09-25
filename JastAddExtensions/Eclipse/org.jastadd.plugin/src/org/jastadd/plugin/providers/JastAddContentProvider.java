package org.jastadd.plugin.providers;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IFileEditorInput;
import org.jastadd.plugin.model.JastAddModel;
import org.jastadd.plugin.model.JastAddModelProvider;

import org.jastadd.plugin.AST.ASTNode;

public class JastAddContentProvider implements ITreeContentProvider {
	
	private ITreeContentProvider parent;
	
	public JastAddContentProvider(ITreeContentProvider parent) {
		this.parent = parent;
	}

	public JastAddContentProvider() {
		this.parent = new ITreeContentProvider() {
			public Object[] getChildren(Object parentElement) { return new Object[] { }; }
			public Object getParent(Object element) { return null; }
			public boolean hasChildren(Object element) { return false; }
			public Object[] getElements(Object inputElement) { return new Object[] { }; }
			public void dispose() {	}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }
		};
	}

	public Object[] getChildren(Object element) {
		if(element instanceof ASTNode) {
			ASTNode node = (ASTNode)element;
			return node.outlineChildren().toArray();
		}
		else if(element instanceof IFile) {
			IFile file = (IFile)element;
			JastAddModel model = JastAddModelProvider.getModel(file);
			if (model != null) {
				ASTNode node = model.getTreeRoot(file);
				return node.outlineChildren().toArray();
			}
		}
		return parent.getChildren(element);
	}

	public Object getParent(Object element) {
		if(element instanceof ASTNode) {
			ASTNode node = (ASTNode)element;
			ASTNode parent = node.getParent();
			if (parent != null && parent.showInContentOutline())
				return parent;
			else getParent(parent);
		}
		return parent.getParent(element);
	}

	public boolean hasChildren(Object element) {
		if(element instanceof ASTNode) {
			ASTNode node = (ASTNode)element;
			return !node.outlineChildren().isEmpty();
		}
		else if(element instanceof IFile) {
			IFile file = (IFile)element;
			JastAddModel model = JastAddModelProvider.getModel(file);
			if (model != null) {
				return true;
			}
		}
		return parent.hasChildren(element);
	}

	public Object[] getElements(Object element) {
		if(element instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput)element;
			IFile file = input.getFile();
			
			JastAddModel model = JastAddModelProvider.getModel(file);
			if (model != null) {
				IDocument document = model.fileToDocument(file);
				ASTNode content = model.getTreeRoot(document);
				if(content != null)
					return content.outlineChildren().toArray();
			}
		}
		return parent.getElements(element);
	}

	public void dispose() {
		parent.dispose();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		parent.inputChanged(viewer, oldInput, newInput);
	}
}