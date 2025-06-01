package finnancy.back.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping
    @Operation(
        summary = "Probar conexión",
        description = "Verifica la conexión con la base de datos MongoDB."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conexión exitosa")
    })
    public String testConnection() {
        return "Conexión a MongoDB establecida correctamente";
    }
}