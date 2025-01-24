import java.util.ArrayList;
import java.util.List;

public class BinaryTreeIndex {

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
        } else {
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
        filterByAgeRangeRec(ageRoot, minAge, maxAge, result);
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

} 