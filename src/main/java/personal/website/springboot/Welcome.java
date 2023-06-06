package personal.website.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Welcome {

    @GetMapping("/")
    public ModelAndView welcome() {
        ModelAndView mav = new ModelAndView("AboutMe");
        mav.addObject("title", "About Me");
        return mav;
    }

    @GetMapping("/ContactMe")
    public ModelAndView contactMe() {
        ModelAndView mav = new ModelAndView("ContactMe");
        mav.addObject("title", "Contact Me");
        return mav;
    }
}
