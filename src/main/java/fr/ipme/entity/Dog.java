package fr.ipme.entity;

import fr.ipme.entity.interfaces.IShout;

public class Dog extends Animal implements IShout {

    public Dog() {
        super(4);
    }

    @Override
    public void shout() {
        System.out.println("Woof woof woof");
    }

    public void patPat() {
        System.out.println("On caresse le toutou");
    }
}
