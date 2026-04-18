package modelo.cafeteria;

import java.util.List;

public class Pasteleria extends ProductoCafeteria {
    private List<String> alergenos;

    public Pasteleria(String nombre, double precio, List<String> alergenos) {
        super(nombre, precio);
        this.alergenos = alergenos;
    }

    public List<String> getAlergenos() { return alergenos; }
}
