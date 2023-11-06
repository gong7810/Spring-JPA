package board.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;

import javax.transaction.Transactional;

public interface JpaBoardRepository extends JpaRepository<BoardEntity, Integer>{

	//@Query("select board from BoardEntity board left outer join fetch board.fileList order by board.boardIdx desc")
	List<BoardEntity> findAllByOrderByBoardIdxDesc();

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx AND file.idx = :idx")
	BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx")
	List<BoardFileEntity> findAllbyBoardFile(@Param("boardIdx") int boardIdx);

	@Query("select board from BoardEntity board left join fetch board.fileList where board.title like %:title%")
	List<BoardEntity> findAllByTitleContainingOrderByCreatedDatetime(String title);
}
