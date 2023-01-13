package core;

import core.types.Clock;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Todo:
 * Input and Output actions sets, set of clocks, local channels
 */
public class Automata {

    private String name;
    private Location initLocation;
    private ArrayList<Transition> edges;
    private ArrayList<Location> locations;
    private HashMap<String, Clock> localClocks;
    private Location currLocation;

    public Automata(String name) {
        this.name = name;
        edges = new ArrayList<>();
        locations = new ArrayList<>();
        localClocks = new HashMap<>();
    }

    public void setInitLocation(Location l) { initLocation = l; }

    public void setCurrLocation(Location currLocation) {
        this.currLocation = currLocation;
    }

    public Location getCurrLocation() { return currLocation; }

    public void addEdge(Transition transition) {
        edges.add(transition);
    }

    public void addLocation(Location loc) {
        locations.add(loc);
    }
    public void addClock(Clock c) { localClocks.put(c.getId(), c); }
    public String getName() {
        return name;
    }

    public Location getInitLocation() {
        return initLocation;
    }

    public void getEnabledTransitions(HashMap<String, Clock> globalClocks) {
        for (Transition transition : currLocation.getOutgoings()) {
            // guards hold
            for (Guard guard : transition.getGuards()) {
                if (globalClocks.containsKey(guard.getClockRef())) {
                    if ( !(globalClocks.get(guard.getClockRef()).getValue() < guard.getValue())) {
                        continue;
                    }
                }
            }
        }
    }
}
