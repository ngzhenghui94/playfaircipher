import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;

class Playfair{
    private static char[][] fpMatrix;
    // To Trim Playfair cipher keyword e.g TEST > TES
    // PASS > PAS
    // Uses LinkedHashSet to remove duplicated char.
    private String cleanKey(String keyword) {
        char[] alphabet = "ABCDEFGHIJKLNMOPQRSTUVWXYZ".toCharArray();
        char[] chars = keyword.toCharArray();
        Set<Character> charSet = new LinkedHashSet<>();
        for (char c : chars) {
            if(c !='J'){
                charSet.add(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Character character : charSet) {
            sb.append(character);
        }
        return sb.toString();
    }

    // Fil the 2d matrix ith 1s
    private char[][] initMatrix() {
        fpMatrix = new char[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                fpMatrix[i][j] = '1';
            }
        }
        return fpMatrix;
    }

    private static void printMatrix(char[][] fpMatrix){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                System.out.print(fpMatrix[i][j]);
            }
            System.out.println();
        }
    }

    private char[][] keyFill(char[][] fpMatrix, String keyword){
        // Fill the matrix with Keyword first.
        int counter = 0;
        int keywordLen = keyword.length();
        int getRows = keywordLen/5;
        int columns = keywordLen%5;
        for(int i=0; i<getRows; i++){
            for(int j=0; j<5; j++){
                fpMatrix[i][j] = keyword.charAt(counter);
                counter += 1;
            }
        }
        for(int j=0; j<columns; j++) {
            fpMatrix[getRows][j] = keyword.charAt(counter);
            counter += 1;
        }
        return fpMatrix;
    }

    private char[][] fillChar(char[][] fpMatrix, String keyword){
        char startChar = 'A';
        int i=0;
        int j=0;
        while(startChar <= 'Z'){
            if(keyword.indexOf(startChar)<0 && startChar!= 'J'){
                if(fpMatrix[i][j] == '1'){
                    fpMatrix[i][j] = startChar;
                    startChar++;
                }if(j==4) {
                    i = (i + 1) % 5;
                }
                j = (j+1)%5;
            }
            else{
                startChar++;
            }
        }
        return fpMatrix;
    }

    // Adjust the plainText by removing "J" for playfair cipher.
    // If plainText len is odd, append 'Z' to it. (https://learncryptography.com/classical-encryption/playfair-cipher/)
    private static String adjustPlainText(String plainText) {
        String newPt = plainText.replaceAll("J", "");
        StringBuilder sb = new StringBuilder();
        if (newPt.length() % 2 == 0) {
            return newPt;
        }else if (newPt.length() % 2 == 1) {
            newPt += 'Z';
        }
        return newPt;
    }

    // Encoding (Encrypt: shift(dir) = 1. Decrypt shift(dir) = 4)
    private String encode(String text, char[][] fpMatrix, int dir){
        StringBuilder encodedText = new StringBuilder();
        int a1 = 0;
        int b1 = 0;
        int a2 = 0;
        int b2 = 0;
        int ptLen = text.length();
        for(int k=0; k<ptLen; k+=2){
            char a = text.charAt(k);
            char b = text.charAt(k+1);
            //Finding the elements in the display_matrix
            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    if(fpMatrix[i][j] == a){
                        a1 = i;
                        b1 = j;
                    }if(fpMatrix[i][j] == b) {
                        a2 = i;
                        b2 = j;
                    }
                }
            }
            if(a1==a2){
                encodedText.append(fpMatrix[a1][(b1 + dir) % 5]).append(fpMatrix[a2][(b2 + dir) % 5]);
            }else if(b1==b2){
                encodedText.append(fpMatrix[(a1 + dir) % 5][b1]).append(fpMatrix[(a2 + dir) % 5][b2]);
            }else{
                encodedText.append(fpMatrix[a2][b1]).append(fpMatrix[a1][b2]);
            }
        }
        return encodedText.toString();
    }

    public static void main(String args[]) throws IOException {
        Playfair obj = new Playfair();
        if (args[0].equals("-e")){
            System.out.println("Playfair Cipher, Rules: J is omitted(From Keyword & String)");
            String keyword = args[1].toUpperCase();
            String plainText = args[2].toUpperCase();
            System.out.println("Encrypt Mode.\n" + "Input'd Keyword: " + keyword);
            keyword = obj.cleanKey(keyword);
            fpMatrix = obj.initMatrix();
            fpMatrix = obj.keyFill(fpMatrix, keyword);
            fpMatrix = obj.fillChar(fpMatrix, keyword);
            obj.printMatrix(fpMatrix);
            String adjustedPlainText = obj.adjustPlainText(plainText);
            System.out.println("Adjusted PlainText: " + adjustedPlainText);
            System.out.println("Adjusted keyword: " + keyword);
            System.out.println("Encrypted Text: "+ obj.encode(adjustedPlainText, fpMatrix, 1));
            //java Playfair -e test kopwo
            // Output: PIWEVU
        }else if(args[0].equals("-d")){
            //java Playfair -d test PIWEVU
            // Output: kopwo
            String keyword = args[1].toUpperCase();
            String cipherText = args[2].toUpperCase();
            System.out.println("Playfair Cipher, Rules: J is omitted(From Keyword & String)");
            System.out.println("Decrypt mode.\n" + "Input'd Keyword: " + keyword);
            keyword = obj.cleanKey(keyword);
            fpMatrix = obj.initMatrix();
            fpMatrix = obj.keyFill(fpMatrix, keyword);
            fpMatrix = obj.fillChar(fpMatrix, keyword);
            obj.printMatrix(fpMatrix);
            System.out.println("Decrypted Text: "+ obj.encode(cipherText, fpMatrix, 4));
        }else{
            System.out.println("Missing args");
        }
    }

}