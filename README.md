# Desafio: Índice e Despacho Integrados (Estrutura de Dados II)

Integrantes: ver cabeçalho dos arquivos `.java` e `JUSTIFICATIVA.md`.

- `src/IndiceAVL.java`     Parte 4: índice de produtos por SKU (AVL)
- `src/HeapsortLote.java`  Parte 2: heapsort do lote (ordem decrescente)
- `src/FilaDespacho.java`  Parte 3: fila de prioridade nascida do vetor do heapsort
- `src/Pedido.java`        Modelo de pedido (urgência + ordem de chegada para desempate)
- `src/Main.java`          Demonstração com o dataset do enunciado
- `src/Testes.java`        23 testes automatizados (sem dependências)
- `JUSTIFICATIVA.md`       Ficha de decisão + análise de complexidade + escala (Parte 1 e 5)

Compilar e rodar (Java 11+):

    javac -d out src/*.java
    java -cp out Main
    java -cp out Testes
