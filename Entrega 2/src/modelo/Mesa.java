package modelo;

public class Mesa {
    private int numero;
    private int numPersonas;
    private boolean hayMenores5;
    private boolean hayMenores18;

    public Mesa(int numero, int numPersonas, boolean hayMenores5, boolean hayMenores18) {
        this.numero = numero;
        this.numPersonas = numPersonas;
        this.hayMenores5 = hayMenores5;
        this.hayMenores18 = hayMenores18;
    }

    public int getNumero() { return numero; }
    public int getNumPersonas() { return numPersonas; }
    public boolean isHayMenores5() { return hayMenores5; }
    public boolean isHayMenores18() { return hayMenores18; }

    public void setNumPersonas(int numPersonas) { this.numPersonas = numPersonas; }
    public void setHayMenores5(boolean hayMenores5) { this.hayMenores5 = hayMenores5; }
    public void setHayMenores18(boolean hayMenores18) { this.hayMenores18 = hayMenores18; }
}
