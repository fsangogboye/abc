// RenameField/test13/in/A.java p A f g
package p;
public class A {
	static int f= 0;
	void m(){
		p.A.f= 0; /**/
	}
}