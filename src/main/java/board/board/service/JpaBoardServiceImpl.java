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
    public List<BoardFileEntity> selectBoardFileList(int boardIdx) throws Exception {
        List<BoardFileEntity> fileEntityList = jpaBoardRepository.findAllbyBoardFile(boardIdx);

        return fileEntityList;
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
        jpaBoardRepository.deleteById(boardIdx);
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
