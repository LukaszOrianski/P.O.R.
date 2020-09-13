package menu;

import data.Payment;
import data.YearPayments;
import dictionary.APP_MSG;
import dictionary.ExtraMSG;
import utils.Utils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class Curiosities {
    private final Path dataPath;
    private final Path curPath;

    public Curiosities(Path path) {
        this.dataPath = path;
        this.curPath = Paths.get(dataPath.toString().replace(".xml", ".cur"));
    }

    public void showCuriosity() {
        List<Integer> integers = new ArrayList<>();
        if (curPath.toFile().exists()) {
            integers = file2List(curPath.toFile());
        }
        System.out.println(getDesrciption(draw(integers, getDescriptionsCount())+1));
        list2File(integers,curPath.toFile());
    }

    private int getDescriptionsCount() {
        int highestValue = 0;
        for (ExtraMSG element : ExtraMSG.values()) {
            if (element.getID() > highestValue) {
                highestValue = element.getID();
            }
        }
        return highestValue;
    }

    private double getMonthAvargeBill(){
        YearPayments y = Utils.loadXmlData(dataPath.toFile());
        if(y==null){
            return 0;
        }
        int monthsCount = y.getMonthPaymentsYearList().size();
        return y.getBillsStream().map(Payment::getAmountOfPayment)
                .mapToDouble(Double::doubleValue)
                .sum()/monthsCount;
    }

    private String getDesrciption(int myNumber) {
        for (ExtraMSG element : ExtraMSG.values()) {
            if (element.getID() == myNumber) {
                return String.format(element.getMessage(),getMonthAvargeBill()/element.getPrice());
            }
        }
        return APP_MSG.DATA_ERROR.getMessage();
    }


    private void list2File(List<Integer> list, File myFile) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(myFile))) {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> file2List(File myFile) {
        List<Integer> a = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(myFile))) {
            a = (ArrayList<Integer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return a;
    }

    private int draw(List<Integer> originalList, int descCount) {
        Random random = new Random();
        int randomInt;
        if (originalList.size() >= descCount) {
            originalList.clear();
        }
        randomInt = random.nextInt(descCount);
        while (originalList.contains(randomInt)) {
            randomInt = random.nextInt(descCount);
        }
        originalList.add(randomInt);
        return originalList.get(originalList.size() - 1);
    }
}
