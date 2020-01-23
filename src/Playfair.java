import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

class Playfair{
    private static char[][] fpMatrix;
    // Generate a random key of length 6.
    private String genKey() {
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int ranIndex = (int) (alphabets.length() * Math.random());
            sb.append(alphabets.charAt(ranIndex));
        }
        return sb.toString();
    }

    // Read string in file.
    private static String readFile(String fileName) throws IOException {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data.toUpperCase();
    }
    // Write string to file.
    private static void writeFile(String filename, String string) throws IOException {
        File file = new File(filename);
        if (file.createNewFile()){
            //System.out.println("File is created!");
        } else {
            //System.out.println("File already exists.");
        }
        FileWriter writer = new FileWriter(file);
        writer.write(string);
        writer.close();
    }

    // To clean the generated keyword
    // Uses LinkedHashSet to remove duplicated char.
    // Removes I from key.
    private String cleanKey(String keyword) {
        char[] alphabet = "ABCDEFGHIJKLNMOPQRSTUVWXYZ".toCharArray();
        char[] chars = keyword.toCharArray();
        Set<Character> charSet = new LinkedHashSet<>();
        for (char c : chars) {
            if(c !='I'){
                charSet.add(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Character character : charSet) {
            sb.append(character);
        }
        return sb.toString();
    }

    // Fill the 2d matrix ith 1s
    private char[][] initMatrix() {
        fpMatrix = new char[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                fpMatrix[i][j] = '1';
            }
        }
        return fpMatrix;
    }
    // fn to print the 2d mx
    private static void printMatrix(char[][] fpMatrix){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                System.out.print(fpMatrix[i][j]);
            }
            System.out.println();
        }
    }

    // Fill the 2d matrix with the keyword first.
    private char[][] keyFill(char[][] fpMatrix, String keyword){
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

    // Fill the remaining of the matrix with left over chars. Omitting I.
    private char[][] fillChar(char[][] fpMatrix, String keyword){
        char startChar = 'A';
        int i=0;
        int j=0;
        while(startChar <= 'Z'){
            if(keyword.indexOf(startChar)<0 && startChar!= 'I'){
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

    // Adjust the plainText by replacing I with J and removes all spaces.
    // Text with consecutive same char, will have x inserted inbetween
    // If plainText len is odd, append 'Z' to it. (https://learncryptography.com/classical-encryption/playfair-cipher/)
    // Therefore: If MSG = "zzzz" Adjusted MSG = "zxzxzxz" (ODD length) = "zxzxzxzz"
    private static String adjustPlainText(String plainText) {
        String newPt = plainText.replaceAll("I", "J").replaceAll(" ", "");
        int length = newPt.length();
        StringBuilder sbPt = new StringBuilder(newPt);
        for (int i = 0; i < length - 1; i+=2) {
            if(sbPt.charAt(i) == sbPt.charAt(i+1)) {
                sbPt.insert(i+1, "X");
                length++;
            }
        }
        if (sbPt.length() % 2 == 0) {
            return newPt;
        }else if (sbPt.length() % 2 == 1) {
            sbPt.append('Z');
        }
        return sbPt.toString();
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
            System.out.println("Plain text with odd length is padded with a Z at the end. \nJ is replaced with I.\nConsecutive same chars have X inserted inbetween them.");
            String keyFileName = args[1];
            String plainFileName = args[2];
            String cipherFileName = args[3];
            String cipherText;
            // In Encrypt mode, generate a key of length 6.
            String keyword = obj.genKey();
            // Write key to Keyfile
            obj.writeFile(keyFileName, keyword);
            // Read Plaintext.txt
            String plainText = obj.readFile(plainFileName);
            System.out.println("Encrypt Mode.\n" + "Generated Keyword: " + keyword);
            keyword = obj.cleanKey(keyword);
            fpMatrix = obj.initMatrix();
            fpMatrix = obj.keyFill(fpMatrix, keyword);
            fpMatrix = obj.fillChar(fpMatrix, keyword);
            obj.printMatrix(fpMatrix);
            String adjustedPlainText = obj.adjustPlainText(plainText);
            System.out.println("PlainText: " + plainText);
            System.out.println("Adjusted PlainText: " + adjustedPlainText);
            System.out.println("Adjusted keyword: " + keyword);
            // Write ciphertext.txt
            cipherText = obj.encode(adjustedPlainText, fpMatrix, 1);
            obj.writeFile(cipherFileName, cipherText);
            System.out.println("Encrypted Text: " + cipherText);

        }else if(args[0].equals("-d")){
            System.out.println("Plain text with odd length is padded with a Z at the end. \nJ is replaced with I.\nConsecutive same chars have X inserted inbetween them.");
            String keyFileName = args[1];

            String cipherFileName = args[2];
            String plainFileName = args[3];
            String cipherText, plainText;
            String keyword = obj.readFile(keyFileName);
            System.out.println("Decrypt mode.\n" + "Read'd Keyword: " + keyword);
            keyword = obj.cleanKey(keyword);
            fpMatrix = obj.initMatrix();
            fpMatrix = obj.keyFill(fpMatrix, keyword);
            fpMatrix = obj.fillChar(fpMatrix, keyword);
            obj.printMatrix(fpMatrix);
            cipherText = obj.readFile(cipherFileName);
            plainText = obj.encode(cipherText, fpMatrix, 4);
            System.out.println("Encrypted Text: "+ cipherText);
            System.out.println("Decrypted Text: "+ plainText);
            obj.writeFile(plainFileName, plainText);
        }else{
            System.out.println("Missing args");
        }
    }


}