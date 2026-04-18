package modelo.turnos;

public class Turno {
    private String dia;
    private String horario; // e.g., "Mañana", "Tarde", "Noche" o "Completo"

    public Turno(String dia, String horario) {
        this.dia = dia;
        this.horario = horario;
    }

    public String getDia() { return dia; }
    public String getHorario() { return horario; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Turno)) return false;
        Turno otro = (Turno) obj;
        return this.dia.equals(otro.dia) && this.horario.equals(otro.horario);
    }
}
