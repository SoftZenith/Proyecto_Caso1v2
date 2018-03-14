package ittepic.edu.mx.proyecto_caso1;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listClientes;
    ArrayList<Cliente> lista;
    baseDatos bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        bd=new baseDatos(this,"proyecto",null,3);
        listClientes=(ListView) findViewById(R.id.listView);
        listClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actualizar(position);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregar();
            }
        });
        lista=buscarContacto();
        if(lista!=null){
            Adaptador adapter=new Adaptador(this,lista);
            listClientes.setAdapter(adapter);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void agregar(){
        Intent i= new Intent(this,Cliente_Agregar.class);
        i.putExtra("Origen","agregar");
        startActivity(i);
        finish();
    }
    private void actualizar(int i){
        Intent inten=new Intent(this,Cliente_Agregar.class);
        inten.putExtra("Origen","actualizar");
        ViewGroup row = (ViewGroup) listClientes.getChildAt(i);
        TextView txt_cliente = (TextView) row.findViewById(R.id.txt_id);
        inten.putExtra("ID",txt_cliente.getText());
        startActivity(inten);
    }

    public ArrayList<Cliente> buscarContacto() {
        Cliente c;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<Cliente> elementos=new ArrayList<Cliente>();
        String SQL = "SELECT C.NOMBRE, C.ID,O.DESCRIPCION FROM CLIENTE C, OBRA O WHERE C.ID=O.ID_CLIENTE Order by C.NOMBRE";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    c=new Cliente(resp.getString(0),resp.getString(2),resp.getInt(1));
                    elementos.add(c);
                }while(resp.moveToNext());
            }
            else{
                //mensaje("Atención","NO HAY REGISTROS");
            }
            base.close();
        } catch (SQLiteException ex) {
            mensaje("ERROR","No se puede ejecutar el select");
        }
        return elementos;
    }
    private void mensaje(String t, String s)
    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setTitle(t).setMessage(s).show();
    }
    private void recarga() {
        Intent actividad=new Intent(MainActivity.this,MainActivity.class);
        startActivity(actividad);
        finish();
    }
}
