import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final String[] needRoman = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    private static final String[] needDec = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final Map<Integer, String> allRoman = new HashMap<>();

    static {
        allRoman.put(1000, "M");
        allRoman.put(900, "CM");
        allRoman.put(500, "D");
        allRoman.put(400, "CD");
        allRoman.put(100, "C");
        allRoman.put(90, "XC");
        allRoman.put(50, "L");
        allRoman.put(40, "XL");
        allRoman.put(10, "X");
        allRoman.put(9, "IX");
        allRoman.put(5, "V");
        allRoman.put(4, "IV");
        allRoman.put(1, "I");
    }

    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }
    public static String toRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static int toDec(String rom) {
        int dec = 0;
        for (Map.Entry<Integer, String> entry : allRoman.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            while (rom.startsWith(value)) {
                dec += key;
                rom = rom.substring(value.length());
            }
        }
        return dec;
    }

    public static String calc(String input) {
        if (input.length() <= 2) {
            throw new IllegalArgumentException();
        }

        int countR = 0;
        int countA = 0;

        String oper;
        String typeCalc = "";
        String[] arrCalc = input.split("[+\\-*/]");

        if (arrCalc.length > 2) {
            throw new IllegalArgumentException();
        }

        arrCalc[0] = arrCalc[0].trim();
        arrCalc[1] = arrCalc[1].trim();

        for (String r : arrCalc) {
            for (String a : needDec) {
                if (r.equals(a)) {
                    countA++;
                }
            }
        }

        for (String r : arrCalc) {
            for (String a : needRoman) {
                if (r.equals(a)) {
                    countR++;
                }
            }
        }

        if (countA == 1 || countR == 1) {
            throw new IllegalArgumentException();
        }

        if (contains(needDec, arrCalc[0]) && contains(needDec, arrCalc[1])) {
            typeCalc = "calcDec";
        }

        if (contains(needRoman, arrCalc[0]) && contains(needRoman, arrCalc[1])) {
            typeCalc = "calcRom";
        }


        if (input.contains("+")) {
            oper = "+";
        } else if (input.contains("-")) {
            oper = "-";
        } else if (input.contains("*")) {
            oper = "*";
        } else if (input.contains("/")) {
            oper = "/";
        } else {
            throw new IllegalArgumentException();
        }

        if ("calcDec".equals(typeCalc)) {
            int num1 = Integer.parseInt(arrCalc[0]);
            int num2 = Integer.parseInt(arrCalc[1]);

            switch (oper) {
                case "+":
                    return Integer.toString(num1 + num2);
                case "-":
                    return Integer.toString(num1 - num2);
                case "*":
                    return Integer.toString(num1 * num2);
                case "/":
                    return Integer.toString(num1 / num2);
            }
        }

        if ("calcRom".equals(typeCalc)) {
            int num1 = toDec(arrCalc[0]);
            int num2 = toDec(arrCalc[1]);

            switch (oper) {
                case "+":
                    return toRoman(num1 + num2);
                case "-":
                    int result = num1 - num2;
                    if (result < 1) {
                        throw new IllegalArgumentException();
                    }
                    return toRoman(result);
                case "*":
                    return toRoman(num1 * num2);
                case "/":
                    return toRoman((int) num1 / num2);
            }
        }

        return "";
    }

    private static boolean contains(String[] arr, String str) {
        for (String a : arr) {
            if (a.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.print(calc(str));
    }
}
