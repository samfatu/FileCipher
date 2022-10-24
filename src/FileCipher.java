

public class FileCipher {
    public static void main(String[] args) throws Exception {
        //ArgParser arguments = new ArgParser(new String[]{"-e", "-i", "i.txt", "-o", "o.txt", "3DES", "CTR", "keyfile.txt"});
        ArgParser arguments = new ArgParser(new String[]{"-d", "-i", "o.txt", "-o", "d.txt", "3DES", "CTR", "keyfile.txt"});
        Crypto crypto = new Crypto(arguments);

        crypto.run();
    }

    public static void printByteArrayAsText(byte[] array) {
        for (byte b : array) {
            System.out.print(b + " ");
        }
        System.out.println();
    }
}
