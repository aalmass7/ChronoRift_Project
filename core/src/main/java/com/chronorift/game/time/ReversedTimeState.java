package com.chronorift.game.time;

public class ReversedTimeState extends AbstractTimeState{

    public ReversedTimeState() {
        super(
            "Reversed Timeline",
            0.80f,
            1.00f,
            0.90f,
            1.20f
        );
    }

    @Override
    public boolean isMovementReversed() {
        return true;
    }
}
