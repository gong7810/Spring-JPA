package board.board.service;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.entity.BoardFileEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public interface BoardService {
	
	List<BoardDto> selectBoardList() throws Exception;

	List<Integer> selectidxList(int boardIdx) throws Exception;

	List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;
	
	void insertBoard(BoardDto board, List<BoardFileDto> list) throws Exception;

	void insertBoard2(BoardDto board, List<BoardFileEntity> list) throws Exception;

	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;

	void deleteBoardFile(int idx) throws Exception;

	BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception; 
}
