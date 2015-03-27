package ga.algorithm;

import documentanalyser.DocumentAnalyser;
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
    private Population pop;
    private static DocumentAnalyser da;
    private static ArrayList<TestDocument> testData;
    private static ArrayList<TestDocument> trainingData;

    public GA(DocumentAnalyser da, ArrayList<TestDocument> dataset) {
        Random rn = new Random();
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

        this.geneSize = Integer.parseInt(prop.getProperty("number_of_rules", "10")) * 3;
        this.pop = new Population(Integer.parseInt(prop.getProperty("population_size", "100")), this.geneSize);
        this.da = da;
        pop.runFitnessAll();
    }

    public double[] run() {
        String dirPath = prop.getProperty("log_location", "C:\\temp");

        // Timestamp for use in the directory name to ensure that it's unique
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        // Create a directory to hold all the data from the runs.
        File dir = new File(dirPath + timeStamp + "\\");
        if (!dir.exists()) {
            dir.mkdir();
        }
        // Create a settings file and enter run settings values
        File s = new File(dirPath + timeStamp + "\\settings.txt");
        try {
            PrintWriter settings = new PrintWriter(s, "UTF-8");
            settings.println("population_size: " + prop.getProperty("population_size", "100"));
            settings.println("number_of_rules: " + prop.getProperty("number_of_rules", "10"));
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
                System.out.println("Run #" + i + " Mean:\t" + df.format(parents.calculateFitnessMean()) + " Best: (" + df.format(generationBest.getFitness()) + ")\t" + generationBest.displayGene());
                writer.print(i + "," + parents.calculateFitnessMean() + "," + generationBest.getFitness() + ',' + generationBest.displayGene() + "\n");
            }
            writer.close();
            return generationBest.getGene();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        return null;
    }

    public static double calculateFitness(Individual ind) {
        double fitness = 0;
        int count = 0;
        for (TestDocument d : trainingData) {
            ArrayList<String> selectedKeywords = da.analyse(d, ind.getGene());
            double tp = 0; // True Positive
            double fp = 0; // False Positive
            double fn = 0; // False Negative
            if (!selectedKeywords.isEmpty()) {
                for (String knownKeyword : d.getKnownKeywords()) {
                    if (selectedKeywords.contains(knownKeyword)) {
                        selectedKeywords.remove(knownKeyword);
                        tp = +1;
                    } else {
                        fn = +1;
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
            /*
             double recall = tp / (tp + fn);
             double f1 = 0;
             if ((precision + recall) != 0) {
             f1 = 2 * ((precision * recall) / (precision + recall));
             }
             fitness = (((fitness * count) + f1) / (count + 1));*/
            fitness = (((fitness * count) + precision) / (count + 1));
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
