package board.board.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="t_jpa_file")
@NoArgsConstructor
@Getter
@Setter
public class 	BoardFileEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idx;
	@Column(nullable=false)
	private String originalFileName;
	
	@Column(nullable=false)
	private String storedFilePath;
	
	@Column(nullable=false)
	private long fileSize;

	@Column(nullable=false)
	private String creatorId;
	
	@Column(nullable=false)
	private LocalDateTime createdDatetime = LocalDateTime.now();
	
	private String updaterId;
	
	private LocalDateTime updatedDatetime;
	@ManyToOne( fetch= FetchType.LAZY)
	@JoinColumn(name = "board_idx")
	private BoardEntity boardEntity;
}
