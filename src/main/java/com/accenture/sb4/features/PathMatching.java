package com.accenture.sb4.features;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/**/pages")
public class PathMatching {
    @GetMapping("/{pageName}")
    public String handlePage(@PathVariable String pageName) {
        return pageName;
    }
}
