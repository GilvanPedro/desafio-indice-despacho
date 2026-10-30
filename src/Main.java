import java.util.Arrays;

/**
 * Demonstracao ponta a ponta com o dataset do enunciado:
 *   Parte 4 -> indice AVL de produtos
 *   Parte 2 -> heapsort do lote
 *   Parte 3 -> fila de despacho nascida do heapsort + pedidos novos
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== PARTE 4: INDICE DE PRODUTOS (AVL) ===");
        String[] skus = {
            "SKU-3010", "SKU-1045", "SKU-5520", "SKU-2230", "SKU-4190",
            "SKU-1800", "SKU-6005", "SKU-2990", "SKU-3770", "SKU-4450"
        };
        IndiceAVL indice = new IndiceAVL();
        for (String sku : skus) {
            indice.inserir(sku, "Produto " + sku);
        }
        System.out.println("Produtos inseridos: " + indice.tamanho());
        System.out.println("Altura da arvore: " + indice.altura()
                + " (minimo possivel para 10 nos: 4)");
        System.out.println("Rotacoes feitas: " + indice.totalRotacoes());
        System.out.println("Balanceada (invariantes AVL ok): " + indice.estaBalanceada());
        System.out.println("Em ordem: " + indice.emOrdem());
        System.out.println("Arvore (raiz a esquerda):");
        System.out.print(indice.desenhar());
        for (String busca : new String[] {"SKU-2990", "SKU-6005", "SKU-9999"}) {
            String r = indice.buscar(busca);
            System.out.println("buscar(" + busca + ") -> " + (r == null ? "NAO ENCONTRADO" : r));
        }

        System.out.println();
        System.out.println("=== PARTE 2: FECHAMENTO DO LOTE (HEAPSORT) ===");
        long chegada = 0;
        Pedido[] lote = {
            new Pedido("P101", 6, chegada++), new Pedido("P102", 9, chegada++),
            new Pedido("P103", 2, chegada++), new Pedido("P104", 7, chegada++),
            new Pedido("P105", 4, chegada++), new Pedido("P106", 10, chegada++),
            new Pedido("P107", 3, chegada++), new Pedido("P108", 8, chegada++),
            new Pedido("P109", 5, chegada++), new Pedido("P110", 1, chegada++)
        };
        System.out.println("Lote original:  " + Arrays.toString(lote));
        HeapsortLote.ordenarPorPrioridadeDecrescente(lote);
        System.out.println("Lote ordenado:  " + Arrays.toString(lote));

        System.out.println();
        System.out.println("=== PARTE 3: FILA DE DESPACHO POS-FECHAMENTO ===");
        FilaDespacho fila = new FilaDespacho(lote); // nasce do heapsort, sem heapify
        System.out.println("Fila criada a partir do vetor ordenado. Heap valido: "
                + fila.propriedadeDeHeapValida());

        Pedido[] novos = {
            new Pedido("P111", 9, chegada++),
            new Pedido("P112", 10, chegada++),
            new Pedido("P113", 6, chegada++)
        };
        for (Pedido p : novos) {
            fila.inserir(p);
            System.out.println("Inserido " + p + " -> topo agora: " + fila.espiar()
                    + " | heap: " + fila.estadoInterno());
        }

        System.out.println();
        System.out.println("Ordem de despacho:");
        int ordem = 1;
        while (!fila.estaVazia()) {
            System.out.printf("  %2d. %s%n", ordem++, fila.removerMaximo());
        }
    }
}
