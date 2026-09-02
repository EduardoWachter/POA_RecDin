import java.math.BigInteger;
import java.util.Arrays;

// IMPLEMENTAÇÕES DE FIBONACCI
public class ex1 {
    public BigInteger fiboREC(int n) {
        if (n <= 1)
            return BigInteger.valueOf(n);
        else {
            BigInteger a = fiboREC(n - 1);
            BigInteger b = fiboREC(n - 2);
            return a.add(b);
        }
    }

    public BigInteger fibo(int n) {
        BigInteger[] f = new BigInteger[n + 1];
        f[0] = BigInteger.ZERO;
        if (n == 0)
            return f[0];
        f[1] = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            f[i] = f[i - 1].add(f[i - 2]);
        }
        return f[n];
    }

    public BigInteger memoizedFIBO(BigInteger[] f, int n) {
        Arrays.fill(f, null);
        return lookupFIBO(f, n);
    }

    public BigInteger lookupFIBO(BigInteger[] f, int n) {
        if (f[n] != null)
            return f[n];
        if (n <= 1)
            f[n] = BigInteger.valueOf(n);
        else
            f[n] = lookupFIBO(f, n - 1).add(lookupFIBO(f, n - 2));
        return f[n];
    }
}