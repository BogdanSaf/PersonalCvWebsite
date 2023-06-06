package personal.website.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class Projects {

    GithubRequest githubRequest = new GithubRequest("BogdanSaf");

    @GetMapping("/Projects")
    public ModelAndView projects() throws IOException, InterruptedException {
        githubRequest.githubCall();
        if (githubRequest.getStatusCode() == 200) {
            System.out.println(githubRequest.getRepoContributers());
            System.out.println(githubRequest.getRepoDescription());
        } else {
            System.out.println("It doesn't work");
            System.out.println(githubRequest.getStatusCode());
        }

        ModelAndView mav = new ModelAndView("Projects");
        mav.addObject("title", "Projects");
        return mav;
    }
}
