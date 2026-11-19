package br.com.supportmanager.api.repository;

import br.com.supportmanager.api.domain.ChamadoSuporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChamadoSuporteRepository extends JpaRepository<ChamadoSuporte, UUID> {
}