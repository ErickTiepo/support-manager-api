package br.com.supportmanager.api.service;

import br.com.supportmanager.api.dto.DadosNotaFiscalDTO;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeitorXmlNotaFiscalTest {

    private final LeitorXmlNotaFiscal leitor = new LeitorXmlNotaFiscal();

    @Test
    void deveExtrairDadosDeXmlNfeValido() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <nfeProc>
                    <NFe>
                        <infNFe Id="NFe41261112345678000199550010000012341000012345">
                            <ide>
                                <nNF>1234</nNF>
                                <serie>1</serie>
                                <dhEmi>2026-11-22T14:30:00-03:00</dhEmi>
                            </ide>
                            <emit>
                                <CNPJ>12345678000199</CNPJ>
                                <xNome>EMPRESA FORNECEDORA LTDA</xNome>
                            </emit>
                            <total>
                                <ICMSTot>
                                    <vNF>1540.50</vNF>
                                </ICMSTot>
                            </total>
                        </infNFe>
                    </NFe>
                </nfeProc>
                """;

        var stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        DadosNotaFiscalDTO dados = leitor.ler(stream);

        assertEquals("41261112345678000199550010000012341000012345", dados.chaveAcesso());
        assertEquals("1234", dados.numeroNota());
        assertEquals("1", dados.serie());
        assertEquals("12345678000199", dados.cnpjEmitente());
        assertEquals("EMPRESA FORNECEDORA LTDA", dados.nomeEmitente());
        assertEquals(new BigDecimal("1540.50"), dados.valorTotal());
        assertEquals(LocalDateTime.of(2026, 11, 22, 14, 30), dados.dataEmissao());
    }

    @Test
    void deveBloquearXmlComTentativaDeXxe() {
        String xmlMalicioso = """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE foo [
                    <!ENTITY xxe SYSTEM "file:///etc/passwd">
                ]>
                <nfeProc>
                    <emit>
                        <xNome>&xxe;</xNome>
                    </emit>
                </nfeProc>
                """;

        var stream = new ByteArrayInputStream(xmlMalicioso.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> leitor.ler(stream));
    }
}