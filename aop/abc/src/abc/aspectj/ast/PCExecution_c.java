package polyglot.ext.aspectj.ast;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class PCExecution_c extends Pointcut_c implements PCExecution
{
    protected MethodConstructorPattern pat;

    public PCExecution_c(Position pos, MethodConstructorPattern pat)  {
	super(pos);
        this.pat = pat;
    }

    public Precedence precedence() {
	return Precedence.LITERAL;
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
	w.write("execution (");
        print(pat, w, tr);
        w.write(")");
    }

}
