package com.fabianocampos.fidbackapi.config;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import com.fabianocampos.fidbackapi.dto.CategoryDTO;
import com.fabianocampos.fidbackapi.dto.ReportDTO;
import com.fabianocampos.fidbackapi.dto.UserDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.fabianocampos.fidbackapi.domain.Category;
import com.fabianocampos.fidbackapi.domain.Project;
import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.domain.UserProject;
import com.fabianocampos.fidbackapi.domain.UserProjectPK;
import com.fabianocampos.fidbackapi.domain.enums.StatusReport;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.dto.ProjectDTO;
import com.fabianocampos.fidbackapi.dto.converter.CategoryConverter;
import com.fabianocampos.fidbackapi.dto.converter.ProjectConverter;
import com.fabianocampos.fidbackapi.dto.converter.UserConverter;
import com.fabianocampos.fidbackapi.repository.CategoryRepository;
import com.fabianocampos.fidbackapi.repository.UserProjectRepository;
import com.fabianocampos.fidbackapi.services.ProjectService;
import com.fabianocampos.fidbackapi.services.ReportService;
import com.fabianocampos.fidbackapi.services.UserService;

@Configuration
public class Instatiation implements CommandLineRunner {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private ProjectConverter projectConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Override
    public void run(String... args) throws Exception {

      /*  UserDTO newMaria = UserDTO.builder().firstName("Maria").lastName("Brown").email("maria@gmail.com")
                .password("maria123").confirmPassword("maria123").nickname("maria").createdAt(new Date()).build();

        UserDTO newBob = UserDTO.builder().firstName("Bob").lastName("Gray").email("bob@gmail.com")
                .password("bob123").confirmPassword("bob123").nickname("bob").createdAt(new Date()).build();

        UserDTO newAlex = UserDTO.builder().firstName("Alex").lastName("Green").email("alex@gmail.com")
                .password("alex123").confirmPassword("alex123").nickname("alex").createdAt(new Date()).build();

        UserDTO newFabiano = UserDTO.builder().firstName("Fabiano").lastName("Campos").email("fabiano.fic@gmail.com")
                .password("123123").confirmPassword("123123").nickname("ficampos").createdAt(new Date()).build();

        User alex = userService.createOrUpdate(newAlex, Operation.CREATE, null);
        User bob = userService.createOrUpdate(newBob, Operation.CREATE, null);
        User maria = userService.createOrUpdate(newMaria, Operation.CREATE, null);
        User fabiano = userService.createOrUpdate(newFabiano, Operation.CREATE, null);

        ProjectDTO newProject1 = ProjectDTO.builder().name("Projeto 1").description("Projeto teste 1").visibility(true).createdAt(new Date()).build();
        ProjectDTO newProject2 = ProjectDTO.builder().name("Projeto 2").description("Projeto teste 2").visibility(true).createdAt(new Date()).build();
        ProjectDTO newProject3 = ProjectDTO.builder().name("Projeto 3").description("Projeto teste 3").visibility(false).createdAt(new Date()).build();

        Project proj1 = projectService.createOrUpdate(newProject1, Operation.CREATE, fabiano.getEmail());
        Project proj2 = projectService.createOrUpdate(newProject2, Operation.CREATE, fabiano.getEmail());
        Project proj3 = projectService.createOrUpdate(newProject3, Operation.CREATE, fabiano.getEmail());


        UserProject up1 = UserProject.builder().userType(UserType.ADMIN).createdAt(new Date()).id(UserProjectPK.builder().user(fabiano).project(proj1).build()).build();
        UserProject up2 = UserProject.builder().userType(UserType.ADMIN).createdAt(new Date()).id(UserProjectPK.builder().user(fabiano).project(proj2).build()).build();
        UserProject up3 = UserProject.builder().userType(UserType.ADMIN).createdAt(new Date()).id(UserProjectPK.builder().user(fabiano).project(proj3).build()).build();

        UserProject up1Dev = UserProject.builder().userType(UserType.DEVELOPER).createdAt(new Date()).id(UserProjectPK.builder().user(alex).project(proj1).build()).build();
        UserProject up1User = UserProject.builder().userType(UserType.USER).createdAt(new Date()).id(UserProjectPK.builder().user(maria).project(proj1).build()).build();
        UserProject up1User2 = UserProject.builder().userType(UserType.USER).createdAt(new Date()).id(UserProjectPK.builder().user(bob).project(proj1).build()).build();

        userProjectRepository.save(up1);
        userProjectRepository.save(up2);
        userProjectRepository.save(up3);

        userProjectRepository.save(up1Dev);
        userProjectRepository.save(up1User);
        userProjectRepository.save(up1User2);

        CategoryDTO newCategory1 = CategoryDTO.builder().name("Administrativo").projectDTO(projectConverter.encode(proj1)).build();
        CategoryDTO newCategory2 = CategoryDTO.builder().name("Financeiro").projectDTO(projectConverter.encode(proj1)).build();
        CategoryDTO newCategory3 = CategoryDTO.builder().name("Vendas").projectDTO(projectConverter.encode(proj1)).build();

        Category cat1 = categoryService.createOrUpdate(newCategory1, Operation.CREATE);
        Category cat2 = categoryService.createOrUpdate(newCategory2, Operation.CREATE);
        Category cat3 = categoryService.createOrUpdate(newCategory3, Operation.CREATE);
*/

    }
}
