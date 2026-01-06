package classes.week3;
/**
 * Make sure you can 
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class GCDFunctionsTest {

	@Test
	void testGcd5() {
		int x= 12, y=20;
		if(GCD_Functions.gcd0(x, y)!= GCD_Functions.gcd5(x, y)) {
			
			fail("Not yet implemented");
		}
	}
/*
	@Test
	void testGcd6() {
		fail("Not yet implemented");
	}

	@Test
	void testGcd0() {
		fail("Not yet implemented");
	}
*/
	@Test
	void testIsPrime() {
		int[] primes = {2,3,5,3331};
		int[] notPrime = {14,9,33333*3331};
		for(int i=0;i<primes.length;i=i+1) {
			boolean a = GCD_Functions.isPrime(primes[i]);
			if(a==false) {
                fail();}
		}
		for(int i=0;i<notPrime.length;i=i+1) {
			boolean a = GCD_Functions.isPrime(notPrime[i]);
			if(a==true) {
                fail();}
		}
	}

    static class SimpleFunctionsTest {
        @Test
        void testIsPrime() {
            int n = 19;
            boolean p = SimpleFunctions.isPrime(n);
            if (p == false) {
                String err = "ERR: " + n + " is a Prime number";
                System.err.println(err);
                fail(err);
            }
        }


        @Test
        void testIsPrime2() {
            int[] primes = {2, 3, 5, 19, 3331};
            int[] notPrime = {9, 12, 44, 999};
            for (int i = 0; i < primes.length; i = i + 1) {
                boolean ip = SimpleFunctions.isPrime(primes[i]);
                assertEquals(ip, true);
            }
            for (int i = 0; i < notPrime.length; i = i + 1) {
                boolean ip = SimpleFunctions.isPrime(notPrime[i]);
                if (ip == true) {
                    String err = "ERR: " + notPrime[i] + "  should is not Prime";
                    System.err.println(err);
                    fail(err);
                }
            }
        }

        @Test
        void testRandom() {
            int num = 100000;
            double min = 10;
            double max = 20;
            double sum = 0;
            for (int i = 0; i < num; i = i + 1) {
                double r = SimpleFunctions.random(min, max);
                if (r < min || r >= max) {
                    fail();
                }
                sum = sum + r;
            }
            double ave = sum / num;
            double mid = (max + min) / 2.0;
            double dist = Math.abs(ave - mid);
            System.out.println("ave " + ave);
            if (dist > 0.1) {
                fail();
            }
        }
    }
}
