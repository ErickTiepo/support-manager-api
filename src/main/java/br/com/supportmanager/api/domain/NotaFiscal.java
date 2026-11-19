package br.com.supportmanager.api.domain;

import br.com.supportmanager.api.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notas_fiscais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chamado_id", nullable = false)
    private ChamadoSuporte chamado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;

    @Column(name = "chave_acesso", unique = true, length = 44)
    private String chaveAcesso;

    @Column(name = "numero_nota", length = 20)
    private String numeroNota;

    @Column(length = 10)
    private String serie;

    @Column(name = "cnpj_emitente", length = 14)
    private String cnpjEmitente;

    @Column(name = "nome_emitente")
    private String nomeEmitente;

    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_emissao")
    private LocalDateTime dataEmissao;

    @Column(name = "nome_arquivo_original", nullable = false)
    private String nomeArquivoOriginal;

    @Column(name = "caminho_armazenamento")
    private String caminhoArmazenamento;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
}