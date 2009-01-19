package org.jastadd.plugin.ui.view;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.jastadd.plugin.compiler.ast.IASTNode;
import org.jastadd.plugin.compiler.ast.IJastAddNode;

public abstract class AbstractBaseContentOutlinePage extends ContentOutlinePage {
	
	//private IEditorInput fInput;
	private AbstractTextEditor fTextEditor;
	private IASTNode fRoot;
	private ITreeContentProvider fContentProvider;
	private IBaseLabelProvider fLabelProvider;

	
	public AbstractBaseContentOutlinePage(AbstractTextEditor editor) {
		super();
	    fTextEditor = editor;
		fContentProvider = new JastAddContentProvider();
		fLabelProvider = new JastAddLabelProvider();
	}
	
	@Override 
	public void dispose() {
		super.dispose();
	}

	/*
	public void setInput(IEditorInput input) {
		fInput = input;
		if(input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput)input;
			IFile file = fileInput.getFile();
		}
		TreeViewer viewer = getTreeViewer();
		if(viewer != null) {
			ITreeContentProvider cProvider = null;
			IBaseLabelProvider lProvider = null;
			cProvider = new JastAddContentProvider();
			lProvider = new JastAddLabelProvider();
			viewer.setContentProvider(cProvider == null ? new JastAddContentProvider() : cProvider);
			viewer.setLabelProvider(lProvider == null ? new JastAddLabelProvider() : lProvider);
		}
		update();
	}
	*/
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(fContentProvider);
		viewer.setLabelProvider(fLabelProvider);
		viewer.addSelectionChangedListener(this);
		update();
		//if (fInput != null)
		//	viewer.setInput(fInput);
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);
		ISelection selection= event.getSelection();
		if (selection.isEmpty())
			fTextEditor.resetHighlightRange();
		else {
			IStructuredSelection structSelect = (IStructuredSelection)selection; 
			Object obj = structSelect.getFirstElement();
			if (obj instanceof IJastAddNode) {
				IJastAddNode node = (IJastAddNode)obj;
				openFileForNode(node);
			}
		}
	}
	
	protected abstract void openFileForNode(IJastAddNode node);
	

	/**
	 * Updates the AST shown by this outline page
	 * @param ast The AST to show
	 */
	public void updateAST(IASTNode ast) {
		fRoot = ast;
		update();
	}
	
	/**
	 * Redraws the tree view 
	 */
	public void update() {
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			Control control= viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(fRoot); 
				viewer.expandToLevel(3);
				control.setRedraw(true);
			}
		}

		/*
		if(fInput != fTextEditor.getEditorInput())
			setInput(fTextEditor.getEditorInput());
		TreeViewer viewer= getTreeViewer();
		if (viewer != null) {
			Control control= viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(fInput);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
		*/
	}
}
