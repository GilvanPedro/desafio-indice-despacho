# Desafio: Índice e Despacho Integrados

**Disciplina:** Estrutura de Dados II — Engenharia de Software (SENAI)
**Professor:** Luciano Lima

| Aluno | Matrícula |
|---|---|
| Gilvan Pedro de Castro Melo Campos | 2731534 |

---

## 1. Ficha de decisão de arquitetura

**Estrutura escolhida:** AVL.

**Padrão de uso esperado: mais busca ou mais escrita?**
Mais busca. O índice é consultado por SKU o tempo todo, enquanto inserções e remoções só acontecem quando um produto entra ou sai do catálogo, o que é bem menos frequente.

**Por que a AVL se encaixa melhor que a rubro-negra?**
Porque a AVL é mais rigidamente balanceada e fica mais baixa, então cada busca percorre menos nós. Ela gasta um pouco mais nas inserções e remoções, mas como o índice faz muito mais buscas, esse custo compensa.

**Comparação rápida:**

| | AVL | Rubro-negra |
|---|---|---|
| Balanceamento | Mais rígido (diferença de altura ≤ 1) | Mais flexível |
| Altura máxima | ~1,44·log n | ~2·log n |
| Busca | Mais rápida | Um pouco mais lenta |
| Inserção/remoção | Mais rotações | Menos rotações |
| Melhor para | Muita leitura | Muita escrita |

Com os 10 SKUs do dataset, a AVL ficou com altura 4 e a busca por qualquer SKU leva no máximo 4 comparações.

---

## 2. Como o heapsort e a fila se conectam

O heapsort ordena o lote em ordem decrescente de urgência:

```
P106(10) P102(9) P108(8) P104(7) P101(6) P109(5) P105(4) P107(3) P103(2) P110(1)
```

Um vetor em ordem decrescente já é um heap de máximo válido, porque todo pai fica numa posição anterior aos filhos e por isso tem valor maior ou igual ao deles. Então a fila de despacho usa esse vetor direto, sem reconstruir o heap. Os pedidos novos só são inseridos nele.

**Empates:** quando dois pedidos têm a mesma urgência, sai primeiro quem chegou antes. Ordem final de despacho:

```
P106(10) P112(10) P102(9) P111(9) P108(8) P104(7) P101(6) P113(6) P109(5) P105(4) P107(3) P103(2) P110(1)
```

---

## 3. Análise de complexidade

(m = produtos, n = pedidos do lote, k = pedidos novos)

| Parte | Operação | Complexidade |
|---|---|---|
| Índice AVL | Inserir um produto | O(log m) |
| Índice AVL | Buscar por SKU | O(log m) |
| Índice AVL | Montar o índice inteiro | O(m log m) |
| Heapsort | Ordenar o lote | O(n log n), sem memória extra |
| Fila de despacho | Criar a partir do vetor ordenado | O(n), só copia, não reconstrói |
| Fila de despacho | Inserir pedido novo | O(log(n+k)) |
| Fila de despacho | Remover o mais urgente | O(log(n+k)) |

**Solução completa:** O(m log m + n log n + k log(n+k)). Se reordenássemos o lote a cada pedido novo, cada chegada custaria O(n log n). Com a fila, custa só O(log n).

---

## 4. E se o volume fosse de milhões de pedidos?

- **Fila:** como a urgência só vai de 1 a 10, daria para trocar o heap por 10 filas simples, uma por nível de urgência. Inserir e remover passariam a ser O(1), e o desempate por ordem de chegada já sairia naturalmente.
- **Índice:** com milhões de produtos, o índice não caberia bem só na memória. O ideal seria uma árvore B+ em disco, que é o que bancos de dados usam, porque ela tem poucos níveis mesmo com muitos dados.
- Se as inserções e remoções de produtos também crescessem muito, a rubro-negra passaria a ser uma escolha melhor que a AVL.

---

## Como rodar

```bash
javac -d out src/*.java
java -cp out Main
java -cp out Testes
```
