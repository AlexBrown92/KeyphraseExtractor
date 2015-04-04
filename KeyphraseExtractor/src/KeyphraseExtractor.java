
/**
 *
 * @author Alex
 */
public class KeyphraseExtractor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("-t")){
            ExtractorTrainer ext = new ExtractorTrainer();
            ext.train(args[1]);
        } else if (args[0].equalsIgnoreCase("-e")){
            Extractor ex = new Extractor();
            ex.extract(args[1]);
        }
    }

}
