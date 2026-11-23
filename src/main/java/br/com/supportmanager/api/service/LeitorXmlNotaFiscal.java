package br.com.supportmanager.api.service;

import br.com.supportmanager.api.dto.DadosNotaFiscalDTO;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
public class LeitorXmlNotaFiscal {

    public DadosNotaFiscalDTO ler(InputStream xmlStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);
            doc.getDocumentElement().normalize();

            String chaveAcesso = extrairChaveAcesso(doc);
            if (chaveAcesso == null || chaveAcesso.length() != 44) {
                throw new IllegalArgumentException("Chave de acesso da NF-e invalida ou ausente.");
            }
            String numeroNota = extrairTag(doc, "nNF");
            String serie = extrairTag(doc, "serie");
            String cnpjEmitente = extrairTagDeGrupo(doc, "emit", "CNPJ");
            String nomeEmitente = extrairTagDeGrupo(doc, "emit", "xNome");
            BigDecimal valorTotal = parseValor(extrairTag(doc, "vNF"));
            LocalDateTime dataEmissao = parseData(extrairTag(doc, "dhEmi"));

            return new DadosNotaFiscalDTO(
                    chaveAcesso,
                    numeroNota,
                    serie,
                    cnpjEmitente,
                    nomeEmitente,
                    valorTotal,
                    dataEmissao
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao ler o XML da nota fiscal.", e);
        }
    }

    private String extrairChaveAcesso(Document doc) {
        NodeList nodes = doc.getElementsByTagNameNS("*", "infNFe");
        if (nodes.getLength() > 0 && nodes.item(0) instanceof Element infNFe) {
            String id = infNFe.getAttribute("Id");
            if (id != null && id.startsWith("NFe")) {
                return id.substring(3);
            }
        }
        return extrairTag(doc, "chNFe");
    }

    private String extrairTagDeGrupo(Document doc, String grupo, String tag) {
        NodeList grupoNodes = doc.getElementsByTagNameNS("*", grupo);
        if (grupoNodes.getLength() > 0 && grupoNodes.item(0) instanceof Element elemento) {
            NodeList tagNodes = elemento.getElementsByTagNameNS("*", tag);
            if (tagNodes.getLength() > 0) {
                return tagNodes.item(0).getTextContent().trim();
            }
        }
        return null;
    }

    private String extrairTag(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagNameNS("*", tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }

    private BigDecimal parseValor(String valor) {
        return valor != null ? new BigDecimal(valor) : null;
    }

    private LocalDateTime parseData(String dhEmi) {
        return dhEmi != null ? OffsetDateTime.parse(dhEmi).toLocalDateTime() : null;
    }
}