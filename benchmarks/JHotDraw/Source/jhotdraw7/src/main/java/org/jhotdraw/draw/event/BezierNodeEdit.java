/*
 * @(#)BezierNodeEdit.java
 *
 * Copyright (c) 1996-2010 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.draw.event;

import org.jhotdraw.draw.BezierFigure;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.geom.BezierPath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * An {@code UndoableEdit} event which can undo a change of a node in
 * a {@link BezierFigure}.
 *
 * @version $Id: BezierNodeEdit.java -1   $
 * @author Werner Randelshofer
 */
public class BezierNodeEdit extends AbstractUndoableEdit {

    private BezierFigure owner;
    private int index;
    private BezierPath.Node oldValue;
    private BezierPath.Node newValue;

    /** Creates a new instance. */
    public BezierNodeEdit(BezierFigure owner, int index, BezierPath.Node oldValue, BezierPath.Node newValue) {
        this.owner = owner;
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    
    public String getPresentationName() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        if (oldValue.mask != newValue.mask) {
            return labels.getString("edit.bezierNode.changeType.text");
        } else {
            return labels.getString("edit.bezierNode.movePoint.text");
        }
    }

    
    public void redo() throws CannotRedoException {
        super.redo();
        owner.willChange();
        owner.setNode(index, newValue);
        owner.changed();
        if (oldValue.mask != newValue.mask) {
        }
    }

    
    public void undo() throws CannotUndoException {
        super.undo();
        owner.willChange();
        owner.setNode(index, oldValue);
        owner.changed();
    }

    
    public boolean addEdit(UndoableEdit anEdit) {
        if (anEdit instanceof BezierNodeEdit) {
            BezierNodeEdit that = (BezierNodeEdit) anEdit;
            if (that.owner == this.owner && that.index == this.index) {
                this.newValue = that.newValue;
                return true;
            }
        }
        return false;
    }
}