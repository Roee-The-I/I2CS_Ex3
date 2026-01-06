package classes.week1;
import java.util.Scanner;

/**
 * A simple example of how to read an input from the command line
 * This example was partly taken from: https://alvinalexander.com/java/edu/pj/pj010005/
 * @author boaz.benmoshe
 */
public class Input {

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();  // number of mili seconds starting from 1.1.1970 (aka Linux time).
		// create a scanner so we can read the command-line input
		Scanner sc = new Scanner(System.in);
		int i=3;
		// prompt for the user's name.
		System.out.print("Enter your name: ");

		// get a command line input as a String.
		String username = sc.next();

		// prompt for their age.
		System.out.print("Enter your age: ");

		// get a command line input as an integer.
		int age = sc.nextInt();
		//int age = Integer.parseInt(age_str);

		System.out.println("Hi "+username+" your age is "+age);
		System.out.println("Enter double: ");

		// get a command line input as a double
		double d = sc.nextDouble();
		System.out.println("The double you have entered is: "+d);
		long t2 = System.currentTimeMillis();  // number of mili seconds starting from 1.1.1970 (aka Linux time).
		double dt = (t2-t1)/1000.0;
		System.out.print("The program ran for "+dt+" seconds");
	}
}
