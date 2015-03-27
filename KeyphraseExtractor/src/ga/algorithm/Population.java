package ga.algorithm;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Alexander Brown - 10019841
 */
public class Population {

    //private Individual[] population;

    private ArrayList<Individual> population;
    private Random rn;

    public Population(int pop_size, int gene_size) {
        rn = new Random();
        population = new ArrayList<>(pop_size);
        for (int i = 0; i < pop_size; i++) {
            population.add(i, new Individual(gene_size));
        }
    }

    public Population() {
        rn = new Random();
        population = new ArrayList<>();
    }

    public void addIndividual(Individual ind) {
        population.add(ind);
    }

    public Individual getIndividual(int index) {
        return population.get(index);
    }

    public void setIndividual(Individual ind, int index) {
        population.set(index, ind);
    }

    public Individual removeIndividual(int index) {
        return population.remove(index);
    }

    public void runFitnessAll() {
        for (Individual ind : population) {
            ind.updateFitness();
        }
    }

    public double calculateFitnessMean() {
        double mean = this.calculateFitnessSum() / population.toArray().length;
        return mean;
    }

    public int calculateFitnessSum() {
        int sum = 0;
        for (Individual ind : population) {
            sum += ind.getFitness();
        }
        return sum;
    }

    public Individual getFittestIndividual() {
        double best = 0;
        Individual bestInd = null;
        for (Individual ind : population) {
            if (ind.getFitness() > best) {
                best = ind.getFitness();
                bestInd = ind;
            }
        }
        return bestInd;
    }

    public double calculateBestFitness() {
        double best = 0;
        for (Individual ind : population) {
            if (ind.getFitness() > best) {
                best = ind.getFitness();
            }
        }
        return best;
    }

    public ArrayList<Individual> getPopulation() {
        return population;
    }

    public void setPopulation(Population newPop) {
        this.population = newPop.getPopulation();
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return population.toArray().length;
    }

    public Population selectParents() {
        Population parents = new Population();
        for (int i = 0; i < this.getSize(); i++) {
            Individual parent1 = population.get(rn.nextInt(this.getSize() - 1));
            Individual parent2 = population.get(rn.nextInt(this.getSize() - 1));
            if (parent1.getFitness() > parent2.getFitness()) {
                parents.addIndividual(parent1);
            } else {
                parents.addIndividual(parent2);
            }
        }
        return parents;
    }

    public void combine(double rate) {
        Population childPop = new Population();
        while (!population.isEmpty()) {
            Individual ind1 = population.remove(rn.nextInt(this.getSize()));
            Individual ind2 = population.remove(rn.nextInt(this.getSize()));

            if (rn.nextDouble() <= rate) { // Crossover chance
                Individual[] childPair = ind1.crossover(ind2);
                childPop.addIndividual(childPair[0]);
                childPop.addIndividual(childPair[1]);
            } else {
                childPop.addIndividual(ind1);
                childPop.addIndividual(ind2);
            }
        }
        this.setPopulation(childPop);
    }

    public void mutatePopulation(double rate) {
        for (Individual ind : population) {
            ind = ind.mutate(rate);
        }
    }

    public void clear() {
        population.clear();
    }
}
