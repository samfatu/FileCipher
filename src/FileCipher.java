

public class FileCipher {
    public static void main(String[] args) throws Exception {
        ArgParser arguments = new ArgParser(args);
        Crypto crypto = new Crypto(arguments);


        //printByteArrayAsText(inFile);
    }

    public static void printByteArrayAsText(byte[] array) {
        for (byte b : array) {
            System.out.print((char) b);
        }
    }
}
