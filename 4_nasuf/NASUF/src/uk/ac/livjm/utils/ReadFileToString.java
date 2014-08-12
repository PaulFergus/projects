/*
 * ReadFileToString.java
 *
 * Created on 13 March 2005, 14:48
 */

package NASUF.src.uk.ac.livjm.utils;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ReadFileToString {
    
    static public String getContents(File aFile) {
        //...checks on aFile are elided
        StringBuffer contents = new StringBuffer();

        //declared here only to make visible to finally clause
        BufferedReader input = null;
        try {
            //use buffering
            //this implementation reads one line at a time
            input = new BufferedReader( new FileReader(aFile) );
            String line = null; //not declared within while loop
            while (( line = input.readLine()) != null){
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            try {
                if (input!= null) {
                    //flush and close both "input" and its underlying FileReader
                    input.close();
                }
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return contents.toString();
    }
    
    public static void main(String[] args){
        File file = new File("E:\\PhDProject\\Development\\NASUF\\metadata\\service_requests\\player\\dcm\\player_dcm.rdf");
        System.out.println(getContents(file));
    }
}
