package ittepic.edu.mx.proyecto_caso1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Empleados extends AppCompatActivity {

    EditText name,act,ini,fin,cel,cant,pago;
    Button btn_aceptar;
    baseDatos bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);
        bd = new baseDatos(this, "proyecto", null, 3);
        name=(EditText)findViewById(R.id.edt_name);
        act=(EditText)findViewById(R.id.edt_act);
        ini=(EditText)findViewById(R.id.edt_ini);
        fin=(EditText)findViewById(R.id.edt_fin);
        cel=(EditText)findViewById(R.id.edt_phone);
        cant=(EditText)findViewById(R.id.edt_cant);
        pago=(EditText)findViewById(R.id.edt_pago);
        btn_aceptar= (Button)findViewById(R.id.button);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertar();
            }
        });
    }
    private void insertar(){
        String SQL="INSERT INTO TRABAJADOR (NOMBRE, ACTIVIDAD, CELULAR,FECHA_INICIO, FECHA_FIN, CANTIDAD_OBRA, PAGO, ID_OBRA) " +
                "VALUES ('"+name.getText().toString()+"', '"+act.getText().toString()+"', " +
                "'"+cel.getText().toString()+"', '"+ini.getText().toString()+ "', '"+fin.getText().toString()+"', " +
                "'"+cant.getText().toString()+"', "+Integer.parseInt(pago.getText().toString())+","+ 1+")";
        try{
            SQLiteDatabase baseDat=bd.getWritableDatabase();
            baseDat.execSQL(SQL);
            baseDat.close();
            finish();

        }catch(SQLiteException ex){
            mensaje("ERROR",ex.getMessage());
        }
    }
    private void mensaje(String t,String s){
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setTitle(t).setMessage(s).show();
    }
}
