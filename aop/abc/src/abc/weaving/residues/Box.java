/* abc - The AspectBench Compiler
 * Copyright (C) 2004 Ganesh Sittampalam
 * Copyright (C) 2004 Ondrej Lhotak
 *
 * This compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this compiler, in the file LESSER-GPL;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package abc.weaving.residues;

import java.util.*;
import soot.*;
import soot.jimple.*;
import soot.util.Chain;
import abc.soot.util.Restructure;
import abc.soot.util.LocalGeneratorEx;
import abc.weaving.tagkit.Tagger;
import abc.weaving.weaver.*;

/** Box a weaving variable (if necessary) into another one
 *  @author Ganesh Sittampalam
 *  @author Ondrej Lhotak
 */ 

public class Box extends Residue implements BindingLink {
    public WeavingVar from;
    public WeavingVar to;

    public Residue optimize() { return this; }
    public Residue inline(ConstructorInliningMap cim) {
        return new Box(from.inline(cim), to.inline(cim));
    }
    public Box(WeavingVar from,WeavingVar to) {
	this.from=from;
	this.to=to;
    }

    public WeavingVar getAdviceFormal(WeavingVar var) {
		if (var==from)
			return to;
		
		return null;
	}
    
    public Residue resetForReweaving() {
    	from.resetForReweaving();
    	to.resetForReweaving();
    	return this;
    }
    
    public String toString() {
	return "box("+from+"->"+to+")";
    }

    public Stmt codeGen
	(SootMethod method,LocalGeneratorEx localgen,
	 Chain units,Stmt begin,Stmt fail,boolean sense,
	 WeavingContext wc) {
	
	if(!sense) {
	    Stmt jump=Jimple.v().newGotoStmt(fail);
        Tagger.tagStmt(jump, wc);
	    units.insertAfter(jump,begin);
	    return jump;
	}

	Type type=from.getType();
	if(type instanceof PrimType) {
	    SootClass boxClass=Restructure.JavaTypeInfo.getBoxingClass(type);
	    Stmt newStmt=to.set(localgen,units,begin,wc,
				Jimple.v().newNewExpr(boxClass.getType()));
	    List paramTypeList=new ArrayList(1);
	    paramTypeList.add(type);
	    Stmt constrStmt=Jimple.v().newInvokeStmt
		(Jimple.v().newSpecialInvokeExpr
		 (to.get(),Scene.v().makeConstructorRef(boxClass,paramTypeList),from.get()));
        Tagger.tagStmt(constrStmt, wc);
	    units.insertAfter(constrStmt,newStmt);
	    
	    return succeed(units,constrStmt,fail,sense);

	} else {
	    return succeed(units,to.set(localgen,units,begin,wc,from.get()),fail,sense);
	}
    }

}
