//Listing 8.23 MyClass2.java

public class MyClass2 {
    // MyClass2's implementation
    private void desiredCharacteristicMethod1() {
    }

    private void desiredCharacteristicMethod2() {
    }

    public pointcut desiredCharacteristicJoinPoints() :
	call(* MyClass2.desiredCharacteristicMethod1())
	|| call(* MyClass2.desiredCharacteristicMethod2())
	/* || ... */;
}
