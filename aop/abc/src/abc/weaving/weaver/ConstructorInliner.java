/* abc - The AspectBench Compiler
 * Copyright (C) 2004 Laurie Hendren
 * Copyright (C) 2004 Ondrej Lhotak
 * Copyright (C) 2004 Jennifer Lhotak
 * Copyright (C) 2004 Ganesh Sittampalam
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

package abc.weaving.weaver;

import java.util.*;

import soot.*;
import soot.util.*;
import soot.jimple.*;

import polyglot.util.InternalCompilerError;

import abc.weaving.aspectinfo.*;
import abc.weaving.matching.*;
import abc.weaving.weaver.*;
import abc.soot.util.*;

/** Inlines constructor calls in constructors with initialization and
 * preinitialization advice.
 *
 * @author Laurie Hendren
 * @author Ondrej Lhotak
 * @author Jennifer Lhotak
 * @author Ganesh Sittampalam
 */

public class ConstructorInliner {

    private static void debug(String message) {
        if (abc.main.Debug.v().constructorInliner)
            System.err.println("CI*** " + message);
    }

    public static void inlineConstructors(SootClass sc) {
        debug("--- BEGIN Inlining Constructors for class " + sc.getName());

        // for each method in the class, look to see if it is an <init> and
        //    needs to be inlined
        debug("Iterating through methods, looking for <init> methods ");
        debug("that need inlining");

      
        for (Iterator methodIt = sc.getMethods().iterator(); methodIt.hasNext();) {

            // get the next method
            final SootMethod method = (SootMethod) methodIt.next();

            // nothing to do for abstract or native methods 
            if (method.isAbstract())
                continue;
            if (method.isNative())
                continue;

            // if it has init or preinit advice list, inline body 
            if (GlobalAspectInfo.v().getClassInitializationShadowMatch(method) != null || GlobalAspectInfo.v().getPreinitializationShadowMatch(method) != null)

            {
                debug("Must inline body of " + method.getName());
                // add to list of methods to process
                // if returns not restructured, do it now 
                Restructure.restructureReturn(method);

                
                ConstructorInliningMap cim;
                
                // keep inlining until no inlining happened
                while(true) {
                    cim = Restructure.inlineThisCall(method);
                    if(cim == null) break;
                    inlineAdviceApplications(cim);
                };
                
                
            }
        }

        debug(" --- END Constructor Inlining for class " + sc.getName() + "\n");
    }
    private static void inlineAdviceApplications(ConstructorInliningMap cim) {
        debug("   --- BEGIN Inlining advice applications " + cim.inlinee().getName());
        MethodAdviceList mal = GlobalAspectInfo.v().getAdviceList(cim.inlinee());
        for( Iterator aaIt = mal.bodyAdvice.iterator(); aaIt.hasNext(); ) {
            final AdviceApplication aa = (AdviceApplication) aaIt.next();
            MethodAdviceList targetMal =
                GlobalAspectInfo.v().getAdviceList(cim.target());
            AdviceApplication newAA = aa.inline(cim);
            targetMal.addBodyAdvice(newAA);
            newAA.shadowmatch.addIfNecessary();
        }
        for( Iterator aaIt = mal.stmtAdvice.iterator(); aaIt.hasNext(); ) {
            final AdviceApplication aa = (AdviceApplication) aaIt.next();
            MethodAdviceList targetMal =
                GlobalAspectInfo.v().getAdviceList(cim.target());
            AdviceApplication newAA = aa.inline(cim);
            targetMal.addBodyAdvice(newAA);
            newAA.shadowmatch.addIfNecessary();
        }

        debug("   --- END Inlining advice applications " + cim.inlinee().getName() + "\n");
    }
}
