package document;

import java.util.ArrayList;
import java.util.HashMap;
import stemmer.*;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Document {

    private int length;
    private HashMap<String, Term> terms;
    private ArrayList<Paragraph> paragraphs;

    public Document(String text, ArrayList<String> stopWords) {
        this.paragraphs = new ArrayList<>();
        this.terms = new HashMap<>();
        String[] pTemp = text.split("\n");
        IteratedLovinsStemmer stemmer = new IteratedLovinsStemmer();
        this.length = text.replace("\n\r", " ").replace("\r\n", " ").split(" ").length;

        int wordNum = 0;
        
        // Populate Paragraphs ArrayList
        for (String para : pTemp) {
            // Check to see if paragraph is empty
            if (!para.equals("\r") && !para.trim().isEmpty()) {
                Paragraph p = new Paragraph(para);
                paragraphs.add(p);
            }
        }

        // Identify Terms
        for (short pCount = 0; pCount < paragraphs.size(); pCount++) {
            Paragraph p = paragraphs.get(pCount);
            int paragraphSize = p.toString().split(" ").length;
            for (short sCount = 0; sCount < p.getSentences().size(); sCount++) {
                int wordInParagraph = 0;
                String s = p.getSentences().get(sCount);
                //for (String s : p.getSentences()) {
                s = s.replaceAll("[^A-Za-z0-9 '-]", "");
                String[] words = s.split(" ");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i].trim();
                    String stemmed = stemmer.stem(word.toLowerCase());
                    // Make sure term isn't on the stop words list
                    if (!stopWords.contains(word) && !stopWords.contains(stemmed)) {
                        addTerm(word, stemmed, ((double) wordNum / (double) this.length), pCount, ((double) i / (double) words.length), ((double) wordInParagraph / (double) paragraphSize), sCount);
                        // If there are still more words
                        if (words.length > i + 1) {
                            String word2 = words[i + 1].trim();
                            String stem2 = stemmer.stem(word2.toLowerCase());
                            String combinedStem = stemmed.concat(" " + stem2);
                            String combinedTerm = word.concat(" " + word2);
                            // Don't end a term with a stop word
                            if (!stopWords.contains(word2.toLowerCase())) {
                                addTerm(combinedTerm, combinedStem, ((double) wordNum / (double) this.length), pCount, ((double) i / (double) words.length), ((double) wordInParagraph / (double) paragraphSize), sCount);
                            }
                            if (words.length > i + 2) {
                                String word3 = words[i + 2].trim();
                                String stem3 = stemmer.stem(word3.toLowerCase());
                                if (!stopWords.contains(word3.toLowerCase())) {
                                    combinedStem = combinedStem.concat(" " + stem3);
                                    combinedTerm = combinedTerm.concat(" " + word3);
                                    addTerm(combinedTerm, combinedStem, ((double) wordNum / (double) this.length), pCount, ((double) i / (double) words.length), ((double) wordInParagraph / (double) paragraphSize), sCount);
                                }
                            }
                        }
                    }
                    wordInParagraph++;
                    wordNum++;
                }
            }

        }
        // For performance reasons, get rid of any term with a frequency of 1
        ArrayList<String> removeList = new ArrayList<>();
        for (String key : terms.keySet()) {
            if (terms.get(key).getTermCount()== 1){
                removeList.add(key);
            }
        }
        for (String key : removeList) {
            terms.remove(key);
        }
    }

    private void addTerm(String term, String stemmed, double position, short paragraph, double sentencePos, double paragraphPos, int sentence) {
        if (term.length() > 3) {
            if (!terms.containsKey(stemmed)) {
                terms.put(stemmed, new Term(term, stemmed, position, paragraph, sentencePos, paragraphPos, this.length));
                if ((paragraph == 0) && (sentence == 0)) {
                    terms.get(stemmed).setInFirstSentence(true);
                }
            } else {
                terms.get(stemmed).addOccurrence(term, this.length);
                terms.get(stemmed).setLastParagraph(paragraph);
                terms.get(stemmed).updateAvgPos(position);
                terms.get(stemmed).updateAverageSentencePos(sentencePos);
                terms.get(stemmed).updateAverageParagraphPos(paragraphPos);
            }
            if (position <= 0.2) {
                terms.get(stemmed).incrementFreqInFirst20(this.length);
                if (position <= 0.1) {
                    terms.get(stemmed).incrementFreqInFirst10(this.length);
                }
            } else if (position >= 0.8) {
                terms.get(stemmed).incrementFreqInLast20(this.length);
                if (position >= 0.9) {
                    terms.get(stemmed).incrementFreqInLast10(this.length);
                }
            }
            if (paragraph < 2) {
                terms.get(stemmed).incrementFreqInFirst2P((this.paragraphs.get(0).getLength() + this.paragraphs.get(1).getLength()));
                if (paragraph == 0) {
                    terms.get(stemmed).incrementFreqInFirst1P(this.paragraphs.get(0).getLength());
                }
            }
            if (paragraph >= (paragraphs.size() - 2)) {
                terms.get(stemmed).incrementFreqInLast2P((this.paragraphs.get(this.paragraphs.size()-1).getLength() + this.paragraphs.get(this.paragraphs.size()-2).getLength()));
                if (paragraph >= (paragraphs.size() - 1)) {
                    terms.get(stemmed).incrementFreqInLast1P(this.paragraphs.get(this.paragraphs.size()-1).getLength());
                }
            }
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public HashMap<String, Term> getTerms() {
        return terms;
    }

    public void setTerms(HashMap<String, Term> terms) {
        this.terms = terms;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

}
