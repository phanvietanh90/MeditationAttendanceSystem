package edu.mum.waa.service;

import edu.mum.waa.entity.BlockEntity;
import edu.mum.waa.entity.UserEntity;
import edu.mum.waa.repository.BlockRepository;
import edu.mum.waa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class BlockService {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BlockEntity> getBlockList(Integer professorId) {
        return blockRepository.findAllByProfessorIdEquals(professorId);
    }

    public String getBlockName(Integer blockId) {
        return blockRepository.findByBlockIdEquals(blockId).getName();
    }

    public List<BlockEntity> getAllBlock() {
        return blockRepository.findAll();
    }

    public BlockEntity createBlock(BlockEntity blockEntity) {
        return blockRepository.save(blockEntity);
    }

//    @Transactional
    public void assignUserToBlock(UserEntity userEntity, BlockEntity blockEntity) {
//        userService.save(userEntity);
//        blockRepository.save(blockEntity);
        userEntity.getBlockList().add(blockEntity);
        blockEntity.getUserList().add(userEntity);

        userRepository.save(userEntity);
        blockRepository.save(blockEntity);
    }

    public void assignUserToBlock(UserEntity userEntity, Integer blockId) {
        BlockEntity blockEntity = blockRepository.findByBlockIdEquals(blockId);
        userEntity.getBlockList().add(blockEntity);
        blockEntity.getUserList().add(userEntity);

        userRepository.save(userEntity);
        blockRepository.save(blockEntity);
    }


}
