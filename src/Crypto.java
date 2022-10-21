import enums.Algorithm;
import enums.Function;

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
                   padding[i] = '0';
               }
            }
            blocks.add(padding);
        }

        return blocks;
    }

    public void run() {
        if (arguments.getAlgorithm() == Algorithm.DES) {
            DES des = new DES(inFile, keyFile, arguments.getFunction(), arguments.getMode(), arguments.getOutputFilePath());

            if (arguments.getFunction() == Function.ENC) {
                des.encrypt();
            } else if (arguments.getFunction() == Function.DEC) {
                des.decrypt();
            }
        } else if (arguments.getAlgorithm() == Algorithm.THREE_DES) {
            if (arguments.getFunction() == Function.ENC) {
                // call 3des enc
            } else if (arguments.getFunction() == Function.DEC) {
                // call 3des dec
            }
        }
    }
}
