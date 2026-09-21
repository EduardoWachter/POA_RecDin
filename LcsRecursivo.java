public class LcsRecursivo {
    static long iteracoes = 0;

    // Limite de chamadas recursivas. Sem ele, o teste 4 levaria anos para terminar
    // (crescimento exponencial). Aumente se quiser deixar rodar por mais tempo.
    static final long LIMITE_ITERACOES = 1_000_000_000L;

    // Usada somente para interromper a recursão quando o limite é atingido
    // (sem stack trace, para não pesar).
    static class LimiteExcedidoException extends RuntimeException {
        LimiteExcedidoException() {
            super("Limite de iterações atingido", null, false, false);
        }
    }

    public static void main(String[] args) {
        testLcs("AGGTAB", "GXTXAYB");

        testLcs("ABCDEF", "ACE");

        testLcs("XMJYAUZ", "MZJAWXU");

        testLcs("PROGRAMACAO TESTE DRONES JK DOIDINHO", "LOGICA DE PROGAMACAO EM JAVA");

    }

    static void testLcs(String s1, String s2) {
        iteracoes = 0;
        String result;

        long inicio = System.nanoTime();
        try {
            result = String.valueOf(lcs(s1, s2));
        } catch (LimiteExcedidoException e) {
            result = "NÃO FINALIZOU (limite de " + LIMITE_ITERACOES + " iterações atingido)";
        }
        long fim = System.nanoTime();

        long tempo = fim - inicio;

        System.out.println("-----------------------");
        System.out.println("Primeira combinação: " + s1);
        System.out.println("Segunda combinação: " + s2);
        System.out.println("Iterações: " + iteracoes);
        System.out.println("Resultado: " + result);
        System.out.println("Tempo: " + tempo + " ns");
        System.out.println("Tempo: " + (tempo / 1_000_000.0) + " ms");
    }

    static int lcs(String s1, String s2) {
        return lcsRec(s1, s2, s1.length(), s2.length());
    }

    // LCS entre os m primeiros caracteres de s1 e os n primeiros de s2.
    // Recursão pura: os mesmos subproblemas são recalculados várias vezes
    // (não há tabela nem memorização), por isso o custo é exponencial.
    static int lcsRec(String s1, String s2, int m, int n) {
        if (iteracoes >= LIMITE_ITERACOES) {
            throw new LimiteExcedidoException();
        }
        iteracoes++; // conta cada chamada (cada subproblema resolvido)

        // caso base: alguma das strings ficou vazia
        if (m == 0 || n == 0) {
            return 0;
        }

        // últimos caracteres iguais: entram na subsequência
        if (s1.charAt(m - 1) == s2.charAt(n - 1)) {
            return 1 + lcsRec(s1, s2, m - 1, n - 1);
        }

        // diferentes: descarta um caractere de cada lado e fica com o melhor
        return Math.max(lcsRec(s1, s2, m - 1, n), lcsRec(s1, s2, m, n - 1));
    }
}