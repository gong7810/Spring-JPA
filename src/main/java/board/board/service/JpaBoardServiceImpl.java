package board.board.service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import board.board.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.repository.JpaBoardRepository;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

    @Autowired
    JpaBoardRepository jpaBoardRepository;


    @Override
    public List<BoardEntity> selectBoardList() throws Exception {
        List<BoardEntity> boardEntities = jpaBoardRepository.findAll();
//		for (BoardEntity boardEntity : boardEntities) {
//
//			boardEntity.getFileList().stream().forEach(boardFileEntity -> boardFileEntity.getIdx());
//		}
        return boardEntities;
    }

    @Override
    public void saveBoard(BoardEntity board) throws Exception {
        board.setCreatorId("admin");

        jpaBoardRepository.save(board);


    }

    @Override
    public BoardEntity selectBoardDetail(int boardIdx) throws Exception {
        Optional<BoardEntity> optional = jpaBoardRepository.findById(boardIdx);
        if (optional.isPresent()) {
            BoardEntity board = optional.get();
//			if(board.getFileList().size()>0) {
//				board.getFileList().get(0).getBoardEntity().getBoardIdx();
//			}
            board.setHitCnt(board.getHitCnt() + 1);
            jpaBoardRepository.save(board);
//			jpaBoardRepository.flush(); 도 가능
            return board;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void deleteBoard(int boardIdx) {
        deleteBoardFile(boardIdx);
        //파일을 삭제하고싶으면 사용, 저장해놓고싶으면 주석처리
        jpaBoardRepository.deleteById(boardIdx);
    }

    @Override
    public void deleteBoardFile(int boardIdx) {
        int idx = boardIdx + 1;
        List<BoardFileEntity> boardFileImgs = new ArrayList<BoardFileEntity>();
        while (jpaBoardRepository.findBoardFile(boardIdx, idx) != null) {
            BoardFileEntity boardFileImg = jpaBoardRepository.findBoardFile(boardIdx, idx);
            boardFileImgs.add(boardFileImg);
            idx++;
        }
        int od = 0;
        while (boardFileImgs.size() != 0) {
            String storedFilePath = Paths.get("").toAbsolutePath() + "\\" + boardFileImgs.get(od).getStoredFilePath();
            File file = new File(storedFilePath);
            file.delete();

            boardFileImgs.remove(od);
        }
//		파일 위까지 절대경로 = Paths.get("").toAbsolutePath()
//		사진이름겸 경로 = boardFileImg.getStoredFilePath()
//      od = 리스트 인덱스 접근변수
        jpaBoardRepository.deleteBoardEntityByBoardIdx(boardIdx, boardIdx+1);
    }

    @Override
    public BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception {
        BoardFileEntity boardFile = jpaBoardRepository.findBoardFile(boardIdx, idx);
        return boardFile;
    }

    @Override
    public List<BoardEntity> searchingBoardByTitle(String title) throws Exception {
        List<BoardEntity> result = jpaBoardRepository.findAllByTitleContainingOrderByCreatedDatetime(title);

        return result;
    }
}
