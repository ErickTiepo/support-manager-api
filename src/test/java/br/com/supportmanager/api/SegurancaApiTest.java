package br.com.supportmanager.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SegurancaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.security.api-key}")
    private String apiKey;

    @Test
    void deveBloquearRequisicaoSemApiKey() throws Exception {
        mockMvc.perform(get("/api/teste"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveBloquearRequisicaoComApiKeyInvalida() throws Exception {
        mockMvc.perform(get("/api/teste")
                        .header("X-API-Key", "chave-errada"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveLiberarRequisicaoComApiKeyValida() throws Exception {
        mockMvc.perform(get("/api/teste")
                        .header("X-API-Key", apiKey))
                .andExpect(status().isNotFound());
    }
}