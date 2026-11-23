package br.com.supportmanager.api.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class LeitorPdfNotaFiscal {

    private static final int MAXIMO_PAGINAS = 10;

    public String extrairTexto(InputStream pdfStream) {
        try {
            byte[] bytes = pdfStream.readAllBytes();

            try (PDDocument documento = Loader.loadPDF(bytes)) {
                if (documento.getNumberOfPages() > MAXIMO_PAGINAS) {
                    throw new IllegalArgumentException("O PDF excede o limite de " + MAXIMO_PAGINAS + " paginas.");
                }

                PDFTextStripper stripper = new PDFTextStripper();
                String texto = stripper.getText(documento).trim();

                if (texto.isEmpty()) {
                    throw new IllegalArgumentException("Nao foi possivel extrair texto do PDF.");
                }

                return texto;
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao ler o arquivo PDF.", e);
        }
    }
}