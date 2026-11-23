package br.com.supportmanager.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosNotaFiscalDTO(
        String chaveAcesso,
        String numeroNota,
        String serie,
        String cnpjEmitente,
        String nomeEmitente,
        BigDecimal valorTotal,
        LocalDateTime dataEmissao
) {
}