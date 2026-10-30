/*
 * Integrantes do grupo:
 *  - Gilvan Pedro de Castro Melo Campos (2731534)
 *  - João Paulo Marques Ferreira (2759688)
 *  - João Pedro Martini Denani (2758712)
 *  - Sidney Emanuel Barbosa de Oliveira (2759803)
 */

import java.util.ArrayList;
import java.util.List;

public class IndiceAVL {

    private static final class No {
        final String sku;
        String descricao;
        int altura = 1;
        No esq, dir;

        No(String sku, String descricao) {
            this.sku = sku;
            this.descricao = descricao;
        }
    }

    private No raiz;
    private int tamanho;
    private int rotacoes;

    //Insere (ou atualiza a descricao, se o SKU ja existe).
    public void inserir(String sku, String descricao) {
        raiz = inserir(raiz, sku, descricao);
    }

    //Busca por SKU. Retorna a descricao, ou null se nao existir.
    public String buscar(String sku) {
        No atual = raiz;
        while (atual != null) {
            int c = sku.compareTo(atual.sku);
            if (c == 0) return atual.descricao;
            atual = (c < 0) ? atual.esq : atual.dir;
        }
        return null;
    }

    public boolean contem(String sku) {
        return buscar(sku) != null;
    }

    // Remove um SKU do catalogo. Retorna true se removeu.
    public boolean remover(String sku) {
        int antes = tamanho;
        raiz = remover(raiz, sku);
        return tamanho < antes;
    }

    public int tamanho() { return tamanho; }
    public int altura() { return h(raiz); }
    public int totalRotacoes() { return rotacoes; }

    //Coloca os SKUs em ordem (percurso em-ordem).
    public List<String> emOrdem() {
        List<String> out = new ArrayList<>();
        emOrdem(raiz, out);
        return out;
    }

    //Verifica as invariantes da AVL e da BST.
    public boolean estaBalanceada() {
        return verificar(raiz, null, null) >= 0;
    }

    // Desenho da arvore deitada (raiz a esquerda)
    public String desenhar() {
        StringBuilder sb = new StringBuilder();
        desenhar(raiz, "", sb);
        return sb.toString();
    }

    private No inserir(No no, String sku, String descricao) {
        if (no == null) {
            tamanho++;
            return new No(sku, descricao);
        }
        int c = sku.compareTo(no.sku);
        if (c < 0) {
            no.esq = inserir(no.esq, sku, descricao);
        } else if (c > 0) {
            no.dir = inserir(no.dir, sku, descricao);
        } else {
            no.descricao = descricao;
            return no;
        }
        return balancear(no);
    }

    private No remover(No no, String sku) {
        if (no == null) return null;
        int c = sku.compareTo(no.sku);
        if (c < 0) {
            no.esq = remover(no.esq, sku);
        } else if (c > 0) {
            no.dir = remover(no.dir, sku);
        } else {
            if (no.esq == null || no.dir == null) {
                tamanho--;
                return (no.esq != null) ? no.esq : no.dir;
            }

            No suc = no.dir;
            while (suc.esq != null) suc = suc.esq;
            No novo = new No(suc.sku, suc.descricao);
            novo.esq = no.esq;
            novo.dir = remover(no.dir, suc.sku);
            no = novo;
        }
        return balancear(no);
    }

    private No balancear(No no) {
        atualizarAltura(no);
        int fb = fatorBalanceamento(no);

        if (fb > 1) {
            if (fatorBalanceamento(no.esq) < 0) {
                //Caso for LR
                no.esq = rotacaoEsquerda(no.esq);
            }
            //Caso for LL
            return rotacaoDireita(no);
        }
        if (fb < -1) {
            if (fatorBalanceamento(no.dir) > 0) {
                //Caso for RL
                no.dir = rotacaoDireita(no.dir);
            }
            //Caso for RR
            return rotacaoEsquerda(no);
        }
        return no;
    }

    private No rotacaoDireita(No y) {
        No x = y.esq;
        y.esq = x.dir;
        x.dir = y;
        atualizarAltura(y);
        atualizarAltura(x);
        rotacoes++;
        return x;
    }

    private No rotacaoEsquerda(No x) {
        No y = x.dir;
        x.dir = y.esq;
        y.esq = x;
        atualizarAltura(x);
        atualizarAltura(y);
        rotacoes++;
        return y;
    }

    private static int h(No no) { return no == null ? 0 : no.altura; }

    private static void atualizarAltura(No no) {
        no.altura = 1 + Math.max(h(no.esq), h(no.dir));
    }

    private static int fatorBalanceamento(No no) {
        return no == null ? 0 : h(no.esq) - h(no.dir);
    }

    private static void emOrdem(No no, List<String> out) {
        if (no == null) return;
        emOrdem(no.esq, out);
        out.add(no.sku);
        emOrdem(no.dir, out);
    }

    // Retorna a altura se ok, ou -1 se alguma invariante quebrou.
    private static int verificar(No no, String min, String max) {
        if (no == null) return 0;
        if (min != null && no.sku.compareTo(min) <= 0) return -1;
        if (max != null && no.sku.compareTo(max) >= 0) return -1;
        int he = verificar(no.esq, min, no.sku);
        int hd = verificar(no.dir, no.sku, max);
        if (he < 0 || hd < 0 || Math.abs(he - hd) > 1) return -1;
        int alt = 1 + Math.max(he, hd);
        return (alt == no.altura) ? alt : -1;
    }

    private static void desenhar(No no, String prefixo, StringBuilder sb) {
        if (no == null) return;
        desenhar(no.dir, prefixo + "        ", sb);
        sb.append(prefixo).append(no.sku).append('\n');
        desenhar(no.esq, prefixo + "        ", sb);
    }
}
