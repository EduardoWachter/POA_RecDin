public class ex3 {

    // Contador de iterações
    public long iteracoes;

    public Integer backPack(Integer n, Integer c, Integer[][] itens) {

        int melhorValor = 0;

        iteracoes = 0;

        // 2^n combinações
        long totalCombinacoes = 1L << n;

        for (long combinacao = 0; combinacao < totalCombinacoes; combinacao++) {

            int pesoTotal = 0;
            int valorTotal = 0;

            // Percorre os itens
            for (int i = 0; i < n; i++) {

                iteracoes++;

                // Verifica se o item está na combinação
                if ((combinacao & (1L << i)) != 0) {

                    pesoTotal += itens[i][0];
                    valorTotal += itens[i][1];
                }
            }

            // Verifica se essa configuração é melhor
            if (pesoTotal <= c && valorTotal > melhorValor) {
                melhorValor = valorTotal;
            }
        }

        return melhorValor;
    }


    public static void main(String[] args) {

        int[] tamanhos = {4, 8, 16, 32, 128, 1000};

        for (int n : tamanhos) {

            System.out.println("\n==============================");
            System.out.println("n = " + n);

            // Criar os itens
            Integer[][] itens = new Integer[n][2];

            for (int i = 0; i < n; i++) {

                // Peso entre 1 e 20
                itens[i][0] = (int) (Math.random() * 20) + 1;

                // Valor entre 1 e 100
                itens[i][1] = (int) (Math.random() * 100) + 1;
            }

            // Capacidade da mochila
            int capacidade = n * 5;

            ex3 mochila = new ex3();

            long inicio = System.nanoTime();

            try {

                int resultado = mochila.backPack(
                        n,
                        capacidade,
                        itens
                );

                long fim = System.nanoTime();

                double tempo = (fim - inicio) / 1_000_000.0;

                System.out.println("Melhor valor: " + resultado);
                System.out.println("Iterações: " + mochila.iteracoes);
                System.out.println("Tempo: " + tempo + " ms");

            } catch (Exception e) {

                System.out.println("Execução interrompida!");
                System.out.println("Iterações realizadas: " + mochila.iteracoes);
            }
        }
    }
}

