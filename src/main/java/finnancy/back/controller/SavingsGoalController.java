package finnancy.back.controller;

import finnancy.back.model.SavingsGoal;
import finnancy.back.service.SavingsGoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

/**
 * Controlador REST para manejar operaciones con metas de ahorro
 */
@RestController
@RequestMapping("/api/savings-goals")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping
    @Operation(
        summary = "Crear meta de ahorro",
        description = "Crea una nueva meta de ahorro para un usuario."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Meta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public SavingsGoal createSavingsGoal(@RequestBody SavingsGoal savingsGoal) {
        return savingsGoalService.createSavingsGoal(savingsGoal);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar meta de ahorro",
        description = "Actualiza una meta de ahorro existente por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Meta actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Meta no encontrada")
    })
    public SavingsGoal updateSavingsGoal(
            @Parameter(description = "ID de la meta de ahorro") @PathVariable String id,
            @RequestBody SavingsGoal savingsGoal) {
        return savingsGoalService.updateSavingsGoal(id, savingsGoal);
    }

    @PostMapping("/{id}/add")
    @Operation(
        summary = "Agregar fondos a meta de ahorro",
        description = "Agrega una cantidad a una meta de ahorro existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fondos agregados exitosamente"),
        @ApiResponse(responseCode = "404", description = "Meta no encontrada")
    })
    public SavingsGoal addToSavingsGoal(
            @Parameter(description = "ID de la meta de ahorro") @PathVariable String id,
            @Parameter(description = "Cantidad a agregar") @RequestParam Double amount) {
        return savingsGoalService.addToSavingsGoal(id, amount);
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Obtener todas las metas de ahorro de un usuario",
        description = "Obtiene todas las metas de ahorro de un usuario específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metas obtenidas exitosamente")
    })
    public List<SavingsGoal> getAllUserSavingsGoals(
            @Parameter(description = "ID del usuario") @PathVariable String userId) {
        return savingsGoalService.getAllUserSavingsGoals(userId);
    }

    @GetMapping("/user/{userId}/active")
    @Operation(
        summary = "Obtener metas de ahorro activas",
        description = "Obtiene las metas de ahorro activas (no completadas) de un usuario."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metas activas obtenidas exitosamente")
    })
    public List<SavingsGoal> getActiveUserSavingsGoals(
            @Parameter(description = "ID del usuario") @PathVariable String userId) {
        return savingsGoalService.getActiveUserSavingsGoals(userId);
    }

    @GetMapping("/user/{userId}/completed")
    @Operation(
        summary = "Obtener metas de ahorro completadas",
        description = "Obtiene las metas de ahorro completadas de un usuario."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metas completadas obtenidas exitosamente")
    })
    public List<SavingsGoal> getCompletedUserSavingsGoals(
            @Parameter(description = "ID del usuario") @PathVariable String userId) {
        return savingsGoalService.getCompletedUserSavingsGoals(userId);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar meta de ahorro",
        description = "Elimina una meta de ahorro por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Meta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Meta no encontrada")
    })
    public ResponseEntity<?> deleteSavingsGoal(
            @Parameter(description = "ID de la meta de ahorro") @PathVariable String id) {
        savingsGoalService.deleteSavingsGoal(id);
        return ResponseEntity.ok().build();
    }
}