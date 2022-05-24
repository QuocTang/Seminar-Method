package com.rfid.app;

public class Url {

    String urlInsert = "http://172.20.10.7/php/rfid_final/insert.php";
    String urlGetData = "http://172.20.10.7/php/rfid_final/getdata.php";
    String urlInsertReal = "http://172.20.10.7/php/rfid_final/insert_real.php";
    String urlDelete = "http://172.20.10.7/php/rfid_final/delete.php";

    public String Insert() {
        return urlInsert;
    }

    public String GetData() {
        return urlGetData;
    }

    public String InsertReal() {
        return urlInsertReal;
    }

    public String Delete() {
        return urlDelete;
    }
}