import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompilerCode {
    // Constants
    private static final String[] KEYWORDS = {"start", "finish", "then", "if", "repeat", "var", "int", "float", "do", "read", "print", "void", "return"};
    private static final String[] RELATIONAL_OPERATORS = {"==", "<", ">", "!=", ">=", "<="};
    private static final String[] OTHER_OPERATORS = {"=", "+", "-", "*", "/", "%"};
    private static final String[] DELIMITERS = {".", "(", ")", ",", "{", "}", ";"};

    // Symbol table
    private static Map<String, SymbolInfo> symbolTable = new HashMap<>();

    // Function to check if a given word is an identifier
    private static boolean isIdentifier(String word) {
        if (!Character.isLetter(word.charAt(0))) {
            return false;
        }
        if (word.length() > 8) {
            return false;
        }
        for (char c : word.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Function to check if a given word is a keyword
    private static boolean isKeyword(String word) {
        for (String keyword : KEYWORDS) {
            if (keyword.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    // Function to check if a given word is a valid number
    private static boolean isNumber(String word) {
        if (word.length() > 8) {
            return false;
        }
        if (word.charAt(0) == '-' && word.length() == 1) {
            return false;
        }
        if (!Character.isDigit(word.charAt(0))) {
            return false;
        }
        for (char c : word.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Tokenize the input code
    private static List<Token> tokenize(String input) {
        // Variables to keep track of the line number and the total number of lexemes and tokens
        int lineNumber = 0;
        int lexemeCount = 0;
        int tokenCount = 0;

        // List to store the tokens and their information
        List<Token> tokens = new ArrayList<>();

        // Split the input into lines and iterate through them
        String[] lines = input.split("\n");
        for (String line : lines) {
           
            // Remove comments from the line
            line = removeComments(line);

            // Split the line into words and iterate through them
            String[] words = line.split("\\s+");
            for (String word : words) {
                // Check if the word is a keyword, identifier, number, operator, delimiter, or invalid
                if (isKeyword(word)) {
                    tokens.add(new Token(word, "Keyword", lineNumber));
                    tokenCount++;
                    lexemeCount++;
					lineNumber++;
                } else if (isIdentifier(word)) {
                    // Add the identifier to the symbol table if it's not already there
                    if (!symbolTable.containsKey(word)) {
                        symbolTable.put(word, new SymbolInfo("Identifier", lineNumber));
                    }
                    tokens.add(new Token(word, "Identifier", lineNumber));
                    tokenCount++;
                    lexemeCount++;
					lineNumber++;
                } else if (isNumber(word)) {
                    tokens.add(new Token(word, "Number", lineNumber));
                    tokenCount++;
                    lexemeCount++;
					lineNumber++;
                } else if (containsElement(RELATIONAL_OPERATORS, word)) {
                    tokens.add(new Token(word, "Relational Operator", lineNumber));
                    tokenCount++;
                    lexemeCount++;
					lineNumber++;
                } else if (containsElement(OTHER_OPERATORS, word)) {
                    tokens.add(new Token(word, "Other Operator", lineNumber));
                    tokenCount++;
                    lexemeCount++;
					lineNumber++;
                } else if (containsElement(DELIMITERS, word)) {
                    tokens.add(new Token(word, "Delimiter", lineNumber));
                    tokenCount++;
                    lexemeCount++;
					lineNumber++;
                } else {
                    tokens.add(new Token(word, "Invalid", lineNumber));
                    tokenCount++;
                    lexemeCount++;
					lineNumber++;
                }
            }
        }

        // Print the tokens and their information
        System.out.println("Tokens:");
        System.out.println("   Value:  Type:  Line:");
        for (Token token : tokens) {
            System.out.printf("    %s,  %s,  %d%n", token.getValue(), token.getType(), token.getLineNumber());
        }

        // Print the symbol table
        System.out.println("\nSymbol Table:");
        System.out.println("ID:  Type:  Line:");
        for (Map.Entry<String, SymbolInfo> entry : symbolTable.entrySet()) {
            String id = entry.getKey();
            SymbolInfo info = entry.getValue();
            System.out.printf("  %s, %s, %d%n", id, info.getType(), info.getLineNumber());
        }

        // Print the total number of lexemes and tokens
        System.out.println("\nTotal number of lexemes: " + lexemeCount);
        System.out.println("Total number of tokens: " + tokenCount);

        // Return the tokens
        return tokens;
    }

    // Remove comments from a line
    private static String removeComments(String line) {
        int commentIndex = line.indexOf("//");
        if (commentIndex != -1) {
            line = line.substring(0, commentIndex);
        }
        return line.trim();
    }

    // Check if an array contains a specific element
    private static boolean containsElement(String[] array, String element) {
        for (String item : array) {
            if (item.equals(element)) {
                return true;
            }
        }
        return false;
    }

    // Token class
    private static class Token {
        private final String value;
        private final String type;
        private final int lineNumber;

        public Token(String value, String type, int lineNumber) {
            this.value = value;
            this.type = type;
            this.lineNumber = lineNumber;
        }

        public String getValue() {
            return value;
        }

        public String getType() {
            return type;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }

    // SymbolInfo class to store type and line number information for identifiers
    private static class SymbolInfo {
        private final String type;
        private final int lineNumber;

        public SymbolInfo(String type, int lineNumber) {
            this.type = type;
            this.lineNumber = lineNumber;
        }

        public String getType() {
            return type;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }

    public static void main(String[] args) {
        // Test cases
        String input1 = "start\n var x  int ;\n read x ;\n print x ;\n finish";
		
        String input2 = " start\n var LongIdentifierThatExceedsTheEightCharacterLimit  int ;\n read LongIdentifierThatExceedsTheEightCharacterLimit;\n print LongIdentifierThatExceedsTheEightCharacterLimit;\n finish";
        String input3 = "start\n if ( x == 123456789 )\n then {\n print x ;\n }\n finish";
        String input4 = "start\n if ( x = = 5 )\n then {\n  print x ;\n  }\n  finish";
		String input5 = "start\n var n , m , add : int ;\n read n , m ;\n add = n + m ;\n print add ;\n finish";
		String input6 = "start\n var n , m , subtract : int ;\n read n , m ;\n subtract = n - m ;\n print subtract ;\n finish";
		String input7 = "start\n var n , m, multiply : int ;\n read n , m ;\n multiply = n * m ;\n print multiply ;\n finish";
		String input8 = "start\n if ( a = 5 )\n then {\n print a ;\n }\n finish ";
        
        System.out.println("Test Case 1:");
        System.out.println(input1);
        System.out.println();
        tokenize(input1);
        
        System.out.println("\nTest Case 2:");
        System.out.println(input2);
        System.out.println();
        tokenize(input2);
        
        System.out.println("\nTest Case 3:");
        System.out.println(input3);
        System.out.println();
        tokenize(input3);
        
        System.out.println("\nTest Case 4:");
        System.out.println(input4);
        System.out.println();
        tokenize(input4);

		System.out.println("\nTest Case 5:");
        System.out.println(input5);
        System.out.println();
        tokenize(input5);

		System.out.println("\nTest Case 6:");
        System.out.println(input6);
        System.out.println();
        tokenize(input6);

		System.out.println("\nTest Case 7:");
        System.out.println(input7);
        System.out.println();
        tokenize(input7);

		System.out.println("\nTest Case 8:");
        System.out.println(input8);
        System.out.println();
        tokenize(input8);
    }
}