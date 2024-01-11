package ru.javaops.restaurant_voting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurant_voting.repository.MenuRepository;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {

    static final String REST_URL = "/api/admin/menus";

    private final MenuRepository repository;

    public AdminMenuController(MenuRepository repository) {
        this.repository = repository;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu with id {}", id);
        repository.deleteExisted(id);
    }


}
