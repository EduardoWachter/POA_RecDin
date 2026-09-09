public class DistED {
      // ED(S, T, i, j): int
        // S: String inicial, T: String final, i: [1..m], j:[1..n]
            // retorna o número mínimo de edições quando comparando
            // S[i] com T[j]. m é o tamanho de S, n o tamanho de T
    public int ED(String S, String T, int i, int j) {
        // Caso Base:
        // Quando ficamos sem caracteres para comparer em S ou em T. 
        //Se em ambas, o resultado é 0. Se uma das duas, retorna o restante dos caracteres da que não está vazia;
        if (i == -1) {
            
        }
        
        // Casos Recursivos
        // Se S[i] == T[i], chame recursivamente ED(S, T, i-1, j-1) (foi match, nãoprecisa fazer nada nesta posição, o custo é zero.
        // Se não, três chamadas recursivas são necessárias:
        // • Substituição: ED(S, T, i-1, j-1) + 1
        // • Inserção: ED(S, T, i, j-1) + 1
        // • Remoção: ED(S, T, i-1, j) + 1
        // • Retorne a que resultar em menor custo
}