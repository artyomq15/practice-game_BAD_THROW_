package by.bsu.mmf.badthrowgame.dice;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable{
    private int value;
    public int getValue(){
        return value;
    }
    public void throwDice(){
        value = new Random().nextInt(6)+1;
    }
}
