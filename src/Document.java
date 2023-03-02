/*
----Документ
НомерДок
ДатаДок*/
public class Document {
    String number;
    String data;
    Client client;
    Auto auto;

    public Document() {
    }

    public Document(String number, String data) {
        this.number = number;
        this.data = data;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Auto getAuto() {
        return auto;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }
}
