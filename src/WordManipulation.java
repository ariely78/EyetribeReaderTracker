import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;


public class WordManipulation {
    public static String getWord(int caretPosition, JTextArea txtContent) throws BadLocationException {
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
    
    public static void changeWordXWordsInfront(String wordToInsert,
    											int numberOfWordsAhead,
								    			int caretPosition, 
								    			JTextArea txtContent) throws BadLocationException {
    	int startIndex;
        int endIndex;
        int currentWord = 0;
        int i = 0;

        while (currentWord < numberOfWordsAhead) {
//        	if(txtContent.getText(caretPosition + i, 1).equals("\n"))
//        		return;
	        while (!txtContent.getText(caretPosition + i, 1).equals(" ")){
	                //&& !txtContent.getText(caretPosition + i, 1).equals("\n")) {

	            System.out.println(""+txtContent.getText(caretPosition+i,1));
	            i++;
	        }
	        
	        //move 2 carets forward to start of next word by adding 1 
	        //(the space, then next word) assuming there is only one space!
	        caretPosition+=i+1;
	        currentWord++;
	        i = 0;
        }
        String wordToReplace = getWord(caretPosition,txtContent);
        endIndex = caretPosition + wordToReplace.length()-1;
        
        //set caret position to endIndex - length of word we are replacing 
        //but -1 from that so we get start index of word
        startIndex = endIndex-(wordToReplace.length()-1);

        //If the word we are replacing is shorter than the word we are 
        //inserting then add extra space
        if(wordToReplace.length() < wordToInsert.length()){
        	int charsToAdd = wordToInsert.length() - wordToReplace.length();
        	i = 1;
        	while(i < charsToAdd){
        		txtContent.insert(" ", endIndex + i);
        		i++;
        	}
        } else {
        	//if the word we are replacing is longer than the word we are
        	//inserting then remove the extra space
        	int charsToRemove = wordToReplace.length() - wordToInsert.length();
        	i = 0;
        	while(i < charsToRemove){
        		txtContent.insert("", startIndex + wordToInsert.length() + i);
        		i++;
        	}
        }
        //then insert our new word
        txtContent.replaceRange(wordToInsert, startIndex, startIndex+wordToReplace.length());//endIndex - startIndex);
    }
}
