package fr.ipme.entity;

import fr.ipme.entity.interfaces.IShout;

public class Cat extends Animal implements IShout {

    public Cat() {
        super(4);
    }

    @Override
    public void shout() {
        System.out.println("Meow meow meow");
    }
}
