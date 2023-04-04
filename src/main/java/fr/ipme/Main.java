package fr.ipme;

import fr.ipme.entity.*;
import fr.ipme.entity.spotifish.Subscription;
import fr.ipme.exception.PawsException;
import fr.ipme.entity.interfaces.IShout;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main {

    private long id = 0L;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
//        cours();
//        exo1();
//        exo2();
//        exo3();
//        exo4();
//        jdbc();
//        reflectionClass();
    }

    private static void reflectionClass() {
        System.out.println(Subscription.class.getSimpleName().toLowerCase());
        Object subscription = new Subscription();
        Field[] fields = subscription.getClass().getDeclaredFields();
        for (Field field: fields) {
            System.out.println(field.getType() + " " + field.getName());
        }
        System.out.println(Arrays.toString(Subscription.class.getConstructors()));
    }

    private static void jdbc() {
        String url = "jdbc:mariadb://localhost:3307/db_spotifish";
//        String url = "jdbc:mysql://localhost:3306/db_spotifish";
        List<Subscription> subscriptions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM " + Subscription.class.getName().toLowerCase();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                double cost = rs.getDouble("cost");
                subscriptions.add(new Subscription(id, name, cost));
            }
        } catch (Exception e) {
            System.out.println("Error while trying to access the DB : " + e.getMessage());
        }
//        System.out.println(subscriptions);

    }

    private static void cours() {
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
                animals[i].setPaws(-2);
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

        List<Animal> animalsList = Arrays.asList(anOtherDog, rabbit, cat, dog);

        List<Animal> dogs = animalsList.stream()
                .filter(element -> (element instanceof Dog))
                .toList();

        List<Animal> onlyDogs = new ArrayList<>();
        for (Animal a : animals) {
            if (a instanceof Dog) {
                onlyDogs.add(a);
            }
        }

        animalsList.sort((Animal::compareTo));
    }

    private static void exo1() {
        boolean success = false;
        Double first = null;
        Double second = null;
        while (!success) {
            try {
                if (first == null) {
                    System.out.println("Saisir un premier nombre :");
                    first = scanner.nextDouble();
                }
                if (second == null) {
                    System.out.println("Saisir un deuxième nombre :");
                    second = scanner.nextDouble();
                }
                if (first != null && second != null) {
                    success = true;
                    System.out.println("Le premier nombre est : " + first);
                    System.out.println("Le deuxième nombre est : " + second);
                    System.out.println("La somme est : " + (first + second));
                }
            } catch (Exception e) {
                scanner.next();
            }
        }
    }

    private static void exo2() {
        boolean success = false;
        Integer evenOdd = null;
        while (!success) {
            try {
                if (evenOdd == null) {
                    System.out.println("Saisir un nombre entier :");
                    evenOdd = scanner.nextInt();
                }
                if (evenOdd != null) {
                    success = true;
                    System.out.println("Le nombre est : " + evenOdd);
                    if (evenOdd % 2 == 0) {
                        System.out.println("Le nombre est pair !");
                    } else {
                        System.out.println("Le nombre est impair !");
                    }
                }
            } catch (Exception e) {
                scanner.next();
            }
        }
    }

    private static void exo3() {
        System.out.println("Saisir une chaine de caractères :");
        String str = scanner.nextLine();
        System.out.println("La chaine de caractères est : " + str);
        System.out.println("Sa taille est de : " + str.length());
    }

    private static void exo4() {
        // Écrire un programme qui demande des entiers,
        // les met dans un tableau et qui affiche la somme des éléments du tableau
        // (il y aura peut-être une étape préalable…)
        boolean success = false;
        Integer intNumber = null;
        while (!success) {
            try {
                if (intNumber == null) {
                    System.out.println("Combien d'entier voulez vous insérer dans le tableau ?");
                    intNumber = scanner.nextInt();
                }
                if (intNumber != null) {
                    success = true;
                    int[] array = new int[intNumber];
                    int sum = 0;
                    for (int i = 0; i < intNumber; i++) {
                        System.out.println("Saisir le chiffre n°" + (i+1));
                        int aNumber = scanner.nextInt();
                        array[i] = aNumber;
                        sum += aNumber;
                    }
                    System.out.println("Vous avez saisi les chiffres suivant : ");
                    StringBuilder sb = new StringBuilder();
                    for (int aNumber: array) {
                        sb.append(aNumber);
                        sb.append(" ");
                    }
                    System.out.println(sb);
                    System.out.println("La somme totale est : " + sum);
                }
            } catch (Exception e) {
                scanner.next();
            }
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