/*
 * Desafio: Indice e Despacho Integrados - Estrutura de Dados II
 * Integrantes do grupo:
 *   - [Nome completo 1] - [Matricula]
 *   - [Nome completo 2] - [Matricula]
 *   - [Nome completo 3] - [Matricula]
 */

/**
 * Parte 2: fechamento do lote com heapsort.
 *
 * Objetivo: ordenar o vetor de pedidos por prioridade DECRESCENTE
 * (mais urgente na posicao 0).
 *
 * Truque: o heapsort classico usa um heap de maximo e gera ordem
 * crescente (o maior vai para o fim). Como queremos ordem decrescente,
 * usamos um heap de MINIMO de prioridade: a cada passo o pedido menos
 * prioritario sai da raiz e vai para o fim do vetor. No final, o vetor
 * fica do mais prioritario para o menos prioritario.
 *
 * Bonus que conecta com a Parte 3: um vetor ordenado em ordem
 * decrescente JA E um heap de maximo valido, porque para todo i,
 * a[i] vem antes de a[2i+1] e a[2i+2] na ordenacao. Ou seja, o
 * resultado do heapsort vira a fila de despacho sem reconstrucao.
 *
 * Complexidade: O(n log n) tempo, O(1) memoria extra (in-place).
 */
public final class HeapsortLote {

    private HeapsortLote() { }

    public static void ordenarPorPrioridadeDecrescente(Pedido[] v) {
        int n = v.length;

        // 1) Constroi o heap de minimo de prioridade (bottom-up, O(n)).
        for (int i = n / 2 - 1; i >= 0; i--) {
            descer(v, i, n);
        }

        // 2) Extrai o menos prioritario e joga para o fim, n-1 vezes.
        for (int fim = n - 1; fim > 0; fim--) {
            trocar(v, 0, fim);
            descer(v, 0, fim);
        }
    }

    /**
     * Sift-down no heap de minimo de prioridade: o pai precisa ser
     * MENOS prioritario (ou igual) que os filhos.
     */
    private static void descer(Pedido[] v, int i, int tamanho) {
        while (true) {
            int esq = 2 * i + 1;
            int dir = 2 * i + 2;
            int menor = i;

            if (esq < tamanho && v[menor].temPrioridadeSobre(v[esq])) {
                menor = esq;
            }
            if (dir < tamanho && v[menor].temPrioridadeSobre(v[dir])) {
                menor = dir;
            }
            if (menor == i) {
                return;
            }
            trocar(v, i, menor);
            i = menor;
        }
    }

    private static void trocar(Pedido[] v, int a, int b) {
        Pedido t = v[a];
        v[a] = v[b];
        v[b] = t;
    }
}
