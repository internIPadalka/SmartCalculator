import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface SmartCalculator {
    void addition(String [] array);
}

class Addition implements SmartCalculator{
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void addition(String [] array) {
        int sum = 0;
        boolean isAurevoir = false;

        do {
            String userInput = scanner.nextLine();

            if (userInput.equals("/exit")){
                GeneralInfos.bye();
                break;
            } else if (userInput.equals("/help")){
                GeneralInfos.help();
                continue;
            } else if (userInput.equals("")) {
                continue;
            } else if (userInput.substring(0,1).equals("/")){
                System.out.println("Unknown command");
                continue;
            }

            String userInputcleaner = GeneralInfos.regexCleaner(userInput);
            try {
                array = userInputcleaner.split(" ");
                sum = Integer.parseInt(array[0]);
                for (int i = 1; i < array.length; i+=2){
                    if(array[i].equals("+")){
                        sum += Integer.parseInt(array[i+1]);
                    }else if (array[i].equals("-")){
                        sum -= Integer.parseInt(array[i+1]);
                    }
                }
                System.out.println(sum);
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid expression");
                continue;
            } catch (ArrayIndexOutOfBoundsException exception){
                System.out.println("Invalid expression");
                continue;
            }
        } while (!isAurevoir);
    }
}

class SmartCalculatorContext {
    private SmartCalculator smartCalculator;
    void setCalculationMethod(SmartCalculator smartCalculator){
        this.smartCalculator = smartCalculator;
    }

    void addition(String [] array) {
        this.smartCalculator.addition(array);
    }
}

class GeneralInfos {
    static void bye(){
        System.out.println("Bye!");
    }

    static void help(){
        System.out.println("The program support the:" +
                "\nAddition + and Subtraction - operators:" +
                "\nConsider that the even number of minuses gives a plus," +
                "\nand the odd number of minuses gives a minus! Look at it this way:" +
                "\n2 -- 2 equals 2 - (-2) equals 2 + 2.");
    }

    static String regexCleaner(String userInput) {
        Pattern extraSpace = Pattern.compile("\\s+");
        Matcher matchSpace = extraSpace.matcher(userInput);
        String spaceCleaner = matchSpace.replaceAll(" ");

        Pattern patternPLUS = Pattern.compile("\\+{2,}");
        Matcher matcherPLUS = patternPLUS.matcher(spaceCleaner);
        String mergePLUS = matcherPLUS.replaceAll("+");

        Pattern patternMINUS = Pattern.compile("-{3}");
        Matcher matcherMINUS = patternMINUS.matcher(mergePLUS);
        String mergeMINUS = matcherMINUS.replaceAll("-");

        Pattern patternDOUBLEMINUS = Pattern.compile("-{2}");
        Matcher matcherDOUBLEMINUS = patternDOUBLEMINUS.matcher(mergeMINUS);
        String mergeDOUBLEMINUS = matcherDOUBLEMINUS.replaceAll("+");

        Pattern patternStartByMINUS = Pattern.compile("\\b-.*?");
        Matcher matcherStartByMINUS = patternStartByMINUS.matcher(mergeDOUBLEMINUS);
        String refactoringZERO = matcherStartByMINUS.replaceFirst("0 - ");

        Pattern finalCheck = Pattern.compile("(-\\+|\\+-)");
        Matcher finalMatch = finalCheck.matcher(refactoringZERO);
        String result = finalMatch.replaceAll("-");
        return result;
    }
}

class Main{
    public static void main(String[] args) {
        SmartCalculatorContext calculMethod = new SmartCalculatorContext();
        calculMethod.setCalculationMethod(new Addition());
        calculMethod.addition(null);
    }
}