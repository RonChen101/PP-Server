
import java.io.*;

public class test {
    public static void main(String[] args) {
        File folder = new File("resources");
        System.out.println(folder.exists());
        folder.mkdir();
        System.out.println(folder.exists());
    }
}
