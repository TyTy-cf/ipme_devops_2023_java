package fr.ipme.entity.enums;

public enum EnumDiet {
    CARNIVOROUS("Carnivore"),
    HERBIVORE("Herbivore");

    private String value;

    EnumDiet(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
