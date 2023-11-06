package board.board.service;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.entity.BoardFileEntity;
import board.board.mapper.BoardMapper;
import board.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BoardDto> selectBoardList() throws Exception {
		return boardMapper.selectBoardList();
	}

	@Override
	public List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception{
		return boardMapper.selectBoardFileList(boardIdx);
	}
	
	@Override
	public void insertBoard(BoardDto board, List<BoardFileDto> list) throws Exception {

			boardMapper.insertBoard(board);

			int boardIdx = board.getBoardIdx();

			list.stream().forEach(BoardFileDto -> BoardFileDto.setBoardIdx(boardIdx));

			log.debug("보드파일 객체 상태는 : " + list);

			for(BoardFileDto l : list){
				if(!ObjectUtils.isEmpty(l.getOriginalFileName())){
					if (CollectionUtils.isEmpty(list) == false) {
						boardMapper.insertBoardFileList(list);
					}
				}
			}
//			파일이 안들어있으면 빈 파일명으로 t_file에 insert되는 오류 해결
	}

	@Override
	public void insertBoard2(BoardDto board, List<BoardFileEntity> list) throws Exception {

	}

	@Override
	public BoardDto selectBoardDetail(int boardIdx) throws Exception{
		BoardDto board = boardMapper.selectBoardDetail(boardIdx);
		List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
		board.setFileList(fileList);
		
		boardMapper.updateHitCount(boardIdx);
		
		return board;
	}
	
	@Override
	public void updateBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		List<BoardFileDto> addFileList = fileUtils.parseFileInfo1(board.getBoardIdx(), multipartHttpServletRequest);
		if(CollectionUtils.isEmpty(addFileList) == false){
			boardMapper.insertBoardFileList(addFileList);
		}
		boardMapper.updateBoard(board);
	}

	@Override
	public void deleteBoard(int boardIdx) throws Exception {
		boardMapper.deleteBoard(boardIdx);
	}

	@Override
	public void deleteBoardFile(int idx, int boardIdx) throws Exception {
		boardMapper.deleteBoardFile(idx, boardIdx);
	}

	@Override
	public BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception {
		return boardMapper.selectBoardFileInformation(idx, boardIdx);
	}
}	

