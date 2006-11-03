public class NCWeakRefs4 {
    public static void foo(String s) { }
    public static void bar() { 
	//System.out.println("Matched " + matched + " times."); 
    }

    public static int matched = 0;

    public static void main(String[] args) {
	String s;
	for(int i = 0; i < 100; i++) {
	    s = "" + i;
	    foo(s);
	    System.gc(); System.gc(); System.gc(); System.gc(); System.gc(); 
	}
	bar();
	bar();
	if(matched != 100) throw new RuntimeException("Matched " + matched + " times, rather than 100.");
    }
}

aspect A {
    tracematch(String s) {
	sym foo before : call(* *.foo(String)) && args(s);
	sym bar before : call(* *.bar());

	foo bar bar {
	    NCWeakRefs4.matched++;
	}
    }
}
