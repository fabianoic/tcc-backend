package com.fabianocampos.fidbackapi.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(length = 255)
    private String description;
    private boolean visibility;
    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "project")
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "id.project")
    private List<UserProject> participants = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Category> categorys = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Label> labels = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Project(String name, String description, boolean visibility) {
        super();
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.createdAt = new Date();
    }

    public List<User> getUsuarios() {
        return participants.stream().map(UserProject::getId).map(UserProjectPK::getUser).collect(Collectors.toList());
    }
}