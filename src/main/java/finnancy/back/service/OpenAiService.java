package finnancy.back.service;

import finnancy.back.dto.OpenAiResponse;
import finnancy.back.model.Transaction;
import finnancy.back.model.SavingsGoal;
import finnancy.back.repository.TransactionRepository;
import finnancy.back.repository.SavingsGoalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import finnancy.back.enums.TransactionType;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    private final WebClient webClient;
    private final String openAiModel;
    private final TransactionRepository transactionRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public OpenAiService(WebClient openAiWebClient,
                       @Value("${openai.api.model}") String openAiModel,
                       TransactionRepository transactionRepository,
                       SavingsGoalRepository savingsGoalRepository) {
        this.webClient = openAiWebClient;
        this.openAiModel = openAiModel;
        this.transactionRepository = transactionRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public Mono<String> getFinancialAnswer(String userId, String question) {
        // Obtener datos del usuario
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        List<SavingsGoal> goals = savingsGoalRepository.findByUserId(userId);
        
        // Construir contexto
        String context = buildFinancialContext(transactions, goals);
        
        // Construir prompt con contexto
        String prompt = String.format(
            "Eres un asistente financiero. Aquí tienes información del usuario:\n%s\n\n" +
            "Pregunta: %s\n\n" +
            "Responde de manera concisa y útil basándote en los datos proporcionados. " +
            "Si no hay datos relevantes, indica que no tienes información suficiente.", 
            context, question);
        
        // Construir cuerpo de la solicitud
        Map<String, Object> requestBody = Map.of(
            "model", openAiModel,
            "prompt", prompt,
            "max_tokens", 300,
            "temperature", 0.3
        );

        return webClient.post()
                .uri("/v1/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    // Extraer la respuesta
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Object textObj = choices.get(0).get("text");
                        return textObj != null ? textObj.toString() : "";
                    }
                    return "";
                });
    }

    public String buildFinancialContext(List<Transaction> transactions, List<SavingsGoal> goals) {
        // Resumen de transacciones
        double totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
        
        double totalExpenses = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
        
        // Resumen de metas
        String goalsSummary = goals.stream()
                .map(g -> String.format("- %s: %.2f/%.2f (%.1f%%)", 
                    g.getName(), g.getCurrentAmount(), g.getTargetAmount(), g.getProgressPercentage()))
                .collect(Collectors.joining("\n"));
        
        // Construir contexto
        return String.format(
            "Resumen financiero:\n" +
            "Ingresos totales: %.2f\n" +
            "Gastos totales: %.2f\n" +
            "Balance: %.2f\n\n" +
            "Metas de ahorro:\n%s",
            totalIncome, totalExpenses, (totalIncome - totalExpenses),
            goalsSummary.isEmpty() ? "No hay metas registradas" : goalsSummary);
    }
}