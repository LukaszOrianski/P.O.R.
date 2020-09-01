import init.DataFilesInit;
import menu.Menu;
import data.MonthPayments;
import data.Months;
import data.Payment;
import data.YearPayments;
import utils.Utils;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static utils.Utils.loadXmlData;
import static utils.Utils.updateXmlDataFile;


//*********************************************************************************************************//
// musibyc nowy monthPayment i nowy payment + metoda ktora kopiuje dane do  ze starego paymenta do nowego**//
//*********************************************************************************************************//


public class Main {

    // watek bedzie sprawdzal date i jesli okaze sie ze aktualny miesiac > ostatniego obecnego to utworz nowy miesiac i przerzuc
    // do nowego miesiaca wszystkie ktore maja termin do tego miesiaca :)
    // pobuierz miesiac zoobowiazan i dodaj "payPeriod" , wyjdzie nowy miesiac :D

    // mozna dodac liste ulubionych rzeczy razem z cenami, zeby porownac ile jest wart dany rachunek / ile musisz pracowac na dany rachunek


    // !!!!!!!!!!!!!!!!!!!!!!!!!  dodac sprawdzanie, czy plik jest i jest w porzadku, jesli nie :
    // albo stwórz nowy
    // albo sprawdz w katalogu, czy istnieja pliki


    // sortowanie rachunkow w miesiacu - po dodaniu nwego miesiaca gdy sa rachunki o wiekszym patperiod niz 2 to wpycha sie na poczatek

    public static void main(String[] args) {

        DataFilesInit init = new DataFilesInit();
        Path dataFile = init.chooseDataFile();
        Menu menu = new Menu(dataFile);
        menu.start();
    }
}


