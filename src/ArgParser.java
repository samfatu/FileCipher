import enums.Algorithm;
import enums.Function;
import enums.Mode;

import java.io.File;

public class ArgParser {
    private Function function;
    private String inputFilePath;
    private String outputFilePath;
    private String keyFilePath;
    private Algorithm algorithm;
    private Mode mode;
    private static final String argumentErrorMessage = "Arguments or order of arguments is incorrect. " +
            "Please provide the arguments in the correct format.\nCorrect argument format is: " +
            "−e −i inputFile.txt −o outFile.txt algorithm mode key_file.txt";

    // Parses the given Command Line Arguments to necessary parts
    public ArgParser(String[] args) throws Exception {
        if (args.length != 8) {
            throw new Exception(argumentErrorMessage);
        }

        if (args[0].equals("-e")) {
            function = Function.ENC;
        } else if (args[0].equals("-d")) {
            function = Function.DEC;
        } else {
            throw new Exception(argumentErrorMessage);
        }

        if (args[1].equals("-i") && (new File(args[2]).isFile())) {
            inputFilePath = args[2];
        } else {
            throw new Exception(argumentErrorMessage);
        }

        if (args[3].equals("-o")) {
            outputFilePath = args[4];
        } else {
            throw new Exception(argumentErrorMessage);
        }

        if (args[5].equals("DES")) {
            this.algorithm = Algorithm.DES;
        } else if (args[5].equals("3DES")) {
            this.algorithm = Algorithm.THREE_DES;
        } else {
            throw new Exception(argumentErrorMessage);
        }

        switch (args[6]) {
            case "CBC":
                this.mode = Mode.CBC;
                break;
            case "CFB":
                this.mode = Mode.CFB;
                break;
            case "OFB":
                this.mode = Mode.OFB;
                break;
            case "CTR":
                this.mode = Mode.CTR;
                break;
            default:
                throw new Exception(argumentErrorMessage);
        }

        if ((new File(args[7]).isFile())) {
            keyFilePath = args[7];
        } else {
            throw new Exception(argumentErrorMessage);
        }
    }

    public Function getFunction() {
        return function;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public Mode getMode() {
        return mode;
    }
}
