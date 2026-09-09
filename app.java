public class app {
    public static void main(String[] args) {
        // roda o metodo não dinamico para calcular a distância de edição entre duas strings
        DistED distEd = new DistED();
        String S = "Maven, a Yiddish word meaning accumulator of knowledge, began as an attempt to " +
				"simplify the build processes in the Jakarta Turbine project. There were several" + 
				" projects, each with their own Ant build files, that were all slightly different." +
				"JARs were checked into CVS. We wanted a standard way to build the projects, a clear "+ 
				"definition of what the project consisted of, an easy way to publish project information" +
				"and a way to share JARs across several projects. The result is a tool that can now be" +
				"used for building and managing any Java-based project. We hope that we have created " +
				"something that will make the day-to-day work of Java developers easier and generally help " +
				"with the comprehension of any Java-based project.";
        String T = "This post is not about deep learning. But it could be might as well. This is the power of " +
				"kernels. They are universally applicable in any machine learning algorithm. Why you might" +
				"ask? I am going to try to answer this question in this article." + 
			        "Go to the profile of Marin Vlastelica Pogančić" + 
			        "Marin Vlastelica Pogančić Jun";
        int m = S.length();
        int n = T.length();
        int resultado = distEd.ED(S, T, m - 1, n - 1);
        System.out.println("Distância de edição: " + resultado);
        System.out.println("Número de iterações (programação recursiva): " + distEd.iteracoes);
        //roda o metodo dinamico para calcular a distância de edição entre duas strings
        DistEdPD distEdPD = new DistEdPD();
        int resultadoPD = distEdPD.distEdProgDina(S, T);
        System.out.println("Distância de edição (programação dinâmica): " + resultadoPD);
        System.out.println("Número de iterações (programação dinâmica): " + distEdPD.iteracoes);
    }
}
