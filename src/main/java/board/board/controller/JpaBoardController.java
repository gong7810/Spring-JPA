package board.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.service.JpaBoardService;
import sun.tools.jconsole.JConsole;

@Controller
@ComponentScan
public class JpaBoardController {

    @Autowired
    private JpaBoardService jpaBoardService;

    @Autowired
    private board.common.FileUtils fileUtils;

    @RequestMapping(value = "/jpa/board", method = RequestMethod.GET)
    public ModelAndView openBoardList(ModelMap model) throws Exception {
        ModelAndView mv = new ModelAndView("/board/jpaBoardList");

        List<BoardEntity> list = jpaBoardService.selectBoardList();
        mv.addObject("list", list);

        return mv;
    }

    //글을 추가할때 필요한 부분
    @RequestMapping(value = "/jpa/board/write", method = RequestMethod.GET)
    public String openBoardWrite() throws Exception {
        return "/board/jpaBoardWrite";
    }

    //글을 적고 저장할때 필요한 부분
    @RequestMapping(value = "/jpa/board/write", method = RequestMethod.POST)
    public String writeBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        jpaBoardService.saveBoard(board, multipartHttpServletRequest);
        return "redirect:/jpa/board";
    }

    @RequestMapping(value = "/jpa/board/{boardIdx}", method = RequestMethod.GET)
    public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception {
        ModelAndView mv = new ModelAndView("/board/jpaBoardDetail");

        BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
        mv.addObject("board", board);

        return mv;
    }

    //	파일추가 할때 기존 파일과 합쳐서 update
    @RequestMapping(value = "/jpa/board/{boardIdx}", method = RequestMethod.POST)
    public String updateBoard(@PathVariable("boardIdx") int boardIdx, BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        jpaBoardService.saveBoard2(boardIdx, board, multipartHttpServletRequest);
        return "redirect:/jpa/board";
    }

    @RequestMapping(value = "/jpa/board/{boardIdx}", method = RequestMethod.DELETE)
    public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception {
        List<BoardFileEntity> fileEntityList = jpaBoardService.selectBoardFileList(boardIdx);

        fileUtils.deleteBoardFile(fileEntityList);
        jpaBoardService.deleteBoard(boardIdx);
        return "redirect:/jpa/board";
    }

    @RequestMapping(value = "/jpa/board/deleteFile", method = RequestMethod.DELETE)
    public String deleteFile(@RequestParam int idx) throws Exception {
        List<String> filePathList = jpaBoardService.selectBoardFilePathList(idx);

        fileUtils.deleteFile_Path(filePathList);
        jpaBoardService.deleteBoardFile(idx);
        return "redirect:/jpa/board/";
    }

    @RequestMapping(value = "/jpa/board/file", method = RequestMethod.GET)
    public void downloadBoardFile(int boardIdx, int idx, HttpServletResponse response) throws Exception {
        BoardFileEntity file = jpaBoardService.selectBoardFileInformation(boardIdx, idx);

        byte[] files = FileUtils.readFileToByteArray(new File(file.getStoredFilePath()));

        response.setContentType("application/octet-stream");
        response.setContentLength(files.length);
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(file.getOriginalFileName(), "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(files);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    @RequestMapping(value = "/jpa/board/search", method = RequestMethod.GET)
    public String searchBoard(@RequestParam String searchKeyword, Model model) throws Exception {

        List<BoardEntity> list = jpaBoardService.searchingBoardByTitle(searchKeyword);

        model.addAttribute("list", list);

        return "/board/jpaBoardList";
    }


}
