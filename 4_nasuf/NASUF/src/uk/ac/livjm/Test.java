/*
 * Test.java
 *
 * Created on 30 November 2005, 19:50
 */

package NASUF.src.uk.ac.livjm;

import java.util.Random;

/**
 *
 * @author  Monkey
 */
public class Test {
    
    /** Creates a new instance of Test */
    public Test() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	// TODO code application logic here
	Random ran = new Random();
	System.out.println(ran.nextInt(10));
    }
    
    
    
}
