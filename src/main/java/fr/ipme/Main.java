package fr.ipme;

import fr.ipme.entity.*;
import fr.ipme.entity.spotifish.Subscription;
import fr.ipme.entity.interfaces.IShout;
import fr.ipme.repository.Repository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


public class Main {

    private long id = 0L;

    private static final Scanner scanner = new Scanner(System.in);

    private static final Repository repository = new Repository("jdbc:mariadb://localhost:3307/db_geo-gouv");

    public static void main(String[] args) {
//        cours();
//        exo1();
//        exo2();
//        exo3();
//        exo4();
//        exo5();
//        exo6("Thomas Florian Guillaume Yvain Gauvain");
//        exo7();
//        exo8();
//        exo9();
//        reflectionClass();
//        jdbc();
        http();
//        post();
//        initDepartmentsDataBase();
//        try {
//            ResultSet rs = repository.findAll("department");
//            List<Department> departments = new ArrayList<>();
//            if (rs != null) {
//                while (rs.next()) {
//                    String name = rs.getString("name");
//                    String code = rs.getString("code");
//                    String regionCode = rs.getString("region_code");
//                    departments.add(new Department(code, name, regionCode));
//                }
//            }
//            System.out.println(departments.size());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    private static void post() {
        Map<String, String> datas = new HashMap<>();
        datas.put("name", "SuperDevOps");
        datas.put("nickname", "ZuperDevOpz");
        datas.put("password", "unPassword");
        datas.put("email", "superdevops@ipme.fr");
        JSONObject requestBody = new JSONObject(datas);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .uri(URI.create("https://steam-ish.test-02.drosalys.net/api/account"))
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200  || response.statusCode() == 201) {
                System.out.println(response.body());
            } else {
                System.out.println("Il y a eu un problème avec la requête... Code : " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (Exception e) {
            System.out.println("Coup dur... rien ne marche !");
        }
    }

    private static void initDepartmentsDataBase() {
        try {
            HttpResponse<String> response = getResponseByUrl("https://geo.api.gouv.fr/departements/");
            if (response != null) {
                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    System.out.println(response);
                    createDepartmentTable();
                    JSONTokener jsonTokener = new JSONTokener(response.body());
                    JSONArray jsonArray = new JSONArray(jsonTokener);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonDpt = jsonArray.getJSONObject(i);
                        String name = jsonDpt.getString("nom");
                        if (name.contains("'")) {
                            name = name.replace("'", "\\'");
                        }
                        String sql =
                                "INSERT INTO department VALUES ( '"
                                        + jsonDpt.getString("code") + "', '"
                                        + name + "', '"
                                        + jsonDpt.getString("codeRegion") + "');";
                        repository.executeQuery(sql);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createDepartmentTable() {
        String drop = "DROP TABLE IF EXISTS department;";
        String sql = "CREATE TABLE department(" +
                "    `code` VARCHAR(3) NOT NULL," +
                "    name VARCHAR(128) NOT NULL," +
                "    region_code VARCHAR(2) NOT NULL," +
                "    PRIMARY KEY(`code`)" +
                ") ENGINE = INNODB;";
        repository.executeQuery(drop);
        repository.executeQuery(sql);
    }

    private static HttpResponse<String> getResponseByUrl(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
        HttpClient client = HttpClient.newHttpClient();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("An issue occured while trying to fetch the API...");
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void http() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://geo.api.gouv.fr/regions"))
                .build();
        HttpClient client = HttpClient.newHttpClient();

        List<Region> regions = new ArrayList<>();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null) {
                if (response.statusCode() == 200) {
//                    System.out.println(response.body());
                    JSONTokener jsonTokener = new JSONTokener(response.body());
                    JSONArray jsonArray = new JSONArray(jsonTokener);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonRegion = jsonArray.getJSONObject(i);
                        for (String key : jsonRegion.keySet()) {
                            System.out.println(key);
                        }
                        // INSERT INTO departement VALUES (... jsonRegion.getString("nom") );
                        regions.add(new Region(
                                jsonRegion.getString("nom"),
                                jsonRegion.getString("code")
                        ));
                    }
                    System.out.println("Il existe " + regions.size() + " régions en France !");
                    System.out.println(
                            "La plus belle région de France est : " +
                            (regions.stream()
                                    .filter(region -> region.getName().contains("Auvergne"))
                                    .toList()).get(0).getName()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("An issue occured while trying to fetch the API...");
            System.out.println(e.getMessage());
        }
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
        String url = "jdbc:mariadb://localhost:3307/spotifish";
//        String url = "jdbc:mysql://localhost:3306/db_spotifish";
        List<Subscription> subscriptions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM subscription";
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
//                System.out.println(animals[i].getClass() + " ne peux pas crier !");
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
        System.out.println(
                Arrays.stream(animals)
                        .filter(element -> (element instanceof Dog))
                        .toList()
        );

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

    private static void exo5() {
        System.out.println("Dé 6 faces : " + throwDice(6));
        System.out.println("Lancer 5 dés :");
        for (int i = 0; i < 4; i++) {
            System.out.println("Dé 20 faces : " + throwDice(20));
        }
    }

    private static void exo6(String word) {
        int nbVowels = 0;
        List<Character> vowels = Arrays.asList('a', 'e', 'i', 'o', 'u', 'y', 'é', 'è', 'ê', 'î', 'û', 'ï');
        for (char c : word.toLowerCase().toCharArray()) {
            if (vowels.contains(c)) {
                nbVowels++;
            }
        }
        System.out.println("Il y a : " + nbVowels +  " voyelles dans " + word);
    }

    private static void exo7() {
        boolean success = false;
        Integer aNumber = null;
        while (!success) {
            try {
                if (aNumber == null || aNumber < 0) {
                    System.out.println("Saisir un nombre entier :");
                    aNumber = scanner.nextInt();
                }
                if (aNumber != null && aNumber > 0) {
                    success = true;
                    System.out.println("Le nombre saisie est : " + aNumber);
                    System.out.println("Sa racine carré est : " + Math.sqrt(aNumber));
                }
            } catch (Exception e) {
                scanner.next();
            }
        }
    }

    private static int throwDice(int max) {
        Random rand = new Random();
        return (rand.nextInt(max)) + 1;
    }

    private static void exo8() {
        Random rand = new Random();
        int numberToGuess = (rand.nextInt(100)) + 1;
        int trys = 1;
        int userNumber;
        int previousUserNumber = -1;

        System.out.println("J'ai trouvé mon nombre, peux tu le deviner en moins de 10 coups ?");
        while (trys <= 10) {
            try {
                System.out.println("Saisir un nombre : ");
                userNumber = scanner.nextInt();
                if (userNumber == previousUserNumber) {
                    System.out.println("Tu as déjà saisit ce nombre...");
                } else {
                    if (userNumber == numberToGuess) {
                        System.out.println("Félicitation, tu as trouvé le bon nombre !");
                        System.out.println("Tu l'as trouvé en " + trys + " coups.");
                        break;
                    } else if (userNumber > numberToGuess) {
                        System.out.println("Ton nombre est plus grand que le miens !");
                    } else {
                        System.out.println("Ton nombre est plus petit que le miens !");
                    }

                    previousUserNumber = userNumber;
                    trys++;
                }
            } catch (Exception e) {
                scanner.next();
            }
        }
        if (trys == 11) {
            System.out.println("Tu n'as pas trouvé le nombre durant le nombre de coups impartie :(");
            System.out.println("Le nombre était : " + numberToGuess);
        }
    }

    private static void exo9() {
        int[] array = new int[]{1, 5, 34, 52};
        int i = 0;
        int j = i++; // i++ : affecte puis incrémente
        System.out.println(j);
        i = 0;
        j = ++i; // i++ : incrémente puis affecte
        System.out.println(j);
        arrayRecursiveDisplay(array, 0);
    }

    private static void arrayRecursiveDisplay(int[] array, int index) {
        if (index < array.length) {
            System.out.println(array[index]);
            arrayRecursiveDisplay(array, ++index);
        }
//        while (index < array.length) {
//            System.out.println(array[index]);
//            index++;
//        }
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