package ittepic.edu.mx.proyecto_caso1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Cliente_Agregar extends AppCompatActivity {
    Button agregar,agregarEmp;
    EditText nombre,direccion,cel,mail,descripcion,monto;
    baseDatos bd;
    String name,dir,tel,email,desc,costo;
    CheckBox fin;
    int finalizado;
    ArrayList lista;
    ListView listEmpl;
    String origen;
    int ID_Obra=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente__agregar);
        origen=getIntent().getStringExtra("Origen");
        agregar=(Button) findViewById(R.id.btn_agregar);
        nombre=(EditText) findViewById(R.id.edt_nombre);
        bd = new baseDatos(this, "proyecto", null, 3);
        direccion=(EditText)findViewById(R.id.edt_direccion);
        cel=(EditText)findViewById(R.id.edt_tel);
        mail=(EditText)findViewById(R.id.edt_mail);
        descripcion=(EditText)findViewById(R.id.edt_desc);
        monto=(EditText)findViewById(R.id.edt_monto);
        fin=(CheckBox) findViewById(R.id.checkBox);
        listEmpl= (ListView)findViewById(R.id.lst_empleados);
        if(fin.isChecked()){
            finalizado=1;
        }else finalizado=0;
        agregarEmp=(Button)findViewById(R.id.btn_agregarEmp);
        if(origen.equals("actualizar")){
            llenarCampo(getIntent().getStringExtra("ID"));
        }
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(origen.equals("agregar")){
                name=nombre.getText().toString();
                dir=direccion.getText().toString();
                tel=cel.getText().toString();
                email=mail.getText().toString();
                desc=descripcion.getText().toString();
                costo=monto.getText().toString();
                insertar();
                }
                else actualiza();//Falta agregar update
            }
        });
        agregarEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarEmpleado();
            }
        });

        if(origen.equals("agregar")){
            lista=buscarContacto();
            if(lista!=null){
                AdaptadorEmp adapter=new AdaptadorEmp(this,lista);
                listEmpl.setAdapter(adapter);}
            }
        else{
            lista=buscarContacto(ID_Obra);
            if(lista!=null){
                AdaptadorEmp adapter=new AdaptadorEmp(this,lista,ID_Obra);
                listEmpl.setAdapter(adapter);
            }

        }
        //mensaje("Elementos en lista",""+listEmpl.getAdapter().getCount());

    }
    public void llenarCampo(String id){
        SQLiteDatabase baseDat=bd.getWritableDatabase();
        int id_cliente=Integer.parseInt(id);
        String SQL= "SELECT c.NOMBRE,c.DIRECCION,c.CEL, c.MAIL FROM CLIENTE c WHERE c.ID="+id_cliente;
        Cursor cursor= baseDat.rawQuery(SQL,null);
        if (cursor.moveToFirst()) {
            do {
                nombre.setText(cursor.getString(0));
                direccion.setText(cursor.getString(1));
                cel.setText(cursor.getString(2));
                mail.setText(cursor.getString(3));

            } while (cursor.moveToNext());
        }
        SQL="Select O.DESCRIPCION,O.FINALIZADA,O.MONTO,O.ID_OBRA FROM OBRA O WHERE O.ID_CLIENTE="+id_cliente;
        cursor= baseDat.rawQuery(SQL,null);
        if(cursor.moveToFirst()){
            do{
                descripcion.setText(cursor.getString(0));
                monto.setText(cursor.getString(2));
                if(cursor.getInt(1)==1){
                    fin.setChecked(true);
                }
                else
                    fin.setChecked(false);
                ID_Obra=cursor.getInt(3);
            }while(cursor.moveToNext());
        }
        baseDat.close();

    }
    public void insertarEmpleado(){
        Intent i=new Intent(this,Empleados.class);
        startActivity(i);
    }
    private void insertar(){
        String SQL="INSERT INTO CLIENTE (NOMBRE,DIRECCION,CEL,MAIL) VALUES ('"+name+"', '"+dir+"', '"+tel+"', '"+email+ "')";
        try{
            SQLiteDatabase baseDat=bd.getWritableDatabase();
            baseDat.execSQL(SQL);
            Cursor cursor= baseDat.rawQuery("SELECT MAX(ID) FROM CLIENTE",null);
            int id = 0;
            if (cursor.moveToFirst()) {
                do {
                    id = Integer.parseInt(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
            baseDat.execSQL("INSERT INTO OBRA (DESCRIPCION, MONTO, FINALIZADA, ID_CLIENTE) VALUES ('"+desc+"', "+Integer.parseInt(costo)+", "
                    +finalizado+","+id+")");
            int count = listEmpl.getAdapter().getCount();
            cursor= baseDat.rawQuery("SELECT MAX(ID_OBRA) FROM OBRA",null);
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
            for (int i = 0; i < count; i++)
            {
                ViewGroup row = (ViewGroup) listEmpl.getChildAt(i);
                if(row!=null){
                    CheckBox tvTest = (CheckBox) row.findViewById(R.id.chk_emp);
                    String nombre = tvTest.getText().toString();
                    if (tvTest.isChecked())
                    {
                        baseDat.execSQL("UPDATE TRABAJADOR SET ID_OBRA= "+id+" where NOMBRE='"+nombre+"'");
                    }
                }

            }
            baseDat.close();
            regresar();

        }catch(SQLiteException ex){
            mensaje("ERROR",ex.getMessage());
        }
    }
    private void mensaje(String t,String s){
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setTitle(t).setMessage(s).show();
    }
    private void actualiza(){
        SQLiteDatabase baseDat=bd.getWritableDatabase();
        baseDat.execSQL("UPDATE CLIENTE SET NOMBRE= '"+nombre.getText().toString()+"'," +
                "DIRECCION= '"+direccion.getText().toString() +"',"+
                "CEL= '"+cel.getText().toString()+"',"+
                "MAIL='"+mail.getText().toString()+"'"+
                "where ID='"+Integer.parseInt(getIntent().getStringExtra("ID"))+"'");
        if(fin.isChecked()){
            finalizado=1;
        }else finalizado=0;
        baseDat.execSQL("UPDATE OBRA SET DESCRIPCION = '" + descripcion.getText().toString()+"',"+
                "MONTO = "+Integer.parseInt(monto.getText().toString())+", " +
                "FINALIZADA ="+finalizado +" "+
                "WHERE ID_OBRA = "+ID_Obra);
        int count = listEmpl.getAdapter().getCount();
        for (int i = 0; i < count; i++)
        {

            View row = (View) listEmpl.getChildAt(i);
            if(row!=null) {
                CheckBox tvTest = (CheckBox) row.findViewById(R.id.chk_emp);
                String nombre = tvTest.getText().toString();
                if (tvTest.isChecked()) {
                    baseDat.execSQL("UPDATE TRABAJADOR SET ID_OBRA= " + ID_Obra + " where NOMBRE='" + nombre + "'");
                } else
                    baseDat.execSQL("UPDATE TRABAJADOR SET ID_OBRA= " + 1 + " where NOMBRE='" + nombre + "'");
            }
        }
        baseDat.close();
        regresar();
    }
    public void regresar(){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);

    }
    public ArrayList<Empleado> buscarContacto() {
        Empleado c;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<Empleado> elementos=new ArrayList<Empleado>();
        String SQL = "SELECT NOMBRE, ID_OBRA FROM TRABAJADOR WHERE ID_OBRA=1 Order by ID_OBRA";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    c=new Empleado(resp.getString(0),resp.getInt(1));
                    elementos.add(c);
                }while(resp.moveToNext());
            }
            else{
                //mensaje("Atención","NO HAY REGISTROS");
            }
            base.close();
        } catch (SQLiteException ex) {
            //mensaje("ERROR","No se puede ejecutar el select");
        }
        return elementos;
    }
    public ArrayList<Empleado> buscarContacto(int id) {//Modificar clase de
        Empleado c;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<Empleado> elementos=new ArrayList<Empleado>();
        String SQL = "SELECT NOMBRE, ID_OBRA FROM TRABAJADOR WHERE ID_OBRA="+id+" OR ID_OBRA=1 Order by ID_OBRA";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    c=new Empleado(resp.getString(0),resp.getInt(1));
                    elementos.add(c);
                }while(resp.moveToNext());
            }
            else{
                //mensaje("Atención","NO HAY REGISTROS");
            }
            base.close();
        } catch (SQLiteException ex) {
            //mensaje("ERROR","No se puede ejecutar el select");
        }
        return elementos;
    }
}
