package com.fabianocampos.fidbackapi.dto;

import com.fabianocampos.fidbackapi.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private ParticipantDTO participant;

    private Integer userId;

    private String comment;

    private Integer reportId;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date updatedAt;
}
