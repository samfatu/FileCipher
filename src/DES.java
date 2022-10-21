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
                break;
            case CFB:
                break;
            case OFB:
                break;
            case CTR:
                break;
        }
    }

    // TODO: REMEMBER LOG
    private void encryptCBC() {
        ArrayList<byte[]> blocks = Crypto.divideToBlocks(this.input);

        try {
            for (byte[] block : blocks) {
                byte[] XORed = Crypto.xor(this.input, this.IV);
                SecretKeySpec key = new SecretKeySpec(this.key, "DES");
                Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] crypted = cipher.doFinal(XORed);

                FileIO.writeFile(this.outputFilePath, crypted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptCFB() {

    }

    private void encryptOFB() {

    }

    private void encryptCTR() {

    }
}
