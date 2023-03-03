import java.io.*;
import java.util.*;

public class Main {
    /*
    ----Документ
    НомерДок
    ДатаДок
    -----Клиент
    Код
    Наименование
    Телефоны
    -----Авто
    Наименование
    НомерАвто
    НомерШасси
    НомерДвигателя
    ГодВыпуска
    Пробег
    -----ТМЦ
    ТМЦ
    Кво
    ЦенаБезНДС
    СуммаБезНДС
    Исполнитель
    Номер
     */

    public static void main(String[] args) throws IOException {
        String line = "";
        String splitBy = ",";
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> devidedList = new ArrayList<>();
        ArrayList<Document> documentList = new ArrayList<>();
        Document doc = null;
        Client client = null;
        Parts parts = new Parts();
        Auto auto = null;
        String tmc = "";
        String number = "";
        ArrayList<ArrayList<String>> partsList = new ArrayList<>();
        ArrayList<Parts> partsArray = new ArrayList<>();
        ArrayList<String> xString = null;
        ArrayList<String> docxString = null;
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        HashMap<Integer, ArrayList<String>> docmap = new HashMap<>();
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("/home/sergio/Документы/final.csv"));
            while ((line = br.readLine()) != null) {  //returns a Boolean value
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 0;
        for (String s: list ) {
            String[] str = s.split(splitBy);
            if (str[1].equals("0")) {
                i++;
                xString = new ArrayList<>();
                map.put(i, xString);
            }
            if (!str[1].equals("0")){
                xString.add(s);
            }
        }


        for(Map.Entry<Integer, ArrayList<String>> item : map.entrySet()) {
            //System.out.println(item.getKey() + " " + item.getValue());
            doc = new Document();
            int x = 0;
            docxString = new ArrayList<>();

            for (String d: item.getValue()) {
                String[] filteredString = d.split(splitBy);

                if (filteredString[2].equals("НомерДок")){
                    doc.setNumber(filteredString[5]);
                }else if (filteredString[2].equals("ДатаДок")){
                    doc.setData(filteredString[5]);
                }else if (filteredString[2].equals("ОтборЗаказаКонтрагент")){
                    client = new Client();
                    client.setName(filteredString[5]);
                    doc.setClient(client);
                }else if (filteredString[2].equals("Автомобиль")){
                    auto = new Auto();
                    auto.setVin(filteredString[5]);
                } else if (filteredString[2].equals("Пробег")){
                    auto.setKm(filteredString[5]);
                    doc.setAuto(auto);
                } else {
                    if (filteredString[2].equals("ТМЦ")) {
                        docxString.add("ТМЦ " + filteredString[5]);
                    } else {
                        docxString.add(filteredString[5]);
                    }
                    //System.out.println(filteredString[5]);
                }
            }
            doc.setParts(docxString);
            documentList.add(doc);
        }

        /*for (Document faktura: documentList) {
            System.out.println(faktura.getNumber() + " " + faktura.getData() + " " + faktura.getClient().getName()
                    + " " + faktura.getAuto().getVin()  + " " + faktura.getAuto().getKm()
            + " " + faktura.getParts());
            //System.out.println(faktura.getNumber() + " " + faktura.getData());
        }*/


        File csvFile = new File("/home/sergio/Документы/employees.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        for (Document schet: documentList) {
            String listString = String.join(", ", schet.getParts());
            //System.out.println(listString);
            String[] splitParts = listString.split("ТМЦ");
            int index = 1;
            while (index < splitParts.length) {
                String[] sParts = splitParts[index].split(", ");

                String nm;
                if (isValidIndex(sParts, 4)) {
                    nm = sParts[4];
                } else {
                    nm = " ";
                }

                //System.out.println(schet.getNumber() + " " + schet.getData() + " " + schet.getClient().getName()
                //        + " " + schet.getAuto().getVin()  + " " + schet.getAuto().getKm() + " " + splitParts[index]);
                System.out.println(schet.getNumber() + " " + schet.getData() + " " + schet.getClient().getName()
                        + " " + schet.getAuto().getVin()  + " " + schet.getAuto().getKm() + " " + sParts[0] + " " + sParts[1]
                        + " " + sParts[2] + " " + sParts[3] + " " + nm);
                //System.out.println();

                StringBuilder lineCSV = new StringBuilder();
                lineCSV.append(schet.getNumber()).append(',').
                        append(schet.getData()).append(',').
                        append(schet.getClient().getName()).append(',').
                        append(schet.getAuto().getVin()).append(',').
                        append(schet.getAuto().getKm()).append(',').
                        append(sParts[0].trim()).append(',').
                        append(sParts[1]).append(',').
                        append(sParts[2]).append(',').
                        append(sParts[3]).append(',').
                        append(nm).append("\n");

                fileWriter.write(lineCSV.toString());
                index++;
            }
        }
        fileWriter.close();
    }
    public static boolean isValidIndex(String[] arr, int index) {
        try {
            Objects.checkIndex(index, arr.length);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}