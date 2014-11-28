import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainClass {
    public static void main(String[] args) throws IOException
    {
        SettingsPanel settings = new SettingsPanel();
        settings.pack();
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
