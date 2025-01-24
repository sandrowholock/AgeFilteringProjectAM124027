import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class SimulatedDatabase {
    static class Row {
        int id;
        String name;
        int age;

        Row(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Row{id=" + id + ", name='" + name + "', age=" + age + '}';
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public int getAge() {
            return this.age;
        }
    }

    static List<Row> table = new ArrayList<>();

    static {
        table.add(new Row(1, "Alice", 30));
        table.add(new Row(2, "Bob", 25));
        table.add(new Row(3, "Charlie", 35));
        table.add(new Row(4, "David", 40));
        table.add(new Row(5, "Eve", 22));
        table.add(new Row(6, "Frank", 29));
        table.add(new Row(7, "Grace", 33));
        table.add(new Row(8, "Hank", 27));
        table.add(new Row(9, "Ivy", 31));
        table.add(new Row(10, "Jack", 23));
        table.add(new Row(11, "Kevin", 37));
        table.add(new Row(12, "Liam", 28));
        table.add(new Row(13, "Mia", 26));
        table.add(new Row(14, "Nina", 32));
        table.add(new Row(15, "Oscar", 34));
        table.add(new Row(16, "Paul", 21));
        table.add(new Row(17, "Quinn", 24));
        table.add(new Row(18, "Rachel", 38));
        table.add(new Row(19, "Sam", 39));
        table.add(new Row(20, "Tina", 20));
    }
}

class BinaryTreeIndex {
    static class Node {
        int key;
        SimulatedDatabase.Row row;
        Node left, right;

        public Node(int key, SimulatedDatabase.Row row) {
            this.key = key;
            this.row = row;
        }
    }

    private Node root;

    public void insert(int key, SimulatedDatabase.Row row) {
        root = insertRec(root, key, row);
    }

    private Node insertRec(Node root, int key, SimulatedDatabase.Row row) {
        if (root == null) {
            return new Node(key, row);
        }
        if (key < root.key) {
            root.left = insertRec(root.left, key, row);
        } else if (key > root.key) {
            root.right = insertRec(root.right, key, row);
        }
        return root;
    }

    public List<SimulatedDatabase.Row> filterByAgeRange(int minAge, int maxAge) {
        if (minAge > maxAge) {
            throw new IllegalArgumentException("Minimalny wiek nie może być większy niż maksymalny");
        }
        if (minAge < 0) {
            throw new IllegalArgumentException("Wiek nie może być ujemny");
        }
        
        List<SimulatedDatabase.Row> result = new ArrayList<>();
        filterByAgeRangeRec(root, minAge, maxAge, result);
        return result;
    }

    private void filterByAgeRangeRec(Node root, int minAge, int maxAge, List<SimulatedDatabase.Row> result) {
        if (root == null) {
            return;
        }
        
        filterByAgeRangeRec(root.left, minAge, maxAge, result);
        
        if (root.row.getAge() >= minAge && root.row.getAge() <= maxAge) {
            result.add(root.row);
        }
        
        filterByAgeRangeRec(root.right, minAge, maxAge, result);
    }

    private Node ageRoot;

    public void buildAgeIndex() {
        ageRoot = null;
        for (SimulatedDatabase.Row row : SimulatedDatabase.table) {
            insertByAge(row.getAge(), row);
        }
    }

    private void insertByAge(int age, SimulatedDatabase.Row row) {
        ageRoot = insertAgeRec(ageRoot, age, row);
    }

    private Node insertAgeRec(Node root, int age, SimulatedDatabase.Row row) {
        if (root == null) {
            return new Node(age, row);
        }
        if (age < root.key) {
            root.left = insertAgeRec(root.left, age, row);
        } else if (age > root.key) {
            root.right = insertAgeRec(root.right, age, row);
        }
        return root;
    }

    public List<SimulatedDatabase.Row> filterByAgeRange() {
        List<SimulatedDatabase.Row> result = new ArrayList<>();
        filterByAgeRangeRec(ageRoot, result);
        return result;
    }

    private void filterByAgeRangeRec(Node root, List<SimulatedDatabase.Row> result) {
        if (root == null) {
            return;
        }
        filterByAgeRangeRec(root.left, result);
        result.add(root.row);
        filterByAgeRangeRec(root.right, result);
    }

    public void printIndex() {
        System.out.println("\nid\timię\twiek");
        System.out.println("------------------------");
        printIndexRec(root);
    }

    private void printIndexRec(Node root) {
        if (root != null) {
            printIndexRec(root.left);
            System.out.println(root.row.getId() + "\t" + 
                             root.row.getName() + "\t" + 
                             root.row.getAge());
            printIndexRec(root.right);
        }
    }

    public SimulatedDatabase.Row search(int id) {
        return searchRec(root, id);
    }

    private SimulatedDatabase.Row searchRec(Node root, int id) {
        if (root == null) {
            return null;
        }
        if (root.key == id) {
            return root.row;
        }
        if (id < root.key) {
            return searchRec(root.left, id);
        } else {
            return searchRec(root.right, id);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Symulowana baza danych:\n");
        
        System.out.println("id\t|\timię\t|\twiek");
        System.out.println("------------------------------------");
        for (SimulatedDatabase.Row row : SimulatedDatabase.table) {
            System.out.print(row.getId());
            System.out.print("\t|\t");
            System.out.print(row.getName());
            System.out.print("\t|\t");
            System.out.println(row.getAge());
        }
        
        System.out.println();
        BinaryTreeIndex index = new BinaryTreeIndex();

        for (SimulatedDatabase.Row row : SimulatedDatabase.table) {
            index.insert(row.id, row);
        }

        System.out.println("Utworzony indeks (przejście w porządku in-order):");
        index.printIndex();

        Scanner scanner = new Scanner(System.in);
        int minAge = 0;
        int maxAge = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("\nPodaj minimalny wiek: ");
                String minAgeInput = scanner.nextLine();
                minAge = Integer.parseInt(minAgeInput);
                
                System.out.print("Podaj maksymalny wiek: ");
                String maxAgeInput = scanner.nextLine();
                maxAge = Integer.parseInt(maxAgeInput);
                
                if (minAge < 0 || maxAge < 0) {
                    System.out.println("Wiek nie może być ujemny. Spróbuj ponownie.");
                    continue;
                }
                if (minAge > maxAge) {
                    System.out.println("Minimalny wiek nie może być większy niż maksymalny. Spróbuj ponownie.");
                    continue;
                }
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę całkowitą.");
            }
        }

        List<SimulatedDatabase.Row> filteredResults = index.filterByAgeRange(minAge, maxAge);
        
        System.out.println("\nWyniki filtrowania (wiek " + minAge + "-" + maxAge + "):");
        System.out.println("------------------------------------");
        if (filteredResults.isEmpty()) {
            System.out.println("Nie znaleziono osób w podanym przedziale wiekowym.");
        } else {
            System.out.println("id\t|\timię\t|\twiek");
            System.out.println("------------------------------------");
            filteredResults.sort((a, b) -> Integer.compare(a.getAge(), b.getAge()));
            for (SimulatedDatabase.Row row : filteredResults) {
                System.out.printf("%d\t|\t%s\t|\t%d%n", 
                    row.getId(), row.getName(), row.getAge());
            }
            System.out.println("\nZnaleziono osób: " + filteredResults.size());
        }

        int searchId = 10;
        SimulatedDatabase.Row result = index.search(searchId);
        if (result != null) {
            System.out.println("\nWynik wyszukiwania dla ID " + searchId + ": " + result);
        } else {
            System.out.println("\nNie znaleziono wiersza o podanym ID " + searchId);
        }

        searchId = 25;
        result = index.search(searchId);
        if (result != null) {
            System.out.println("Wynik wyszukiwania dla ID " + searchId + ": " + result);
        } else {
            System.out.println("Nie znaleziono wiersza o podanym ID " + searchId);
        }

        scanner.close();
    }
} 