/*
 * Created on Sep 15, 2004
 */
package abc.aspectj.parse;

/**
 * The interface implemented by abc's lexer, exposing some additional functionality
 * to allow for extensibility.
 * 
 * @author pavel
 */
public interface AbcLexer extends polyglot.lex.Lexer {
    /**
     * Returns the integer constant representing the JAVA lexer state.
     */
    public int java_state();

    /**
     * Returns the integer constant representing the ASPECTJ lexer state.
     */
    public int aspectj_state();

    /**
     * Returns the integer constant representing the POINTCUT lexer state.
     */
    public int pointcut_state();

    /**
     * Returns the integer constant representing the POINTCUTIFEXPR lexer state.
     */
    public int pointcutifexpr_state();
    
    /**
     * Makes the lexer change into the specified state, pushing the current state and
     * some state-specific information onto its internal stack.
     * 
     * When states are terminated is state-dependent - it could be at the next ';' or '{'
     * (for some pointcuts), at the matching '}' (for Java/AspectJ code), or at the matching
     * ')' (for pointcut if expressions).
     * 
     * @param state
     */
    public void enterLexerState(int state);
    
    /**
     * Adds a keyword with the specified action to the JAVA lexer state.
     * 
     * @param keyword the keyword to be added
     * @param ka the action to be performed.
     */
    public void addJavaKeyword(String keyword, LexerAction ka);
    
    /**
     * Adds a keyword with the specified action to the ASPECTJ lexer state.
     * 
     * @param keyword the keyword to be added
     * @param ka the action to be performed.
     */
    public void addAspectJKeyword(String keyword, LexerAction ka);
    
    /**
     * Adds a keyword with the specified action to the POINTCUT lexer state.
     * 
     * @param keyword the keyword to be added
     * @param ka the action to be performed.
     */
    public void addPointcutKeyword(String keyword, LexerAction ka);
    
    /**
     * Adds a keyword with the specified action to the POINTCUTIFEXPR lexer state.
     * 
     * @param keyword the keyword to be added
     * @param ka the action to be performed.
     */
    public void addPointcutIfExprKeyword(String keyword, LexerAction ka);
    
    /**
     * Adds a keyword with the specified action to all four lexer states.
     * 
     * @param keyword the keyword to be added
     * @param ka the action to be performed.
     */
    public void addGlobalKeyword(String keyword, LexerAction ka);
    
    /**
     * Adds a keyword with the specified action to the ASPECTJ and POINTCUTIFEXPR lexer states.
     * 
     * @param keyword the keyword to be added
     * @param ka the action to be performed.
     */
    public void addAspectJContextKeyword(String keyword, LexerAction ka);

    /**
     * Indicate whether or not the lexer is currently examining a "per-pointcut".
     */
    public void setInPerPointcut(boolean b);
    
    /**
     * Obtain the integer constant representing the current state of the lexer.
     */
    public int currentState();
    
    /**
     * Indicates whether the last token consumed was a dot.
     */
    public boolean getLastTokenWasDot();
}
