package org.neocities.daviddev.simmdiff.core;

import de.tudarmstadt.es.juppaal.Automaton;
import de.tudarmstadt.es.juppaal.Location;
import de.tudarmstadt.es.juppaal.Transition;
import de.tudarmstadt.es.juppaal.labels.Label;

import org.neocities.daviddev.simmdiff.core.types.Channel;


public class ExtendedTransition extends Transition {

    private Channel channel;
    private ExtendedLocation extSource, extTarget;
    private Automaton automaton;

    public ExtendedTransition(Automaton automaton, Location source, Location destination, Channel channel) {
        super(automaton, source, destination);
       /* if (source != null) {
            this.setSource(new ExtendedLocation(automaton, source.getName(),
                    source.getType(),
                    0, 0,
                    source.getOutgoingTransitions(),
                    source.getIncommingTransitions()));
        }

        if (destination != null){
            this.setTarget(new ExtendedLocation(automaton, destination.getName(),
                    destination.getType(),
                    0, 0,
                    destination.getOutgoingTransitions(),
                    destination.getIncommingTransitions()));
        }*/
        this.automaton = automaton;
        this.channel = channel;

    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Transition)) {
            return false;
        }

        ExtendedTransition tran = (ExtendedTransition) o;

        if (!this.getGuardAsString().equals(tran.getGuardAsString())) {
            return false;
        }

        if (labelsAreDifferent(this.getUpdate(), tran.getUpdate())) {
            return false;
        }
        if (labelsAreDifferent(this.getSelect(), tran.getSelect())) {
            return false;
        }
        if (labelsAreDifferent(this.getSync(), tran.getSync())) {
            return false;
        }

        if (this.channel != null ^ tran.channel != null) {
            return false;
        }

        if (this.channel != null ) {
            return this.channel.equals(tran.getChannel());
        }

        if (tran.channel != null ) {
            return tran.channel.equals(tran.getChannel());
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.getGuardAsString().hashCode();

/*
        if (this.getSource() != null) {
            //ExtendedLocation extSource = new ExtendedLocation(this.getSource());
            result = 31 * result + this.getSource().hashCode();
        }
        if (this.getTarget() != null) {
            //ExtendedLocation extTarget = new ExtendedLocation(this.getTarget());
            result = 31 * result + this.getTarget().hashCode();
        }
*/

        if (this.getSelect() != null) {
            result = 31 * result + this.getSelect().toString().hashCode();
        }

        if (this.getUpdate() != null)
            result = 31 * result + this.getUpdate().toString().hashCode();

        if (this.getSync() != null) {
            result = 31 * result + this.getSync().toString().hashCode();
            result = 31 * result + this.getChannel().hashCode();
        }

        return result;
    }

    private boolean labelsAreDifferent(Label label1, Label label2) {
        //System.out.println(label1 + " " + label2);
        if (label1 == null ^ label2 == null ) {
            return true;
        } else if (label1 != null && label2 != null) {
            return !label1.toString().equals(label2.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", this.getSource(), this.getTarget());
    }

    public Channel getChannel() {
        return channel;
    }
}
