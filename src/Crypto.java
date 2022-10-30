import enums.Algorithm;
import enums.Function;

import java.util.ArrayList;
import java.util.Arrays;

public class Crypto {
    private byte[] inFile;
    private String[] keyFile;
    private ArgParser arguments;

    // Takes arguments as a Data Transport Object
    public Crypto(ArgParser arguments) {
        this.arguments = arguments;
        // Reads given files and assigns its contents to the fields
        this.inFile = FileIO.readFileAsByteArray(arguments.getInputFilePath());
        this.keyFile = FileIO.readKeyFile(arguments.getKeyFilePath());
    }

    public byte[] getInFile() {
        return inFile;
    }

    public String[] getKeyFile() {
        return keyFile;
    }

    public ArgParser getArguments() {
        return arguments;
    }

    // XOR's given 64 bit input and secret key
    public static byte[] xor(final byte[] input, final byte[] key) {
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) (input[i] ^ key[i]);
        }

        return result;
    }

    // Divides the given input array to 64 bit blocks and returns a list of blocks
    public static ArrayList<byte[]> divideToBlocks(byte[] input) {
        ArrayList<byte[]> blocks = new ArrayList<>();
        int size = input.length / 8;
        int reminder = input.length % 8;

        for (int i = 0; i < size; i++) {
            blocks.add(Arrays.copyOfRange(input, i * 8, (i +1) * 8));
        }

        if (reminder != 0) {
            byte[] padding = new byte[8];
            for (int i = 0; i < padding.length; i++) {
               if (i < reminder) {
                   padding[i] = input[size * 8 + i];
               } else {
                   // We used the least used delimiter character in ASCII according to this: https://stackoverflow.com/a/41555511
                   padding[i] = 31;
               }
            }
            blocks.add(padding);
        }

        return blocks;
    }

    // Checks if given array contains padding value that we determined and returns true if it contains
    public static boolean byteArrayContainsPadding(byte[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 31) {
                return true;
            }
        }
        return false;
    }

    // Combines 56 bit nonce value and given 8 bit counter value and returns 64 bit array
    public static byte[] combineNonceAndCounter(byte[] nonce, byte count) {
        byte[] combined = new byte[nonce.length + 1];
        System.arraycopy(nonce, 0, combined, 0, nonce.length);
        combined[combined.length - 1] = count;
        return combined;
    }

    // Creates necessary objects for preferred cryption algorithm and runs them
    public void run() {
        if (arguments.getAlgorithm() == Algorithm.DES) {
            DES des = new DES(inFile, keyFile, arguments.getMode(), arguments.getOutputFilePath(), arguments.getInputFilePath());

            if (arguments.getFunction() == Function.ENC) {
                des.encrypt();
            } else if (arguments.getFunction() == Function.DEC) {
                des.decrypt();
            }
        } else if (arguments.getAlgorithm() == Algorithm.THREE_DES) {
            THREE_DES three_des = new THREE_DES(inFile, keyFile, arguments.getMode(), arguments.getOutputFilePath(), arguments.getInputFilePath());

            if (arguments.getFunction() == Function.ENC) {
                three_des.encrypt();
            } else if (arguments.getFunction() == Function.DEC) {
                three_des.decrypt();
            }
        }
    }
}
