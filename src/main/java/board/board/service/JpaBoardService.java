package board.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;

public interface JpaBoardService {

	List<BoardEntity> selectBoardList() throws Exception;

	List<String> selectBoardFilePathList(int idx) throws Exception;

	List<BoardFileEntity> selectBoardFileList(int boardIdx) throws Exception;

	void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

	void saveBoard2(int boardIdx, BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

	BoardEntity selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

	void deleteBoard(int boardIdx);

	void deleteBoardFile(int idx);
	BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception;

	List<BoardEntity> searchingBoardByTitle(String title) throws Exception;

}
