package br.com.supportmanager.api.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorArquivoTest {

    private final ValidadorArquivo validador = new ValidadorArquivo();

    @Test
    void deveAceitarArquivoPdfValido() {
        byte[] conteudoPdf = "%PDF-1.4\n%conteudo teste".getBytes(StandardCharsets.UTF_8);
        var arquivo = new MockMultipartFile("arquivo", "nota.pdf", "application/pdf", conteudoPdf);

        String tipoDetectado = validador.validar(arquivo);

        assertEquals("application/pdf", tipoDetectado);
    }

    @Test
    void deveAceitarArquivoXmlValido() {
        byte[] conteudoXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><nfeProc></nfeProc>".getBytes(StandardCharsets.UTF_8);
        var arquivo = new MockMultipartFile("arquivo", "nota.xml", "application/xml", conteudoXml);

        String tipoDetectado = validador.validar(arquivo);

        assertEquals("application/xml", tipoDetectado);
    }

    @Test
    void deveBloquearArquivoDisfarcadoDePdf() {
        byte[] textoComum = "esse arquivo nao e um pdf de verdade".getBytes(StandardCharsets.UTF_8);
        var arquivoFalso = new MockMultipartFile("arquivo", "virus.pdf", "application/pdf", textoComum);

        assertThrows(IllegalArgumentException.class, () -> validador.validar(arquivoFalso));
    }

    @Test
    void deveBloquearArquivoVazio() {
        var arquivoVazio = new MockMultipartFile("arquivo", "vazio.pdf", "application/pdf", new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> validador.validar(arquivoVazio));
    }
}