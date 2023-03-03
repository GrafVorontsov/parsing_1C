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

    public static void main(String[] args) {
        String line = "";
        String splitBy = ",";
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> devidedList = new ArrayList<>();
        ArrayList<Document> documentList = new ArrayList<>();
        Document doc = null;
        Client client = null;
        Auto auto = null;
        ArrayList<String> xString = null;
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("/run/media/forever/Files/Install/s/DataExport/test.csv"));
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

        String name = "";
        String phone = "";
        for(Map.Entry<Integer, ArrayList<String>> item : map.entrySet()) {
            System.out.println(item.getKey());
            doc = new Document();
            client = new Client();
            for (String d: item.getValue()) {
                String[] filteredString = d.split(splitBy);
                if (filteredString[2].equals("НомерДок")){
                    doc.setNumber(filteredString[5]);
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("ДатаДок")){
                    doc.setData(filteredString[5]);
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[1].equals("999999") && filteredString[3].equals("Справочник.Контрагенты")){
                    client.setName(filteredString[2]);
                    System.out.println("  " + filteredString[2]);
                }
                if (filteredString[2].equals("Телефоны")){
                    client.setPhone(filteredString[5]);
                    doc.setClient(client);
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[1].equals("999999") && filteredString[3].equals("Справочник.Автомобили")){

                    System.out.println("  " + filteredString[2]);
                }
                if (filteredString[2].equals("НомерАвто")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("НомерШасси")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("НомерДвигателя")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("ГодВыпуска")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("Пробег")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("ТМЦ")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("Кво")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("ЦенаБезНДС")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("СуммаБезНДС")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("Исполнитель")){
                    System.out.println("  " + filteredString[5]);
                }
                if (filteredString[2].equals("Номер")){
                    System.out.println("  " + filteredString[5]);
                }

            }


            documentList.add(doc);
        }

        for (Document faktura: documentList) {
            System.out.println(faktura.getNumber() + " " + faktura.getData() + " " + faktura.getClient().getName()
                   + " " + faktura.getClient().getPhone());
            //System.out.println(faktura.getNumber() + " " + faktura.getData());
        }
    }
}