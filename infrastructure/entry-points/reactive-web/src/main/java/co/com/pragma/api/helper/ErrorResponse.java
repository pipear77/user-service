package co.com.pragma.api.helper;

import co.com.pragma.usecase.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public static Mono<ServerResponse> ErrorResponse(HttpStatus status, String mensaje) {
    return ServerResponse.status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new ErrorDto(mensaje, status.value()));
}
