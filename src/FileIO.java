import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileIO {
    public static String[] readKeyFile(String filePath) {
        String fileAsString;
        String[] keyFile = null;

        try {
            fileAsString = new String(Files.readAllBytes(Paths.get(filePath)), Charset.defaultCharset());
            keyFile = fileAsString.split(" - ");
            if (keyFile.length != 3) {
                throw new Exception("Key file format is not valid!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyFile;
    }

    public static byte[] readFileAsByteArray(String filePath) {
        File file = new File(filePath);
        byte[] content = null;
        try {
            content = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFile(String outputFilePath, byte[] content) {
        File yourFile = new File(outputFilePath);

        try{
            yourFile.createNewFile(); // if file already exists will do nothing
            FileOutputStream oFile = new FileOutputStream(yourFile, false);
            oFile.write(content);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
