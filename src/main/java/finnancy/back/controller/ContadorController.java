package finnancy.back.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContadorController {

    private int contador = 0;

    @GetMapping("/contador")
    @Operation(
        summary = "Obtener contador",
        description = "Incrementa y devuelve el valor actual del contador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contador obtenido exitosamente")
    })
    public Map<String, Object> obtenerContador() {
        contador++;
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "contador: " + contador);
        return response;
    }
}