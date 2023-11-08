package board.board.service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import board.board.mapper.BoardMapper;
import board.common.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.repository.JpaBoardRepository;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

    @Autowired
    JpaBoardRepository jpaBoardRepository;
    @Autowired
    FileUtils fileUtils;

    @Override
    public List<BoardEntity> selectBoardList() throws Exception {
        List<BoardEntity> boardEntities = jpaBoardRepository.findAll();

        return boardEntities;
    }

    @Override public List<String> selectBoardFilePathList(int idx) throws Exception{
        List<BoardFileEntity> boardFileEntityList = jpaBoardRepository.selectBoardFile(idx);
        List<String> filePathList = new ArrayList<>();
        for(BoardFileEntity file : boardFileEntityList){
            String storedFilePath = Paths.get("").toAbsolutePath() + "\\" + file.getStoredFilePath();
            filePathList.add(storedFilePath);
        }

        return filePathList;
    }
    @Override
    public List<BoardFileEntity> selectBoardFileList(int boardIdx) throws Exception {
        List<BoardFileEntity> fileEntityList = jpaBoardRepository.findAllbyBoardFile(boardIdx);

        return fileEntityList;
    }

    public void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        List<BoardFileEntity> list = fileUtils.parseFileInfo(multipartHttpServletRequest);

        board.setFileList(list);
        board.setCreatorId("admin");

        jpaBoardRepository.save(board);
    }

    @Override
    public void saveBoard2(int boardIdx, BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        List<BoardFileEntity> postFileList = jpaBoardRepository.findAllbyBoardFile(boardIdx);
        List<BoardFileEntity> list = fileUtils.parseFileInfo(multipartHttpServletRequest);
        int Count = jpaBoardRepository.findHitCount(boardIdx);
        postFileList.addAll(list);

        board.setFileList(postFileList);
        board.setCreatorId("admin");
        board.setHitCnt(Count);
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
    public void updateBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        List<BoardFileEntity> addFileList = fileUtils.parseFileInfo(multipartHttpServletRequest);
        List<BoardFileEntity> pro_board = board.getFileList();
        if(CollectionUtils.isEmpty(addFileList) == false){
            for(BoardFileEntity addfile : addFileList){
                pro_board.add(addfile);
            }
            board.setFileList(pro_board);
            jpaBoardRepository.save(board);
        }
    }

    @Override
    public void deleteBoard(int boardIdx) {
        jpaBoardRepository.deleteById(boardIdx);
    }

    @Override
    public void deleteBoardFile(int idx){
        jpaBoardRepository.deleteFileById(idx);
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
