package io.github.solvo.infrastructure.authorization;

import io.github.solvo.application.ports.out.AuthorizationServicePort;
import io.github.solvo.domain.entities.Transfer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthorizationServiceAdapter implements AuthorizationServicePort {

    private final RestClient restClient;

    public AuthorizationServiceAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public boolean authorize(Transfer transferRequest) {
        AuthorizationResponse response = restClient.get()
                .uri("https://util.devi.tools/api/v2/authorize")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AuthorizationResponse.class);

        return response != null && response.data().authorization();
    }
}
