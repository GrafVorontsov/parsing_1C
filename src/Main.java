import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        ArrayList<Document> documentList = new ArrayList<>();
        Document doc = null;
        Client client = null;
        Auto auto = null;
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("/run/media/forever/Files/Install/s/DataExport/test.csv"));
            while ((line = br.readLine()) != null) {  //returns a Boolean value
                list.add(line);
                //String[] str = line.split(splitBy);    // use comma as separator

                
                //System.out.println("Employee [First Name=" + employee[0] + ", Last Name=" + employee[1] + ", Designation=" + employee[2] + ", Contact=" + employee[3] + ", Salary= " + employee[4] + ", City= " + employee[5] +"]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String l: list) {
            String[] strings = l.split(splitBy);    // use comma as separator
            if (strings[1].equals("0")){
                //System.out.println(strings[2]);
                doc = new Document();
                for (String e: list) {
                    String[] stringsInner = e.split(splitBy);    // use comma as separator

                    if (stringsInner[2].equals("НомерДок")){
                        doc.setNumber(stringsInner[5]);
                        //System.out.println(strings[5]);
                    }
                    if (stringsInner[2].equals("ДатаДок")){
                        doc.setData(stringsInner[5]);
                        //System.out.println(strings[5]);
                    }

                    //client
                    if (stringsInner[1].equals("4") && stringsInner[2].equals("Код")) {
                        client = new Client();
                        client.setKod(stringsInner[5]);

                    }
                    if (stringsInner[1].equals("4") && stringsInner[2].equals("Наименование")) {
                        client.setName(stringsInner[5]);

                    }
                    if (stringsInner[1].equals("4") && stringsInner[2].equals("Телефоны")) {
                        client.setPhone(stringsInner[5]);
                        doc.setClient(client);
                    }

                    //auto
                    if (stringsInner[1].equals("9") && stringsInner[2].equals("Наименование")) {
                        auto = new Auto();
                        auto.setName(stringsInner[5]);
                        doc.setAuto(auto);
                    }


                    //System.out.println("   " + e);
                }



                //System.out.println(strings[2]);
            }
            documentList.add(doc);
        }

        for (Document dcs: documentList) {
            System.out.println(dcs.number + " " + dcs.data + " " +
                    dcs.getClient().kod + " " + dcs.getClient().name + " " + dcs.getClient().phone + " " +
                    dcs.getAuto().name);
            //System.out.println(dcs.number + " " + dcs.data);
        }
    }
}