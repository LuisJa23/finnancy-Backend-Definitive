package finnancy.back.controller;

import finnancy.back.service.ReportService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
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
}