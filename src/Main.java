import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String line;
        String splitBy = ",";
        ArrayList<String> list = new ArrayList<>(); //массив строк из файла со Счетами-фактурами
        ArrayList<Document> documentList = new ArrayList<>();
        Document doc;
        Client client;
        Auto auto = null;
        ArrayList<String> xString = null;
        ArrayList<String> docxString;
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        try {
            //Парсим файл со Счетами-фактурами, который выгрузили с 1С
            BufferedReader br = new BufferedReader(new FileReader("/home/sergio/Документы/final.csv"));
            while ((line = br.readLine()) != null) {  //returns a Boolean value
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //пробегаем по массиву со Счетами-фактурами
        int i = 0;
        for (String s: list ) {
            String[] str = s.split(splitBy);
            //если в строке есть 0 то добавляем новый элемент в map
            if (str[1].equals("0")) {
                i++; //увеличиваем счётчик ключей map
                xString = new ArrayList<>();
                map.put(i, xString); //добавляем новый элемент в map
            }
            if (!str[1].equals("0")){
                assert xString != null;
                xString.add(s); //собираем массив строк из истальных строк
            }
        }

        //распарсиваем строки из map
        for(Map.Entry<Integer, ArrayList<String>> item : map.entrySet()) {
            doc = new Document(); //создали новый документ (отдельный счёт-фактура)
            docxString = new ArrayList<>();

            for (String d: item.getValue()) {
                String[] filteredString = d.split(splitBy);

                switch (filteredString[2]) {
                    case "НомерДок":
                        doc.setNumber(filteredString[5]);
                        break;
                    case "ДатаДок":
                        doc.setData(filteredString[5]);
                        break;
                    case "ОтборЗаказаКонтрагент":
                        client = new Client();
                        client.setName(filteredString[5]);
                        doc.setClient(client);
                        break;
                    case "Автомобиль":
                        auto = new Auto();
                        auto.setVin(filteredString[5]);
                        break;
                    case "Пробег":
                        assert auto != null;
                        auto.setKm(filteredString[5]);
                        doc.setAuto(auto);
                        break;
                    default:  //собираем массив из всех строк ТМЦ конкретного счёта
                        if (filteredString[2].equals("ТМЦ")) {
                            docxString.add("ТМЦ " + filteredString[5]);
                        } else {
                            docxString.add(filteredString[5]);
                        }
                        break;
                }
            }
            doc.setParts(docxString);
            documentList.add(doc);
        }

        String tovarline;
        Set<String> tovarlist = new LinkedHashSet<>();
        Set<String> uslugilist = new LinkedHashSet<>();
        try {
            //парсим файл списка товаров и услуг
            BufferedReader br = new BufferedReader(new FileReader("/home/sergio/Документы/tovar.csv"));
            while ((tovarline = br.readLine()) != null) {  //returns a Boolean value
                String[] splittovarline = tovarline.split(splitBy);
                if (splittovarline[3].equals("281")){ // если 281 значит товар
                    tovarlist.add(splittovarline[1].trim().replaceAll("\"", ""));
                } else { //если нет то услуги
                    uslugilist.add(splittovarline[1].trim().replaceAll("\"", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String clientline;
        ArrayList<String> clientlist = new ArrayList<>();
        try {
            //парсим список клиентов
            BufferedReader br = new BufferedReader(new FileReader("/home/sergio/Документы/clients.csv"));
            while ((clientline = br.readLine()) != null) {  //returns a Boolean value
                clientlist.add(clientline);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File csvFileSchet = new File("/home/sergio/Документы/schet.csv"); //файл с готовыми счетами
        FileWriter fileWriter = new FileWriter(csvFileSchet);

        File csvFile = new File("/home/sergio/Документы/parts.csv"); //файл с запчастями
        FileWriter fileWriterTovar = new FileWriter(csvFile);
        File csvFileUslugi = new File("/home/sergio/Документы/works.csv"); //файл с работами
        FileWriter fileWriterUslugi = new FileWriter(csvFileUslugi);

        Set<String> clientsList = new LinkedHashSet<>();

        for (Document schet: documentList) {
            String listString = String.join(", ", schet.getParts());
            String[] splitParts = listString.split("ТМЦ");
            int index = 1;
            while (index < splitParts.length) {
                String[] sParts = splitParts[index].split(", ");

                String nazvaTovara = sParts[0].trim().replaceAll("\"", "");
                String kolvoTovara = sParts[1];
                String cenaTovara = sParts[2];
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
                        lineCSV.append(schet.getNumber()).append(','). //номер счёта
                                append(clientKod).append(','). //код клиента
                                append(schet.getAuto().getVin()).append(','). //vin код авто
                                append(clientPhone).append(','). //телефон клиента
                                append(schet.getData()).append(','). //дата счёта
                                append(schet.getAuto().getKm()).append("\n"); //пробег

                        clientsList.add(lineCSV.toString());
                    }
                }

                StringBuilder lineCSVUslugi = new StringBuilder();
                lineCSVUslugi.append(schet.getNumber()).append(','). //номер счёта
                        append(nazvaTovara).append(','). //название услуг
                        append(nomerTovara).append(','). //механик, который делал
                        append(cenaTovara).append(','). //цена услуг
                        append(kolvoTovara).append("\n"); //количество

                StringBuilder lineCSVTovar = new StringBuilder();
                lineCSVTovar.append(schet.getNumber()).append(','). //номер счёта
                        append(nomerTovara).append(','). //номер детали
                        append(nazvaTovara).append(','). //название товара
                        append(cenaTovara).append(','). //цена товара
                        append(kolvoTovara).append("\n"); //количество

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
    //проверка на наличие индекса в массиве
    public static boolean isValidIndex(String[] arr, int index) {
        try {
            Objects.checkIndex(index, arr.length);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}