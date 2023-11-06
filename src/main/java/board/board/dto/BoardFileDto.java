package board.board.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BoardFileDto {
	
	private int idx;
	
	private int boardIdx;
	
	private String originalFileName;
	
	private String storedFilePath;
	
	private long fileSize;
//	private Date created_datetime;
}
