package abc.aspectj.ast;

import java.util.List;

import abc.aspectj.ast.AspectBody;
import abc.aspectj.extension.AJClassBody_c;
import polyglot.ast.ClassBody;
import polyglot.ext.jl.ast.ClassBody_c;
import polyglot.util.Position;

/**
 * An <code>AspectBody</code> represents the body of an aspect
 * declaration 
 * 
 * @author Oege de Moor
 */
public class AspectBody_c extends AJClassBody_c implements AspectBody
{

    public AspectBody_c(Position pos, List members) {
        super(pos,members);
    }
     
}
