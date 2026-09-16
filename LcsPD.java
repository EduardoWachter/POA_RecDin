public class LcsPD {
    static long iteracoes = 0;

    public static void main(String[] args) {
        
        testLcs("AGGTAB", "GXTXAYB");

        testLcs("ABCDEF", "ACE");

        testLcs("XMJYAUZ", "MZJAWXU");

        testLcs("PROGRAMACAO TESTE DRONES JK DOIDINHO", "LOGICA DE PROGAMACAO EM JAVA");

    }

    static void testLcs(String s1, String s2) {
        iteracoes = 0;

        long inicio = System.nanoTime();
        long result = lcs(s1, s2);
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
        int m = s1.length();
        int n = s2.length();

        int[][] matriz = new int[m + 1][n + 1];

        for (int i = 0; i < matriz[0].length; i++) {
            iteracoes++;
            matriz[0][i] = 0;
        }

        for (int j = 0; j < matriz.length; j++) {
            iteracoes++;
            matriz[j][0] = 0;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                iteracoes++;

                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    matriz[i][j] = matriz[i - 1][j - 1] + 1;
                } else {
                    matriz[i][j] = Math.max(matriz[i - 1][j], matriz[i][j - 1]);
                }
            }
        }

        return matriz[m][n];
    }
}