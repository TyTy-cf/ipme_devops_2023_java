package fr.ipme.entity;

import fr.ipme.entity.enums.EnumDiet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Animal implements Comparable<Animal> {

    protected int paws;
    protected EnumDiet diet;

    @Override
    public int compareTo(Animal animal) {
        return this.diet.ordinal() - animal.diet.ordinal();
    }
}
