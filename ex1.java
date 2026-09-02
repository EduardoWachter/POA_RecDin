import java.math.BigInteger;

public class ex1 {
    private long iteracoesREC, instrucoesREC;
    private long iteracoesFibo, instrucoesFibo;
    private long iteracoesMemo, instrucoesMemo;

    public void resetContadores() {
        iteracoesREC = instrucoesREC = 0;
        iteracoesFibo = instrucoesFibo = 0;
        iteracoesMemo = instrucoesMemo = 0;
    }

    public long getIteracoesREC()  { return iteracoesREC; }
    public long getInstrucoesREC() { return instrucoesREC; }
    public long getIteracoesFibo()  { return iteracoesFibo; }
    public long getInstrucoesFibo() { return instrucoesFibo; }
    public long getIteracoesMemo()  { return iteracoesMemo; }
    public long getInstrucoesMemo() { return instrucoesMemo; }

    public BigInteger fiboREC(int n) {
        iteracoesREC++;                 // cada chamada = 1 "iteração" da recursão
        instrucoesREC++;                // comparação n <= 1
        if (n <= 1) {
            instrucoesREC++;
            return BigInteger.valueOf(n);
        } else {
            BigInteger a = fiboREC(n - 1);
            BigInteger b = fiboREC(n - 2);
            instrucoesREC++;            
            return a.add(b); // soma a + b
        }
    }

    public BigInteger fibo(int n) {
        BigInteger[] f = new BigInteger[n + 1];
        f[0] = BigInteger.ZERO;
        instrucoesFibo++;
        if (n == 0)
            return f[0];
        f[1] = BigInteger.ONE;
        instrucoesFibo++;
        for (int i = 2; i <= n; i++) {
            iteracoesFibo++;
            f[i] = f[i - 1].add(f[i - 2]);
            instrucoesFibo += 2;        // 1 soma + 1 atribuição
        }
        return f[n];
    }

    public BigInteger memoizedFIBO(BigInteger[] f, int n) {
        for (int i = 0; i <= n; i++) {
            iteracoesMemo++;
            f[i] = null;
            instrucoesMemo++;
        }
        return lookupFIBO(f, n);
    }

    public BigInteger lookupFIBO(BigInteger[] f, int n) {
        iteracoesMemo++;                // cada chamada = 1 "iteração" da recursão
        instrucoesMemo++;               // comparação f[n] != null
        if (f[n] != null)
            return f[n];
        instrucoesMemo++;               // comparação n <= 1
        if (n <= 1) {
            f[n] = BigInteger.valueOf(n);
            instrucoesMemo++;
        } else {
            f[n] = lookupFIBO(f, n - 1).add(lookupFIBO(f, n - 2));
            instrucoesMemo++;           // soma
        }
        return f[n];
    }
}