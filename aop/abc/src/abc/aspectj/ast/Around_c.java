package abc.aspectj.ast;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

import abc.weaving.aspectinfo.AbcFactory;

/** specification part of around advice.
 * 
 * @author Oege de Moor
 */
public class Around_c extends AdviceSpec_c 
                              implements Around
{
    private MethodDecl proceed;

    public Around_c(Position pos, 
		    TypeNode returnType,
		    List formals)
    {
	super(pos, formals, returnType, null);
    }
    
    public String kind() {
    	return "around";
    }
    // string representation for error messages
	public String toString() {
		String s = returnType + " around(";

		for (Iterator i = formals.iterator(); i.hasNext(); ) {
			Formal t = (Formal) i.next();
			s += t.toString();

			if (i.hasNext()) {
				s += ", ";
			}
		}
		s = s + ")";
		
		return s;
	}

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		w.begin(0);
		print(returnType,w,tr);
		w.allowBreak(0," ");

        w.write("around (");

		w.begin(0);

		for (Iterator i = formals.iterator(); i.hasNext(); ) {
	   		 Formal f = (Formal) i.next();
	   		 print(f, w, tr);

	    	if (i.hasNext()) {
				w.write(",");
				w.allowBreak(0, " ");
	   		 }
		}

		w.end();
		w.write(")");
        w.end();
    }

    public void setProceed(MethodDecl proceed) {
	this.proceed = proceed;
    }

    public MethodDecl proceed() {
	return proceed;
    }

    public abc.weaving.aspectinfo.AdviceSpec makeAIAdviceSpec() {
	abc.weaving.aspectinfo.AbcType rtype = AbcFactory.AbcType(returnType.type());
	abc.weaving.aspectinfo.MethodSig psig = AbcFactory.MethodSig(proceed);
	return new abc.weaving.aspectinfo.AroundAdvice(rtype, psig, position());
    }
}
