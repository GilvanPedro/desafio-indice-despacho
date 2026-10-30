public class Pedido {
    private final String id;
    private final int urgencia;   // 1 a 10, 10 = mais urgente
    private final long chegada;   // ordem de chegada (menor = chegou antes)

    public Pedido(String id, int urgencia, long chegada) {
        if (urgencia < 1 || urgencia > 10) {
            throw new IllegalArgumentException("Urgencia deve estar entre 1 e 10: " + urgencia);
        }
        this.id = id;
        this.urgencia = urgencia;
        this.chegada = chegada;
    }

    public String getId() { return id; }
    public int getUrgencia() { return urgencia; }
    public long getChegada() { return chegada; }

    //Retorna true se o pedido verificado deve ser despachado ANTES do outro.
    public boolean temPrioridadeSobre(Pedido outro) {
        if (this.urgencia != outro.urgencia) {
            return this.urgencia > outro.urgencia;
        }
        return this.chegada < outro.chegada;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + urgencia + ")";
    }
}