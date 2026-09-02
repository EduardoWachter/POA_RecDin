import java.math.BigInteger;

public class teste {
    public static void main(String[] args) {
        ex1 fibonacci = new ex1();
        int[] valoresComuns = {4, 8, 16, 32};

        for (int valor : valoresComuns) {
            BigInteger esperado = fibonacciEsperado(valor);
            verificar("fiboREC(" + valor + ")", esperado, fibonacci.fiboREC(valor));
            verificar("fibo(" + valor + ")", esperado, fibonacci.fibo(valor));
            verificar("memoizedFIBO(" + valor + ")", esperado,
                    fibonacci.memoizedFIBO(new BigInteger[valor + 1], valor));
        }

        for (int valor : new int[] {128, 1000}) {
            BigInteger esperado = fibonacciEsperado(valor);
            verificar("fibo(" + valor + ")", esperado, fibonacci.fibo(valor));
            verificar("memoizedFIBO(" + valor + ")", esperado,
                    fibonacci.memoizedFIBO(new BigInteger[valor + 1], valor));
        }
    }

    private static BigInteger fibonacciEsperado(int n) {
        BigInteger anterior = BigInteger.ZERO;
        BigInteger atual = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            BigInteger proximo = anterior.add(atual);
            anterior = atual;
            atual = proximo;
        }
        return anterior;
    }

    private static void verificar(String caso, BigInteger esperado, BigInteger resultado) {
        System.out.println(caso + ": esperado " + esperado + ", obtido " + resultado);
        if (!esperado.equals(resultado)) {
            throw new AssertionError(caso + ": esperado " + esperado + ", obtido " + resultado);
        }
    }
}