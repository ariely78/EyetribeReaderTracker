import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.Preferences;


public class SettingsPanel extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
    JLabel messagesLabel = new JLabel( "Switch messages on:" );
    JLabel calibratePointTimeLabel = new JLabel( "Time for Calibration point:" );
    JLabel moveToCalibratePointTimeLabel = new JLabel( "Time for Calibration point:" );

    JTextField wordActivateTF = new JTextField( 20 );
    JTextField wordToSwapTF = new JTextField( 20 );
    JTextField fontSizeTF = new JTextField( 20 );
    JTextField timeNextChangeTF = new JTextField( 20 );
    JTextField numWordsInfrontTF = new JTextField( 20 );
    JTextField wordReadingTimeTF = new JTextField( 20 );
    JTextField textareaXTF = new JTextField( 20 );
    JTextField textareaYTF = new JTextField( 20 );
    JTextField calibratePointTime = new JTextField( 20 );
    JTextField moveToCalibratePointTime = new JTextField(20);

    public JCheckBox messagesCheckBox = new JCheckBox("Turn On Messages");;

	MouseWordSelection mousetxtContent = new MouseWordSelection(this);
	EyetrackerWordSelection eyetrackerTxtContent = new EyetrackerWordSelection(this);
	MainEyeTrackerFrame testWindow = new MainEyeTrackerFrame(mousetxtContent,eyetrackerTxtContent);

	// Retrieve the user preference node for the package com.mycompany
	Preferences prefs = Preferences.userNodeForPackage(this.getClass());
	// Preference key name
	final String FONT_SIZE = "fontSize";
	
	public SettingsPanel() {
	    super("Settings Panel");
	    setContentPane(createContent());
	    setTextFields();
	    setVisible(true);
	}

	private Container createContent()
	{
	    JPanel result = new JPanel();
	    result.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
	    messagesCheckBox.addItemListener(new CheckBoxListener());

	    startMouseButton = new JButton("Start Mouse");
	    startMouseButton.setName("mouse");
	    startMouseButton.setSize(100, 50);
	    startMouseButton.addActionListener(this);

        startEyetrackerButton = new JButton("Start Eyetracker");
        startEyetrackerButton.setName("eyetracker");
        startEyetrackerButton.setSize(100, 50);
        startEyetrackerButton.addActionListener(this);
        
        setSettings = new JButton("Start Experiment");
        setSettings.setName("start");
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
	                                                          .addComponent( messagesLabel )
	                                                          .addComponent(calibratePointTimeLabel)
	                                                          .addComponent(moveToCalibratePointTimeLabel)
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
	                                                          .addComponent( messagesCheckBox )
	                                                          .addComponent(calibratePointTime)
	                                                          .addComponent(moveToCalibratePointTime)
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
	                                                        .addComponent( messagesLabel )
	                                                        .addComponent( messagesCheckBox ) )	
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( calibratePointTimeLabel )
	                                                        .addComponent( calibratePointTime ) )
	                                     .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
	                                                        .addComponent( moveToCalibratePointTimeLabel )
	                                                        .addComponent( moveToCalibratePointTime ) )
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
		prefs.putInt(FONT_SIZE, eyetrackerTxtContent.fontSize);
		timeNextChangeTF.setText(eyetrackerTxtContent.wordChanger.timeUntilNextWordChange+"");
		numWordsInfrontTF.setText(eyetrackerTxtContent.wordChanger.numWordsInfront+"");
		wordReadingTimeTF.setText(eyetrackerTxtContent.wordReadingTime+"");
		textareaXTF.setText(eyetrackerTxtContent.textareaX+"");
		textareaYTF.setText(eyetrackerTxtContent.textareaY+"");
		moveToCalibratePointTime.setText(Settings.timeToMoveToGaze+"");
		calibratePointTime.setText(Settings.calibrateTime+"");
	}

	public void setVariables()
	{
		mousetxtContent.wordChanger.wordToActivateChange = wordActivateTF.getText();
		mousetxtContent.wordChanger.swapWord = wordToSwapTF.getText();
		mousetxtContent.fontSize = 	Integer.parseInt(prefs.get(FONT_SIZE, fontSizeTF.getText()));
		mousetxtContent.wordChanger.timeUntilNextWordChange = Integer.parseInt(timeNextChangeTF.getText());
		mousetxtContent.wordChanger.numWordsInfront = Integer.parseInt(numWordsInfrontTF.getText());
		mousetxtContent.wordReadingTime = Integer.parseInt(wordReadingTimeTF.getText());
		
		eyetrackerTxtContent.wordChanger.wordToActivateChange = wordActivateTF.getText();
		eyetrackerTxtContent.wordChanger.swapWord = wordToSwapTF.getText();
		eyetrackerTxtContent.fontSize = Integer.parseInt(prefs.get(FONT_SIZE, fontSizeTF.getText()));
		eyetrackerTxtContent.wordChanger.timeUntilNextWordChange = Integer.parseInt(timeNextChangeTF.getText());
		eyetrackerTxtContent.wordChanger.numWordsInfront = Integer.parseInt(numWordsInfrontTF.getText());
		eyetrackerTxtContent.wordReadingTime = Integer.parseInt(wordReadingTimeTF.getText());
		eyetrackerTxtContent.textareaX = Integer.parseInt(textareaXTF.getText());
		eyetrackerTxtContent.textareaY = Integer.parseInt(textareaYTF.getText());
		
		Settings.calibrateTime = Integer.parseInt(calibratePointTime.getText());
		Settings.timeToMoveToGaze = Integer.parseInt(moveToCalibratePointTime.getText());
//		moveToCalibratePointTimeLabel.setText(Settings.timeToMoveToGaze+"");
//		calibratePointTime.setText(Settings.calibrateTime+"");
	}
	
    @Override
	public void actionPerformed(ActionEvent e)
    {
	    JButton o = (JButton)e.getSource();
	    String name = o.getName();
	    setVariables();
    	if(name.equalsIgnoreCase("start")) {
    		//set fullscreen
    	    testWindow.parentPanel.showNameInputBox(name);
    	    GraphicsDevice gd = 
					GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			testWindow.setUndecorated(true);
			gd.setFullScreenWindow(testWindow);
    	    this.eyetrackerTxtContent.setTextAreaText();
    	    testWindow.pack();
    	    testWindow.setVisible(true);
    	    CardLayout cl = (CardLayout) (testWindow.parentPanel.getLayout());
			cl.show(testWindow.parentPanel, "Calibrate");
			testWindow.parentPanel.init_calibration_process(false);
        	
    	} else if(name.equalsIgnoreCase("mouse")) {
    		testWindow.parentPanel.showNameInputBox(name);
    	    this.mousetxtContent.setTextAreaText();
    		testWindow.pack();
    	    testWindow.setVisible(true);
    	    CardLayout cl = (CardLayout) (testWindow.parentPanel.getLayout());
			cl.show(testWindow.parentPanel, "MouseTracker");
    	} else { //eyetracker pressed
			testWindow.parentPanel.showNameInputBox(name);
    	    this.eyetrackerTxtContent.setTextAreaText();
    		testWindow.pack();
    	    testWindow.setVisible(true);
    	    CardLayout cl = (CardLayout) (testWindow.parentPanel.getLayout());
			cl.show(testWindow.parentPanel, "Eyetracker");
    	}
    }
    
    private class CheckBoxListener implements ItemListener{
        public void itemStateChanged(ItemEvent e) {
            if(e.getSource()==messagesCheckBox){
                if(messagesCheckBox.isSelected()) {
                	Settings.hideMessages = true;
                } else {
                	Settings.hideMessages = false;

                	System.out.println("nothing");
                	}
            }
        }
    }
}
	      