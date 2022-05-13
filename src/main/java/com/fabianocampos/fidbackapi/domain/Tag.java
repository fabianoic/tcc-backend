package com.fabianocampos.fidbackapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false, length = 45)
	private String name;
	@Column(nullable = false, length = 7)
	private String color;

	@ManyToMany(mappedBy = "tags")
	private List<Card> cards = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

}
