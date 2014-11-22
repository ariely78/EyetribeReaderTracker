import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;


public class DocumentReader {

	public static String readTextFile(String file)
	{
        //reading   
		String string="";
        try{
            InputStream ips=new FileInputStream(file); 
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String line;
            while ((line=br.readLine())!=null){
            	
                System.out.println(line);
                string+="\n"+line+"\n";
            }
            br.close();
            

        }       
        catch (Exception e){
            System.out.println(e.toString());
        }
        return string;
	}
	
	public static void writeToTextFile(String file, String data)
	{
        //writing
		try(PrintWriter output = new PrintWriter(new FileWriter(file, true))) 
		{
//		    output.printf("%s\r\n", "NEWLINE");
		    PrintWriter fileOut = new PrintWriter (output); 
		    fileOut.println (data);
		    fileOut.close();
		} 
		catch (Exception e) {}
//        try {
//            FileWriter fw = new FileWriter (file);
////            BufferedWriter bw = new BufferedWriter (fw);
//            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
//            output.append(data);
//            output.close();
////            PrintWriter fileOut = new PrintWriter (output); 
////            fileOut.println (string+"\n test of read and write !!"); 
////            fileOut.close();
//            System.out.println("the file " + file + " is created!"); 
//        }
//        catch (Exception e){
//            System.out.println(e.toString());
//        }  
	}
	

}
