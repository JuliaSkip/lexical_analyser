import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
Прочитайте текст завдання  з лексичного аналізу.
Побудуйте  лексичний аналізатор для фрагменту мови програмування.
Програма отримує на вхід певний текст, написаний такою  мовою,
і при кожному зверненні видає наступну лексему, вказуючи тип лексеми. Мінімально,
повинні розпізнаватися такі класи лексем : ідентифікатори (дозволяється використовувати числа та літери українського алфавіту,
які присутні у прізвищі студента, числа(цілі та дробові), арифметичні оператори(+,-,*,/),
вбудовані функції(sin, cos,..), зарезервовані слова (if,else, while,...), помилкові лексеми (тобто,неправильно побудовані).
Студент, на  свій розсуд, має можливість розширити кількість класів лексем.
Завантажте текст програми та скріншоти, які демонструють роботу програми.
*/

class Lexeme {
    private String lexeme;
    private String type;

    public Lexeme(String lexeme, String type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getType() {
        return type;
    }
}

public class LexicalAnalyser {
    private static final LinkedHashMap<String, Pattern> patterns = new LinkedHashMap<>();

    static {
        patterns.put("CONDITION_SIGN", Pattern.compile("(<=|>=|==|<|>|!=|&&|\\|\\|)"));
        patterns.put("ARITHMETIC_OPERATOR", Pattern.compile("[+\\-*/=%]"));
        patterns.put("DELIMITER", Pattern.compile("[\\[\\](){}.,!?;:'\"\\\\]"));
        patterns.put("BUILTIN_FUNCTION", Pattern.compile("(sin|cos|tan|log|exp|max|min)"));
        patterns.put("RESERVED_WORD", Pattern.compile("(if|else|while|char|double|float|String|int|void|false|true|null|private|public|static|return)"));
        patterns.put("FLOAT", Pattern.compile("\\d+\\.\\d+"));
        patterns.put("INTEGER", Pattern.compile("\\d+"));
        patterns.put("IDENTIFIER", Pattern.compile("(?=.*[СсКкІіПп])[СсКкІіПп\\d]+"));
    }

    public static ArrayList<Lexeme> analyse(String text) {
        ArrayList<Lexeme> result = new ArrayList<>();
        String[] words = text.split("\\s+");

        for (String word : words) {
            boolean matched = false;

            for (HashMap.Entry<String, Pattern> entry : patterns.entrySet()) {
                String name = entry.getKey();
                Pattern pattern = entry.getValue();

                Matcher matcher = pattern.matcher(word);
                if (matcher.matches()) {
                    result.add(new Lexeme(word, name));
                    matched = true;
                    break;
                }
            }

            if (!matched) result.add(new Lexeme(word, "ERROR"));
        }

        return result;
    }

    public static void main(String[] args) {
        ArrayList<Lexeme> res = analyse(
                "private static int скк0ппі8п8Скіп = 123 ; " +
                     "public float пік5 = 45.7 ;  " +
                     "private char нескіп = ' s ' ; " +
                     "if ( скк0ппі8п8Скіп == пік5 && ( 4 - 6 ) != 1 ) { " +
                          "return sin ( 23 ) " +
                     "} else { " +
                           "return tan ( 67.9 ) " +
                     "}" +
                     " return нескіп != ' s '");

        System.out.println("Lexical Analysis Result:");
        System.out.println("------------------------");
        for (Lexeme lexeme : res) {
            String color;

            switch (lexeme.getType()) {
                case "RESERVED_WORD":
                    color = "\u001B[32m";
                    break;
                case "BUILTIN_FUNCTION":
                    color = "\u001B[36m";
                    break;
                case "ARITHMETIC_OPERATOR","CONDITION_SIGN", "DELIMITER" :
                    color = "\u001B[33m";
                    break;
                case "INTEGER":
                    color = "\u001B[35m";
                    break;
                case "FLOAT":
                    color = "\u001B[34m";
                    break;
                case "IDENTIFIER":
                    color = "\u001B[38m";
                    break;
                case "ERROR":
                    color = "\u001B[31m";
                    break;
                default:
                    color = "\u001B[0m";
                    break;
            }

            System.out.printf(color + "%-20s : %s" + "\u001B[0m" + "%n", lexeme.getType(), lexeme.getLexeme());
        }
    }
}