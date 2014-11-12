
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import javax.swing.JFrame;

public class MainClass {
    public static void main(String[] args) throws IOException
    {
    	JFrame frame = new JFrame();
    	//more initialization code here
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setSize(dim.width, dim.height);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
    	DragCircle d = new DragCircle();

        String string="";
        String file ="text.txt";

        //reading   
        try{
            InputStream ips=new FileInputStream(file); 
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String line;
            while ((line=br.readLine())!=null){
                System.out.println(line);
                string+=line+"\n";
            }
        	DrawText dt = new DrawText(string, dim.width, dim.height);
        	frame.add(dt);
        	dt.repaint();
            br.close(); 
        }       
        catch (Exception e){
            System.out.println(e.toString());
        }
    	frame.add(d);

//        //writing
//        try {
//            FileWriter fw = new FileWriter (file);
//            BufferedWriter bw = new BufferedWriter (fw);
//            PrintWriter fileOut = new PrintWriter (bw); 
//                fileOut.println (string+"\n test of read and write !!"); 
//            fileOut.close();
//            System.out.println("the file " + file + " is created!"); 
//        }
//        catch (Exception e){
//            System.out.println(e.toString());
//        }   
    	
    }



}
