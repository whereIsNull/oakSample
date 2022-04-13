package org.example;

import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jcr.GuestCredentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Hello world!
 *
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        Session session = null;
        try {
            Repository repository = JcrUtils.getRepository();
            session = repository.login(new GuestCredentials());

            String user = session.getUserID();
            String name = repository.getDescriptor(Repository.REP_NAME_DESC);
            System.out.println(
                    "Logged in as " + user + " to a " + name + " repository.");
        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.logout();
            }
        }

        return "Greetings from Spring Boot!";
    }
}
