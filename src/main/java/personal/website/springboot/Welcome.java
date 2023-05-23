package personal.website.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Welcome {

    @GetMapping("/")
    public ModelAndView welcome() {
        ModelAndView mav = new ModelAndView("BasePage");
        mav.addObject("title", "Welcome to my website!");
        return mav;
    }
}
