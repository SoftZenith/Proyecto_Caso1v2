package ittepic.edu.mx.proyecto_caso1;


public class Empleado {
    String nombre;
    int id_obra;
    Empleado(String nombre,int id_obra){
        this.id_obra=id_obra;
        this.nombre=nombre;
    }
    public String getNombre(){
        return nombre;
    }
    public int getId_obra(){
        return id_obra;
    }
}
