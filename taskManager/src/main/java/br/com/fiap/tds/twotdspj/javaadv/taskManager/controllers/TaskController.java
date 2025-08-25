package br.com.fiap.tds.twotdspj.javaadv.taskManager.controllers;


import br.com.fiap.tds.twotdspj.javaadv.taskManager.domainmodel.Task;
import br.com.fiap.tds.twotdspj.javaadv.taskManager.domainmodel.TaskPriority;
import br.com.fiap.tds.twotdspj.javaadv.taskManager.domainmodel.TaskStatus;
import br.com.fiap.tds.twotdspj.javaadv.taskManager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String listTasks(Model model){
        model.addAttribute("tasks", this.taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String showCreateForm( Model model){
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        return  "tasks/form";
    }

    @PostMapping("/save")
    public String saveTask(@Valid @ModelAttribute("task") Task task,
                           BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            return "tasks/form";
        }
        this.taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("delete/{id}")
    public String deleteTask(@PathVariable("id") Long id){
        this.taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/view/{id}")
    public String viewTask(@PathVariable("id") Long id, Model model){
        Task task = this.taskService.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Task not found " + id.toString()
            )
        );
        model.addAttribute("task", task);
        return "tasks/view";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model){
        Task newTask = this.taskService.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Task not found " + id.toString()
                )
        );
        model.addAttribute("task", newTask);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        return "tasks/form";
    }
}
