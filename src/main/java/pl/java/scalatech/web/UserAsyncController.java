package pl.java.scalatech.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.service.userInformation.UserAccountService;

@RestController
@RequestMapping(value = UserInfoController.API, produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class UserAsyncController {
    protected static final String API = "/api/async/users";

    private final @NonNull UserAccountService userAccountService;
    private final @NonNull UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/login/{login}")
    public ResponseEntity<User> findUserByLogin(@PathVariable("login") String login) {
        User loaded = userAccountService.findUserByLogin(login);
        if (loaded != null) { return new ResponseEntity<>(loaded, HttpStatus.OK); }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
