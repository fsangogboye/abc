package abc.weaving.aspectinfo;

import soot.*;
import polyglot.util.Position;
import abc.weaving.residues.*;

/** Handler for <code>withincode</code> lexical pointcut with a method pattern. */
public class WithinMethod extends LexicalPointcut {
    private MethodPattern pattern;

    public WithinMethod(MethodPattern pattern,Position pos) {
	super(pos);
	this.pattern = pattern;
    }

    public MethodPattern getPattern() {
	return pattern;
    }

    protected Residue matchesAt(SootClass cls,SootMethod method) {
	// FIXME: Remove this once pattern is built properly
	if(getPattern()==null) 
	    return 
		method.getName().equals(SootMethod.constructorName) ||
		method.getName().equals(SootMethod.staticInitializerName)
		? null : AlwaysMatch.v;

	if(!getPattern().matchesMethod(method)) return null;
	return AlwaysMatch.v;
    }

    public String toString() {
	return "withinmethod("+pattern+")";
    }
}
