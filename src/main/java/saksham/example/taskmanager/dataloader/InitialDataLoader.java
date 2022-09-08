package saksham.example.taskmanager.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import saksham.example.taskmanager.model.Role;
import saksham.example.taskmanager.model.Task;
import saksham.example.taskmanager.model.User;
import saksham.example.taskmanager.service.RoleService;
import saksham.example.taskmanager.service.TaskService;
import saksham.example.taskmanager.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

        private UserService userService;
        private TaskService taskService;
        private RoleService roleService;
        private final Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        @Value("${default.admin.mail}")
        private String defaultAdminMail;
        @Value("${default.admin.name}")
        private String defaultAdminName;
        @Value("${default.admin.password}")
        private String defaultAdminPassword;
        @Value("${default.admin.image}")
        private String defaultAdminImage;

        @Autowired
        public InitialDataLoader(UserService userService, TaskService taskService, RoleService roleService) {
                this.userService = userService;
                this.taskService = taskService;
                this.roleService = roleService;
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {

                // ROLES
                // --------------------------------------------------------------------------------------------------------
                roleService.createRole(new Role("ADMIN"));
                roleService.createRole(new Role("USER"));
                roleService.findAll().stream().map(role -> "saved role: " + role.getRole()).forEach(logger::info);

                // USERS
                // --------------------------------------------------------------------------------------------------------
                // 1
                User admin = new User(
                                defaultAdminMail,
                                defaultAdminName,
                                defaultAdminPassword,
                                defaultAdminImage);
                userService.createUser(admin);
                userService.changeRoleToAdmin(admin);

                // 2
                User manager = new User(
                                "manager@mail.com",
                                "Manager",
                                "112233",
                                "images/admin.png");
                userService.createUser(manager);
                userService.changeRoleToAdmin(manager);

                // 3
                userService.createUser(new User(
                                "mark@mail.com",
                                "Mark",
                                "112233",
                                "images/mark.jpg"));

                // 4
                userService.createUser(new User(
                                "ann@mail.com",
                                "Ann",
                                "112233",
                                "images/ann.jpg"));

                // 5
                userService.createUser(new User(
                                "ralf@mail.com",
                                "Ralf",
                                "112233",
                                "images/ralf.jpg"));

                // 6
                userService.createUser(new User(
                                "kate@mail.com",
                                "Kate",
                                "112233",
                                "images/kate.jpg"));

                // 7
                userService.createUser(new User(
                                "tom@mail.com",
                                "Tom",
                                "112233",
                                "images/tom.jpg"));

                userService.findAll().stream()
                                .map(u -> "saved user: " + u.getName())
                                .forEach(logger::info);

                // TASKS
                // --------------------------------------------------------------------------------------------------------
                // tasks from Web Design Checklist
                // https://www.beewits.com/the-ultimate-web-design-checklist-things-to-do-when-launching-a-website/

                LocalDate today = LocalDate.now();

                // 1
                taskService.createTask(new Task(
                                "Lorem Ipsum ",
                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                                today.minusDays(40),
                                true,
                                userService.getUserByEmail("mark@mail.com").getName(),
                                userService.getUserByEmail("mark@mail.com")));

                // 2
                taskService.createTask(new Task(
                                "Lorem Ipsum ",
                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                                today.minusDays(30),
                                true,
                                userService.getUserByEmail("ann@mail.com").getName(),
                                userService.getUserByEmail("ann@mail.com")));

                // 3
                taskService.createTask(new Task(
                                "Lorem Ipsum ",
                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                                today.minusDays(20),
                                true,
                                userService.getUserByEmail("ann@mail.com").getName(),
                                userService.getUserByEmail("ann@mail.com")));

                // 4
                taskService.createTask(new Task(
                                "Lorem Ipsum ",
                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                                today.minusDays(10),
                                true,
                                userService.getUserByEmail("kate@mail.com").getName(),
                                userService.getUserByEmail("kate@mail.com")));

                // 5
                taskService.createTask(new Task(
                                "Lorem Ipsum ",
                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                                today.minusDays(5),
                                false,
                                userService.getUserByEmail("manager@mail.com").getName(),
                                userService.getUserByEmail("manager@mail.com")));

                taskService.findAll().stream().map(t -> "saved task: '" + t.getName()
                                + "' for owner: " + getOwnerNameOrNoOwner(t)).forEach(logger::info);
        }

        private String getOwnerNameOrNoOwner(Task task) {
                return task.getOwner() == null ? "no owner" : task.getOwner().getName();
        }
}
