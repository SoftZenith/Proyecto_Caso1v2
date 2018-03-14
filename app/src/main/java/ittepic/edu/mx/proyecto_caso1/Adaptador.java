package ittepic.edu.mx.proyecto_caso1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class Adaptador extends ArrayAdapter{
    private Context context;
    private ArrayList<Cliente> datos;
    public Adaptador(Context context, ArrayList lista){
        super(context,R.layout.elementos,lista);
        datos=lista;
        this.context=context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.elementos, null);

        TextView cliente = (TextView) item.findViewById(R.id.txt_cliente);
        cliente.setText(datos.get(position).getCliente());


        TextView descrip = (TextView) item.findViewById(R.id.txt_desc);
        descrip.setText(datos.get(position).getDescripcion());

        TextView id=(TextView) item.findViewById(R.id.txt_id);
        id.setText(""+datos.get(position).getID());

        //nombre.setText(c.getNombre());
        return item;
    }
}
