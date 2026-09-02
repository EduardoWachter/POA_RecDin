import java.math.BigInteger;
import java.util.Map;

public class ex4 {
    Integer backPackPD(Integer N, Integer C, Map<Integer, Integer> itens) {
    N = 4;
    C = 7;
    itens[N+1];   // (O índice 0 guarda null), Tupla com peso e valor
    maxTab[N+1][C+1];

    Inicialize com 0 toda a linha 0 e também a coluna 0;
    para i = 1 até N
        para j = 1 até C
            se item itens[i].peso <= j // se o item cabe na mochila atual
                maxTab[i][j] = Max(maxTab[i-1][j], 
                                itens[i].valor + 
                                    maxTab[i-1][j – itens[i].peso]);
            senão
                maxTab[i][j] = maxTab[i-1][j];

    retorne maxTab[N][C] // valor máximo para uma mochila de capacidade C e 		         
                            //que pode conter itens que vão do item 1 até o item N.
    }
}
