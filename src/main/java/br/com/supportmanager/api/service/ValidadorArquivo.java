package br.com.supportmanager.api.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ValidadorArquivo {

    private static final List<String> TIPOS_PERMITIDOS = List.of(
            "application/pdf",
            "application/xml",
            "text/xml"
    );

    private final Tika tika = new Tika();

    public String validar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado esta vazio.");
        }

        try (InputStream inputStream = arquivo.getInputStream()) {
            String tipoReal = tika.detect(inputStream);

            if (!TIPOS_PERMITIDOS.contains(tipoReal)) {
                throw new IllegalArgumentException("Tipo de arquivo nao permitido: " + tipoReal);
            }

            return tipoReal;
        } catch (IOException e) {
            throw new IllegalArgumentException("Nao foi possivel ler o arquivo enviado.", e);
        }
    }
}