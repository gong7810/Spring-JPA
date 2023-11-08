package board.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import org.springframework.transaction.annotation.Transactional;

public interface JpaBoardRepository extends JpaRepository<BoardEntity, Integer>{

	//@Query("select board from BoardEntity board left outer join fetch board.fileList order by board.boardIdx desc")
	List<BoardEntity> findAllByOrderByBoardIdxDesc();

	@Query("SELECT file FROM BoardFileEntity file WHERE file.idx = :idx")
	List<BoardFileEntity> selectBoardFile(@Param("idx") int idx);

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx AND file.idx = :idx")
	BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx")
	List<BoardFileEntity> findAllbyBoardFile(@Param("boardIdx") int boardIdx);

	@Query("SELECT board.hitCnt FROM BoardEntity board WHERE board.boardIdx = :boardIdx")
	int findHitCount(@Param("boardIdx") int boardIdx);

	@Query("SELECT board FROM BoardEntity board LEFT JOIN FETCH board.fileList WHERE board.title LIKE %:title%")
	List<BoardEntity> findAllByTitleContainingOrderByCreatedDatetime(String title);

	@Modifying
	@Query("DELETE FROM BoardFileEntity file WHERE file.idx = :idx")
	void deleteFileById(@Param("idx") int idx);

//	@Modifying
//	@Transactional
//	@Query(value = "INSERT INTO  BoardFileEntity (idx, originFileName, storedFilePath, fileSize, creator_id, created_datetime) " +
//			"VALUES (add.boardIdx, add.originFileName, add.storedFilePath, add.fileSize, 'admin', NOW())")
//	void insertFileList(@Param("addFileList") List<BoardFileEntity> add);
}
