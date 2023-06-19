package cmc.mellyserver.mellydomain.memory.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_memory_image")
@Getter
public class MemoryImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memory_image_id")
	private Long id;

	private String imagePath;

	@ManyToOne
	@JoinColumn(name = "memory_id")
	private Memory memory;

	public MemoryImage(String imagePath) {
		this.imagePath = imagePath;
	}

	public MemoryImage(Long id) {
		this.id = id;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MemoryImage that = (MemoryImage)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
