/*
 * Integrantes do grupo:
 *  - Gilvan Pedro de Castro Melo Campos (2731534)
 *  - João Paulo Marques Ferreira (2759688)
 *  - João Pedro Martini Denani (2758712)
 *  - Sidney Emanuel Barbosa de Oliveira (2759803)
 */

import java.util.Arrays;
public class FilaDespacho {

    private Pedido[] heap;
    private int tamanho;

    public FilaDespacho(Pedido[] loteOrdenadoDecrescente) {
        this.tamanho = loteOrdenadoDecrescente.length;
        int capacidade = Math.max(16, tamanho * 2);
        this.heap = Arrays.copyOf(loteOrdenadoDecrescente, capacidade);

        if (!propriedadeDeHeapValida()) {
            throw new IllegalArgumentException(
                "O vetor recebido nao e um heap de maximo. Rode o heapsort antes.");
        }
    }

    public boolean estaVazia() { return tamanho == 0; }
    public int tamanho() { return tamanho; }

    public Pedido espiar() {
        if (estaVazia()) throw new IllegalStateException("Fila vazia");
        return heap[0];
    }

    //Insere um novo pedido urgente sem reprocessar o lote.
    public void inserir(Pedido p) {
        if (tamanho == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[tamanho] = p;
        subir(tamanho);
        tamanho++;
    }

    // Remove e devolve o pedido de maior prioridade.
    public Pedido removerMaximo() {
        if (estaVazia()) throw new IllegalStateException("Fila vazia");
        Pedido topo = heap[0];
        tamanho--;
        heap[0] = heap[tamanho];
        heap[tamanho] = null;
        if (tamanho > 0) {
            descer(0);
        }
        return topo;
    }

    private void subir(int i) {
        while (i > 0) {
            int pai = (i - 1) / 2;
            if (!heap[i].temPrioridadeSobre(heap[pai])) {
                return;
            }
            trocar(i, pai);
            i = pai;
        }
    }

    private void descer(int i) {
        while (true) {
            int esq = 2 * i + 1;
            int dir = 2 * i + 2;
            int maior = i;
            if (esq < tamanho && heap[esq].temPrioridadeSobre(heap[maior])) {
                maior = esq;
            }
            if (dir < tamanho && heap[dir].temPrioridadeSobre(heap[maior])) {
                maior = dir;
            }
            if (maior == i) {
                return;
            }
            trocar(i, maior);
            i = maior;
        }
    }

    private void trocar(int a, int b) {
        Pedido t = heap[a];
        heap[a] = heap[b];
        heap[b] = t;
    }

    // Confere se nenhum filho tem prioridade sobre o pai.
    public boolean propriedadeDeHeapValida() {
        for (int i = 1; i < tamanho; i++) {
            int pai = (i - 1) / 2;
            if (heap[i].temPrioridadeSobre(heap[pai])) {
                return false;
            }
        }
        return true;
    }

    // Visualizacao do array interno (ordem de heap, nao de despacho).
    public String estadoInterno() {
        return Arrays.toString(Arrays.copyOf(heap, tamanho));
    }
}
