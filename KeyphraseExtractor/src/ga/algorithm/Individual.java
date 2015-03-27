package ga.algorithm;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Individual {

    private double[] gene;
    private double fitness;
    private Random rn;

    public Individual(int size) {
        this.gene = new double[size];
        this.rn = new Random();
        this.populate();
        this.fitness = 0;
    }

    public Individual(double[] gene) {
        this.gene = gene;
        this.fitness = 0;
        this.rn = new Random();
    }

    private void populate() {
        for (int i = 0; i < gene.length; i++) {
            if (i % 3 == 0) {
                gene[i] = rn.nextInt(19);
            } else {
                gene[i] = rn.nextDouble();
            }
        }
    }

    public Individual[] crossover(Individual parent2) {
        Individual parent1 = this;
        int crossoverPoint = generateCrossOverPoint();
        double[] g1, g2;

        g1 = new double[gene.length];
        g2 = new double[gene.length];

        for (int i = 0; i < gene.length; i++) {
            if (i < crossoverPoint) {
                g1[i] = parent1.getGene()[i];
                g2[i] = parent2.getGene()[i];
            } else {
                g1[i] = parent2.getGene()[i];
                g2[i] = parent1.getGene()[i];
            }
        }

        Individual[] children = new Individual[2];
        children[0] = new Individual(g1);
        children[1] = new Individual(g2);

        return children;
    }

    private int generateCrossOverPoint() {
        int ub = gene.length - 1;
        int lb = 1;
        int point = rn.nextInt(ub - lb) + lb;
        return point;
    }

    public Individual mutate(double mutationRate, double maxMutation) {
        double[] tempGene = this.gene;
        for (int i = 0; i < tempGene.length; i++) {
            if (rn.nextDouble() <= mutationRate) {
                if (i % 3 == 0) {
                    tempGene[i] = rn.nextInt(19);
                } else {
                    double modifier;
                    if (rn.nextBoolean()) {
                        modifier = rn.nextDouble() * 0.2;
                    } else {
                        modifier = -(rn.nextDouble() * 0.2);
                    }
                    if ((tempGene[i] + modifier) > 1) {
                        tempGene[i] = 1;
                    } else if ((modifier + tempGene[i]) < 0) {
                        tempGene[i] = 0;
                    } else {
                        tempGene[i] += modifier;
                    }
                }

            }
        }
        return new Individual(tempGene);
    }
    
    public void updateFitness() {
        this.fitness = GA.calculateFitness(this);
    }

    public String displayGene() {
        String out = "";
        for (int i = 0; i < gene.length; i++) {
            out = out.concat(" " + Double.toString(gene[i]));
        }
        return out;
    }

    public double[] getGene() {
        return gene;
    }

    public ArrayList<Double> getGeneArrayList() {
        ArrayList<Double> al = new ArrayList<>();
        for (double g : gene) {
            al.add(g);
        }
        return al;
    }

    public void setGene(double[] gene) {
        this.gene = gene;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
