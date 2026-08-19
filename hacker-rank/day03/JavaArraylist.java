import java.util.ArrayList;
import java.util.Scanner;

public class JavaArraylist {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int rowCount = scanner.nextInt();

        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

        for(int i = 0; i < rowCount; i++){
            int colCount = scanner.nextInt();
            ArrayList<Integer> row = new ArrayList<>();
            for(int j = 0; j < colCount; j++){
                int element = scanner.nextInt();
                row.add(element);
            }

            matrix.add(row);
        }

        int queryCount = scanner.nextInt();
        for(int query = 0; query < queryCount; query++){
            int x_position = scanner.nextInt();
            int y_position = scanner.nextInt();
            if(x_position >= 1
                && x_position <= matrix.size()
                && y_position >= 1
                && y_position <= matrix.get(x_position-1).size()){
                int result = matrix.get(x_position-1).get(y_position-1);
                System.out.println(result);
            }else{
                System.out.println("ERROR!");
            }
        }

        scanner.close();
    }
}