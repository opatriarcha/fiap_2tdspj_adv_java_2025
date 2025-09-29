package br.com.fiap.tds.twotdspj.javaadv.taskManager.controllers;

import br.com.fiap.tds.twotdspj.javaadv.taskManager.domainmodel.Task;
import br.com.fiap.tds.twotdspj.javaadv.taskManager.domainmodel.TaskPriority;
import br.com.fiap.tds.twotdspj.javaadv.taskManager.domainmodel.TaskStatus;
import br.com.fiap.tds.twotdspj.javaadv.taskManager.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(TaskManagerControllerTest.MvcTestConfig.class)
public class TaskManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @TestConfiguration
    static class MvcTestConfig {
        @Bean
        @Primary
        TaskService taskService() {
            return Mockito.mock(TaskService.class);
        }

        @Bean
        @Primary
        SpringResourceTemplateResolver thymeleafTemplateResolver() {
            SpringResourceTemplateResolver r = new SpringResourceTemplateResolver();
            r.setSuffix(".html");
            r.setPrefix("classpath:/templates/");
            r.setTemplateMode("HTML");
            r.setCharacterEncoding("UTF-8");
            r.setCacheable(false);
            return r;
        }

        @Bean
        @Primary
        SpringTemplateEngine thymeleafTemplateEngine(Set<ITemplateResolver> templateResolvers) {
            SpringTemplateEngine t = new SpringTemplateEngine();
            t.setTemplateResolvers(templateResolvers);
            t.setTemplateResolver(thymeleafTemplateResolver());
            t.setEnableSpringELCompiler(true);
            return t;
        }

        @Bean
        @Primary
        ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine t) {
            ThymeleafViewResolver v = new ThymeleafViewResolver();
            v.setTemplateEngine(t);
            v.setCharacterEncoding("UTF-8");
            return v;
        }
    }

    private Task task(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription("DESCRIPTION");
        task.setCreationDate(LocalDate.of(2025, 9, 29));
        task.setDueDate(LocalDate.of(2025, 9, 30));
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.MEDIUM);
        return task;
    }

    @Nested
    @DisplayName("GET /tasks")
    class ListAllTasks {
        @Test
        @DisplayName("Dado tarefas existentes, quando listar, entáo 200, refirecionar para view tasks/list e model tasks")
        void shouldReturnAllTasks() throws Exception {
            BDDMockito.given(taskService.findAll())
                    .willReturn(
                            List.of(
                                    task(1L, "TASK01"),
                                    task(2L, "TASK02"),
                                    task(3L, "TASK03")
                            )
                    );

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/int_list"))
                    .andExpect(model().attributeExists("tasks"))
                    .andExpect(model().attribute("tasks", hasSize(3)))
                    .andExpect(model().attribute("tasks", hasItem(task(1L, "TASK01"))))
                    .andExpect(model().attribute("tasks", hasItem(task(2L, "TASK02"))))
                    .andExpect(model().attribute("tasks", hasItem(task(3L, "TASK03"))));
        }

    }

    @Nested
    @DisplayName("GET /tasks/new")
    class newForm {
        @Test
        @DisplayName("Quando abrir o form entao cod 200, view tasks/int_form e enums no model")
        void shouldReturnNewForm() throws Exception {
            mockMvc.perform(get("/tasks/new"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/int_form"))
                    .andExpect(model().attributeExists("task", "statuses", "priorities"));
        }
    }

    @Nested
    @DisplayName("POST /tasks/save")
    class saveTask {
        @Test
        @DisplayName("Quando payload inválido, quando salvar entao volta 'tasks/int_form com erros'")
        void should_return_form_on_valitation_erros() throws Exception {
            mockMvc.perform(post("/tasks/save")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("title", "")
                            .param("description", "x")
                    )

                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/int_form"))
                    .andExpect(model().attributeHasFieldErrors(
                                    "task", "title", "creationDate", "status", "priority"
                            )
                    );
            BDDMockito.then(taskService).shouldHaveNoInteractions();
        }


        @Test
        @DisplayName("Dado payload válido, quando salvar, então 302 redirect:/tasks e service.save chamado")
        void should_save_and_redirect() throws Exception {
            BDDMockito.given(taskService.save(any(Task.class))).willAnswer(inv -> {
                Task t = inv.getArgument(0);
                t.setId(1L);
                return t;
            });


            mockMvc.perform(post("/tasks/save")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("title", "Nova Tarefa")
                            .param("description", "desc")
                            .param("creationDate", "2025-09-01")
                            .param("dueDate", "2025-10-01")
                            .param("status", TaskStatus.PENDING.name())
                            .param("priority", TaskPriority.HIGH.name()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/tasks"));

            BDDMockito.then(taskService).should().save(any(Task.class));
        }
    }
    @Nested @DisplayName("GET /tasks/delete/{id}")
    class DeleteTask {
        @Test @DisplayName("Dado ID válido, quando deletar, então 302 redirect:/tasks e service.delete chamado")
        void should_delete_and_redirect() throws Exception {
            mockMvc.perform(get("/tasks/delete/{id}", 7L))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/tasks"));
            BDDMockito.then(taskService).should().deleteById(7L);
        }
    }

    @Nested @DisplayName("GET /tasks/view/{id}")
    class ViewTask {
        @Test @DisplayName("Dado ID existente, quando visualizar, então 200, view 'tasks/view' e model 'task'")
        void should_view_task() throws Exception {
            BDDMockito.given(taskService.findById(10L)).willReturn(Optional.of(task(10L,"Ver")));
            mockMvc.perform(get("/tasks/view/{id}", 10L))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/view"))
                    .andExpect(model().attributeExists("task"))
                    .andExpect(model().attribute("task", hasProperty("id", is(10L))));
        }

        @Test @DisplayName("Dado ID inexistente, quando visualizar, então 5xx (IllegalArgumentException sem handler)")
        void should_5xx_when_not_found() throws Exception {
            BDDMockito.given(taskService.findById(404L)).willReturn(Optional.empty());
            mockMvc.perform(get("/tasks/view/{id}", 404L))
                    .andExpect(status().is5xxServerError());
        }
    }

    @Nested @DisplayName("GET /tasks/edit/{id}")
    class EditForm {
        @Test @DisplayName("Dado ID existente, quando editar, então 200, 'tasks/form' e enums")
        void should_show_edit_form() throws Exception {
            BDDMockito.given(taskService.findById(8L)).willReturn(Optional.of(task(8L,"Editar")));
            mockMvc.perform(get("/tasks/edit/{id}", 8L))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/form"))
                    .andExpect(model().attributeExists("task","statuses","priorities"))
                    .andExpect(model().attribute("task", hasProperty("id", is(8L))));
        }
    }
}
