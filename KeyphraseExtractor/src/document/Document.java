package document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import stemmer.*;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Document {

    private String text;
    private int length;
    private HashMap<String, Term> terms;
    private ArrayList<Paragraph> paragraphs;
    private ArrayList<String> stopWords;

    public Document(String text, ArrayList<String> stopWords) {
        this.stopWords = stopWords;
        this.text = text;
        this.paragraphs = new ArrayList<>();
        this.terms = new HashMap<>();
        String[] pTemp = text.split("\n");
        IteratedLovinsStemmer stemmer = new IteratedLovinsStemmer();
        this.length = text.replace("\n\r", " ").replace("\r\n", " ").split(" ").length;

        int wordNum = 0;

        // Identify Terms
        for (int pCount = 0; pCount < pTemp.length; pCount++) {
            String paragraph = pTemp[pCount];
            //for (String paragraph : pTemp) {
            // Check to see if paragraph is empty
            if (!paragraph.equals("\r") && !paragraph.trim().isEmpty()) {
                Paragraph p = new Paragraph(paragraph);
                paragraphs.add(p);
                int paragraphSize = p.toString().split(" ").length;
                for (int sCount = 0; sCount < p.getSentences().size(); sCount++) {
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
        }
    }

    private void addTerm(String term, String stemmed, double position, int paragraph, double sentencePos, double paragraphPos, int sentence) {
        if (term.length() > 3) {
            if (!terms.containsKey(stemmed)) {
                terms.put(stemmed, new Term(term, stemmed, position, paragraph, sentencePos, paragraphPos));
                if ((paragraph == 0) && (sentence == 0)) {
                    terms.get(stemmed).setInFirstSentence(true);
                }
            } else {
                terms.get(stemmed).addOccurrence(term);
                terms.get(stemmed).setLastParagraph(paragraph);
                terms.get(stemmed).updateAvgPos(position);
                terms.get(stemmed).updateAverageSentencePos(sentencePos);
                terms.get(stemmed).updateAverageParagraphPos(paragraphPos);
            }
            if (position <= 0.2) {
                terms.get(stemmed).incrementFreqInFirst20();
                if (position <= 0.1) {
                    terms.get(stemmed).incrementFreqInFirst10();
                }
            } else if (position >= 0.8) {
                terms.get(stemmed).incrementFreqInLast20();
                if (position >= 0.9) {
                    terms.get(stemmed).incrementFreqInLast10();
                }
            }
        }
    }
    
    

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public ArrayList<String> getStopWords() {
        return stopWords;
    }

    public void setStopWords(ArrayList<String> stopWords) {
        this.stopWords = stopWords;
    }

}
