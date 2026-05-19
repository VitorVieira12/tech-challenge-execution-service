package com.techchallenge.execution.domain.repository;

import com.techchallenge.execution.domain.model.Execucao;
import com.techchallenge.execution.domain.model.StatusExecucao;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ExecucaoRepository extends MongoRepository<Execucao, String> {
    Optional<Execucao> findByOsId(Long osId);
    List<Execucao> findByStatusOrderByCriadoEmAsc(StatusExecucao status);
    List<Execucao> findAllByOrderByCriadoEmAsc();
}
