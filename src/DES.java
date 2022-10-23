import enums.Function;
import enums.Mode;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DES {
    private byte[] input;
    private byte[] IV;
    private byte[] key;
    private byte[] nonce;
    private Function function;
    private Mode mode;
    private String outputFilePath;

    public DES(byte[] input, String[] keys, Function function, Mode mode, String outputFilePath) {
        this.input = input;
        this.function = function;
        this.mode = mode;
        this.outputFilePath = outputFilePath;
        // Takes the last 8 characters of the words in key file as key
        this.IV = keys[0].substring(keys[0].length() - 8).getBytes(StandardCharsets.UTF_8);
        this.key = keys[1].substring(keys[1].length() - 8).getBytes(StandardCharsets.UTF_8);
        this.nonce = keys[2].substring(keys[2].length() - 8).getBytes(StandardCharsets.UTF_8);
    }

    public void encrypt() {
        switch (this.mode) {
            case CBC:
                encryptCBC();
                break;
            case CFB:
                encryptCFB();
                break;
            case OFB:
                encryptOFB();
                break;
            case CTR:
                encryptCTR();
                break;
        }
    }

    public void decrypt() {
        switch (this.mode) {
            case CBC:
                decryptCBC();
                break;
            case CFB:
                break;
            case OFB:
                decryptOFB();
                break;
            case CTR:
                break;
        }
    }

    // TODO: REMEMBER LOG
    private void encryptCBC() {
        ArrayList<byte[]> blocks = Crypto.divideToBlocks(this.input);

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            byte[] XORed;

            for (byte[] block : blocks) {
                XORed = Crypto.xor(block, this.IV);

                SecretKeySpec key = new SecretKeySpec(this.key, "DES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] crypted = cipher.doFinal(XORed);

                FileIO.writeFile(this.outputFilePath, crypted);

                this.IV = crypted;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptCBC() {
        ArrayList<byte[]> blocks = Crypto.divideToBlocks(this.input);

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            byte[] XORed;

            for (byte[] block : blocks) {
                SecretKeySpec key = new SecretKeySpec(this.key, "DES");
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] encrypted = cipher.doFinal(block);

                XORed = Crypto.xor(encrypted, this.IV);

                Crypto.writeDecryptedFile(this.outputFilePath, XORed);

                this.IV = block;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptCFB() {

    }

    private void encryptOFB() {
        ArrayList<byte[]> blocks = Crypto.divideToBlocks(this.input);

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            byte[] XORed;

            for (byte[] block : blocks) {
                SecretKeySpec key = new SecretKeySpec(this.key, "DES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] crypted = cipher.doFinal(this.IV);

                XORed = Crypto.xor(block, crypted);
                this.IV = crypted;

                FileIO.writeFile(this.outputFilePath, XORed);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptOFB() {
        ArrayList<byte[]> blocks = Crypto.divideToBlocks(this.input);

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            byte[] XORed;

            for (byte[] block : blocks) {
                SecretKeySpec key = new SecretKeySpec(this.key, "DES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] crypted = cipher.doFinal(this.IV);

                XORed = Crypto.xor(block, crypted);
                this.IV = crypted;

                Crypto.writeDecryptedFile(this.outputFilePath, XORed);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptCTR() {

    }
}
