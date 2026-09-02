public class ex4 {
    //matriz[linha][coluna]
    public Integer backPackPD(Integer N, Integer C, Map<Integer, Integer> itens) {
    N = 4;
    C = 7;
    itens.put(0, null);   // (O índice 0 guarda null), Tupla com peso e valor
    Integer[][] maxTab = new Integer[N+1][C+1];

    //Inicialize com 0 toda a linha 0 e também a coluna 0;
    for (int i = 0; i<=N; i++) {
        maxTab[i][0] = 0;
    }
    for (int j = 0; j<=C; j++) {
        maxTab[0][j] = 0;
    }

    for (int i = 1; i<=N; i++) {
        for (int j = 1; j<=C; j++) {
            if (itens.get(i) <= j) { // se o item cabe na mochila atual
                maxTab[i][j] = Math.max(maxTab[i-1][j], itens[i][1] + maxTab[i-1][j - itens[i][0]]);
            } else {
                maxTab[i][j] = maxTab[i-1][j];
            }
        }
    }

    return maxTab[N][C]; // valor máximo para uma mochila de capacidade C e 		         
                            //que pode conter itens que vão do item 1 até o item N.
    }
}

