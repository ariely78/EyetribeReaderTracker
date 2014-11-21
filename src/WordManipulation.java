import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;


public class WordManipulation {
	String wordToReplace;
	String selectedWord;
	String wordToInsert;
	int startIndex;
    int endIndex;
    
    public String getWord(int caretPosition, JTextArea txtContent) throws BadLocationException {
        int startIndex;
        int endIndex;
        int i = 0;
        while (!txtContent.getText(caretPosition + i, 1).equals(" ")
                && !txtContent.getText(caretPosition + i, 1).equals("\n")) {
            i++;
        }
        endIndex = caretPosition + i;
        int j = 0;
        while (j < caretPosition && !txtContent.getText(caretPosition - j - 1, 1).equals(" ")) {
            j++;
        }
        startIndex = caretPosition - j;
        return txtContent.getText(startIndex, endIndex - startIndex);
    }
    
    public boolean changeWordXWordsInfront(String wordToInsert,
    											int numberOfWordsAhead,
								    			int caretPosition, 
								    			JTextArea txtContent) throws BadLocationException {
    	
        int currentWord = 0;
        int i = 0;

        while (currentWord < numberOfWordsAhead) {

	        while (!txtContent.getText(caretPosition + i, 1).equals(" ")){
	        	//come across newline then just return, dont change words between lines
	        	if(txtContent.getText(caretPosition + i, 1).equals("\n"))
	        		return false;
	            System.out.println(""+txtContent.getText(caretPosition+i,1));
	            i++;
	        }
	        
	        //move 2 carets forward to start of next word by adding 1 
	        //(the space, then next word) assuming there is only one space!
	        caretPosition+=i+1;
	        currentWord++;
	        i = 0;
        }
    	this.wordToInsert = wordToInsert;
        wordToReplace = getWord(caretPosition,txtContent);
        endIndex = caretPosition + wordToReplace.length()-1;
        
        //set caret position to endIndex - length of word we are replacing 
        //but -1 from that so we get start index of word
        startIndex = endIndex-(wordToReplace.length()-1);

        //If the word we are replacing is shorter than the word we are 
        //inserting then add extra space
        if(wordToReplace.length() < wordToInsert.length()){
        	int charsToAdd = wordToInsert.length() - wordToReplace.length();
        	i = 1;
        	while(i <= charsToAdd){
        		txtContent.insert(" ", endIndex + i);
        		i++;
        	}
            //then insert our new word
            txtContent.replaceRange(wordToInsert, startIndex, startIndex+wordToInsert.length());//endIndex - startIndex);

        } else {
        	//if the word we are replacing is longer than the word we are
        	//inserting then remove the extra space
        	int charsToRemove = wordToReplace.length() - wordToInsert.length();
        	i = 0;
        	while(i < charsToRemove){
        		txtContent.insert("", startIndex + wordToInsert.length() + i);
        		i++;
        	}
            //then insert our new word
            txtContent.replaceRange(wordToInsert, startIndex, startIndex+wordToReplace.length());//endIndex - startIndex);
        }
        return true;
    }
}
