package buggy;
import java.io.*;
import java.util.*;

public class Main {
	public static void main (String[] args) throws java.io.IOException {
		
		int num1 = readInteger();
		System.out.println ("\nYou entered the integer: " + num1);
		
		int num2 = reverse(num1);
		
		int result = squareDiff(num1,num2);

		System.out.println ("The computed result is: " + result);
	}

	private static int squareDiff(int num1, int num2) {
		C1 o1 =  new C1(num1,num2);
		C2 o2 =  new C2(num1,num2);
		
		int result1 = o1.compute();
		int result2 = o2.compute();
		
		return result1*result2;
	}

	private static int reverse(int number ) {
		int reversedNumber = 0;
		int temp = 0;
		
		if(number == 0 ) return 0;
		else if(number < 0) number = -number;
		
		while(number > 0){
 
			temp = number%10;
 
			reversedNumber = reversedNumber * 10 + temp;
			number = number/10;
 
		}
		return reversedNumber;
	}

	private static int readInteger() throws java.io.IOException {
		
		String s1;
		String s2;
		int num = 0;
		
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
 
		boolean cont = true;

		while (cont) 
		{
		     
			System.out.print ("Enter an integer:");
			s1 = br.readLine();
			StringTokenizer st = new StringTokenizer (s1);
			s2 = "";

			while (cont && st.hasMoreTokens())
			{
				s2 = st.nextToken();
				num = Integer.parseInt(s2);
				cont = false;
			}
		}
		return num;
	}
}
