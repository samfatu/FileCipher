
public class FileCipher {
    public static void main(String[] args) throws Exception {
        ArgParser parser = new ArgParser(args);
        byte[] inFile = FileIO.readFileAsByteArray(parser.getInputFilePath());
        String[] keyFile = FileIO.readKeyFile(parser.getKeyFilePath());

    }

    public static void printByteArrayAsText(byte[] array) {
        for (byte b : array) {
            System.out.print((char) b);
        }
    }
}
