public class DistEdPD {
    // Assumindo os Custos: Remoção=R, Inserção=I , Substituição=S e Match=M=0;
    // Contagem de iterações
    int iteracoes = 0;

    public Integer distEdProgDina(String A, String B) {
        int m = A.length();
        int n = B.length();
        int[][] matriz = new int[m + 1][n + 1];
        matriz[0][0] = 0;

        for (int i = 1; i <= m; i++) {
            matriz[i][0] = matriz[i - 1][0] + 1;  // soma uma I;
        }
        for (int j = 1; j <= n; j++) {
            matriz[0][j] = matriz[0][j - 1] + 1;  // Soma uma R;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int custoExtra;
                if (A.charAt(i - 1) == B.charAt(j - 1)) {
                    custoExtra = 0;  // Operação M;
                } else {
                    custoExtra = 1;  // Operação S;
                }
                matriz[i][j] = Math.min(Math.min(matriz[i - 1][j] + 1, matriz[i][j - 1] + 1), matriz[i - 1][j - 1] + custoExtra);
                iteracoes++;
            }
        }
        return matriz[m][n];
    }
}
