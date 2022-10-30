import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCipher {
    public static void main(String[] args) throws Exception {
        ArgParser arguments = new ArgParser(args);
        Crypto crypto = new Crypto(arguments);

        crypto.run();

        //runTests("-e", "", "o");
        //runTests("-d", "o", "d");
    }

    // Input file name must be "i.txt" to use this function.
    public static void runTests(String encDec, String inFilePrefix, String outFilePrefix) throws Exception {
        String[] algorithms = {"DES", "3DES"};
        String[] modes = {"CBC", "CFB", "OFB", "CTR"};

        // This part is used for eleminating the initialization overhead, first value in the log file should be ignored
        ArgParser arguments = new ArgParser(new String[]{"-e", "-i", "i.txt", "-o", "blabla.txt", "DES", "CBC", "keyfile.txt"});
        Crypto crypto = new Crypto(arguments);
        crypto.run();

        // Testing for every mode and every algorithm
        for (String mode : modes) {
            for (String algo : algorithms) {
                arguments = new ArgParser(new String[]{
                        encDec,
                        "-i", encDec.equals("-e") ? "i.txt" : inFilePrefix + algo + mode + ".txt",
                        "-o", outFilePrefix + algo + mode + ".txt",
                        algo,
                        mode,
                        "keyfile.txt"});
                crypto = new Crypto(arguments);
                long start;
                long total = 0;
                int iter = 100;
                for (int i = 0; i < iter; i++) {
                    start = System.nanoTime();
                    crypto.run();
                    total += System.nanoTime() - start;
                    Path fileToDeletePath = Paths.get(outFilePrefix + algo + mode + ".txt");
                    Files.delete(fileToDeletePath);
                }

                crypto.run();
                System.out.println(algo + "-" + mode + " runtime: " + (total / iter) / 1000000);
            }
        }
    }
}
