package org.neocities.daviddev.simmdiff.entrypoint;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import de.tudarmstadt.es.juppaal.Automaton;
import org.neocities.daviddev.simmdiff.core.BidirectionalSearch;
import org.neocities.daviddev.simmdiff.core.ExtendedLocation;
import org.neocities.daviddev.simmdiff.core.ExtendedNTA;
import org.neocities.daviddev.simmdiff.core.ExtendedTransition;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Engine {
    private ExtendedNTA model, mutant;
    private ListMultimap<String, ExtendedLocation> diffLocations;
    private ListMultimap<String, ExtendedTransition> diffTransitions;
    public Engine(ExtendedNTA model, ExtendedNTA mutant) {
        this.mutant=mutant; this.model=model;
        diffLocations = ArrayListMultimap.create();
        diffTransitions = ArrayListMultimap.create();
    }

    public void start() {
        /*
        todo:
        extend NTA class
        add method to compare it against another NTA
        save elements which are different
        add method to generate XML only with different elements in each TA(?)
         */
        mutant.compareNTA(model);
        diffTransitions = mutant.getDiffTransitions();
        diffLocations = mutant.getDiffLocations();
    }
    /*
    Compute the shortest path for every TAIO to diff loc. (A*) -> create new automata at the same time
    Discard everything else... create a new automata only with those paths
    add new automata to nta.
     */

    public void computePaths() {
        BidirectionalSearch search = new BidirectionalSearch(null);
        for(Automaton ta : mutant.getAutomata()) {
            search.setAutomaton(ta);
            Iterator<ExtendedLocation> iter = diffLocations.asMap().get(ta.getName().getName()).iterator();
            while(iter.hasNext()) {
                System.out.println(search.search(new ExtendedLocation(ta.getInit()),iter.next()));
            }
        }
    }

    public void getPaths() {

        ProcessBuilder verifyPb = new ProcessBuilder();

        for (var entry : diffLocations.entries()) {
            String reachFormula = String.format("E<> %s.%s\n", mutant.getProcessName(entry.getKey()), entry.getValue());
            System.out.printf("Reach: %s\n", reachFormula);
            try (FileWriter writer = new FileWriter("src/main/resources/prop.q")) {
                writer.write(reachFormula);

                Random rand = new Random();
                verifyPb.command(
                        "verifyta",
                        "-t" , "0",
                        "-r", Integer.toString(rand.nextInt()),
                        "-y",
                        mutant.getPathToFile(),
                        "src/main/resources/prop.q"
                );
                verifyPb.redirectErrorStream(true);
                Process p = verifyPb.start();

                String result = new String(p.getInputStream().readAllBytes());
                System.out.println(result);

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

}
