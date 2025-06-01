package finnancy.back.controller;

import finnancy.back.dto.OpenAiRequest;
import finnancy.back.dto.OpenAiResponse;
import finnancy.back.service.OpenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/ai")
public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/ask/{userId}")
    @Operation(
        summary = "Pregunta financiera a la IA",
        description = "Envía una pregunta financiera personalizada para un usuario y recibe una respuesta generada por IA."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuesta generada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Pregunta inválida o error de procesamiento")
    })
    public Mono<ResponseEntity<OpenAiResponse>> askFinancialQuestion(
            @Parameter(description = "ID del usuario") @PathVariable String userId,
            @RequestBody OpenAiRequest request) {
        
        return openAiService.getFinancialAnswer(userId, request.getQuestion())
                .map(answer -> {
                    OpenAiResponse response = new OpenAiResponse();
                    response.setAnswer(answer);
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}