package ru.otus.homework.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexView {

    @GetMapping("/")
    public String getIndexView() {
        return "redirect:/clients";
    }

}
