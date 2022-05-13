package com.fabianocampos.fidbackapi.domain;

import com.fabianocampos.fidbackapi.domain.log.LogCard;
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
@NoArgsConstructor
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer timeSpent;

    @OneToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToMany
    @JoinTable(name = "CARD_USERS", joinColumns = @JoinColumn(name = "card_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CARD_TAGS", joinColumns = @JoinColumn(name = "card_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @OneToMany(mappedBy = "card")
    private List<CommentCard> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card")
    private List<LogCard> logs = new ArrayList<>();

    @Builder
    public Card(Report report, Label label) {
        super();
        this.report = report;
        this.label = label;
    }
}
