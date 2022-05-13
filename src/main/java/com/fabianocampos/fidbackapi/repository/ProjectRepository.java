package com.fabianocampos.fidbackapi.repository;

import com.fabianocampos.fidbackapi.domain.*;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByName(String name);

    @Query(value = "select p from Project p inner join UserProject up on p.id = up.id.project.id inner join User u on u.id = up.id.user.id where u.id = ?1 order by p.name")
    List<Project> findProjectsByUserIdOrderByName(Integer id);

    @Query(value = "select p from Project p inner join UserProject up on p.id = up.id.project.id inner join User u on u.id = up.id.user.id where u.id = ?1 order by p.createdAt")
    List<Project> findProjectsByUserIdOrderByCreatedAt(Integer id);

    @Query(value = "select p from Project p inner join UserProject up on p.id = up.id.project.id inner join User u on u.id = up.id.user.id where u.id = ?1 order by p.visibility desc")
    List<Project> findProjectsByUserIdOrderByVisibility(Integer id);

    @Query(value = "select p.* from project p inner join user_project up on p.id = up.project_id inner join user u on u.id = up.user_id where u.id = ?1 and p.name = ?2", nativeQuery = true)
    Project findProjectByNameAndByUserId(Integer id, String name);

    @Query(value = "select u from User u inner join UserProject up on up.id.user.id = u.id where up.id.project.id = ?1 order by up.userType asc")
    List<User> findParticipantsByProject(Integer projectId);

    @Query(value = "select u from User u where u.id not in (select u2.id from User u2 inner join UserProject up on up.id.user.id = u2.id where up.id.project.id = ?1) and (u.firstName like %?2% or u.lastName like %?2% or u.nickname like %?2%)")
    List<User> findNewParticipants(Integer projectId, String nameValue);

    @Query(value = "select u from User u inner join UserProject up on up.id.user.id = u.id where up.id.project.id = ?1 and up.id.user.id = ?2")
    User findParticipantById(Integer projectId, Integer userId);

    @Query(value = "delete from user_project where project_id = ?1", nativeQuery = true)
    void deleteAllParticipantsByProjectId(Integer projectId);

    @Query(value = "delete from report where project_id = ?1", nativeQuery = true)
    void deleteAllReportsByProjectId(Integer projectId);

    @Query(value = "delete from category where project_id = ?1", nativeQuery = true)
    void deleteAllCategoriesByProjectId(Integer projectId);

    @Query(value = "delete from tag where project_id = ?1", nativeQuery = true)
    void deleteAllTagsByProjectId(Integer projectId);

    @Query(value = "delete from label where project_id = ?1", nativeQuery = true)
    void deleteAllLabelsByProjectId(Integer projectId);

    @Query(value = "select t from Tag t where t.project.id = ?1")
    List<Tag> findTags(Integer projectId);

    @Query(value = "select r from Report r where r.project.id = ?1 order by r.title")
    List<Report> findReportsOrderByTitle(Integer projectId);

    @Query(value = "select r from Report r where r.project.id = ?1 order by r.status")
    List<Report> findReportsOrderByStatus(Integer projectId);

    @Query(value = "select r from Report r where r.project.id = ?1 order by r.createdAt")
    List<Report> findReportsOrderByCreatedAt(Integer projectId);

    @Query(value = "select u from User u inner join UserProject up on u = up.id.user where up.id.project.id = ?1 and up.userType = ?2")
    User findOwnerProjectByProjectId(Integer projectId, UserType userType);

    @Query(value = "select distinct p from Project p inner join UserProject up on p = up.id.project where p.visibility = true and p.id not in (select p.id from Project p inner join UserProject up on p = up.id.project where up.id.user.id = ?1) order by p.name")
    List<Project> findOpenProjectsOrderByName(Integer userId);

    @Query(value = "select distinct p from Project p inner join UserProject up on p = up.id.project where p.visibility = true and p.id not in (select p.id from Project p inner join UserProject up on p = up.id.project where up.id.user.id = ?1) order by p.createdAt")
    List<Project> findOpenProjectsOrderByCreatedAt(Integer userId);

    @Query(value = "select distinct p from Project p inner join UserProject up on p = up.id.project where p.visibility = true and p.id not in (select p.id from Project p inner join UserProject up on p = up.id.project where up.id.user.id = ?1) order by p.visibility desc")
    List<Project> findOpenProjectsOrderByVisibility(Integer userId);

    @Query(value = "select l from Label l where l.project.id = ?1")
    List<Label> findLabelByProject(Integer projectId);
}
