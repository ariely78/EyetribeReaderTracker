
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainClass {
    public static void main(String[] args) throws IOException
    {
    	JFrame frame = new JFrame();
    	//more initialization code here
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setSize(300,700);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
        //new container  
        Container con = frame.getContentPane();    
        //configure the layout  
        con.setLayout(new GridBagLayout());  
            GridBagConstraints c = new GridBagConstraints();  
//            c.fill = GridBagConstraints.HORIZONTAL;  
//            c.gridwidth = dim.width;
//            c.gridheight = dim.height;
//
////            c.insets = new Insets(0,0,0,0);  
//            c.weightx = 0.0;  
//            c.gridx = 0;  
//            c.gridy = 0;  
    	
    	DragCircle d = new DragCircle();
//    	d.setOpaque(false);
//        con.add(d,c);  

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
                string+=line;//+"\n";
            }

//            DrawText dt = new DrawText(string, 300,400);
//        	dt.setOpaque(false);
//        	dt.repaint();
            
            //add panels to the container  
            d.createPanel(string);
            frame.add(d);
            d.repaint();
            br.close(); 
        }       
        catch (Exception e){
            System.out.println(e.toString());
        }
 
        //add container to the jFrame, positioned in the center  
        //frame.add("Center",con); 
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
