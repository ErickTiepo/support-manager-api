package br.com.supportmanager.api.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeitorPdfNotaFiscalTest {

    private final LeitorPdfNotaFiscal leitor = new LeitorPdfNotaFiscal();

    @Test
    void deveExtrairTextoDePdfValido() throws Exception {
        byte[] pdfBytes = criarPdfComTexto("DANFE NF-e 1234 Valor R$ 500,00");

        String textoExtraido = leitor.extrairTexto(new ByteArrayInputStream(pdfBytes));

        assertEquals("DANFE NF-e 1234 Valor R$ 500,00", textoExtraido);
    }

    @Test
    void deveRejeitarPdfSemTexto() throws Exception {
        byte[] pdfVazio = criarPdfComPaginas(1);

        assertThrows(IllegalArgumentException.class,
                () -> leitor.extrairTexto(new ByteArrayInputStream(pdfVazio)));
    }

    @Test
    void deveRejeitarPdfComMaisDeDezPaginas() throws Exception {
        byte[] pdfGrande = criarPdfComPaginas(11);

        assertThrows(IllegalArgumentException.class,
                () -> leitor.extrairTexto(new ByteArrayInputStream(pdfGrande)));
    }

    private byte[] criarPdfComTexto(String texto) throws Exception {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(50, 700);
                content.showText(texto);
                content.endText();
            }

            doc.save(out);
            return out.toByteArray();
        }
    }

    private byte[] criarPdfComPaginas(int quantidade) throws Exception {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            for (int i = 0; i < quantidade; i++) {
                doc.addPage(new PDPage());
            }
            doc.save(out);
            return out.toByteArray();
        }
    }
}