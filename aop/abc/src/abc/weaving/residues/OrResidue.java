package abc.weaving.residues;

/** Disjunction of two residues
 *  @author Ganesh Sittampalam
 *  @date 28-Apr-04
 */ 
public class OrResidue extends AbstractResidue {
    private Residue left;
    private Residue right;

    /** Get the left operand */
    public Residue getLeftOp() {
	return left;
    }

    /** Get the right operand */
    public Residue getRightOp() {
	return right;
    }

    /** Private constructor to force use of smart constructor */
    private OrResidue(Residue left,Residue right) {
	this.left=left;
	this.right=right;
    }

    /** Smart constructor; some short-circuiting may need to be removed
     *  to mimic ajc behaviour
     */
    public static Residue construct(Residue left,Residue right) {
	if(left instanceof NeverMatch || right instanceof AlwaysMatch) 
	    return right;
	if(left instanceof AlwaysMatch || right instanceof NeverMatch)
	    return left;
	return new OrResidue(left,right);
    }

}
