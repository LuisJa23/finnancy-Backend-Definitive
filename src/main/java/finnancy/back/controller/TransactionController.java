package finnancy.back.controller;

import finnancy.back.model.Transaction;
import finnancy.back.enums.TransactionType;
import finnancy.back.dto.FinancialSummaryDTO;
import finnancy.back.enums.PaymentMethod;
import finnancy.back.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para manejar operaciones con transacciones
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(
        summary = "Crear transacción",
        description = "Crea una nueva transacción para un usuario."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacción creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar transacción",
        description = "Actualiza una transacción existente por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacción actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    public Transaction updateTransaction(
            @Parameter(description = "ID de la transacción") @PathVariable String id,
            @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar transacción",
        description = "Elimina una transacción por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacción eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    public ResponseEntity<?> deleteTransaction(
            @Parameter(description = "ID de la transacción") @PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/summary/{userId}")
    @Operation(
        summary = "Resumen financiero de usuario",
        description = "Obtiene un resumen financiero para un usuario específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<FinancialSummaryDTO> getFinancialSummary(
            @Parameter(description = "ID del usuario") @PathVariable String userId) {
        FinancialSummaryDTO summary = transactionService.getFinancialSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping
    @Operation(
        summary = "Obtener transacciones",
        description = "Obtiene transacciones filtradas por usuario, fechas, categoría, tipo y método de pago."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public List<Transaction> getTransactions(
            @Parameter(description = "ID del usuario") @RequestParam String userId,
            @Parameter(description = "Fecha de inicio (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha de fin (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Categoría de la transacción") @RequestParam(required = false) String category,
            @Parameter(description = "Tipo de transacción") @RequestParam(required = false) TransactionType type,
            @Parameter(description = "Método de pago") @RequestParam(required = false) PaymentMethod paymentMethod) {

        return transactionService.getTransactionsByFilters(userId, startDate, endDate, category, type, paymentMethod);
    }
}