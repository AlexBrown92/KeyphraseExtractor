package keyphraseextractor.document;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Paragraph {

    ArrayList<String> sentences;

    public Paragraph(String text) {
        String[] s_temp = text.split("\\.?!");
        sentences = new ArrayList<>();
        for (String sentence : s_temp) {
            if (!sentence.equals("\r") && !sentence.trim().isEmpty()){
                sentences.add(sentence);
            }
        }
        
    }

    public ArrayList<String> getSentences() {
        return sentences;
    }

    public void setSentences(ArrayList<String> sentences) {
        this.sentences = sentences;
    }
    
    public int getLength(){
        return this.toString().split(" ").length;
    }

    @Override
    public String toString() {
        String out = "";
        for (String sentence : sentences) {
            out = out.concat(sentence + " ");
        }
        return out;
    }

}
