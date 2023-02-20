package org.neocities.daviddev.simmdiff.core;

import com.google.common.collect.ImmutableSet;
import de.tudarmstadt.es.juppaal.Automaton;
import de.tudarmstadt.es.juppaal.Location;
import de.tudarmstadt.es.juppaal.NTA;
import de.tudarmstadt.es.juppaal.Transition;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.*;

public class ExtendedNTA extends NTA {

    public ExtendedNTA(String pathToFile) {
        super(pathToFile);
    }

    public void compareNTA(NTA model) {
        for (Automaton taio : model.getAutomata()) {
            System.out.printf("Getting counterpart with name %s \n", taio.getName().getName());
            Automaton mutant = this.getAutomaton(taio.getName().getName());
            System.out.println(taio.getInit().getOutgoingTransitions().size());
            List<Location> locsMutant = new ArrayList<>(mutant.getLocations());
            List<Location> locsModel = new ArrayList<>(taio.getLocations());
            System.out.println(locsMutant.get(0).getIncommingTransitions().size());
            Set<ExtendedLocation> locationSetMutant = ImmutableSet.copyOf(
                    locsMutant.stream()
                            .map(loc -> new ExtendedLocation(
                                    loc.getAutomaton(),
                                    loc.getName(),
                                    loc.getType(),
                                    0, 0,
                                    loc.getOutgoingTransitions(),
                                    loc.getIncommingTransitions()
                            ))
                            .collect(Collectors.toSet())
            );

            Set<ExtendedLocation> locationSetModel = ImmutableSet.copyOf(
                    locsModel.stream()
                            .map(loc -> new ExtendedLocation(
                                    loc.getAutomaton(),
                                    loc.getName(),
                                    loc.getType(),
                                    0, 0,
                                    loc.getOutgoingTransitions(),
                                    loc.getIncommingTransitions()))
                            .collect(Collectors.toSet())
            );

            System.out.println(difference(locationSetModel, locationSetMutant));
            System.out.println(intersection(locationSetMutant, locationSetModel));

            /*for (Location loc : counterpart.getLocations()) {

            }

            Set<ExtendedLocation> bar = new HashSet<ExtendedLocation>((Collection<? extends ExtendedLocation>) counterpart.getLocations());
            System.out.printf("Locations in mutant: %d, locations in model: %d\n", bar.size(), foo.size());
            Set<ExtendedLocation> diff2 = new HashSet<>(bar);
            diff2.removeIf(foo::contains);
            for (ExtendedLocation extendedLocation : diff2) {
                System.out.printf("loc %s not in original\n", extendedLocation.toString());
            }
            Set<Transition> transitionSet = new HashSet<Transition>(counterpart.getTransitions());
            Set<Transition> transitionSetB = new HashSet<Transition>(taio.getTransitions());
            System.out.printf("Transitions in mutant: %d, transitions in model: %d\n", transitionSet.size(), transitionSetB.size());
            Set<Transition> diff = new HashSet<>(transitionSet);
            diff.removeIf(transitionSetB::contains);

            for (Transition difT : diff) {
                System.out.printf("tran %s not in original\n", difT.toString());
            }*/

        }
    }
}