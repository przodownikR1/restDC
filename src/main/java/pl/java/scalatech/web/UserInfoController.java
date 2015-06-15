package pl.java.scalatech.web;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.User;
import pl.java.scalatech.service.userInformation.UserAccountService;

@RestController
@RequestMapping(value = UserInfoController.API, produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class UserInfoController {
    protected static final String API = "/api/users";

    private final @NonNull UserAccountService userAccountService;

    @RequestMapping(method = RequestMethod.GET, value = "/login/{login}")
    public ResponseEntity<User> findUserByLogin(@PathVariable("login") String login) {
        return new ResponseEntity<>(userAccountService.findUserByLogin(login), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(userAccountService.findUserByEmail(email), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/nip/{nip}")
    public ResponseEntity<User> findUserByNip(@PathVariable("nip") String nip) {
        return new ResponseEntity<>(userAccountService.findUserByNip(nip), HttpStatus.OK);
    }

    public ResponseEntity<Page<User>> getAllUser(Pageable pagable) {
        return null;

    }

    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    HttpEntity<PagedResources<User>> persons(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<User> persons = userAccountService.getUsers(pageable);
        return new ResponseEntity<>(assembler.toResource(persons), HttpStatus.OK);
    }

}
