import java.awt.Point;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;


public class WordManipulation {
	String lastWordChanged;
	int startIndex;
    int endIndex;
	boolean changeWordBack = false;
	String swapWord = "name";
	String wordToActivateChange = "to";
	int timeUntilNextWordChange = 0;
	boolean wordChanged = false;
	private long lastTimeStamp = 0;
	int numWordsInfront = 2;
	
    public String getWord(int caretPosition, JTextArea txtContent) throws BadLocationException {
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
    
    public int letterTracked(JTextArea txtContent, 
						    		int caretPosition, 
						    		Point trackPoint,
						    		int fontHeight) throws BadLocationException
    {
    	int newCaretPosition = 0;
		newCaretPosition = caretPosition;

    	if(isCharALetter(txtContent, caretPosition)){
    		return newCaretPosition;
    	}
    	int extraSpace = fontHeight/2;
    	Point newTrackPoint = new Point(trackPoint.x, trackPoint.y-extraSpace);
    	newCaretPosition = txtContent.viewToModel(newTrackPoint);
    	if(!isCharALetter(txtContent, newCaretPosition)){
    		newTrackPoint = new Point(trackPoint.x, trackPoint.y+extraSpace);
        	newCaretPosition = txtContent.viewToModel(newTrackPoint);
	    	if(isCharALetter(txtContent, newCaretPosition)){
	        	newCaretPosition = txtContent.viewToModel(newTrackPoint);
	    	}
    	}
    	return newCaretPosition;
    }
    
    public boolean isCharALetter(JTextArea jta, int caretPosition) throws BadLocationException
    {
    	return Character.isLetter(charAtPosition(jta,caretPosition));
    }
    
    public char charAtPosition(JTextArea jta, int caretPosition) throws BadLocationException
    {
    	char ch = jta.getText(caretPosition,1).charAt(0);
    	return ch;
    }
    
	public boolean changeWordXWordsInfront(String wordToInsertParam,
    											int numberOfWordsAhead,
								    			int caretPosition, 
								    			JTextArea txtContent) throws BadLocationException {
        int currentWord = 0;
        int i = 0;
    	String wordToReplace= "";

        while (currentWord < numberOfWordsAhead) {
        	//while(isCharALetter(txtContent, caretPosition + i)){
	        while (!txtContent.getText(caretPosition + i, 1).equals(" ")){
	            System.out.println(""+txtContent.getText(caretPosition+i,1));
	            i++;
	        }
        	
	        //move 2 carets forward to start of next word by adding 1 
	        //(the space, then next word) assuming there is only one space!
	        caretPosition+=i+1;
	        currentWord++;
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
        
         removeAddSpace(wordToInsertParam, wordToReplace, txtContent);

        return true;
    }

    public boolean swapWordBack(String wordToSwap,
			int caretPosition, 
			JTextArea txtContent) throws BadLocationException
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
	    
        removeAddSpace(this.lastWordChanged, wordToSwap, txtContent);

        return false;
    }

	public void ChangeWords(int caretPosition, JTextArea txtContent) throws BadLocationException
	{
        if((System.currentTimeMillis() - lastTimeStamp) > timeUntilNextWordChange){
        	lastTimeStamp = System.currentTimeMillis();
        	
        	if(!this.wordChanged)
        		wordChanged = changeWordXWordsInfront(swapWord, numWordsInfront, caretPosition, txtContent);

	        //only change word if selected word is "to"
//	    	if(getWord(caretPosition, txtContent).equalsIgnoreCase(wordToActivateChange) && !this.wordChanged)
//	    	{
//	    		wordChanged = changeWordXWordsInfront(swapWord, numWordsInfront, caretPosition, txtContent);
//	    	}
	    	
	        //if we changed a word and the current word selected is same as the word we changed to, change it back!
	        if(wordChanged && getWord(caretPosition, txtContent).equalsIgnoreCase(swapWord))
	        {
	        	wordChanged = swapWordBack(swapWord, caretPosition, txtContent);
	        }
        }
	}
	
	public void removeAddSpace(String wordToInsertParam, String wordToReplaceParam,JTextArea txtContent)
	{
		int i = 0;
        if(wordToReplaceParam.length() < wordToInsertParam.length()){
        	int charsToAdd = wordToInsertParam.length() - wordToReplaceParam.length();
        	i = 1;
        	while(i <= charsToAdd){
        		txtContent.insert(" ", endIndex + i);
        		i++;
        	}
            //then insert our new word
            txtContent.replaceRange(wordToInsertParam, startIndex, startIndex + wordToInsertParam.length());//endIndex - startIndex);

        } else {
        	//if the word we are replacing is longer than the word we are
        	//inserting then remove the extra space
        	int charsToRemove = wordToReplaceParam.length() - wordToInsertParam.length();
        	i = 0;
        	while(i < charsToRemove){
        		txtContent.insert("", startIndex + wordToInsertParam.length() + i);
        		i++;
        	}
            //then insert our new word
            txtContent.replaceRange(wordToInsertParam, startIndex, startIndex+wordToReplaceParam.length());//endIndex - startIndex);
        }
	}
}
