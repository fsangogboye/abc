/* abc - The AspectBench Compiler
 * Copyright (C) 2004 Ondrej Lhotak
 *
 * This compiler is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
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

import soot.*;
import soot.util.*;
import soot.jimple.*;
import java.util.*;
import abc.weaving.aspectinfo.*;
import abc.weaving.matching.*;
import abc.weaving.weaver.*;
import abc.main.AbcTimer;

/** Saves all method bodies so that they can be unwoven after weaving.
 * @author Ondrej Lhotak
 * @date August 3, 2004
 */

public class Unweaver {
    private static void debug(String message) {
        if (abc.main.Debug.v().unweaver) 
            System.err.println("UNWEAVER ***** " + message);
    }	

    Map/*SootMethod->Body*/ savedBodies;
    Map/*SootMethod->List/Type/*/ savedParameters;
    Map/*SootClass->Collection/SootMethod/*/ classToMethods;
    Map/*SootClass->Collection/SootField/*/ classToFields;

    /** Save Jimple bodies of all weavable classes to be restored later. */
    public void save() {
        savedBodies = new HashMap();
        classToMethods = new HashMap();
        classToFields = new HashMap();
        savedParameters = new HashMap();
        for( Iterator abcClassIt = GlobalAspectInfo.v().getWeavableClasses().iterator(); abcClassIt.hasNext(); ) {
            final AbcClass abcClass = (AbcClass) abcClassIt.next();
            SootClass cl = abcClass.getSootClass();
            classToMethods.put( cl, new HashSet() );
            classToFields.put( cl, new HashSet() );
            debug( "saving "+cl );
            for( Iterator mIt = cl.getMethods().iterator(); mIt.hasNext(); ) {
                final SootMethod m = (SootMethod) mIt.next();
                if( m.hasActiveBody() ) {
                    savedBodies.put( m, m.getActiveBody() );
                }
                savedParameters.put(m, m.getParameterTypes());
                ((Collection)classToMethods.get(cl)).add(m);
            }
            for( Iterator fIt = cl.getFields().iterator(); fIt.hasNext(); ) {
                final SootField f = (SootField) fIt.next();
                ((Collection)classToFields.get(cl)).add(f);
            }
        }
    }

    /** Restore saved bodies to their original methods. */
    public Map restore() {
        Map ret = new HashMap();
        for( Iterator mIt = savedBodies.keySet().iterator(); mIt.hasNext(); ) {
            final SootMethod m = (SootMethod) mIt.next();
            debug( "restoring body of "+m );
            Body newBody = Jimple.v().newBody(m);
            Map newBindings = 
                newBody.importBodyContentsFrom((Body)savedBodies.get(m));
            m.setActiveBody(newBody);
            m.setParameterTypes((List)savedParameters.get(m));
            ret.putAll( newBindings );
        }
        for( Iterator abcClassIt = GlobalAspectInfo.v().getWeavableClasses().iterator(); abcClassIt.hasNext(); ) {
            final AbcClass abcClass = (AbcClass) abcClassIt.next();
            SootClass cl = abcClass.getSootClass();
            Collection methods = (Collection) classToMethods.get(cl);
            for( Iterator mIt = new ArrayList(cl.getMethods()).iterator(); mIt.hasNext(); ) {
                final SootMethod m = (SootMethod) mIt.next();
                if( !methods.contains(m) ) {
                    debug( "removing "+m+" from cl" );
                    cl.removeMethod(m);
                }
            }
            Collection fields = (Collection) classToFields.get(cl);
            for( Iterator fIt = new ArrayList(cl.getFields()).iterator(); fIt.hasNext(); ) {
                final SootField f = (SootField) fIt.next();
                if( !fields.contains(f) ) {
                    debug( "removing "+f+" from cl" );
                    cl.removeField(f);
                }
            }
        }
        return ret;
    }
}
