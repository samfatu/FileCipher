import enums.Algorithm;
import enums.Function;
import enums.Mode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class THREE_DES {
    private byte[] input;
    private byte[] IV;
    private byte[] key1;
    private byte[] key2;
    private byte[] nonce;
    private String inputFilePath;
    private Mode mode;
    private String outputFilePath;
    private Cipher cipher;
    private byte[] XORed;
    private ArrayList<byte[]> blocks;

    public THREE_DES(byte[] input, String[] keys, Mode mode, String outputFilePath, String inputFilePath) {
        this.input = input;
        this.mode = mode;
        this.outputFilePath = outputFilePath;
        this.inputFilePath = inputFilePath;
        // Takes the last 8 characters of the words in key file as key
        this.IV = keys[0].substring(keys[0].length() - 8).getBytes(StandardCharsets.UTF_8);
        // TODO: key2 shuffle algo
        this.key1 = keys[1].substring(keys[1].length() - 8).getBytes(StandardCharsets.UTF_8);
        this.key2 = keys[1].substring(0, keys[1].length() - 8).getBytes(StandardCharsets.UTF_8);
        this.nonce = keys[2].substring(keys[2].length() - 7).getBytes(StandardCharsets.UTF_8);

        this.blocks = Crypto.divideToBlocks(this.input);
        try {
            this.cipher = Cipher.getInstance("DES/ECB/NoPadding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encrypt() {
        long startTime = 0;
        long endTime = 0;

        switch (this.mode) {
            case CBC:
                startTime = System.nanoTime();
                encryptCBC();
                endTime = System.nanoTime();
                break;
            case CFB:
                startTime = System.nanoTime();
                encryptCFB();
                endTime = System.nanoTime();
                break;
            case OFB:
                startTime = System.nanoTime();
                encryptOFB();
                endTime = System.nanoTime();
                break;
            case CTR:
                startTime = System.nanoTime();
                encryptCTR();
                endTime = System.nanoTime();
                break;
        }
        FileIO.writeToLogFile(this.inputFilePath, this.outputFilePath, Function.ENC, Algorithm.THREE_DES, this.mode, (endTime - startTime));
    }

    public void decrypt() {
        long startTime = 0;
        long endTime = 0;

        switch (this.mode) {
            case CBC:
                startTime = System.nanoTime();
                decryptCBC();
                endTime = System.nanoTime();
                break;
            case CFB:
                startTime = System.nanoTime();
                decryptCFB();
                endTime = System.nanoTime();
                break;
            case OFB:
                startTime = System.nanoTime();
                decryptOFB();
                endTime = System.nanoTime();
                break;
            case CTR:
                startTime = System.nanoTime();
                decryptCTR();
                endTime = System.nanoTime();
                break;
        }
        FileIO.writeToLogFile(this.inputFilePath, this.outputFilePath, Function.DEC, Algorithm.THREE_DES, this.mode, (endTime - startTime));
    }

    private byte[] threeDES(byte[] input, int firstAndLastOpmode, int middleOpmode) throws Exception {
        SecretKeySpec key1 = new SecretKeySpec(this.key1, "DES");
        cipher.init(firstAndLastOpmode, key1);
        byte[] firstStep = cipher.doFinal(input);

        SecretKeySpec key2 = new SecretKeySpec(this.key2, "DES");
        cipher.init(middleOpmode, key2);
        byte[] secondStep = cipher.doFinal(firstStep);

        cipher.init(firstAndLastOpmode, key1);
        return cipher.doFinal(secondStep);
    }

    private void encryptCBC() {
        try {
            for (byte[] block : this.blocks) {
                XORed = Crypto.xor(block, this.IV);

                byte[] crypted = threeDES(XORed, Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE);

                FileIO.writeFile(this.outputFilePath, crypted);

                this.IV = crypted;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptCBC() {
        try {
            for (byte[] block : this.blocks) {
                byte[] crypted = threeDES(block, Cipher.DECRYPT_MODE, Cipher.ENCRYPT_MODE);

                XORed = Crypto.xor(crypted, this.IV);

                FileIO.writeDecryptedFile(this.outputFilePath, XORed);

                this.IV = block;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptCFB() {
        try {
            for (byte[] block : this.blocks) {
                byte[] crypted = threeDES(this.IV, Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE);

                XORed = Crypto.xor(block, crypted);
                this.IV = XORed;

                FileIO.writeFile(this.outputFilePath, XORed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptCFB() {
        try {
            for (byte[] block : this.blocks) {
                byte[] crypted = threeDES(this.IV, Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE);

                XORed = Crypto.xor(block, crypted);
                this.IV = block;

                FileIO.writeDecryptedFile(this.outputFilePath, XORed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptOFB() {
        try {
            for (byte[] block : this.blocks) {
                byte[] crypted = threeDES(this.IV, Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE);

                XORed = Crypto.xor(block, crypted);
                this.IV = crypted;

                FileIO.writeFile(this.outputFilePath, XORed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptOFB() {
        try {
            for (byte[] block : this.blocks) {
                byte[] crypted = threeDES(this.IV, Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE);

                XORed = Crypto.xor(block, crypted);
                this.IV = crypted;

                FileIO.writeDecryptedFile(this.outputFilePath, XORed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptCTR() {
        byte counter = 0;

        try {
            byte[] combinedNonce;

            for (byte[] block : this.blocks) {
                combinedNonce = Crypto.combineNonceAndCounter(this.nonce, counter);
                counter = (byte) (counter + 1);

                byte[] crypted = threeDES(combinedNonce, Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE);

                XORed = Crypto.xor(block, crypted);

                FileIO.writeFile(this.outputFilePath, XORed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptCTR() {
        byte counter = 0;

        try {
            byte[] combinedNonce;

            for (byte[] block : this.blocks) {
                combinedNonce = Crypto.combineNonceAndCounter(this.nonce, counter);
                counter = (byte) (counter + 1);

                byte[] crypted = threeDES(combinedNonce, Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE);

                XORed = Crypto.xor(block, crypted);

                FileIO.writeDecryptedFile(this.outputFilePath, XORed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
