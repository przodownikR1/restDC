package pl.java.scalatech.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.service.userInformation.UserAccountService;

@RestController
@RequestMapping(value = UserInfoController.API, produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class UserInfoController {
    protected static final String API = "/api/users";

    private final @NonNull UserAccountService userAccountService;
    private final @NonNull UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/login/{login}")
    public ResponseEntity<User> findUserByLogin(@PathVariable("login") String login) {
        User loaded = userAccountService.findUserByLogin(login);
        if (loaded != null) { return new ResponseEntity<>(loaded, HttpStatus.OK); }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("email") String email) {
        User loaded = userAccountService.findUserByEmail(email);
        if (loaded != null) { return new ResponseEntity<>(loaded, HttpStatus.OK); }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/nip/{nip}")
    public ResponseEntity<User> findUserByNip(@PathVariable("nip") String nip) {
        User loaded = userAccountService.findUserByNip(nip);
        if (loaded != null) { return new ResponseEntity<>(loaded, HttpStatus.OK); }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    HttpEntity<PagedResources<User>> persons(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<User> persons = userAccountService.getUsers(pageable);

        return new ResponseEntity<>(assembler.toResource(persons), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        User loaded = userAccountService.saveUser(user);
        response.setHeader("Location", request.getRequestURL().append("/").append(loaded.getId()).toString());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSeries(@PathVariable("id") long id) {
        User loaded = userRepository.findOne(id);
        if (loaded != null) {
            userAccountService.removeUser(loaded);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
