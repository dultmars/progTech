package hu.nye.wumpusgame.service.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameBoardRepository extends JpaRepository<GameBoardEntity, Long> {
    List<GameBoardEntity> findAll();

}
