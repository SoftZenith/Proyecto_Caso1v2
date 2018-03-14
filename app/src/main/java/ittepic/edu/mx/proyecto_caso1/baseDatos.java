package ittepic.edu.mx.proyecto_caso1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class baseDatos extends SQLiteOpenHelper{
    String error;
    public baseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE CLIENTE (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR(225) NOT NULL," +
                    "DIRECCION VARCHAR(225) NOT NULL, CEL VARCHAR(10) NOT NULL, MAIL VARCHAR(100) NOT NULL)");
            db.execSQL("CREATE TABLE TRABAJADOR (ID_OBRA INTEGER REFERENCES OBRA(ID_OBRA), NOMBRE VARCHAR(225) NOT NULL," +
                    "ACTIVIDAD VARCHAR(225) NOT NULL, FECHA_INICIO VARCHAR(20) NOT NULL, FECHA_FIN VARCHAR(20) NOT NULL," +
                    "CELULAR VARCHAR(10) NOT NULL, CANTIDAD_OBRA VARCHAR(100) NOT NULL,PAGO INTEGER NOT NULL)");
            db.execSQL("CREATE TABLE OBRA (DESCRIPCION VARCHAR(500) NOT NULL, MONTO INTEGER NOT NULL, FINALIZADA INTEGER NOT NULL," +
                    "ID_CLIENTE INTEGER REFERENCES CLIENTE(ID), ID_OBRA INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT)");

        }catch(SQLiteException ex){
            error=ex.getMessage();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TRABAJADOR");
        db.execSQL("CREATE TABLE TRABAJADOR (ID_OBRA INTEGER REFERENCES OBRA(ID_OBRA), NOMBRE VARCHAR(225) NOT NULL," +
                "ACTIVIDAD VARCHAR(225) NOT NULL, FECHA_INICIO VARCHAR(20) NOT NULL, FECHA_FIN VARCHAR(20) NOT NULL," +
                "CELULAR VARCHAR(10) NOT NULL, CANTIDAD_OBRA VARCHAR(100) NOT NULL,PAGO INTEGER NOT NULL)");
    }
}
