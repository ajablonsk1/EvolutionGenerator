package Objects;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Random;

public class Genome {

    LinkedList<Integer> Genome = new LinkedList<>();


    public void animalStartGenome() {
        while (true) {
            for (int i = 0; i < 32; i++) {
                Random genomGenerator = new Random();
                int genome = genomGenerator.nextInt(8);
                this.Genome.add(genome);
            }
            if (this.ifContainAllGenomes()) return;
            else {
                Genome.clear();
            }
        }
    }

    public boolean ifContainAllGenomes() {
        LinkedList<Boolean> allGenomes = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            allGenomes.add(false);
        }
        for (Integer genome : this.Genome) {
            allGenomes.set(genome, true);
        }
        for (Boolean genome : allGenomes) {
            if (!genome) {
                return false;
            }
        }
        return true;
    }

    public void genomeSort() {
        IntegerComparator Comparator = new IntegerComparator();
        this.Genome.sort(Comparator);
    }

    public LinkedList<Integer> getGenome() {
        return this.Genome;
    }

    public Genome genomeForChild(Genome parent1, Genome parent2) {
        Genome childGenome = new Genome();
        Random Generator = new Random();
        int first = Generator.nextInt(32);
        int second = Generator.nextInt(32);
        int which = Generator.nextInt(2);
        LinkedList<Integer> parts = new LinkedList<>();
        parts.add(first);
        parts.add(second);
        IntegerComparator Comparator = new IntegerComparator();
        parts.sort(Comparator);
        if (which == 0) {
            for (int i = 0; i <= parts.get(0); i++) {
                childGenome.Genome.add(parent1.Genome.get(i));
            }
            for (int i = parts.get(0) + 1; i <= parts.get(1); i++) {
                childGenome.Genome.add(parent1.Genome.get(i));
            }
            for (int i = parts.get(1) + 1; i <= 31; i++) {
                childGenome.Genome.add(parent2.Genome.get(i));
            }
        }
        else{
            for (int i = 0; i <= parts.get(0); i++) {
                childGenome.Genome.add(parent2.Genome.get(i));
            }
            for (int i = parts.get(0) + 1; i <= parts.get(1); i++) {
                childGenome.Genome.add(parent2.Genome.get(i));
            }
            for (int i = parts.get(1) + 1; i <= 31; i++) {
                childGenome.Genome.add(parent1.Genome.get(i));
            }
        }
        return childGenome;
    }
}
