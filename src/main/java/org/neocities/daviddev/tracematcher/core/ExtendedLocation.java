package org.neocities.daviddev.tracematcher.core;

import be.unamur.uppaal.juppaal.Automaton;
import be.unamur.uppaal.juppaal.Location;
import be.unamur.uppaal.juppaal.Name;
import be.unamur.uppaal.juppaal.Transition;
import org.jdom.Element;

import java.util.List;

public class ExtendedLocation extends Location {
    public ExtendedLocation(Automaton automaton) {
        super(automaton);
    }

    public ExtendedLocation(Automaton automaton, Element locationElement) {
        super(automaton, locationElement);
    }

    public ExtendedLocation(Automaton automaton, Name name, LocationType type, int x, int y, List<Transition> outgoingTransitions, List<Transition> incomingTransitions) {
        super(automaton, name, type, x, y);
        if (name == null)
            this.setName(new  Name(String.valueOf(this.hashCode())));

        for (Transition transition : outgoingTransitions) {
            this.addOutgoingTransition(transition);
        }

        for (Transition transition : incomingTransitions) {
            this.addIncomingTransition(transition);
        }
    }

    public ExtendedLocation(Location location) {
        super(location.getAutomaton(), location.getName(), location.getType(), location.getPosX(), location.getPosY());

        if (location.getName() == null)
            this.setName(new  Name(String.valueOf(this.hashCode())));

        for (Transition transition : location.getOutgoingTransitions()) {
            this.addOutgoingTransition(transition);
        }

        for (Transition transition : location.getIncommingTransitions()) {
            assert transition != null;
            this.addIncomingTransition(transition);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Location)) {
            return false;
        }
        ExtendedLocation loc = (ExtendedLocation) o;
        //System.out.printf("%s != %s |", loc.getName().getName(), this.getName().getName());

        boolean namesAreEqual;
        if (this.getName() == null ^ loc.getName() == null) {
            namesAreEqual = false;
        } else if (this.getName() != null && loc.getName() != null) {
            namesAreEqual = this.getName().getName().equals(loc.getName().getName());
        } else {
            namesAreEqual = true;
        }

        if (!namesAreEqual) return false;

        //System.out.printf("%d != %d ", loc.getIncommingTransitions().size(), this.getIncommingTransitions().size());
        if (loc.getIncommingTransitions().size() != this.getIncommingTransitions().size()) {
            return false;
        }

        //System.out.printf("%d != %d ", loc.getOutgoingTransitions().size(), this.getOutgoingTransitions().size());
        if (loc.getOutgoingTransitions().size() != this.getOutgoingTransitions().size()) {
            return false;
        }
        if (!loc.getType().equals(this.getType()))
            return false;

        //System.out.printf("%s != %s ", loc.getAutomaton().getName().getName(), this.getAutomaton().getName().getName());
        if (!loc.getAutomaton().getName().getName().equals(this.getAutomaton().getName().getName()))
            return false;

        //System.out.printf("%s == %s \n", loc.getInvariantAsString(), this.getInvariantAsString());
        boolean areEqual = loc.toString().equals(this.toString());
        //System.out.println(areEqual);
        return areEqual;
    }

    @Override
    public int hashCode() {
        int result = this.getAutomaton().getName().getName().hashCode();

        if (this.getType() != null) {
            result = 31 * result + this.getType().hashCode();
        } else {
            result = 31 * result + LocationType.NORMAL.hashCode();
        }

        result = 31 * result + Integer.hashCode(this.getIncommingTransitions().size());
        result = 31 * result + Integer.hashCode(this.getOutgoingTransitions().size());
        if (this.getName() != null) {
            result = 31 * result + this.getName().getName().hashCode();
        }

        if (this.getInvariant() != null) {
            result = 31 * result + this.toString().hashCode();
        }

        return result;
    }
}
