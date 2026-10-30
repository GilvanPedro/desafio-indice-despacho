public final class HeapsortLote {

    private HeapsortLote() { }

    public static void ordenarPorPrioridadeDecrescente(Pedido[] v) {
        int n = v.length;

        //Responsável por construir o heap de minimo de prioridade.
        for (int i = n / 2 - 1; i >= 0; i--) {
            descer(v, i, n);
        }

        //Responsável por extrair o menor prioritario e joga para o fim.
        for (int fim = n - 1; fim > 0; fim--) {
            trocar(v, 0, fim);
            descer(v, 0, fim);
        }
    }

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
