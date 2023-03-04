import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String line = "";
        String splitBy = ",";
        ArrayList<String> list = new ArrayList<>();
        //ArrayList<String> devidedList = new ArrayList<>();
        ArrayList<Document> documentList = new ArrayList<>();
        Document doc = null;
        Client client = null;
        //Parts parts = new Parts();
        Auto auto = null;
        //String tmc = "";
        //String number = "";
        //ArrayList<ArrayList<String>> partsList = new ArrayList<>();
        //ArrayList<Parts> partsArray = new ArrayList<>();
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

        String tovarline = "";
        Set<String> tovarlist = new LinkedHashSet<>();
        Set<String> uslugilist = new LinkedHashSet<>();
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("/home/sergio/Документы/tovar.csv"));
            while ((tovarline = br.readLine()) != null) {  //returns a Boolean value
                String[] splittovarline = tovarline.split(splitBy);
                if (splittovarline[3].equals("281")){
                    tovarlist.add(splittovarline[1].trim().replaceAll("\"", ""));
                } else {
                    uslugilist.add(splittovarline[1].trim().replaceAll("\"", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String clientline = "";
        ArrayList<String> clientlist = new ArrayList<>();
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("/home/sergio/Документы/clients.csv"));
            while ((clientline = br.readLine()) != null) {  //returns a Boolean value
                clientlist.add(clientline);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File csvFileSchet = new File("/home/sergio/Документы/schet.csv");
        FileWriter fileWriter = new FileWriter(csvFileSchet);

        File csvFile = new File("/home/sergio/Документы/parts.csv");
        FileWriter fileWriterTovar = new FileWriter(csvFile);
        File csvFileUslugi = new File("/home/sergio/Документы/works.csv");
        FileWriter fileWriterUslugi = new FileWriter(csvFileUslugi);

        Set<String> clientsList = new LinkedHashSet<>();

        for (Document schet: documentList) {
            String listString = String.join(", ", schet.getParts());
            //System.out.println(listString);
            String[] splitParts = listString.split("ТМЦ");
            int index = 1;
            while (index < splitParts.length) {
                String[] sParts = splitParts[index].split(", ");

                String nazvaTovara = sParts[0].trim().replaceAll("\"", "");
                String kolvoTovara = sParts[1];
                String cenaTovara = sParts[2];
                String summaTovara = sParts[3];
                String nomerTovara;
                if (isValidIndex(sParts, 4)) {
                    nomerTovara = sParts[4].replaceAll("\"", "").replaceAll("\\+", "")
                            .replaceAll("---", "").replaceAll("--", "");
                } else {
                    nomerTovara = " ";
                }

                for (String c: clientlist) {
                    String[] splitClient = c.split(splitBy);
                    String clientKod = splitClient[0];
                    String clientName = splitClient[1];
                    String clientPhone;
                    if (isValidIndex(splitClient, 2)) {
                        clientPhone = splitClient[2].replaceAll("\\+38", "");
                    } else {
                        clientPhone = " ";
                    }

                    if (clientName.equals(schet.getClient().getName())){
                        StringBuilder lineCSV = new StringBuilder();
                        lineCSV.append(schet.getNumber()).append(',').
                                append(clientKod).append(','). //код клиента
                                append(schet.getAuto().getVin()).append(',').
                                append(clientPhone).append(','). //телефон клиента
                                append(schet.getData()).append(',').
                                append(schet.getAuto().getKm()).append("\n");

                        clientsList.add(lineCSV.toString());
                    }
                }

                StringBuilder lineCSVUslugi = new StringBuilder();
                lineCSVUslugi.append(schet.getNumber()).append(',').
                        append(nazvaTovara).append(',').
                        append(nomerTovara).append(',').
                        append(cenaTovara).append(',').
                        append(kolvoTovara).append("\n");

                StringBuilder lineCSVTovar = new StringBuilder();
                lineCSVTovar.append(schet.getNumber()).append(',').
                        append(nomerTovara).append(',').
                        append(nazvaTovara).append(',').
                        append(cenaTovara).append(',').
                        append(kolvoTovara).append("\n");

                for (String u: uslugilist) {
                    if (u.equals(nazvaTovara)){
                        fileWriterUslugi.write(lineCSVUslugi.toString());
                    }
                }
                for (String t: tovarlist) {
                    if (t.equals(nazvaTovara)){
                        fileWriterTovar.write(lineCSVTovar.toString());                    }
                }

                index++;
            }
        }

        for (String clList: clientsList) {
            fileWriter.write(clList);
        }

        fileWriter.close();
        fileWriterTovar.close();
        fileWriterUslugi.close();
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