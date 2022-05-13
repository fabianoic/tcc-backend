package com.fabianocampos.fidbackapi.domain;

import com.fabianocampos.fidbackapi.domain.enums.StatusReport;
import com.fabianocampos.fidbackapi.domain.log.LogReport;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(length = 255)
    private String description;
    @Enumerated(EnumType.ORDINAL)
    private StatusReport status;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "report")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "USER_REPORT", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "report")
    private List<CommentReport> comments = new ArrayList<>();

    @OneToMany(mappedBy = "report")
    private List<LogReport> logs = new ArrayList<>();

    @Builder
    public Report(String title, String description, StatusReport status, Project project, Category category, User user) {
        super();
        this.title = title;
        this.description = description;
        this.status = status;
        this.project = project;
        this.category = category;
        this.user = user;
    }
}
