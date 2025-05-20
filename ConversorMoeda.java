import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMoeda {
    // Chave da API e URL base para obter taxas de câmbio
    private static final String API_KEY = "ed7d00b95d21f0ddf580cc13";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    // Método para obter a cotação de uma moeda em relação a outra
    public static double obterCotacao(String moedaOrigem, String moedaDestino) {
        try {
            // Cria a URL completa da API com a moeda de origem
            URL url = new URL(BASE_URL + moedaOrigem);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET"); // Define o método de requisição como GET

            // Lê a resposta da API e converte para string
            Scanner scanner = new Scanner(conexao.getInputStream());
            String resposta = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Converte a string recebida em um objeto JSON
            JSONObject json = new JSONObject(resposta);
            JSONObject taxas = json.getJSONObject("conversion_rates");

            // Verifica se a moeda de destino está presente no JSON
            if (taxas.has(moedaDestino)) {
                return taxas.getDouble(moedaDestino); // Retorna a taxa de câmbio
            } else {
                System.out.println("Erro: Cotação não encontrada para " + moedaDestino);
                return 0;
            }

        } catch (Exception e) {
            System.out.println("Erro ao obter cotação.");
            return 0;
        }
    }

    // Método principal para interação com o usuário
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Exibição do menu de opções
        System.out.println("Escolha a conversão:");
        System.out.println("1. Dólar -> Real");
        System.out.println("2. Real -> Dólar");
        System.out.println("3. Euro -> Real");
        System.out.println("4. Real -> Euro");
        System.out.println("5. Libra -> Real");
        System.out.println("6. Real -> Libra");

        int opcao = scanner.nextInt(); // Captura a opção escolhida pelo usuário
        System.out.print("Digite o valor a ser convertido: ");
        double valor = scanner.nextDouble(); // Captura o valor a ser convertido

        // Define as moedas de origem e destino com base na escolha do usuário
        String moedaOrigem = switch (opcao) {
            case 1 -> "USD";
            case 2, 4, 6 -> "BRL";
            case 3 -> "EUR";
            case 5 -> "GBP";
            default -> {
                System.out.println("Opção inválida.");
                yield "";
            }
        };

        String moedaDestino = switch (opcao) {
            case 1 -> "BRL";
            case 2 -> "USD";
            case 3 -> "BRL";
            case 4 -> "EUR";
            case 5 -> "BRL";
            case 6 -> "GBP";
            default -> "";
        };

        // Se as moedas forem válidas, realiza a conversão
        if (!moedaOrigem.isEmpty() && !moedaDestino.isEmpty()) {
            double cotacao = obterCotacao(moedaOrigem, moedaDestino);
            double resultado = valor * cotacao;
            System.out.printf("O valor convertido é: %.2f %s%n", resultado, moedaDestino);
        }

        scanner.close(); // Fecha o scanner para liberar recursos
        System.out.println("Conversão concluída!");
    }
}