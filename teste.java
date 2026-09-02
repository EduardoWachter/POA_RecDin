import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

public class teste {
    private static final int[] VALORES = {4, 8, 16, 32, 128, 1000};
    private static final String[] ALGORITMOS = {"fiboREC", "fibo", "memoizedFIBO"};

    public static void main(String[] args) {
        ex1 fibonacci = new ex1();
        Map<String, Map<Integer, long[]>> contagens = new LinkedHashMap<>();   // [iteracoes, instrucoes]
        Map<String, Map<Integer, BigInteger>> resultados = new LinkedHashMap<>();
        for (String alg : ALGORITMOS) {
            contagens.put(alg, new LinkedHashMap<>());
            resultados.put(alg, new LinkedHashMap<>());
        }

        for (int n : VALORES) {
            BigInteger esperado = fibonacciEsperado(n);

            if (n <= 32) { // acima disso fiboREC é inviável (custo exponencial)
                fibonacci.resetContadores();
                BigInteger r = fibonacci.fiboREC(n);
                verificar("fiboREC(" + n + ")", esperado, r);
                contagens.get("fiboREC").put(n, new long[]{fibonacci.getIteracoesREC(), fibonacci.getInstrucoesREC()});
                resultados.get("fiboREC").put(n, r);
            }

            fibonacci.resetContadores();
            BigInteger rFibo = fibonacci.fibo(n);
            verificar("fibo(" + n + ")", esperado, rFibo);
            contagens.get("fibo").put(n, new long[]{fibonacci.getIteracoesFibo(), fibonacci.getInstrucoesFibo()});
            resultados.get("fibo").put(n, rFibo);

            fibonacci.resetContadores();
            BigInteger rMemo = fibonacci.memoizedFIBO(new BigInteger[n + 1], n);
            verificar("memoizedFIBO(" + n + ")", esperado, rMemo);
            contagens.get("memoizedFIBO").put(n, new long[]{fibonacci.getIteracoesMemo(), fibonacci.getInstrucoesMemo()});
            resultados.get("memoizedFIBO").put(n, rMemo);
        }

        imprimirTabelaContagens("ITERAÇÕES", contagens, 0);
        imprimirTabelaContagens("INSTRUÇÕES", contagens, 1);
        imprimirTabelaResultados(resultados);
    }

    private static void imprimirTabelaContagens(String titulo, Map<String, Map<Integer, long[]>> contagens, int indice) {
        int largCol = 14;
        System.out.println();
        System.out.println("=== " + titulo + " ===");
        StringBuilder cab = new StringBuilder(String.format("%-16s", "algoritmo"));
        for (int n : VALORES) cab.append(String.format("%" + largCol + "s", "n=" + n));
        System.out.println(cab);
        for (String alg : ALGORITMOS) {
            StringBuilder linha = new StringBuilder(String.format("%-16s", alg));
            for (int n : VALORES) {
                long[] c = contagens.get(alg).get(n);
                String valor = (c == null) ? "-" : String.valueOf(c[indice]);
                linha.append(String.format("%" + largCol + "s", valor));
            }
            System.out.println(linha);
        }
    }

    private static void imprimirTabelaResultados(Map<String, Map<Integer, BigInteger>> resultados) {
        System.out.println();
        System.out.println("=== RESULTADOS fib(n) ===");
        for (String alg : ALGORITMOS) {
            System.out.println("-- " + alg + " --");
            for (int n : VALORES) {
                BigInteger v = resultados.get(alg).get(n);
                if (v == null)
                    System.out.println("  n=" + n + ": não executado (custo exponencial)");
                else
                    System.out.println("  n=" + n + ": " + resumo(v));
            }
        }
    }

    private static String resumo(BigInteger v) {
        String s = v.toString();
        if (s.length() <= 40) return s;
        return s.length() + " dígitos (" + s.substring(0, 15) + "..." + s.substring(s.length() - 15) + ")";
    }

    private static BigInteger fibonacciEsperado(int n) {
        BigInteger anterior = BigInteger.ZERO;
        BigInteger atual = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            BigInteger proximo = anterior.add(atual);
            anterior = atual;
            atual = proximo;
        }
        return anterior;
    }

    private static void verificar(String caso, BigInteger esperado, BigInteger resultado) {
        if (!esperado.equals(resultado))
            throw new AssertionError(caso + ": esperado " + esperado + ", obtido " + resultado);
    }
}