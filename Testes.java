import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class Testes {

    private static int passou = 0;
    private static int falhou = 0;

    public static void main(String[] args) {
        testeIndiceDataset();
        testeIndiceRemocao();
        testeIndiceAleatorio();
        testeHeapsortDataset();
        testeHeapsortAleatorio();
        testeVetorOrdenadoEhHeap();
        testeFilaDatasetComEmpates();
        testeFilaRejeitaVetorNaoHeap();
        testeFilaAleatoria();

        System.out.println();
        System.out.println("Resultado: " + passou + " ok, " + falhou + " falha(s).");
        if (falhou > 0) System.exit(1);
    }

    private static void testeIndiceDataset() {
        String[] skus = {
            "SKU-3010", "SKU-1045", "SKU-5520", "SKU-2230", "SKU-4190",
            "SKU-1800", "SKU-6005", "SKU-2990", "SKU-3770", "SKU-4450"
        };
        IndiceAVL idx = new IndiceAVL();
        for (String s : skus) idx.inserir(s, "Produto " + s);

        verificar("indice: 10 produtos inseridos", idx.tamanho() == 10);
        boolean todos = true;
        for (String s : skus) todos &= ("Produto " + s).equals(idx.buscar(s));
        verificar("indice: todos os SKUs do dataset sao encontrados", todos);
        verificar("indice: SKU inexistente retorna null", idx.buscar("SKU-0000") == null);
        verificar("indice: invariantes AVL", idx.estaBalanceada());
        verificar("indice: altura <= 4 para 10 nos", idx.altura() <= 4);
        List<String> ordenado = new ArrayList<>(List.of(skus));
        Collections.sort(ordenado);
        verificar("indice: percurso em-ordem ordenado", idx.emOrdem().equals(ordenado));
    }

    private static void testeIndiceRemocao() {
        IndiceAVL idx = new IndiceAVL();
        String[] skus = {
            "SKU-3010", "SKU-1045", "SKU-5520", "SKU-2230", "SKU-4190",
            "SKU-1800", "SKU-6005", "SKU-2990", "SKU-3770", "SKU-4450"
        };
        for (String s : skus) idx.inserir(s, s);
        verificar("indice: remover raiz/no com 2 filhos", idx.remover("SKU-3010"));
        verificar("indice: removido nao e mais encontrado", !idx.contem("SKU-3010"));
        verificar("indice: remover inexistente retorna false", !idx.remover("SKU-0001"));
        verificar("indice: continua balanceada apos remocao", idx.estaBalanceada());
        verificar("indice: tamanho 9 apos remocao", idx.tamanho() == 9);
    }

    private static void testeIndiceAleatorio() {
        Random r = new Random(42);
        IndiceAVL idx = new IndiceAVL();
        TreeMap<String, String> ref = new TreeMap<>();
        boolean ok = true;
        for (int i = 0; i < 20000; i++) {
            String sku = String.format("SKU-%05d", r.nextInt(5000));
            if (r.nextInt(3) == 0) {
                ok &= idx.remover(sku) == (ref.remove(sku) != null);
            } else {
                idx.inserir(sku, "v" + i);
                ref.put(sku, "v" + i);
            }
        }
        ok &= idx.tamanho() == ref.size();
        ok &= idx.emOrdem().equals(new ArrayList<>(ref.keySet()));
        for (String k : ref.keySet()) ok &= ref.get(k).equals(idx.buscar(k));
        verificar("indice: 20.000 operacoes aleatorias batem com TreeMap", ok);
        verificar("indice: balanceada apos estresse", idx.estaBalanceada());
        double limite = 1.45 * (Math.log(idx.tamanho() + 2) / Math.log(2));
        verificar("indice: altura dentro do limite AVL (~1,44 log n)", idx.altura() <= limite);
    }

    private static Pedido[] loteDataset() {
        long c = 0;
        return new Pedido[] {
            new Pedido("P101", 6, c++), new Pedido("P102", 9, c++),
            new Pedido("P103", 2, c++), new Pedido("P104", 7, c++),
            new Pedido("P105", 4, c++), new Pedido("P106", 10, c++),
            new Pedido("P107", 3, c++), new Pedido("P108", 8, c++),
            new Pedido("P109", 5, c++), new Pedido("P110", 1, c++)
        };
    }

    private static String ids(Pedido[] v) {
        StringBuilder sb = new StringBuilder();
        for (Pedido p : v) sb.append(p.getId()).append(' ');
        return sb.toString().trim();
    }

    private static void testeHeapsortDataset() {
        Pedido[] lote = loteDataset();
        HeapsortLote.ordenarPorPrioridadeDecrescente(lote);
        verificar("heapsort: ordem decrescente de urgencia do dataset",
            ids(lote).equals("P106 P102 P108 P104 P101 P109 P105 P107 P103 P110"));
    }

    private static void testeHeapsortAleatorio() {
        Random r = new Random(7);
        boolean ok = true;
        for (int rodada = 0; rodada < 200; rodada++) {
            int n = r.nextInt(300);
            Pedido[] v = new Pedido[n];
            for (int i = 0; i < n; i++) v[i] = new Pedido("X" + i, 1 + r.nextInt(10), i);
            HeapsortLote.ordenarPorPrioridadeDecrescente(v);
            for (int i = 1; i < n; i++) ok &= !v[i].temPrioridadeSobre(v[i - 1]);
        }
        verificar("heapsort: 200 vetores aleatorios com empates saem ordenados", ok);
    }

    private static void testeVetorOrdenadoEhHeap() {
        Pedido[] lote = loteDataset();
        HeapsortLote.ordenarPorPrioridadeDecrescente(lote);
        FilaDespacho f = new FilaDespacho(lote);
        verificar("integracao: vetor do heapsort ja e heap de maximo valido",
            f.propriedadeDeHeapValida() && f.tamanho() == 10);
    }

    private static void testeFilaDatasetComEmpates() {
        Pedido[] lote = loteDataset();
        HeapsortLote.ordenarPorPrioridadeDecrescente(lote);
        FilaDespacho f = new FilaDespacho(lote);
        long c = 100;
        f.inserir(new Pedido("P111", 9, c++));
        f.inserir(new Pedido("P112", 10, c++));
        f.inserir(new Pedido("P113", 6, c++));
        verificar("fila: aceita os 3 pedidos novos", f.tamanho() == 13);
        verificar("fila: heap continua valido apos insercoes", f.propriedadeDeHeapValida());

        Pedido[] saida = new Pedido[13];
        for (int i = 0; i < 13; i++) saida[i] = f.removerMaximo();
        // Empates resolvidos por chegada: P106 antes de P112 (10),
        // P102 antes de P111 (9), P101 antes de P113 (6).
        verificar("fila: ordem de despacho correta, inclusive empates",
            ids(saida).equals(
                "P106 P112 P102 P111 P108 P104 P101 P113 P109 P105 P107 P103 P110"));
        verificar("fila: vazia ao final", f.estaVazia());
    }

    private static void testeFilaRejeitaVetorNaoHeap() {
        Pedido[] lote = loteDataset(); // NAO ordenado
        boolean lancou = false;
        try {
            new FilaDespacho(lote);
        } catch (IllegalArgumentException e) {
            lancou = true;
        }
        verificar("fila: recusa vetor que nao passou pelo heapsort", lancou);
    }

    private static void testeFilaAleatoria() {
        Random r = new Random(123);
        boolean ok = true;
        for (int rodada = 0; rodada < 100; rodada++) {
            long c = 0;
            int n = 1 + r.nextInt(200);
            Pedido[] lote = new Pedido[n];
            for (int i = 0; i < n; i++) lote[i] = new Pedido("L" + i, 1 + r.nextInt(10), c++);
            HeapsortLote.ordenarPorPrioridadeDecrescente(lote);
            FilaDespacho f = new FilaDespacho(lote);
            int extras = r.nextInt(200);
            for (int i = 0; i < extras; i++) f.inserir(new Pedido("N" + i, 1 + r.nextInt(10), c++));
            Pedido anterior = f.removerMaximo();
            while (!f.estaVazia()) {
                Pedido atual = f.removerMaximo();
                ok &= !atual.temPrioridadeSobre(anterior);
                anterior = atual;
            }
        }
        verificar("fila: 100 cenarios aleatorios removem sempre o mais prioritario", ok);
    }

    private static void verificar(String nome, boolean condicao) {
        if (condicao) {
            passou++;
            System.out.println("[OK]    " + nome);
        } else {
            falhou++;
            System.out.println("[FALHA] " + nome);
        }
    }
}
