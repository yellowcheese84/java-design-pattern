package pattern.strategy;

import java.util.Scanner;

public class StrategyPatternDemo {

    public static void main(String[] args) {


        int num1 = 10;
        int num2 = 5;

        Context context = new Context(new OperationAdd());
        System.out.println("10 + 5 = " + context.executeStrategy(num1, num2));

        context = new Context(new OperationSubstract());
        System.out.println("10 - 5 = " + context.executeStrategy(num1, num2));

        context = new Context(new OperationMultiply());
        System.out.println("10 * 5 = " + context.executeStrategy(num1, num2));

        context = new Context(new OperationDivision());
        System.out.println("10 / 5 = " + context.executeStrategy(num1, num2));


        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        double d = scan.nextDouble();
        scan.nextLine();
        String s = scan.nextLine();

        System.out.println("String: " + s);
        System.out.println("double: " + d);
        System.out.println("int: " + i);
    }
}
