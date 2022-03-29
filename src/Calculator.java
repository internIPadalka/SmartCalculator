import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private static boolean exitCommand;
    private boolean validExpression = true;
    private Map<String, String> variables = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(!exitCommand) {
            String input = scanner.nextLine();
            String inputWithoutWithspaces = input.replace(" ", "");
            Calculator calculator = new Calculator();
            calculator.evaluateInput(inputWithoutWithspaces);
        }

    }


    public void evaluateInput(String input) {
        validExpression = true;
        Deque<String> postfixNotation;

        if(input.isEmpty()) {
        } else if(input.startsWith("/")){
            evaluateCommands(input);
        } else if(input.contains("=")) {
            saveVariablesToMap(input);
        } else if(input.matches("[a-zA-Z]+")) {
            System.out.println(returnVariableValue(input));
        } else {
            postfixNotation = convertToPostfixNotation(analyzeOperators(input));
            if(validExpression) {
                System.out.println(calculate(postfixNotation));
            } else {
                System.out.println("Invalid expression");
            }
        }
    }

    public BigInteger calculate(Deque<String> input) {
        Deque<BigInteger> finalResult = new ArrayDeque<>();
        Iterator<String> it = input.descendingIterator();

        BigInteger operationResult = new BigInteger("0");
        while(it.hasNext()){
            String expression = it.next();

            if(expression.matches("[-+]?\\d+")) {
                BigInteger number = new BigInteger(expression);
                finalResult.offer(number);
            } else if(expression.matches("[*/+-]")) {

                BigInteger firstNumber = finalResult.pollLast();
                BigInteger secondNumber = finalResult.pollLast();

                if(expression.matches("\\+")) {
                    operationResult = secondNumber.add(firstNumber);
                } else if(expression.matches("-")) {
                    operationResult = secondNumber.subtract(firstNumber);
                } else if(expression.matches("\\*")) {
                    operationResult = secondNumber.multiply(firstNumber);
                } else if (expression.matches("/")) {
                    operationResult = secondNumber.divide(firstNumber);
                } else {
                    System.out.println("Something went wrong here...");
                }

                finalResult.offer(operationResult);
            }
        }
        return finalResult.getFirst();
    }


    public Deque<String> convertToPostfixNotation(String input) {
        List<String> expressions = new ArrayList<>();
        Deque<String> operators = new ArrayDeque<>();
        Deque<String> result = new ArrayDeque<>();

        Matcher matcher = Pattern.compile("\\w+|[-+*/()]").matcher(input);

        while(matcher.find()) {
            expressions.add(matcher.group());
        }

        try {
            for (String expression : expressions) {
                if (expression.matches("\\w+")) {
                    if (expression.matches("\\d+")) {
                        result.push(expression);
                    } else {
                        result.push(returnVariableValue(expression));
                    }
                } else {
                    if (operators.isEmpty() || operators.getFirst().equals("(")) {
                        operators.push(expression);
                    } else if (expression.equals(")")) {
                        while (!operators.isEmpty()) {
                            if (operators.getFirst().equals("(")) {
                                break;
                            }
                            result.push(operators.pop());
                        }
                        operators.pop();
                    } else if (expression.equals(operators.getFirst()) ||
                            (expression.matches("[*/]") && operators.getFirst().matches("[*/]")) ||
                            (expression.matches("[+-]") && operators.getFirst().matches("[*/()+-]"))) {
                        if (expression.matches("[+-]")) {
                            while (!operators.isEmpty()) {
                                if (operators.getFirst().equals("(")) {
                                    break;
                                }
                                result.push(operators.pop());
                            }
                        } else if (expression.matches("[*/]")) {
                            while (!operators.isEmpty()) {
                                if (operators.getFirst().matches("[()+-]")) {
                                    break;
                                }
                                result.push(operators.pop());
                            }
                        }
                        operators.push(expression);
                    } else {
                        operators.push(expression);
                    }
                }

            }
        } catch (Exception e) {
            validExpression = false;
        }

        while (!operators.isEmpty()) {
            if(operators.peek().matches("[()]")) {
                validExpression = false;
                break;
            }
            result.push(operators.pop());
        }
        return result;
    }


    public String analyzeOperators(String input) {
        String updatedInput = "";

        Matcher matcher = Pattern.compile("\\(*[+/*-]*\\w+\\)*|[+/*-]*\\(*[+/*-]*\\(*\\w+\\)*").matcher(input);
        while(matcher.find()) {
            updatedInput += removeUnnecessaryOperators(matcher.group());
        }
        return updatedInput;
    }


    public String removeUnnecessaryOperators(String input) {
        String updatedInput = "";
        int countMinus = 0;

        if(input.contains("+")) {
            updatedInput = "+" + input.replace("+", "");
        } else if(input.contains("-")) {

            for(Character minus : input.toCharArray()) {
                if(minus == '-') {
                    countMinus++;
                }
            }

            if(countMinus % 2 == 0){
                updatedInput = "+" + input.replace("-", "");
            } else {
                updatedInput = "-" + input.replace("-", "");
            }

        } else if(input.matches(".*[/]{2,}.*") || input.matches(".*[*]{2,}.*")) {
            validExpression = false;
        } else {
            updatedInput = input;
        }
        return updatedInput;
    }


    public String returnVariableValue(String input) {
        if(variables.get(input) != null) {
            return variables.get(input);
        }
        return "Unknown variable";
    }


    public void saveVariablesToMap(String input) {
        String[] pieces = input.split("=");

        if(pieces.length > 2) {
            System.out.println("Invalid assignment");
        } else if(pieces[0].matches("[a-zA-Z]+") && pieces[1].matches("\\-?\\d+")) {
            variables.putIfAbsent(pieces[0], pieces[1]);
            variables.computeIfPresent(pieces[0], (k, v) -> v = pieces[1]);
        } else if(pieces[1].matches("[a-zA-Z]+") && pieces[0].matches("\\-?\\d+")) {
            variables.putIfAbsent(pieces[1], pieces[0]);
            variables.computeIfPresent(pieces[1], (k, v) -> v = pieces[0]);
        } else if(pieces[0].matches("[a-zA-Z]+") && pieces[1].matches("[a-zA-Z]+")) {
            if(variables.containsKey(pieces[0]) && variables.containsKey(pieces[1])) {
                variables.computeIfPresent(pieces[0], (k, v) -> v = variables.get(pieces[1]));
            } else if(variables.containsKey(pieces[0])) {
                variables.put(pieces[1], variables.get(pieces[0]));
            } else if(variables.containsKey(pieces[1])) {
                variables.put(pieces[0], variables.get(pieces[1]));
            } else {
                System.out.println("Unknown variable");
            }
        } else {
            if(pieces[0].matches("\\d+")) {
                System.out.println("Invalid identifier");
            } else {
                System.out.println("Invalid assignment");
            }
        }
    }


    public void evaluateCommands(String input) {
        if(input.equals("/exit")) {
            System.out.println("Bye!");
            exitCommand = true;
        } else if(input.equals("/help")) {
            System.out.println("This program calculates numbers, as you would expect.");
        } else {
            System.out.println("Unknown command");
        }
    }
}
