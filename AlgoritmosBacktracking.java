import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.io.PrintStream;

/**
 * Exercicios de algoritmos gulosos e backtracking:
 * - Troco com moedas (guloso)
 * - Escalonamento de intervalos (guloso)
 * - Soma dos subconjuntos (backtracking: primeira / todas)
 * - N-rainhas (backtracking: primeira / todas)
 * Inclui uma heuristica gulosa ilustrativa para a soma dos subconjuntos.
 *
 * Metricas:
 * - iteracoes: candidatos examinados nos gulosos, chamadas recursivas na
 *   soma e posicoes testadas nas N-rainhas. No troco, cada moeda adicionada
 *   tambem conta uma iteracao.
 * - instrucoes: estimativa de operacoes logicas instrumentadas. Nao inclui
 *   operacoes internas das bibliotecas de ordenacao e de colecoes.
 * - tempo: medido com System.nanoTime(); inclui a ordenacao dos gulosos.
 *
 * Os testes grandes sao limitados: um resultado parcial NAO significa
 * inexistencia de outras solucoes.
 */
public class AlgoritmosBacktracking {

    private static final long SEM_LIMITE = Long.MAX_VALUE;
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    static class Metricas {
        long iteracoes;
        long instrucoes;
        long tempoNs;
        boolean interrompidoPorLimite;

        double segundos() {
            return tempoNs / 1_000_000_000.0;
        }

        @Override
        public String toString() {
            return String.format(
                    "iteracoes=%d | instrucoes~=%d | tempo=%.6f s%s",
                    iteracoes, instrucoes, segundos(),
                    interrompidoPorLimite ? " | BUSCA PARCIAL (limite atingido)" : "");
        }
    }

    static class ResultadoSoma {
        final List<List<Integer>> solucoes = new ArrayList<>();
        final Metricas metricas = new Metricas();
    }

    static class ResultadoRainhas {
        final List<int[]> solucoes = new ArrayList<>();
        final Metricas metricas = new Metricas();
    }

    static class ResultadoTroco {
        final List<Integer> moedasUsadas = new ArrayList<>();
        final Metricas metricas = new Metricas();
        int restante;
    }

    static class Intervalo {
        final int id;
        final int inicio;
        final int fim;

        Intervalo(int id, int inicio, int fim) {
            if (inicio > fim) {
                throw new IllegalArgumentException("O inicio nao pode superar o fim.");
            }
            this.id = id;
            this.inicio = inicio;
            this.fim = fim;
        }

        @Override
        public String toString() {
            return "I" + id + "(" + inicio + "," + fim + ")";
        }
    }

    static class ResultadoIntervalos {
        final List<Intervalo> selecionados = new ArrayList<>();
        final Metricas metricas = new Metricas();
    }

    // ------------------------------------------------------------
    // 1) TROCO - ALGORITMO GULOSO
    // ------------------------------------------------------------

    /**
     * Da troco escolhendo repetidamente a maior moeda que ainda cabe.
     * Entrada: valor e moedas EM CENTAVOS (evita erros com ponto flutuante).
     * Nem todo sistema de denominacoes admite solucao otima gulosa.
     * Iteracoes = denominacoes examinadas + moedas efetivamente escolhidas.
     */
    static ResultadoTroco trocoGuloso(int[] moedas, int valor) {
        if (valor < 0 || moedas == null || moedas.length == 0) {
            throw new IllegalArgumentException("Valor >= 0 e moedas nao vazias sao obrigatorios.");
        }
        for (int moeda : moedas) {
            if (moeda <= 0) throw new IllegalArgumentException("Moedas devem ser positivas.");
        }

        ResultadoTroco resultado = new ResultadoTroco();
        Metricas m = resultado.metricas;
        long inicio = System.nanoTime();
        int[] ordenadas = moedas.clone();
        Arrays.sort(ordenadas); // Ordena crescente; vamos percorrer de tras para frente.
        resultado.restante = valor;
        m.instrucoes += 2;

        for (int i = ordenadas.length - 1; i >= 0 && resultado.restante > 0; i--) {
            m.iteracoes++; // Uma denominacao analisada.
            m.instrucoes += 2;
            while (resultado.restante >= ordenadas[i]) {
                resultado.moedasUsadas.add(ordenadas[i]);
                resultado.restante -= ordenadas[i];
                m.iteracoes++; // Uma moeda escolhida.
                m.instrucoes += 4;
            }
        }
        m.tempoNs = System.nanoTime() - inicio;
        return resultado;
    }

    // ------------------------------------------------------------
    // 2) ESCALONAMENTO DE INTERVALOS - ALGORITMO GULOSO
    // ------------------------------------------------------------

    /**
     * Escolhe o maximo de intervalos dois a dois disjuntos.
     * Primeiro ordena pelo termino mais cedo, depois percorre a lista.
     * O enunciado usa extremos INCLUSIVOS: proximo.inicio > ultimo.fim.
     * Iteracoes = quantidade de intervalos examinados (nao conta ordenacao).
     */
    static ResultadoIntervalos escalonarIntervalos(Intervalo[] intervalos) {
        if (intervalos == null) throw new IllegalArgumentException("Lista nula.");
        for (Intervalo intervalo : intervalos) {
            if (intervalo == null) throw new IllegalArgumentException("Intervalo nulo.");
        }
        ResultadoIntervalos resultado = new ResultadoIntervalos();
        Metricas m = resultado.metricas;
        long inicio = System.nanoTime();
        Intervalo[] ordenados = intervalos.clone();
        Arrays.sort(ordenados, Comparator.comparingInt((Intervalo x) -> x.fim)
                .thenComparingInt(x -> x.inicio).thenComparingInt(x -> x.id));
        long fimAnterior = Long.MIN_VALUE;
        m.instrucoes += 2;

        for (Intervalo intervalo : ordenados) {
            m.iteracoes++; // Um candidato analisado.
            m.instrucoes += 2;
            if (intervalo.inicio > fimAnterior) {
                resultado.selecionados.add(intervalo);
                fimAnterior = intervalo.fim;
                m.instrucoes += 2;
            }
        }
        m.tempoNs = System.nanoTime() - inicio;
        return resultado;
    }

    // ------------------------------------------------------------
    // 1) SOMA DOS SUBCONJUNTOS - BACKTRACKING
    // ------------------------------------------------------------

    /** Retorna a primeira solucao encontrada, caso exista. */
    static ResultadoSoma somaUmaSolucao(int[] numeros, long limiteIteracoes) {
        return resolverSoma(numeros, false, limiteIteracoes, 1);
    }

    /** Retorna todas as solucoes (ou uma parte, se algum limite for atingido). */
    static ResultadoSoma somaTodasSolucoes(int[] numeros, long limiteIteracoes,
                                           int limiteSolucoes) {
        return resolverSoma(numeros, true, limiteIteracoes, limiteSolucoes);
    }

    private static ResultadoSoma resolverSoma(int[] numeros, boolean todas,
                                               long limiteIteracoes, int limiteSolucoes) {
        if (limiteIteracoes < 1 || limiteSolucoes < 1) {
            throw new IllegalArgumentException("Os limites devem ser positivos.");
        }
        ResultadoSoma resultado = new ResultadoSoma();
        long inicio = System.nanoTime();
        buscarSoma(numeros, 0, 0L, new ArrayList<>(), resultado,
                   todas, limiteIteracoes, limiteSolucoes);
        resultado.metricas.tempoNs = System.nanoTime() - inicio;
        return resultado;
    }

    private static boolean buscarSoma(int[] numeros, int indice, long soma,
                                      List<Integer> atual, ResultadoSoma resultado,
                                      boolean todas, long limiteIteracoes,
                                      int limiteSolucoes) {
        Metricas m = resultado.metricas;
        if (m.iteracoes >= limiteIteracoes) {
            m.interrompidoPorLimite = true;
            return true;
        }
        m.iteracoes++; // Conta um estado visitado
        m.instrucoes += 2; // Entrada na funcao e verificacao de limite

        // Para apenas UMA solucao, ja podemos terminar quando a soma chega a zero.
        if (!todas && !atual.isEmpty() && soma == 0) {
            resultado.solucoes.add(new ArrayList<>(atual));
            m.instrucoes += 2;
            return true;
        }
        m.instrucoes += 2; // Verificacoes de conjunto nao vazio e soma

        // Para TODAS as solucoes, so registramos nos nos folha:
        // assim cada subconjunto e contado exatamente uma vez.
        if (indice == numeros.length) {
            m.instrucoes++;
            if (todas && !atual.isEmpty() && soma == 0) {
                resultado.solucoes.add(new ArrayList<>(atual));
                m.instrucoes += 2;
                if (resultado.solucoes.size() >= limiteSolucoes) {
                    m.interrompidoPorLimite = true;
                    return true;
                }
            }
            return false;
        }

        // Escolha 1: incluir o numero.
        atual.add(numeros[indice]);
        m.instrucoes++;
        boolean parar = buscarSoma(numeros, indice + 1,
                                   soma + numeros[indice], atual, resultado,
                                   todas, limiteIteracoes, limiteSolucoes);
        atual.remove(atual.size() - 1); // Desfaz a escolha (backtracking)
        m.instrucoes++;
        if (parar) return true;

        // Escolha 2: nao incluir o numero.
        return buscarSoma(numeros, indice + 1, soma, atual, resultado,
                          todas, limiteIteracoes, limiteSolucoes);
    }

    // ------------------------------------------------------------
    // 2) SOMA DOS SUBCONJUNTOS - HEURISTICA GULOSA (COMPARACAO)
    // ------------------------------------------------------------

    /**
     * Comeca pelo numero de menor valor absoluto. Em seguida, sempre escolhe
     * o numero ainda nao utilizado que mais reduz |soma|. Pode falhar mesmo
     * quando existe uma solucao; portanto NAO substitui o backtracking.
     */
    static ResultadoSoma somaGulosa(int[] numeros) {
        ResultadoSoma resultado = new ResultadoSoma();
        Metricas m = resultado.metricas;
        long inicio = System.nanoTime();
        if (numeros.length == 0) {
            m.tempoNs = System.nanoTime() - inicio;
            return resultado;
        }

        int inicial = 0;
        for (int i = 0; i < numeros.length; i++) {
            m.iteracoes++;
            m.instrucoes += 2;
            if (Math.abs((long) numeros[i]) < Math.abs((long) numeros[inicial])) {
                inicial = i;
                m.instrucoes++;
            }
        }

        boolean[] usado = new boolean[numeros.length];
        List<Integer> atual = new ArrayList<>();
        long soma = numeros[inicial];
        usado[inicial] = true;
        atual.add(numeros[inicial]);
        m.instrucoes += 3;

        while (soma != 0) {
            int melhorIndice = -1;
            long melhorDistancia = Math.abs(soma);
            for (int i = 0; i < numeros.length; i++) {
                m.iteracoes++;
                m.instrucoes += 2;
                if (usado[i]) continue;
                long distancia = Math.abs(soma + numeros[i]);
                m.instrucoes++;
                if (distancia < melhorDistancia) {
                    melhorDistancia = distancia;
                    melhorIndice = i;
                    m.instrucoes += 2;
                }
            }
            if (melhorIndice == -1) break;
            usado[melhorIndice] = true;
            atual.add(numeros[melhorIndice]);
            soma += numeros[melhorIndice];
            m.instrucoes += 3;
        }

        if (soma == 0) resultado.solucoes.add(atual);
        m.tempoNs = System.nanoTime() - inicio;
        return resultado;
    }

    // ------------------------------------------------------------
    // 3) N-RAINHAS - BACKTRACKING
    // ------------------------------------------------------------

    /** Retorna a primeira disposicao valida, se existir. */
    static ResultadoRainhas rainhasUmaSolucao(int n, long limiteIteracoes) {
        return resolverRainhas(n, false, limiteIteracoes, 1);
    }

    /** Retorna todas as disposicoes (ou uma parte, se houver limite). */
    static ResultadoRainhas rainhasTodasSolucoes(int n, long limiteIteracoes,
                                                 int limiteSolucoes) {
        return resolverRainhas(n, true, limiteIteracoes, limiteSolucoes);
    }

    private static ResultadoRainhas resolverRainhas(int n, boolean todas,
                                                    long limiteIteracoes, int limiteSolucoes) {
        if (n < 2) throw new IllegalArgumentException("N precisa ser >= 2.");
        if (limiteIteracoes < 1 || limiteSolucoes < 1) {
            throw new IllegalArgumentException("Os limites devem ser positivos.");
        }

        ResultadoRainhas resultado = new ResultadoRainhas();
        int[] posicoes = new int[n];
        Arrays.fill(posicoes, -1);
        boolean[] colunas = new boolean[n];
        boolean[] diagonalPrincipal = new boolean[2 * n - 1];
        boolean[] diagonalSecundaria = new boolean[2 * n - 1];

        long inicio = System.nanoTime();
        buscarRainhas(0, n, posicoes, colunas, diagonalPrincipal,
                     diagonalSecundaria, resultado, todas, limiteIteracoes,
                     limiteSolucoes);
        resultado.metricas.tempoNs = System.nanoTime() - inicio;
        return resultado;
    }

    private static boolean buscarRainhas(int linha, int n, int[] posicoes,
                                         boolean[] colunas, boolean[] diagonalPrincipal,
                                         boolean[] diagonalSecundaria, ResultadoRainhas resultado,
                                         boolean todas, long limiteIteracoes,
                                         int limiteSolucoes) {
        Metricas m = resultado.metricas;
        m.instrucoes++; // Verificacao de caso-base
        if (linha == n) {
            resultado.solucoes.add(posicoes.clone());
            m.instrucoes++;
            if (!todas) return true;
            if (resultado.solucoes.size() >= limiteSolucoes) {
                m.interrompidoPorLimite = true;
                return true;
            }
            return false;
        }

        for (int coluna = 0; coluna < n; coluna++) {
            if (m.iteracoes >= limiteIteracoes) {
                m.interrompidoPorLimite = true;
                return true;
            }
            m.iteracoes++; // Conta uma posicao (linha, coluna) avaliada
            m.instrucoes += 4; // Controle do laco e tres testes de conflito
            int d1 = linha - coluna + n - 1;
            int d2 = linha + coluna;
            if (colunas[coluna] || diagonalPrincipal[d1] || diagonalSecundaria[d2]) {
                continue;
            }

            posicoes[linha] = coluna;
            colunas[coluna] = true;
            diagonalPrincipal[d1] = true;
            diagonalSecundaria[d2] = true;
            m.instrucoes += 4;

            boolean parar = buscarRainhas(linha + 1, n, posicoes, colunas,
                                          diagonalPrincipal, diagonalSecundaria,
                                          resultado, todas, limiteIteracoes,
                                          limiteSolucoes);

            // Desfaz a escolha, mesmo ao interromper a busca.
            posicoes[linha] = -1;
            colunas[coluna] = false;
            diagonalPrincipal[d1] = false;
            diagonalSecundaria[d2] = false;
            m.instrucoes += 4;
            if (parar) return true;
        }
        return false;
    }

    // ------------------------------------------------------------
    // 4) TABELAS / TESTES / PERFORMANCE
    // ------------------------------------------------------------

    private static final String[] COLUNAS = {
        "Caso", "Método", "Soluções", "Iterações",
        "Instruções (~)", "Tempo (ms)", "Status"
    };

    /** Cria uma tabela de texto alinhada, sem dependencias externas. */
    private static void imprimirTabela(String[] cabecalhos, List<String[]> linhas) {
        int[] larguras = new int[cabecalhos.length];
        for (int i = 0; i < cabecalhos.length; i++) {
            larguras[i] = cabecalhos[i].length();
        }
        for (String[] linha : linhas) {
            if (linha.length != cabecalhos.length) {
                throw new IllegalArgumentException("Numero de colunas incorreto.");
            }
            for (int i = 0; i < linha.length; i++) {
                larguras[i] = Math.max(larguras[i], linha[i].length());
            }
        }

        imprimirBorda(larguras, '┌', '┬', '┐');
        imprimirLinha(cabecalhos, larguras);
        imprimirBorda(larguras, '├', '┼', '┤');
        for (String[] linha : linhas) {
            imprimirLinha(linha, larguras);
        }
        imprimirBorda(larguras, '└', '┴', '┘');
    }

    private static void imprimirBorda(int[] larguras, char inicio, char meio, char fim) {
        StringBuilder texto = new StringBuilder().append(inicio);
        for (int i = 0; i < larguras.length; i++) {
            for (int j = 0; j < larguras[i] + 2; j++) texto.append('─');
            texto.append(i == larguras.length - 1 ? fim : meio);
        }
        System.out.println(texto);
    }

    private static void imprimirLinha(String[] valores, int[] larguras) {
        StringBuilder texto = new StringBuilder("│");
        for (int i = 0; i < valores.length; i++) {
            texto.append(' ').append(valores[i]);
            for (int j = valores[i].length(); j < larguras[i]; j++) texto.append(' ');
            texto.append(" │");
        }
        System.out.println(texto);
    }

    private static String status(Metricas metricas, boolean encontrou, boolean guloso) {
        if (metricas.interrompidoPorLimite) return "PARCIAL";
        if (encontrou) return "OK";
        return guloso ? "NÃO ACHOU" : "SEM SOLUÇÃO";
    }

    private static String[] linhaResultados(String caso, String metodo, int quantidade,
                                             Metricas metricas, boolean guloso) {
        return new String[] {
            caso,
            metodo,
            String.valueOf(quantidade),
            String.format(PT_BR, "%,d", metricas.iteracoes),
            String.format(PT_BR, "%,d", metricas.instrucoes),
            String.format(PT_BR, "%.3f", metricas.tempoNs / 1_000_000.0),
            status(metricas, quantidade > 0, guloso)
        };
    }

    private static String inteiro(long numero) {
        return String.format(PT_BR, "%,d", numero);
    }

    private static String milissegundos(Metricas metricas) {
        return String.format(PT_BR, "%.3f", metricas.tempoNs / 1_000_000.0);
    }

    private static void testesTroco() {
        System.out.println("\n========== TROCO: ALGORITMO GULOSO ==========");
        int[][] denominacoes = {
            {100, 25, 10, 5, 1}, {100, 25, 10, 5, 1},
            {100, 25, 10, 5, 1}, {4, 3, 1}, {10, 6, 4}, {17, 8, 1}
        };
        int[] valores = {289, 137, 99, 6, 8, 24};
        List<String[]> linhas = new ArrayList<>();
        List<String> detalhes = new ArrayList<>();
        for (int i = 0; i < valores.length; i++) {
            String caso = String.valueOf((char) ('A' + i));
            ResultadoTroco r = trocoGuloso(denominacoes[i], valores[i]);
            boolean completo = r.restante == 0;
            linhas.add(new String[] {
                caso, inteiro(valores[i]), inteiro(r.moedasUsadas.size()),
                inteiro(r.metricas.iteracoes), inteiro(r.metricas.instrucoes),
                milissegundos(r.metricas), completo ? "OK" : "INCOMPLETO"
            });
            detalhes.add("  " + caso + " | moedas disponiveis: "
                    + Arrays.toString(denominacoes[i]) + " | troco: "
                    + r.moedasUsadas + (completo ? "" : " | restante: " + r.restante));
        }
        imprimirTabela(new String[] {
            "Caso", "Valor (cent.)", "Qtd moedas", "Iterações",
            "Instruções (~)", "Tempo (ms)", "Status"
        }, linhas);
        System.out.println("\nMoedas escolhidas:");
        for (String detalhe : detalhes) System.out.println(detalhe);
        System.out.println("OBS.: no caso D, o guloso escolhe [4,1,1], mas [3,3] usa menos moedas.");
        System.out.println("No caso E, o guloso nao completa 8, embora [4,4] seja possivel.");
        System.out.println("No caso F, o guloso escolhe 8 moedas, mas [8,8,8] usa apenas 3.");
    }

    private static Intervalo[] gerarIntervalos(int n, Random random) {
        Intervalo[] intervalos = new Intervalo[n];
        for (int i = 0; i < n; i++) {
            int inicio = random.nextInt(n * 3 + 1) + 1;
            int fim = inicio + random.nextInt(15);
            intervalos[i] = new Intervalo(i + 1, inicio, fim);
        }
        return intervalos;
    }

    private static void testesIntervalos() {
        System.out.println("\n========== ESCALONAMENTO: GULOSO POR MENOR TERMINO ==========");
        // Os 11 intervalos do enunciado original (identificados de 1 a 11).
        int[] inicios = {4, 6, 13, 4, 2, 6, 7, 9, 1, 3, 9};
        int[] fins    = {8, 7, 14, 5, 4, 9, 10, 11, 6, 13, 12};
        Intervalo[] original = new Intervalo[inicios.length];
        for (int i = 0; i < original.length; i++) {
            original[i] = new Intervalo(i + 1, inicios[i], fins[i]);
        }
        Intervalo[] exemploB = {
            new Intervalo(1, 1, 3), new Intervalo(2, 2, 5),
            new Intervalo(3, 4, 6), new Intervalo(4, 6, 8),
            new Intervalo(5, 7, 9), new Intervalo(6, 10, 12)
        };
        Intervalo[] exemploC = {
            new Intervalo(1, 1, 5), new Intervalo(2, 1, 2),
            new Intervalo(3, 2, 3), new Intervalo(4, 4, 4),
            new Intervalo(5, 5, 6), new Intervalo(6, 7, 9)
        };
        Intervalo[][] pequenos = {original, exemploB, exemploC};
        List<String[]> linhas = new ArrayList<>();
        List<String> detalhes = new ArrayList<>();
        for (int i = 0; i < pequenos.length; i++) {
            String caso = String.valueOf((char) ('A' + i));
            ResultadoIntervalos r = escalonarIntervalos(pequenos[i]);
            linhas.add(new String[] {
                caso, inteiro(pequenos[i].length), inteiro(r.selecionados.size()),
                inteiro(r.metricas.iteracoes), inteiro(r.metricas.instrucoes),
                milissegundos(r.metricas), "OK"
            });
            detalhes.add("  " + caso + " | escolhidos: " + r.selecionados);
        }
        Random random = new Random(73); // Semente fixa: mesmos testes em cada execucao.
        for (int n : new int[]{50, 100, 250, 500, 1000}) {
            Intervalo[] intervalos = gerarIntervalos(n, random);
            ResultadoIntervalos r = escalonarIntervalos(intervalos);
            linhas.add(new String[] {
                "N=" + n, inteiro(n), inteiro(r.selecionados.size()),
                inteiro(r.metricas.iteracoes), inteiro(r.metricas.instrucoes),
                milissegundos(r.metricas), "OK"
            });
        }
        imprimirTabela(new String[] {
            "Caso", "Intervalos", "Selecionados", "Iterações",
            "Instruções (~)", "Tempo (ms)", "Status"
        }, linhas);
        System.out.println("\nIntervalos escolhidos nos casos pequenos:");
        for (String detalhe : detalhes) System.out.println(detalhe);
        System.out.println("OBS.: a ordenacao esta incluida no tempo, mas nao nas iteracoes instrumentadas.");
        System.out.println("Extremos inclusivos: inicio == fim anterior significa conflito.");
    }

    /** Evita repetir colunas de tabuleiro: mostra apenas as posicoes das rainhas. */
    private static void testesSomaPequenos() {
        int[][] casos = {
            {-7, -3, -2, 5, 8},
            {1, 2, 3, 4, 5, 10},
            {-5, 2, 3, -1, 1},
            {-8, -7, 8} // A heuristica gulosa pode falhar neste exemplo.
        };
        List<String[]> linhas = new ArrayList<>();
        List<String> subconjuntos = new ArrayList<>();

        System.out.println("\n========== SOMA DOS SUBCONJUNTOS: CASOS PEQUENOS ==========");
        for (int i = 0; i < casos.length; i++) {
            String caso = String.valueOf((char) ('A' + i));
            System.out.println("  " + caso + " = " + Arrays.toString(casos[i]));

            ResultadoSoma uma = somaUmaSolucao(casos[i], SEM_LIMITE);
            ResultadoSoma todas = somaTodasSolucoes(casos[i], SEM_LIMITE,
                                                    Integer.MAX_VALUE);
            ResultadoSoma gulosa = somaGulosa(casos[i]);
            linhas.add(linhaResultados(caso, "Backtracking (1)",
                                        uma.solucoes.size(), uma.metricas, false));
            linhas.add(linhaResultados(caso, "Backtracking (todas)",
                                        todas.solucoes.size(), todas.metricas, false));
            linhas.add(linhaResultados(caso, "Guloso (heuristica)",
                                        gulosa.solucoes.size(), gulosa.metricas, true));
            subconjuntos.add("  " + caso + " -> " + todas.solucoes);
        }
        System.out.println();
        imprimirTabela(COLUNAS, linhas);
        System.out.println("\nSubconjuntos validos encontrados pelo backtracking:");
        for (String texto : subconjuntos) System.out.println(texto);
    }

    private static void testesRainhas() {
        List<String[]> linhas = new ArrayList<>();
        List<String> posicoes = new ArrayList<>();

        System.out.println("\n========== N-RAINHAS: BACKTRACKING ==========");
        for (int n : new int[]{2, 3, 4, 7, 8, 10}) {
            ResultadoRainhas uma = rainhasUmaSolucao(n, SEM_LIMITE);
            ResultadoRainhas todas = rainhasTodasSolucoes(n, SEM_LIMITE,
                                                           Integer.MAX_VALUE);
            String caso = "N=" + n;
            linhas.add(linhaResultados(caso, "Backtracking (1)",
                                        uma.solucoes.size(), uma.metricas, false));
            linhas.add(linhaResultados(caso, "Backtracking (todas)",
                                        todas.solucoes.size(), todas.metricas, false));
            if (!uma.solucoes.isEmpty()) {
                posicoes.add("  " + caso + " -> " + Arrays.toString(uma.solucoes.get(0)));
            }
        }
        imprimirTabela(COLUNAS, linhas);
        System.out.println("\nPrimeira solucao por N (coluna de cada rainha, indice iniciando em 0):");
        for (String texto : posicoes) System.out.println(texto);
    }

    private static int[] gerarConjunto(int n, Random random) {
        int[] conjunto = new int[n];
        // Um par com soma zero garante que o teste de UMA solucao seja viavel.
        conjunto[0] = -500;
        conjunto[1] = 500;
        for (int i = 2; i < n; i++) {
            int valor;
            do {
                valor = random.nextInt(2001) - 1000; // -1000 a 1000
            } while (valor == 0);
            conjunto[i] = valor;
        }
        return conjunto;
    }

    private static void testesGrandes() {
        System.out.println("\n========== PERFORMANCE: CONJUNTOS GRANDES ==========");
        System.out.println("Numeros entre -1000 e 1000; par (-500, 500) inserido no inicio.");
        System.out.println("Limites para a busca completa: 100.000 iteracoes ou 100 solucoes.");
        Random random = new Random(42);
        List<String[]> linhas = new ArrayList<>();
        for (int n : new int[]{50, 100, 250, 500, 1000}) {
            int[] conjunto = gerarConjunto(n, random);
            long limiteIteracoes = 100_000;
            int limiteSolucoes = 100;
            ResultadoSoma uma = somaUmaSolucao(conjunto, limiteIteracoes);
            ResultadoSoma todas = somaTodasSolucoes(conjunto, limiteIteracoes,
                                                    limiteSolucoes);
            ResultadoSoma gulosa = somaGulosa(conjunto);
            String caso = "N=" + n;
            linhas.add(linhaResultados(caso, "Backtracking (1)",
                                        uma.solucoes.size(), uma.metricas, false));
            linhas.add(linhaResultados(caso, "Backtracking (todas)",
                                        todas.solucoes.size(), todas.metricas, false));
            linhas.add(linhaResultados(caso, "Guloso (heuristica)",
                                        gulosa.solucoes.size(), gulosa.metricas, true));
        }
        imprimirTabela(COLUNAS, linhas);
        System.out.println("\nPARCIAL = a busca parou por limite; o total pode ser maior.");
        System.out.println("NÃO ACHOU = o guloso não encontrou; isso não prova inexistência.");
        System.out.println("Instruções (~) = estimativa instrumentada, não instruções da CPU/JVM.");
        System.out.println("Tempo (ms) = milissegundos; varia conforme máquina e execução.");
    }

    public static void main(String[] args) throws java.io.UnsupportedEncodingException {
        // Garante caracteres da tabela mesmo em ambientes sem UTF-8 padrao.
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        testesTroco();
        testesIntervalos();
        testesSomaPequenos();
        testesRainhas();
        testesGrandes();
    }
}
