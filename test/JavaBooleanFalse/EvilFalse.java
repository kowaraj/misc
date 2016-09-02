
package temp;

import java.lang.Boolean;

public class EvilFalse { 
	public static void main(String[] args) { 
		System.out.println("Hello, Evil");

		Boolean y;
		y = (Boolean)false;
		if (y)
			System.out.println("True");
		else
			System.out.println("False");

		
		System.out.println("Done.");
	}

	

}
