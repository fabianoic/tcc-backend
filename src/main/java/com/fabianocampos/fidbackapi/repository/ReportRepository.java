package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.CommentReport;
import com.fabianocampos.fidbackapi.domain.Project;
import com.fabianocampos.fidbackapi.domain.Report;
import com.fabianocampos.fidbackapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByUserId(Integer userId);

    @Query(value = "select r.* from report r left join user_report ur on ur.report_id = r.id where r.user_id = ?1 or ur.user_id = ?1 order by r.status", nativeQuery = true)
    List<Report> findAllReportsByUserIdOrderByStatus(Integer userId);

    @Query(value = "select r.* from report r left join user_report ur on ur.report_id = r.id where r.user_id = ?1 or ur.user_id = ?1 order by r.title", nativeQuery = true)
    List<Report> findAllReportsByUserIdOrderByTitle(Integer userId);

    @Query(value = "select r.* from report r left join user_report ur on ur.report_id = r.id where r.user_id = ?1 or ur.user_id = ?1 order by r.created_at", nativeQuery = true)
    List<Report> findAllReportsByUserIdOrderByCreatedAt(Integer userId);


    @Query(value = "select r.users from Report r where r.id = ?1")
    List<User> findParticipants(Integer reportId);

    @Query(value = "select r.user from Report r where r.id = ?1")
    User findOwner(Integer reportId);

    @Modifying
    @Transactional
    @Query(value = "delete from report where user_id = ?1 and project_id = ?2", nativeQuery = true)
    void deleteParticipantAllReports(Integer participantId, Integer projectId);

    @Query(value = "select cr from CommentReport cr where cr.report.id = ?1 order by cr.createdAt")
    List<CommentReport> findComments(Integer reportId);

}
