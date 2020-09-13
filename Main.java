import init.DataFilesInit;
import menu.Menu;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        DataFilesInit init = new DataFilesInit();
        Path dataFile = init.getDataFile();
        Menu menu = new Menu(dataFile);
        menu.start();
    }


}