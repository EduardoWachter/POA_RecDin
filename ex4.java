public class ex4 {
    
// Retorna o valor máximo que pode ser colocado na mochila
    public static int backPackPD(int N, int C, int[][] itens) {

        // Tabela:
        // linhas = quantidade de itens
        // colunas = capacidade da mochila
        int[][] maxTab = new int[N + 1][C + 1];

        long iteracoes = 0;

        // Linha 0 e coluna 0 já são inicializadas com 0
        // pelo Java.

        // Percorre todos os itens
        for (int i = 1; i <= N; i++) {

            // Percorre todas as capacidades
            for (int j = 1; j <= C; j++) {

                iteracoes++;

                int peso = itens[i - 1][0];
                int valor = itens[i - 1][1];

                // Se o item cabe na mochila
                if (peso <= j) {

                    maxTab[i][j] = Math.max(
                        maxTab[i - 1][j],
                        valor + maxTab[i - 1][j - peso]
                    );

                } else {

                    // Item não cabe
                    maxTab[i][j] = maxTab[i - 1][j];
                }
            }
        }

        System.out.println("n = " + N);
        System.out.println("Iterações: " + iteracoes);
        System.out.println("Melhor valor: " + maxTab[N][C]);
        System.out.println("==============================");

        return maxTab[N][C];
    }


    public static void main(String[] args) {

        int[] tamanhos = {4, 8, 16, 32, 128, 1000};

        for (int n : tamanhos) {

            // Matriz:
            // [peso, valor]
            int[][] itens = new int[n][2];

            // Geração dos mesmos tipos de dados
            // utilizados no teste da força bruta
            for (int i = 0; i < n; i++) {

                itens[i][0] = (i % 10) + 1;          // peso
                itens[i][1] = ((i * 17) % 100) + 1;  // valor
            }

            // Capacidade da mochila
            int capacidade = n * 3;

            backPackPD(n, capacidade, itens);
        }
    }
}