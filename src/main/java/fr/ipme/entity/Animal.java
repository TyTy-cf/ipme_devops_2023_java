package fr.ipme.entity;

import fr.ipme.exception.PawsException;

public abstract class Animal {

    protected int paws;

    public Animal(int paws) {
        this.paws = paws;
    }

    public int getPaws() {
        return paws;
    }

    public void setPaws(int paws) throws PawsException {
        if (paws < 0) {
            throw new PawsException();
        }
        this.paws = paws;
    }
}
