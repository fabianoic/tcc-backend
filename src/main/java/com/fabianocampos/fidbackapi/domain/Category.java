package com.fabianocampos.fidbackapi.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false, length = 100)
	private String name;

	@OneToMany(mappedBy = "category")
	private List<Report> reports = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	@Builder
	public Category(String name, Project project) {
		super();
		this.name = name;
		this.project = project;
	}
}
