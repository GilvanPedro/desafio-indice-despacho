/*
 * Desafio: Indice e Despacho Integrados - Estrutura de Dados II
 * Integrantes do grupo:
 *   - Gilvan Pedro de Castro Melo Campos (2731534)
 *   - João Paulo Marques Ferreira (2759688)
 *   - João Pedro Martini Denani (2758712)
 *   - Sidney Emanuel Barbosa de Oliveira (2759803)
 */

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

    /**
     * Retorna true se este pedido deve ser despachado ANTES do outro.
     * Regra: maior urgencia primeiro; empate -> quem chegou antes.
     */
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
