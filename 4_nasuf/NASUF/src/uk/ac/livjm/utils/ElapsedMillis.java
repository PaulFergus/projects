/*
 * Class Title:         ElapsedMillis
 * Class Description:   Need to do
 * Author:              Sun Microsystems
 * Project:             Wireless PnP Home Network
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Date:                20-08-03
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.utils;

//Java SDK imports
import java.util.*;

public class ElapsedMillis {
    
    private long startTime;
    private long finishTime;
    private long lapsedTime;
    
    public ElapsedMillis() {
    }
    
    public void start(){
        startTime = System.currentTimeMillis ();
    }
    
    public void stop(){
        finishTime = System.currentTimeMillis();
        lapsedTime = finishTime - startTime;
    }
    
    public long getElapsedTime(){
        return lapsedTime;
    }

    public static void main(String[] args) {
      
      // the above two dates are one second apart
      ElapsedMillis em = new ElapsedMillis();
      em.start();
      for(int i = 0; i < 200000000; i++){}
      em.stop();
      
      System.out.println("Completed in " + em.getElapsedTime() + " milliseconds");
      System.out.println("Completed in " + em.getElapsedTime() / 1000 + " second(s)");
   }
}
