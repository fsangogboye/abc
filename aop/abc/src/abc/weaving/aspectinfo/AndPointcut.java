package abc.weaving.aspectinfo;

import polyglot.util.Position;

import soot.*;
import soot.jimple.*;

import abc.weaving.matching.MethodPosition;
import abc.weaving.residues.*;

/** Pointcut conjunction. */
public class AndPointcut extends AbstractPointcut {
    private Pointcut pc1;
    private Pointcut pc2;

    public AndPointcut(Pointcut pc1, Pointcut pc2, Position pos) {
	super(pos);
	this.pc1 = pc1;
	this.pc2 = pc2;
    }

    public Pointcut getLeftPointcut() {
	return pc1;
    }

    public Pointcut getRightPointcut() {
	return pc2;
    }

    public Residue matchesAt(ShadowType st,
			     SootClass cls,
			     SootMethod method,
			     MethodPosition pos) {
	return AndResidue.construct(pc1.matchesAt(st,cls,method,pos),
				    pc2.matchesAt(st,cls,method,pos));
    }
}
