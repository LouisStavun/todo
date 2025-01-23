package ch.cern.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


    @GetMapping("/api")
    public String getString() {
        return "Hello World";
    }

    @GetMapping("/api/{resourceId}")
    public String receiveData (@PathVariable String resourceId) {
        return "Resource Id: " + resourceId;
    }

    @GetMapping("/delete")
    public String delete()
    {
        return "This is the delete request";
    }
}
