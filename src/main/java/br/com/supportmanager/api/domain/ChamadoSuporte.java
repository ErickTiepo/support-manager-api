package br.com.supportmanager.api.domain;

import br.com.supportmanager.api.enums.PrioridadeChamado;
import br.com.supportmanager.api.enums.StatusChamado;
import br.com.supportmanager.api.enums.TipoSentimento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chamados_suporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChamadoSuporte {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_mensagem", unique = true)
    private String idMensagem;

    @Column(name = "email_remetente", nullable = false)
    private String emailRemetente;

    @Column(name = "nome_remetente")
    private String nomeRemetente;

    @Column(nullable = false)
    private String assunto;

    @Column(name = "corpo_email", columnDefinition = "TEXT", nullable = false)
    private String corpoEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusChamado status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadeChamado prioridade;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TipoSentimento sentimento;

    @Column(name = "categoria_ia", length = 60)
    private String categoriaIa;

    @Column(name = "resposta_sugerida_ia", columnDefinition = "TEXT")
    private String respostaSugeridaIa;

    @Column(name = "motivo_critico", columnDefinition = "TEXT")
    private String motivoCritico;

    @Column(name = "respondido_em")
    private LocalDateTime respondidoEm;

    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NotaFiscal> notasFiscais = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public void adicionarNotaFiscal(NotaFiscal notaFiscal) {
        notasFiscais.add(notaFiscal);
        notaFiscal.setChamado(this);
    }
}