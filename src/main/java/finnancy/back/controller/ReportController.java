package finnancy.back.controller;

import finnancy.back.service.ReportService;
import finnancy.back.service.TransactionService;
import finnancy.back.model.Transaction;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final TransactionService transactionService;

    public ReportController(ReportService reportService, TransactionService transactionService) {
        this.reportService = reportService;
        this.transactionService = transactionService;
    }

    @GetMapping
    @Operation(
        summary = "Obtener reporte financiero",
        description = "Obtiene un reporte financiero filtrado por usuario, intervalo de tiempo, globalidad y tipo de transacción."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public Map<String, Object> getReport(
            @Parameter(description = "ID del usuario (opcional para reportes globales)") @RequestParam(required = false) String userId,
            @Parameter(description = "Intervalo del reporte: weekly, monthly, semiannual, annual") @RequestParam(defaultValue = "weekly") String interval,
            @Parameter(description = "Si es true, genera un reporte global") @RequestParam(defaultValue = "false") boolean global,
            @Parameter(description = "Tipo de transacción: income o expense") @RequestParam(required = false) String type
    ) {
        LocalDate now = LocalDate.now();
        return reportService.getReport(userId, interval, now, global, type);
    }

    @GetMapping("/debug-transactions/{userId}")
    @Operation(
        summary = "Debug de transacciones de usuario",
        description = "Obtiene todas las transacciones de un usuario para propósitos de debug."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public Map<String, Object> debugTransactions(
            @Parameter(description = "ID del usuario") @PathVariable String userId) {
        
        // Obtener todas las transacciones del usuario usando el método de debug
        List<Transaction> transactions = transactionService.getAllTransactionsForDebug(userId);
        
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("userId", userId);
        debugInfo.put("totalTransactions", transactions.size());
        debugInfo.put("transactions", transactions);
        debugInfo.put("timestamp", LocalDate.now().toString());
        
        // Información adicional de debug
        if (!transactions.isEmpty()) {
            debugInfo.put("firstTransaction", transactions.get(0));
            debugInfo.put("lastTransaction", transactions.get(transactions.size() - 1));
            
            long incomeCount = transactions.stream()
                .filter(t -> t.getType() != null && t.getType().toString().equalsIgnoreCase("INCOME"))
                .count();
            long expenseCount = transactions.stream()
                .filter(t -> t.getType() != null && t.getType().toString().equalsIgnoreCase("EXPENSE"))
                .count();
                
            debugInfo.put("incomeCount", incomeCount);
            debugInfo.put("expenseCount", expenseCount);
        } else {
            debugInfo.put("message", "No se encontraron transacciones para este usuario");
            debugInfo.put("possibleIssues", List.of(
                "El userId podría no existir en la base de datos",
                "Las transacciones podrían estar almacenadas con un userId diferente",
                "Problema de conexión con MongoDB",
                "Problema de configuración de la base de datos"
            ));
        }
        
        return debugInfo;
    }

    @GetMapping("/test-db")
    @Operation(
        summary = "Test de conexión a base de datos",
        description = "Verifica la conexión a MongoDB y muestra estadísticas básicas."
    )
    public Map<String, Object> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Contar total de transacciones
            List<Transaction> allTransactions = transactionService.getTransactionsByFilters(null, null, null, null, null, null);
            result.put("status", "success");
            result.put("message", "Conexión a MongoDB exitosa");
            result.put("totalTransactions", allTransactions.size());
            
            if (!allTransactions.isEmpty()) {
                // Obtener usuarios únicos
                long uniqueUsers = allTransactions.stream()
                    .map(Transaction::getUserId)
                    .distinct()
                    .count();
                result.put("uniqueUsers", uniqueUsers);
                
                // Mostrar algunos userIds de ejemplo
                List<String> sampleUserIds = allTransactions.stream()
                    .map(Transaction::getUserId)
                    .distinct()
                    .limit(5)
                    .toList();
                result.put("sampleUserIds", sampleUserIds);
            }
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Error al conectar con MongoDB: " + e.getMessage());
        }
        
        return result;
    }
}