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
                        System.out.println(Integer.parseInt(list.get(0))+Integer.parseInt(list.get(1)));
                    }
                }
            }
        }while (true);

    }
}