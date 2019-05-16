package edu.mum.waa.repository;

import edu.mum.waa.entity.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockRepository extends JpaRepository<BlockEntity, Integer> {
    BlockEntity findByNameEquals(String blockName);
    List<BlockEntity> findAllByProfessorIdEquals(Integer professorId);
}
