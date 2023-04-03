package fr.ipme;

import fr.ipme.entity.*;
import fr.ipme.exception.PawsException;
import fr.ipme.entity.interfaces.IShout;

public class Main {

    private long id = 0L;

    public static void main(String[] args) throws PawsException {
        Dog dog = new Dog();
        Animal anOtherDog = new Dog();
        Cat cat = new Cat();
        Rabbit rabbit = new Rabbit();

        Animal[] animals = new Animal[]{anOtherDog, rabbit, cat, dog};
        for (int i = 0; i < animals.length; i++) {
            if (animals[i] instanceof IShout) {
                ((IShout) animals[i]).shout();
            } else {
                System.out.println(animals[i].getClass() + " ne peux pas crier !");
                try {
                    animals[i].setPaws(-2);
                } catch (PawsException pe) {
                    System.out.println("PE : " + pe.getMessage());
                }
            }
        }

        try {
            Integer i = Integer.parseInt("abcde");
        } catch (NullPointerException e) {
            System.out.println("NullPointerException : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException : " + e.getMessage());
        } finally {
            System.out.println("Je suis du code exécuté tout le temps !");
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Toto");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Main) {
            return ((Main) obj).id == this.id;
        }
        return false;
    }
}