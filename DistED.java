public class DistED { 
    // Contagem de iterações
    int iteracoes = 0;
      // ED(S, T, i, j): int
        // S: String inicial, T: String final, i: length de S, j: length de T
            // retorna o número mínimo de edições quando comparando
            // S[i] com T[j]. m é o tamanho de S, n o tamanho de T
    public int ED(String S, String T, int i, int j) {
        iteracoes++;
        // Caso Base:
        // Quando ficamos sem caracteres para comparar em S ou em T. 
        // Se em ambas, o resultado é 0. Se uma das duas, retorna o restante dos caracteres da que não está vazia;
        if (i == -1) {
            return j + 1;
        }
        if (j == -1) {
            return i + 1;
        }
                
        // Casos Recursivos
        // Se S[i] == T[j], chame recursivamente ED(S, T, i-1, j-1) (foi match, não precisa fazer nada nesta posição, o custo é zero.
        // Se não, três chamadas recursivas são necessárias:
        // • Substituição: ED(S, T, i-1, j-1) + 1
        // • Inserção: ED(S, T, i, j-1) + 1
        // • Remoção: ED(S, T, i-1, j) + 1
        // • Retorne a que resultar em menor custo
         if (S.charAt(i) == T.charAt(j)) {
            return ED(S, T, i - 1, j - 1);
        } else {
            int substituicao = ED(S, T, i - 1, j - 1) + 1;
            int insercao = ED(S, T, i, j - 1) + 1;
            int remocao = ED(S, T, i - 1, j) + 1;
            return Math.min(substituicao, Math.min(insercao, remocao));
        }
    }
}