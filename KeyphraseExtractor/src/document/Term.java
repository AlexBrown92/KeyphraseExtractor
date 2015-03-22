package document;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Term {

    private String stemmedText;
    private Map occurrences;

    private int frequency;
    private double firstPos;
    private double lastPos;
    private double avgPos;
    private int firstSentence;
    private int lastSentence;
    private int numberOfWords;
    private boolean inFirstSentence;
    private boolean inLastSentence; // TODO
    private boolean hasProperNoun; // TODO?
    private int x2Score; // TODO?
    private int firstParagraph;
    private int lastParagraph;
    private double averageSentencePos;
    private double averageParagraphPos;
    private int freqInFirst10;
    private int freqInFirst20;
    private int freqInLast10;
    private int freqInLast20;
    private int freqInFirst1P;
    private int freqInFirst2P;
    private int freqInLast1P; 
    private int freqInLast2P; 

    public Term(String text, String stemmedText, double position, int paragraph, double sentencePos, double paragraphPos) {
        this.occurrences = new HashMap<>();
        this.addOccurrence(text);
        this.stemmedText = stemmedText;
        this.firstPos = position;
        this.avgPos = position;
        this.lastPos = position;
        this.firstParagraph = paragraph;
        this.lastParagraph = paragraph;
        this.averageParagraphPos = paragraphPos;
        this.averageSentencePos = sentencePos;
        this.numberOfWords = text.split(" ").length;
        this.freqInFirst10 = 0;
        this.freqInFirst20 = 0;
        this.freqInLast10 = 0;
        this.freqInLast20 = 0;
    }

    public String getMostFrequentOccurrence() {
        String mostFrequent = "";
        int highestFrequency = 0;
        for (Object term : this.occurrences.keySet()) {
            if ((int) this.occurrences.get(term) > highestFrequency){
                highestFrequency = (int) this.occurrences.get(term);
                mostFrequent = (String) term;
            }
        }
        return mostFrequent;
    }

    public String getStemmedText() {
        return stemmedText;
    }

    public void setStemmedText(String stemmedText) {
        this.stemmedText = stemmedText;
    }

    public void addOccurrence(String term) {
        this.frequency++;
        if (this.occurrences.containsKey(term)) {
            this.occurrences.put(term, ((int) this.occurrences.get(term) + 1));
        } else {
            this.occurrences.put(term, 1);
        }
    }
    
    public void updateAvgPos(double newValue){
        this.avgPos = recalcAvg(this.avgPos, newValue, this.frequency);
    }
    
    public void updateAverageSentencePos(double newValue){
        this.averageSentencePos = recalcAvg(this.averageSentencePos, newValue, this.frequency);
    }
    
    public void updateAverageParagraphPos(double newValue){
        this.averageParagraphPos = recalcAvg(this.averageParagraphPos, newValue, this.frequency);
    }
    
    // Used to recalculate a mean with a new value added
    private double recalcAvg(double currentAvg, double newValue, int newTotal){
        double newAvg = currentAvg * (newTotal -1);
        newAvg = (newAvg + newValue) / newTotal;
        return newAvg;
    }
    
    /*
    *
    * The following functions are to make life easier for simply incrementing 
    * values
    *
    */
    
    public void incrementFreqInFirst20(){
        this.freqInFirst20 ++;
    }
    
    public void incrementFreqInFirst10(){
        this.freqInFirst10 ++;
    }
    
    public void incrementFreqInLast20(){
        this.freqInLast20 ++;
    }
    
    public void incrementFreqInLast10(){
        this.freqInLast10 ++;
    }
    
    public void incrementFreqInFirst1P(){
        this.freqInFirst1P ++;
    }
    
    public void incrementFreqInFirst2P(){
        this.freqInFirst2P ++;
    }
    
    public void incrementFreqInLast1P(){
        this.freqInLast1P ++;
    }
    
    public void incrementFreqInLast2P(){
        this.freqInLast2P ++;
    }
    
    /*
    *
    * Only Getters and Setters Beyond here
    *
    */

    public Map getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Map occurrences) {
        this.occurrences = occurrences;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public double getFirstPos() {
        return firstPos;
    }

    public void setFirstPos(double firstPos) {
        this.firstPos = firstPos;
    }

    public double getLastPos() {
        return lastPos;
    }

    public void setLastPos(double lastPos) {
        this.lastPos = lastPos;
    }

    public double getAvgPos() {
        return avgPos;
    }

    public void setAvgPos(double avgPos) {
        this.avgPos = avgPos;
    }

    public int getFirstSentence() {
        return firstSentence;
    }

    public void setFirstSentence(int firstSentence) {
        this.firstSentence = firstSentence;
    }

    public int getLastSentence() {
        return lastSentence;
    }

    public void setLastSentence(int lastSentence) {
        this.lastSentence = lastSentence;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public boolean isInFirstSentence() {
        return inFirstSentence;
    }

    public void setInFirstSentence(boolean inFirstSentence) {
        this.inFirstSentence = inFirstSentence;
    }

    public boolean isInLastSentence() {
        return inLastSentence;
    }

    public void setInLastSentence(boolean inLastSentence) {
        this.inLastSentence = inLastSentence;
    }

    public boolean isHasProperNoun() {
        return hasProperNoun;
    }

    public void setHasProperNoun(boolean hasProperNoun) {
        this.hasProperNoun = hasProperNoun;
    }

    public int getX2Score() {
        return x2Score;
    }

    public void setX2Score(int x2Score) {
        this.x2Score = x2Score;
    }

    public int getFirstParagraph() {
        return firstParagraph;
    }

    public void setFirstParagraph(int firstParagraph) {
        this.firstParagraph = firstParagraph;
    }

    public int getLastParagraph() {
        return lastParagraph;
    }

    public void setLastParagraph(int lastParagraph) {
        this.lastParagraph = lastParagraph;
    }

    public double getAverageSentencePos() {
        return averageSentencePos;
    }

    public void setAverageSentencePos(double averageSentencePos) {
        this.averageSentencePos = averageSentencePos;
    }

    public double getAverageParagraphPos() {
        return averageParagraphPos;
    }

    public void setAverageParagraphPos(double averageParagraphPos) {
        this.averageParagraphPos = averageParagraphPos;
    }

    public int getFreqInFirst10() {
        return freqInFirst10;
    }

    public void setFreqInFirst10(int freqInFirst10) {
        this.freqInFirst10 = freqInFirst10;
    }

    public int getFreqInFirst20() {
        return freqInFirst20;
    }

    public void setFreqInFirst20(int freqInFirst20) {
        this.freqInFirst20 = freqInFirst20;
    }

    public int getFreqInLast10() {
        return freqInLast10;
    }

    public void setFreqInLast10(int freqInLast10) {
        this.freqInLast10 = freqInLast10;
    }

    public int getFreqInLast20() {
        return freqInLast20;
    }

    public void setFreqInLast20(int freqInLast20) {
        this.freqInLast20 = freqInLast20;
    }

    public int getFreqInFirst1P() {
        return freqInFirst1P;
    }

    public void setFreqInFirst1P(int freqInFirst1P) {
        this.freqInFirst1P = freqInFirst1P;
    }

    public int getFreqInFirst2P() {
        return freqInFirst2P;
    }

    public void setFreqInFirst2P(int freqInFirst2P) {
        this.freqInFirst2P = freqInFirst2P;
    }

    public int getFreqInLast1P() {
        return freqInLast1P;
    }

    public void setFreqInLast1P(int freqInLast1P) {
        this.freqInLast1P = freqInLast1P;
    }

    public int getFreqInLast2P() {
        return freqInLast2P;
    }

    public void setFreqInLast2P(int freqInLast2P) {
        this.freqInLast2P = freqInLast2P;
    }

}
