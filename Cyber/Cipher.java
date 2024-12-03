import java.util.*;

public class Cipher {

    // Playfair Cipher Methods
    public static String publicKey="Cyber";
    static String playfairEncrypt(String plaintext) {
        String key = publicKey.toUpperCase().replaceAll("[^A-Z]", "");
        String grid = createPlayfairGrid(key);
        plaintext = formatPlaintext(plaintext);
        StringBuilder cipherText = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += 2) {
            char a = plaintext.charAt(i);
            char b = plaintext.charAt(i + 1);
            int[] aPos = getPositionInGrid(a, grid);
            int[] bPos = getPositionInGrid(b, grid);

            if (aPos[0] == bPos[0]) {
                cipherText.append(grid.charAt(aPos[0] * 5 + (aPos[1] + 1) % 5));
                cipherText.append(grid.charAt(bPos[0] * 5 + (bPos[1] + 1) % 5));
            } else if (aPos[1] == bPos[1]) {
                cipherText.append(grid.charAt(((aPos[0] + 1) % 5) * 5 + aPos[1]));
                cipherText.append(grid.charAt(((bPos[0] + 1) % 5) * 5 + bPos[1]));
            } else {
                cipherText.append(grid.charAt(aPos[0] * 5 + bPos[1]));
                cipherText.append(grid.charAt(bPos[0] * 5 + aPos[1]));
            }
        }
        return cipherText.toString();
    }

    static String playfairDecrypt(String ciphertext, String key) {
        key = key.toUpperCase().replaceAll("[^A-Z]", "");
        String grid = createPlayfairGrid(key);
        StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char a = ciphertext.charAt(i);
            char b = ciphertext.charAt(i + 1);
            int[] aPos = getPositionInGrid(a, grid);
            int[] bPos = getPositionInGrid(b, grid);

            if (aPos[0] == bPos[0]) {
                plaintext.append(grid.charAt(aPos[0] * 5 + (aPos[1] + 4) % 5));
                plaintext.append(grid.charAt(bPos[0] * 5 + (bPos[1] + 4) % 5));
            } else if (aPos[1] == bPos[1]) {
                plaintext.append(grid.charAt(((aPos[0] + 4) % 5) * 5 + aPos[1]));
                plaintext.append(grid.charAt(((bPos[0] + 4) % 5) * 5 + bPos[1]));
            } else {
                plaintext.append(grid.charAt(aPos[0] * 5 + bPos[1]));
                plaintext.append(grid.charAt(bPos[0] * 5 + aPos[1]));
            }
        }
        return plaintext.toString();
    }

    static String createPlayfairGrid(String key) {
        StringBuilder grid = new StringBuilder();
        String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ"; // I/J are combined in Playfair
        Set<Character> seen = new HashSet<>();

        for (char c : key.toCharArray()) {
            if (!seen.contains(c)) {
                seen.add(c);
                grid.append(c);
            }
        }

        for (char c : alphabet.toCharArray()) {
            if (!seen.contains(c)) {
                seen.add(c);
                grid.append(c);
            }
        }

        return grid.toString();
    }

    static int[] getPositionInGrid(char c, String grid) {
        int index = grid.indexOf(c);
        return new int[]{index / 5, index % 5};
    }

    static String formatPlaintext(String plaintext) {
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "");
        if (plaintext.length() % 2 != 0) {
            plaintext += 'X'; // Padding X for odd length
        }
        return plaintext;
    }

    // Transposition Cipher Methods
    static String transposeEncrypt(String text) {
        int len = text.length();
        int cols = (int) Math.ceil(len / 5.0);
        char[][] grid = new char[5][cols];

        for (int i = 0; i < len; i++) {
            grid[i % 5][i / 5] = text.charAt(i);
        }

        StringBuilder cipherText = new StringBuilder();
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < 5; i++) {
                if (grid[i][j] != 0) {
                    cipherText.append(grid[i][j]);
                }
            }
        }

        return cipherText.toString();
    }

    static String transposeDecrypt(String text) {
        int len = text.length();
        int rows = 5;
        int cols = (int) Math.ceil(len / 5.0);
        char[][] grid = new char[rows][cols];

        int index = 0;
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                if (index < len) {
                    grid[i][j] = text.charAt(index++);
                }
            }
        }

        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < len; i++) {
            decryptedText.append(grid[i % rows][i / rows]);
        }

        return decryptedText.toString();
    }
    
}
