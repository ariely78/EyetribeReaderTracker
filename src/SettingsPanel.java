import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsPanel extends JFrame implements ActionListener{

	private JButton startMouseButton;
	private JButton startEyetrackerButton;
	private JButton setSettings;

    JLabel wordActivatelabel = new JLabel( "Word to activate change:" );
    JLabel wordToSwapLabel = new JLabel( "Word to swap:" );
    JLabel fontSizeLabel = new JLabel( "Font Size:" );
    JLabel timeNextChangeLabel = new JLabel( "Time till next word change:" );
    JLabel numWordsInfrontLabel = new JLabel( "Number words infront, word changer:" );
    JLabel wordReadingTimeLabel = new JLabel( "Time to read word:" );
    JLabel textareaXLabel = new JLabel( "X offset for text area:" );
    JLabel textareaYLabel = new JLabel( "Y Offset for text area:" );

    JTextField wordActivateTF = new JTextField( 20 );
    JTextField wordToSwapTF = new JTextField( 20 );
    JTextField fontSizeTF = new JTextField( 20 );
    JTextField timeNextChangeTF = new JTextField( 20 );
    JTextField numWordsInfrontTF = new JTextField( 20 );
    JTextField wordReadingTimeTF = new JTextField( 20 );
    JTextField textareaXTF = new JTextField( 20 );
    JTextField textareaYTF = new JTextField( 20 );


	MouseWordSelection mousetxtContent = new MouseWordSelection(DocumentReader.readTextFile("text.txt"),this);
	EyetrackerWordSelection eyetrackerTxtContent = new EyetrackerWordSelection(DocumentReader.readTextFile("text.txt"),this);
	CalibrationPane cali = new CalibrationPane(this);
	MainFrame testWindow = new MainFrame(mousetxtContent,eyetrackerTxtContent,cali);

	
	public SettingsPanel() {
	    super("Settings Panel");
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setContentPane(createContent());
	    setTextFields();
	    setVisible(true);
	}

	private Container createContent()
	{
	    JPanel result = new JPanel();
	    result.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

	    startMouseButton = new JButton("Start Mouse");
	    startMouseButton.setName("mouse");
	    startMouseButton.setSize(100, 50);
	    startMouseButton.addActionListener(this);

        startEyetrackerButton = new JButton("Start Eyetracker");
        startEyetrackerButton.setName("eyetracker");
        startEyetrackerButton.setSize(100, 50);
        startEyetrackerButton.addActionListener(this);
        
        setSettings = new JButton("Set settings");
        setSettings.setName("setSettings");
        setSettings.setSize(100, 50);
        setSettings.addActionListener(this);
        // Create the layout
	    GroupLayout layout = new GroupLayout( result );
	    result.setLayout( layout );
	    layout.setAutoCreateGaps( true );

	    // Horizontally, we want to align the labels and the text fields
	    // along the left (LEADING) edge
	    layout.setHorizontalGroup( layout.createSequentialGroup()
	                                       .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
	                                                          .addComponent( wordActivatelabel )
	                                                          .addComponent( wordToSwapLabel )
	                                                          .addComponent( fontSizeLabel ) 
	                                                          .addComponent( timeNextChangeLabel ) 
	                                                          .addComponent( numWordsInfrontLabel ) 
	                                                          .addComponent( wordReadingTimeLabel ) 
	                                                          .addComponent( textareaXLabel ) 
	                                                          .addComponent( textareaYLabel ) 
	                                                          .addComponent(startMouseButton)
	                                                          .addComponent(setSettings))
	                                       .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
	                                                          .addComponent( wordActivateTF )
	                                                          .addComponent( wordToSwapTF )
	                                                          .addComponent( fontSizeTF )
	                                                          .addComponent( timeNextChangeTF )
	                                                          .addComponent( numWordsInfrontTF )
	                                                          .addComponent( wordReadingTimeTF )
	                                                          .addComponent( textareaXTF )
	                                                          .addComponent( textareaYTF )
	                                                          .addComponent(startEyetrackerButton)
	                                                          .addComponent(setSettings))
	    );

	    // Vertically, we want to align each label with his textfield
	    // on the baseline of the components
	    layout.setVerticalGroup( layout.createSequentialGroup()
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( wordActivatelabel )
	                                                        .addComponent( wordActivateTF ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( wordToSwapLabel )
	                                                        .addComponent( wordToSwapTF ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( fontSizeLabel )
	                                                        .addComponent( fontSizeTF ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( timeNextChangeLabel )
	                                                        .addComponent( timeNextChangeTF ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( numWordsInfrontLabel )
	                                                        .addComponent( numWordsInfrontTF ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( wordReadingTimeLabel )
	                                                        .addComponent( wordReadingTimeTF ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( textareaXLabel )
	                                                        .addComponent( textareaXTF ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( textareaYLabel )
	                                                        .addComponent( textareaYTF ) )	                                                        
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( startMouseButton )
	                                                        .addComponent( startEyetrackerButton ) )
                                        .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
			                                                .addComponent( setSettings )) 
	                                                        
	    );

	    return result;
	}
	
	public void setTextFields()
	{
		wordActivateTF.setText(eyetrackerTxtContent.wordChanger.wordToActivateChange);
		wordToSwapTF.setText(eyetrackerTxtContent.wordChanger.swapWord);
		fontSizeTF.setText(eyetrackerTxtContent.fontSize+"");
		timeNextChangeTF.setText(eyetrackerTxtContent.wordChanger.timeUntilNextWordChange+"");
		numWordsInfrontTF.setText(eyetrackerTxtContent.wordChanger.numWordsInfront+"");
		wordReadingTimeTF.setText(eyetrackerTxtContent.wordReadingTime+"");
		textareaXTF.setText(eyetrackerTxtContent.textareaX+"");
		textareaYTF.setText(eyetrackerTxtContent.textareaY+"");

	}

	public void setVariables()
	{
		mousetxtContent.wordChanger.wordToActivateChange = wordActivateTF.getText();
		mousetxtContent.wordChanger.swapWord = wordToSwapTF.getText();
		mousetxtContent.fontSize = Integer.parseInt(fontSizeTF.getText());
		mousetxtContent.wordChanger.timeUntilNextWordChange = Integer.parseInt(timeNextChangeTF.getText());
		mousetxtContent.wordChanger.numWordsInfront = Integer.parseInt(numWordsInfrontTF.getText());
		mousetxtContent.wordReadingTime = Integer.parseInt(wordReadingTimeTF.getText());
		
		eyetrackerTxtContent.wordChanger.wordToActivateChange = wordActivateTF.getText();
		eyetrackerTxtContent.wordChanger.swapWord = wordToSwapTF.getText();
		eyetrackerTxtContent.fontSize = Integer.parseInt(fontSizeTF.getText());
		eyetrackerTxtContent.wordChanger.timeUntilNextWordChange = Integer.parseInt(timeNextChangeTF.getText());
		eyetrackerTxtContent.wordChanger.numWordsInfront = Integer.parseInt(numWordsInfrontTF.getText());
		eyetrackerTxtContent.wordReadingTime = Integer.parseInt(wordReadingTimeTF.getText());
		eyetrackerTxtContent.textareaX = Integer.parseInt(textareaXTF.getText());
		eyetrackerTxtContent.textareaY = Integer.parseInt(textareaYTF.getText());
	}
	
    @Override
	public void actionPerformed(ActionEvent e)
    {
	    JButton o = (JButton)e.getSource();
	    String name = o.getName();
    	if(name.equalsIgnoreCase("setSettings")) {
    	    setVariables();
    	    mousetxtContent.repaint();
    	    eyetrackerTxtContent.repaint();

    	} else if(name.equalsIgnoreCase("mouse")) {
	        //Execute when button is pressed
	        String reply = JOptionPane.showInputDialog(null, "Please enter your name",
	        		"Press ok to start test",
	        		JOptionPane.OK_OPTION);
	        mousetxtContent.wordChanger.swapWord = reply;

	        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
	        Date date = new Date();
	        Path path = Paths.get(reply+"_"+dateFormat.format(date)+".txt");

	        if (Files.notExists(path) && !reply.isEmpty()) {
	          // file is not exist
	        	mousetxtContent.fileName = path.toString();
	        	
	            //... Set window characteristics.
	        	//testWindow.add(cali);
	        	testWindow.setContentPane(mousetxtContent);
//	        	mousetxtContent.setVisible(false);
	        	testWindow.setTitle("Copyright Ben Smith (c) 2014");
	        	testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        	testWindow.pack();
	        	testWindow.setVisible(true);
	        	//cali.afterCalibration();

	        } else {
	            if (Files.exists(path)) {
	            	JOptionPane.showMessageDialog(null, "File exists with this name try again");
	            }
	        }
    	} else {
            //Execute when button is pressed
            String reply = JOptionPane.showInputDialog(null, "Please enter your name:",
            		"Press ok to start test",
            		JOptionPane.OK_OPTION);
            eyetrackerTxtContent.wordChanger.swapWord = reply;
	        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
	        Date date = new Date();
	        Path path = Paths.get(reply+"-"+dateFormat.format(date)+".txt");

            if (Files.notExists(path) && !reply.isEmpty()) {
            	// file is not exist
            	eyetrackerTxtContent.fileName = path.toString();
	            //... Set window characteristics.
            	testWindow.add(eyetrackerTxtContent);
	        	testWindow.setContentPane(cali);
	        	testWindow.setTitle("Copyright Ben Smith (c) 2014");
	        	testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        	testWindow.pack();
	        	testWindow.setVisible(true);
	    		eyetrackerTxtContent.startEyetracker();

            } else {
                if (Files.exists(path)) {
                	JOptionPane.showMessageDialog(null, "File exists with this name try again");
                }
            }
    	}
    }
    
}
	      