import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        do {
            Scanner scanner = new Scanner(System.in);
            String out = scanner.nextLine();
            if (out.equals("/exit")) {
                System.out.println("Bye!");
                break;
            }else if (out.equals("/help")) {
                System.out.println("The program calculates the sum of numbers");
                continue;
            }else {
                List<String> list=Arrays.asList(out.split(" "));
                if (list.isEmpty()) {
                    continue;
                }else {
                    if (list.get(0)==" " || list.get(0)=="") {
                        continue;
                    }else if(list.size()==1){
                        System.out.println(Integer.parseInt(list.get(0)));
                    }else {
                        int sum = 0;
                        for (String counter:list) {
                            sum+=Integer.parseInt(counter);
                        }
                        System.out.println(sum);
                    }
                }
            }
        }while (true);
    }
}