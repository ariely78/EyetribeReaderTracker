import java.awt.Point;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;


public class WordManipulation {
	String lastWordChanged;
	int startIndex;
    int endIndex;

	boolean changeWordBack = false;
	String swapWord = "name";
	String wordToActivateChange = "to";
	String currentWord = "";

	int timeUntilNextWordChange = 500;
	boolean wordChanged = false;
	private long lastTimeStamp = 0;
	int numWordsInfront = 2;
	
    public String getWord(int caretPosition, JTextPane txtContent) throws BadLocationException {
        int startIndex;
        int endIndex;
        int i = 0;

        while(isCharALetter(txtContent, caretPosition+i)){
        	i++;
        }
//        while (Character.isLetter(txtContent.getText(caretPosition + i, 1).charAt(0)))  {
//            i++;
//        }
        endIndex = caretPosition + i;
        int j = 0;
        while (j < caretPosition && isCharALetter(txtContent, caretPosition - j - 1)) {
            j++;
        }
//        while (j < caretPosition && Character.isLetter(txtContent.getText(caretPosition - j - 1, 1).charAt(0))) {
//            j++;
//        }
        startIndex = caretPosition - j;
        return txtContent.getText(startIndex, endIndex - startIndex);
    }
    
    public int letterTracked(JTextPane txtContent, 
						    		int caretPosition, 
						    		Point trackPoint,
						    		int fontHeight) throws BadLocationException
    {
    	int newCaretPosition = 0;
		newCaretPosition = caretPosition;

    	//calcualte s extra space
    	int extraSpace = ((int)(Settings.lineSpacing) * fontHeight)/2;
    	
    	//get new trackpoing taking off the extra space
    	Point newTrackPoint = new Point(trackPoint.x, trackPoint.y+extraSpace);
    	System.out.println("newTrackPoint"+newTrackPoint);
    	//get new caret position according to new trackpoint
    	newCaretPosition = txtContent.viewToModel(newTrackPoint);
    	//if we find a letter at the caret position then
    	if(!isCharALetter(txtContent, newCaretPosition)){
    		newTrackPoint = new Point(trackPoint.x, trackPoint.y-extraSpace);
        	System.out.println("newTrackPoint2"+newTrackPoint);

        	newCaretPosition = txtContent.viewToModel(newTrackPoint);
	    	if(isCharALetter(txtContent, newCaretPosition)){
	        	newCaretPosition = txtContent.viewToModel(newTrackPoint);
	    	}
    	}
    	return newCaretPosition;
    }
    
    public boolean isCharALetter(JTextPane jta, int caretPosition) throws BadLocationException
    {
    	return Character.isLetter(charAtPosition(jta,caretPosition));
    }
    
    public char charAtPosition(JTextPane jta, int caretPosition) throws BadLocationException
    {
    	char ch = jta.getText(caretPosition,1).charAt(0);
    	return ch;
    }
    
	public boolean changeWordXWordsInfront(String wordToInsertParam,
    											int numberOfWordsAhead,
								    			int caretPosition, 
								    			JTextPane txtContent) throws BadLocationException {
        int currentWordInt = 0;
        int i = 0;
    	String wordToReplace= "";
    	this.currentWord = getWord(caretPosition,txtContent);
        while (currentWordInt < numberOfWordsAhead) {
        	//while(isCharALetter(txtContent, caretPosition + i)){
	        while (!txtContent.getText(caretPosition + i, 1).equals(" ")){
	            System.out.println(""+txtContent.getText(caretPosition+i,1));
	            i++;
	        }
        	
	        //move 2 carets forward to start of next word by adding 1 
	        //(the space, then next word) assuming there is only one space!
	        caretPosition+=i+1;
	        currentWordInt++;
	        i = 0;
	        startIndex = caretPosition;
	        wordToReplace = getWord(startIndex,txtContent);
	        //set caret position to endIndex - length of word we are replacing 
	        //but -1 from that so we get start index of word
	        endIndex = caretPosition + wordToReplace.length()-1;

        }
    	//come across newline then just return, dont change words between lines
    	if(txtContent.getText(caretPosition + i, 1).equals("\n"))
    		return false;
        
        //only change the word if the word X words infront is same length as our swapword
        String temp = getWord(startIndex,txtContent);
        if(temp.length() == this.swapWord.length())
        {
        	this.lastWordChanged = getWord(startIndex,txtContent);
        } else {
        	return false;
        }
        
        changeWordsInDocument(wordToInsertParam, wordToReplace, txtContent);

        return true;
    }

    public boolean swapWordBack(String wordToSwap,
			int caretPosition, 
			JTextPane txtContent) throws BadLocationException
    {
        int i = 0;
    	while(isCharALetter(txtContent, caretPosition + i)){
//        while (!txtContent.getText(caretPosition + i, 1).equals(" ")){
        	//come across newline then just return, dont change words between lines
        	if(txtContent.getText(caretPosition + i, 1).equals("\n"))
        		return false;
            System.out.println(""+txtContent.getText(caretPosition+i,1));
            i++;
        } 

    	caretPosition += i;
        startIndex = caretPosition - wordToSwap.length();
        endIndex = caretPosition-1;
	    
        changeWordsInDocument(this.lastWordChanged, wordToSwap, txtContent);

        return false;
    }

	public void ChangeWords(int caretPosition, JTextPane txtContent) throws BadLocationException
	{
        if((System.currentTimeMillis() - lastTimeStamp) > timeUntilNextWordChange){
        	lastTimeStamp = System.currentTimeMillis();
        }	
		if(isCharALetter(txtContent, caretPosition)) {

        	if(!this.wordChanged)
        	{
        		wordChanged = changeWordXWordsInfront(swapWord, numWordsInfront, caretPosition, txtContent);
        		if (wordChanged){
        			DocumentReader.writeToTextFile(Settings.fileName, "WORD CHANGED: "+this.lastWordChanged+" , Time:" +System.currentTimeMillis());
        		}
        	}
	    	
	        //if we changed a word and the gaze has moved onto the next word
	        if(wordChanged && !getWord(caretPosition, txtContent).equalsIgnoreCase(this.currentWord))
	        {
	        	chooseWordToSwap(txtContent);
	        }
		} else if (wordChanged) { //if we have a non character (whitespace) but the word has been swapped swap it back!
        	chooseWordToSwap(txtContent);
		}
        
	}
	
	public void chooseWordToSwap(JTextPane txtContent) throws BadLocationException{
    	//swaps word that was changed back
    	double d = Math.random();
    	if (d < 0.5){
    	    // 50% chance of being here
    		swapWord = new StringBuilder(swapWord).reverse().toString();
    	}
    	wordChanged = swapWordBack(swapWord, startIndex, txtContent);
		DocumentReader.writeToTextFile(Settings.fileName, "WORD SWAPPED BACK: "+swapWord+" , Time:" +System.currentTimeMillis());
	}
	
	public void changeWordsInDocument(String wordToInsertParam, String wordToReplaceParam,JTextPane txtContent) throws BadLocationException
	{
		StyledDocument doc = txtContent.getStyledDocument();
    	doc.remove(startIndex, wordToReplaceParam.length());
    	doc.insertString(startIndex, wordToInsertParam, null);
	}
}
