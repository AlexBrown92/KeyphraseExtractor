package ga.algorithm;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class GA {

    private Properties prop;
    private int geneSize;
    private Population pop;

    public GA() {
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
        this.geneSize = Integer.parseInt(prop.getProperty("number_of_rules", "10")) * 3;
        this.pop = new Population(Integer.parseInt(prop.getProperty("population_size", "100")), this.geneSize);
    }

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
