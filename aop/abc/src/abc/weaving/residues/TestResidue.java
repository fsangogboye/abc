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

import soot.*;
import soot.util.Chain;
import soot.jimple.*;
import polyglot.util.InternalCompilerError;
import abc.soot.util.LocalGeneratorEx;
import abc.weaving.weaver.WeavingContext;
import abc.weaving.weaver.*;

/** A residue that tests if a local variable has a value
 *  @author Ganesh Sittampalam
 *  @author Ondrej Lhotak
 */ 

public class TestResidue extends Residue {
    
    Local loc;
    Constant val;

    public Residue optimize() { return this; }
    public Residue inline(ConstructorInliningMap cim) {
        return new TestResidue(cim.map(loc), (Constant) Jimple.v().cloneIfNecessary(val));
    }
    public TestResidue(Local l,Constant v) {
	loc=l;
	val=v;
    }

    public Stmt codeGen(SootMethod method,LocalGeneratorEx localgen,
			Chain units,Stmt begin,Stmt fail,boolean sense,
			WeavingContext wc) {

	// We don't expect the frontend/matcher to produce a residue that does this. 
	// There's no reason we couldn't just do the standard "automatic fail" thing 
	// if there was ever a need, though.
	if(!sense) 
	    throw new InternalCompilerError("TestResidue should never be used negated");

	Stmt test=Jimple.v().newIfStmt(Jimple.v().newNeExpr(loc,val),fail);
	units.insertAfter(test,begin);
	return test;
    }

    public String toString() {
	return "test("+loc+","+val+")";
    }

}
