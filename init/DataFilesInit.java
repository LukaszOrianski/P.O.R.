package init;

import appCore.AppCore;
import data.MonthPayments;
import data.Payment;
import data.YearPayments;
import dictionary.APP_MSG;
import utils.Utils;

import javax.xml.bind.DataBindingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.loadXmlData;
import static utils.Utils.updateXmlDataFile;

public class DataFilesInit {

    public Path getDataFile() {
        List<Path> pathList = getPathList("");
        if (pathList.size() < 1) {
            return noDataFile();
        }
        if (pathList.size() == 1) {
            copyBills2NewMonthWithCheck(new File(getMonthFileName(pathList.get(0).toString())), pathList.get(0).toFile());
            return pathList.get(0);
        } else {
            int i;
            System.out.println("Wybierz plik z danymi:");
            for (i = 0; i < pathList.size(); i++) {
                System.out.printf("%s....%s%n", i + 1, pathList.get(i).toString());
            }
            int userChoice = AppCore.inputGivenRangeInt(1, pathList.size());
            System.out.println("wybrano plik " + userChoice + "...." + pathList.get(userChoice - 1).toString());
            copyBills2NewMonthWithCheck(new File(getMonthFileName(pathList.get(userChoice - 1).toString())), pathList.get(userChoice - 1).toFile());
            return pathList.get(userChoice - 1);
        }
    }

    private Path noDataFile() {
        System.out.println("Nieodnaleziono pliku z danymi, tworze nowy...");
        Path newFile = Paths.get("", LocalDate.now().getYear() + ".xml");
        if (!createNewDataFile(newFile.toString())) {
            System.out.println(APP_MSG.DATA_ERROR.getMessage());
            return null;
        }
        serializeMonthIDToFile(LocalDate.now().getMonthValue(), new File(getMonthFileName(newFile.toString())));
        return newFile;
    }

    private String getMonthFileName(String xmlFileName) {
        if (xmlFileName == null) {
            return null;
        }
        return xmlFileName.replace(".xml", ".rac");
    }

    private List<Path> getPathList(String directory) {
        List<Path> pathList = new ArrayList<>();
        try {
            pathList = Files.list(Paths.get(directory))
                    .filter(DataFilesInit::isItValidDataFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    private boolean createNewDataFile(String fileName) {
        YearPayments yp = new YearPayments(LocalDate.now().getYear());
        return Utils.updateXmlDataFile(new File(fileName), yp);
    }

    private static boolean isItValidDataFile(Path path) {
        YearPayments y;
        try {
            y = loadXmlData(path.toFile());
        } catch (DataBindingException e) {
            return false;
        }
        if (y == null) {
            return false;
        }
        return !y.getMonthPaymentsYearList().isEmpty();
    }

    private void copyBills2NewMonthWithCheck(File lastMonthFile, File dataFile) {
        if (!checkMonth(lastMonthFile)) {
            serializeMonthIDToFile(LocalDate.now().getMonthValue(), lastMonthFile);
        }
        if (getMonthIDFromFile(lastMonthFile) < LocalDate.now().getMonthValue()) {
            newMonthPayments(LocalDate.now().getMonthValue(), dataFile);
            serializeMonthIDToFile(LocalDate.now().getMonthValue(), lastMonthFile);
        }
    }

    private boolean checkMonth(File myFile) {
        if (myFile == null) {
            return false;
        }
        int month = getMonthIDFromFile(myFile);
        return month >= 1 && month <= 12;
    }

    private void serializeMonthIDToFile(int monthID, File myFile) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(myFile))) {
            out.writeInt(monthID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getMonthIDFromFile(File myFile) {
        int a = -1;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(myFile))) {
            a = in.readInt();
        } catch (IOException e) {
            serializeMonthIDToFile(LocalDate.now().getDayOfMonth(), myFile);
        }
        return a;
    }

    private void newMonthPayments(int monthID, File file) {
        YearPayments y = Utils.loadXmlData(file);
        if (y == null) {
            return;
        }
        List<Payment> list = y.getBillsStream().filter(e -> (e.getPaymentDeadline().getMonthValue()) + (e.getPayPeriod()) == monthID)
                .map(e -> {
                    Payment p = new Payment(e);
                    p.setPaymentDeadline(p.getPaymentDeadline().plusMonths(p.getPayPeriod()));
                    if (p.getPaymentPaidDate() != null) {
                        p.setPaymentPaidDate(null);
                        p.setItPaid(false);
                    }
                    return p;
                }).collect(Collectors.toList());
        MonthPayments monthPayments = new MonthPayments(monthID, list);
        y.getMonthPaymentsYearList().add(monthPayments);
        if (!updateXmlDataFile(file, y)) {
            System.out.println(APP_MSG.DATA_ERROR.getMessage());
        }
    }
}