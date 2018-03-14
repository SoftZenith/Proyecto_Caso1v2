package ittepic.edu.mx.proyecto_caso1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


public class AdaptadorEmp extends ArrayAdapter{
    private Context context;
    private ArrayList<Empleado> datos;
    private int id=0;
    public AdaptadorEmp(Context context, ArrayList lista){
        super(context,R.layout.empleados,lista);
        datos=lista;
        this.context=context;
    }
    public AdaptadorEmp(Context context, ArrayList lista,int id){
        super(context,R.layout.empleados,lista);
        datos=lista;
        this.context=context;
        this.id=id;//Falta enviar el id de obra
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.empleados, null);


        CheckBox chk_emp=(CheckBox) item.findViewById(R.id.chk_emp);
        chk_emp.setText(datos.get(position).getNombre());
        if(id!=0){
            if(datos.get(position).getId_obra()==id){
                chk_emp.setChecked(true);
            }
        }

        //nombre.setText(c.getNombre());
        return item;
    }
}
