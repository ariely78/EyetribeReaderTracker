import java.io.*;
import javax.swing.JFrame;

public class MainClass {
    public static void main(String[] args) throws IOException
    {
        SettingsPanel settings = new SettingsPanel();
        settings.pack();
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
