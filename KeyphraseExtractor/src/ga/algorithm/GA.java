package ga.algorithm;

import keyphraseextractor.documentanalyser.DocumentAnalyser;
import ga.data.TestDocument;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class GA {

    private Properties prop;
    private int geneSize;
    private static int numSubrules;
    private Population pop;
    private static DocumentAnalyser da;
    private static ArrayList<TestDocument> testData;
    private static ArrayList<TestDocument> trainingData;
    private static boolean debug;

    public GA(DocumentAnalyser da, ArrayList<TestDocument> dataset, boolean debug) {
        Random rn = new Random();
        this.debug = debug;
        if (debug) {
            System.out.println("Initialising GA");
        }
        // Get known properties for the GA stored in training.properties file
        prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("training.properties");
            // load a properties file
            prop.load(input);
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }
        this.testData = new ArrayList<>();

        // Split data into test/training
        int trainingNumber = (int) Math.round(dataset.size() * Double.parseDouble(prop.getProperty("test_percentage", "0.2")));
        for (int i = 0; i < trainingNumber; i++) {
            testData.add(dataset.remove(rn.nextInt(dataset.size())));
        }
        this.trainingData = new ArrayList<>();
        trainingData.addAll(dataset);
        this.numSubrules = Integer.parseInt(prop.getProperty("number_of_subrules", "3"));
        this.geneSize = Integer.parseInt(prop.getProperty("number_of_rules", "3")) * numSubrules * 3;
        this.pop = new Population(Integer.parseInt(prop.getProperty("population_size", "100")), this.geneSize);
        this.da = da;
        pop.runFitnessAll();
    }

    public void run() {
        if (debug) {
            System.out.println("GA Run started");
        }
        String dirPath = prop.getProperty("log_location", "C:\\temp");

        // Timestamp for use in the directory name to ensure that it's unique
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        // Create a directory to hold all the data from the runs.
        File dir = new File(dirPath + timeStamp + "\\");
        if (!dir.exists()) {
            dir.mkdir();
            if (debug) {
                System.out.println("Creating Directory: " + dir.getAbsolutePath());
            }
        }
        // Create a settings file and enter run settings values
        File s = new File(dirPath + timeStamp + "\\settings.txt");
        try {
            PrintWriter settings = new PrintWriter(s, "UTF-8");
            settings.println("population_size: " + prop.getProperty("population_size", "100"));
            settings.println("number_of_rules: " + prop.getProperty("number_of_rules", "3"));
            settings.println("number_of_rules: " + numSubrules);
            settings.println("max_runs: " + prop.getProperty("max_runs", "5000"));
            settings.println("tournament_size: " + prop.getProperty("tournament_size", "5"));
            settings.println("mutation_rate: " + prop.getProperty("mutation_rate", "0.0333333"));
            settings.println("max_mutation: " + prop.getProperty("max_mutation", "0.2"));
            settings.println("crissover_rate: " + prop.getProperty("crissover_rate", "0.9"));
            settings.println("test_percentage: " + prop.getProperty("test_percentage", "0.2"));
            settings.close();

            File out = new File(dirPath + timeStamp + "\\out.csv"); // Desktop
            PrintWriter writer = new PrintWriter(out, "UTF-8");
            // Write in the column headers
            writer.println("Run #,Mean,Best Fitness,Best Gene");

            DecimalFormat df = new DecimalFormat("#.00000");
            Individual generationBest = pop.getFittestIndividual();
            for (int i = 0; i < Integer.parseInt(prop.getProperty("max_runs", "5000")); i++) {
                Population parents = pop.selectParents(Integer.parseInt(prop.getProperty("tournament_size", "5")));
                parents.combine(Double.parseDouble(prop.getProperty("crissover_rate", "0.9")));
                parents.mutatePopulation(Double.parseDouble(prop.getProperty("mutation_rate", "0.0333333")), Double.parseDouble(prop.getProperty("max_mutation", "0.2")));
                parents.runFitnessAll();
                if (parents.getWorstIndividual().getFitness() < generationBest.getFitness()) {
                    parents.removeIndividual(parents.getPopulation().indexOf(parents.getWorstIndividual()));
                    parents.addIndividual(generationBest);
                }
                generationBest = parents.getFittestIndividual();
                pop = parents;
                System.out.println("Run #" + i + " Mean:\t" + df.format(parents.calculateFitnessMean()) + " Best: (" + df.format(generationBest.getFitness()) + ")\t" + generationBest.displayGene());
                writer.print(i + "," + parents.calculateFitnessMean() + "," + generationBest.getFitness() + ',' + generationBest.displayGene() + "\n");
            }
            writer.close();
            System.out.println("End of Training");
            System.out.println("Testing commencing...");
            File testFile = new File(dirPath + timeStamp + "\\results.csv"); // Desktop
            PrintWriter results = new PrintWriter(testFile, "UTF-8");
            results.println("Document Name,Known Keyphrases,Extracted Keyphrases,Precision,Recall,F-Score");
            double fitness = 0;
            int count = 0;
            for (TestDocument testDoc : testData) {
                System.out.println("Testing: " + testDoc.getDocName());
                results.print(testDoc.getDocName() + ",");
                ArrayList<String> selectedKeywords = da.analyse(testDoc, generationBest.getGene(), numSubrules);
                System.out.print("Known Keywords: ");
                for (String knownKeyword : testDoc.getKnownKeywords()) {
                    results.print(knownKeyword + ":");
                    System.out.print(knownKeyword + ", ");
                }
                System.out.print("\n");
                results.print(",");
                System.out.print("Selected Keywords: ");
                for (String selectedKeyword : selectedKeywords) {
                    results.print(selectedKeyword + ":");
                    System.out.print(selectedKeyword + ", ");
                }
                System.out.print("\n");
                results.print(",");
                double tp = 0; // True Positive
                double fp = 0; // False Positive
                double fn = 0; // False Negative
                if (!selectedKeywords.isEmpty()) {
                    for (String knownKeyword : testDoc.getKnownKeywords()) {
                        if (selectedKeywords.contains(knownKeyword)) {
                            selectedKeywords.remove(knownKeyword);
                            tp += 1;
                        } else {
                            fn += 1;
                        }
                    }
                } else {
                    fn = testDoc.getKnownKeywords().size();
                }
                fp = selectedKeywords.size();
                double precision = 0;
                if (tp != 0) {
                    precision = tp / (tp + fp);
                }

                double recall = tp / (tp + fn);
                double f1 = 0;
                if ((precision + recall) != 0) {
                    f1 = 2 * ((precision * recall) / (precision + recall));
                }
                System.out.println("Precision: " + precision + " Recall: " + recall + " F-Score: " + f1);
                results.print(precision + "," + recall + "," + f1 + "\n");
                fitness = (((fitness * count) + f1) / (count + 1));
                //fitness = (((fitness * count) + precision) / (count + 1));
                count++;
            }
            System.out.println("End Test");
            System.out.println("Test average f score: " + fitness);
            results.close();
            File collationFile = new File(dirPath + "combined_results.csv");
            FileWriter fw = new FileWriter(collationFile, true);
            fw.append("" + prop.getProperty("population_size", "100") + "," + prop.getProperty("number_of_rules", "3") + "," + numSubrules + "," + prop.getProperty("max_runs", "5000") + "," + prop.getProperty("tournament_size", "5") + "," + prop.getProperty("mutation_rate", "0.0333333") + "," + prop.getProperty("max_mutation", "0.2") + "," + prop.getProperty("crissover_rate", "0.9") + "," + prop.getProperty("test_percentage", "0.2") + "," + fitness);
            for (double genePart  : generationBest.getGene()) {
                fw.append(","+genePart+"");
            }
            fw.append("\n");
            fw.close();
            Properties config = new Properties();
            config.setProperty("subrules", Integer.toString(numSubrules));
            config.setProperty("gene", generationBest.displayGene());
            FileOutputStream configOut = new FileOutputStream("config.properties");
            config.store(configOut, timeStamp);
            configOut.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static double calculateFitness(Individual ind) {
        double fitness = 0;
        int count = 0;
        for (TestDocument d : trainingData) {
            ArrayList<String> selectedKeywords = da.analyse(d, ind.getGene(), numSubrules);
            double tp = 0; // True Positive
            double fp = 0; // False Positive
            double fn = 0; // False Negative
            if (!selectedKeywords.isEmpty()) {
                for (String knownKeyword : d.getKnownKeywords()) {
                    if (selectedKeywords.contains(knownKeyword)) {
                        selectedKeywords.remove(knownKeyword);
                        tp += 1;
                    } else {
                        fn += 1;
                    }
                }
            } else {
                fn = d.getKnownKeywords().size();
            }
            fp = selectedKeywords.size();
            double precision = 0;
            if (tp != 0) {
                precision = tp / (tp + fp);
            }

            double recall = tp / (tp + fn);
            double f1 = 0;
            if ((precision + recall) != 0) {
                f1 = 2 * ((precision * recall) / (precision + recall));
            }
            fitness = (((fitness * count) + f1) / (count + 1));
            //fitness = (((fitness * count) + precision) / (count + 1));
            count++;
        }
        return fitness;
    }

    /*
     Getters and setters beyond here
     */
    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public int getGeneSize() {
        return geneSize;
    }

    public void setGeneSize(int geneSize) {
        this.geneSize = geneSize;
    }

    public Population getPop() {
        return pop;
    }

    public void setPop(Population pop) {
        this.pop = pop;
    }

}
