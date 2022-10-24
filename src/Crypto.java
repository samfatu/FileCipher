import enums.Algorithm;
import enums.Function;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Crypto {
    private byte[] inFile;
    private String[] keyFile;
    private ArgParser arguments;

    public Crypto(ArgParser arguments) {
        this.arguments = arguments;
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

    // TODO: PADDING
    // TODO: circularPos kaldır
    public static byte[] xor(final byte[] input, final byte[] key) {
        byte[] result = new byte[input.length];

        int circularPos = 0;
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) (input[i] ^ key[circularPos]);
            circularPos++;
            if (circularPos >= key.length) {
                circularPos = 0;
            }
        }

        return result;
    }

    public static ArrayList<byte[]> divideToBlocks(byte[] input) {
        ArrayList<byte[]> blocks = new ArrayList<>();
        int size = input.length / 8;
        int reminder = input.length % 8;

        for (int i = 0; i < size; i++) {
            blocks.add(Arrays.copyOfRange(input, i * 8, (i +1) * 8));
        }

        byte[] padding = new byte[8];
        if (reminder != 0) {
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

    public static boolean byteArrayContainsPadding(byte[] array) {
        for (byte b : array) {
            if (b == 31) {
                return true;
            }
        }
        return false;
    }



    public static byte[] combineNonceAndCounter(byte[] nonce, byte count) {
        byte[] combined = new byte[nonce.length + 1];
        System.arraycopy(nonce, 0, combined, 0, nonce.length);
        combined[combined.length - 1] = count;
        return combined;
    }

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
