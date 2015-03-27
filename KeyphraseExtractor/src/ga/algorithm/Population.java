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

    public double calculateFitnessSum() {
        double sum = 0;
        for (Individual ind : population) {
            sum += ind.getFitness();
        }
        return sum;
    }

    public Individual getFittestIndividual() {
        Individual bestInd = population.get(0);
        double best = bestInd.getFitness();
        for (Individual ind : population) {
            if (ind.getFitness() > best) {
                best = ind.getFitness();
                bestInd = ind;
            }
        }
        return bestInd;
    }

    public Individual getWorstIndividual() {
        Individual worstInd = population.get(0);
        double worst = worstInd.getFitness();
        for (Individual ind : population) {
            if (ind.getFitness() <= worst) {
                worst = ind.getFitness();
                worstInd = ind;
            }
        }
        return worstInd;
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

    public Population selectParents(int tournamentSize) {
        Population parents = new Population();
        Population tournament = new Population();
        for (int i = 0; i < this.getSize(); i++) {          
            for (int j = 0; j < tournamentSize; j++) {
                tournament.addIndividual(population.get(rn.nextInt(this.getSize()-1)));
            }
            parents.addIndividual(tournament.getFittestIndividual());
            tournament.clear();
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

    public void mutatePopulation(double rate, double maxMutation) {
        for (Individual ind : population) {
            ind = ind.mutate(rate, maxMutation);
        }
    }

    public void clear() {
        population.clear();
    }
}
