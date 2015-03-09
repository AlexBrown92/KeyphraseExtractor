package Document;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Paragraph {

    ArrayList<String> sentences;

    public Paragraph(String text) {
        String[] s_temp = text.split("\\.");
        sentences = new ArrayList<>();
        for (String sentence : s_temp) {
            if (!sentence.equals("\r") && !sentence.equals("")){
                sentences.add(sentence);
            }
        }
        
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
