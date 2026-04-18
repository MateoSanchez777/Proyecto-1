package modelo.cafeteria;

public class Bebida extends ProductoCafeteria {
    private boolean esAlcoholica;
    private boolean esCaliente;

    public Bebida(String nombre, double precio, boolean esAlcoholica, boolean esCaliente) {
        super(nombre, precio);
        this.esAlcoholica = esAlcoholica;
        this.esCaliente = esCaliente;
    }

    public boolean isEsAlcoholica() { return esAlcoholica; }
    public boolean isEsCaliente() { return esCaliente; }
}
