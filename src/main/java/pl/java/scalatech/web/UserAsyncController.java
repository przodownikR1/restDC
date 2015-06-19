package pl.java.scalatech.web;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.service.async.userInformation.UserAccountAsyncService;

@RestController
@Slf4j
@RequestMapping(value = UserAsyncController.API, produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class UserAsyncController {
    protected static final String API = "/api/async";

    private final @NonNull UserAccountAsyncService userAccountAsyncService;
    private final @NonNull UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/users/login/{login}")
    public ResponseEntity<?> findUserByLogin_firstApproach(@PathVariable("login") String login) throws InterruptedException, ExecutionException {
        User loaded = userAccountAsyncService.findUserByLogin(login).get();
        if (loaded != null) { return new ResponseEntity<>(loaded, HttpStatus.OK); }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping("/users/login/{login}")
    DeferredResult<ResponseEntity<?>> findUserByLogin(@PathVariable("login") String login) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        ListenableFuture<User> listener = userAccountAsyncService.findUserByLogin(login);
        userCallBack(deferredResult, listener);
        return deferredResult;
    }

    @RequestMapping("/users/email/{email}")
    DeferredResult<ResponseEntity<?>> findUserByEmail(@PathVariable("email") String email) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        ListenableFuture<User> listener = userAccountAsyncService.findUserByEmail(email);
        userCallBack(deferredResult, listener);
        return deferredResult;
    }

    @RequestMapping("/users/nip/{nip}")
    DeferredResult<ResponseEntity<?>> findUserByNip(@PathVariable("nip") String nip) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        ListenableFuture<User> listener = userAccountAsyncService.findUserByNip(nip);
        userCallBack(deferredResult, listener);
        return deferredResult;
    }

    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    DeferredResult<?> persons(Pageable pageable, PagedResourcesAssembler assembler) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        ListenableFuture<Page<User>> listener = userAccountAsyncService.getUsers(pageable);
        SuccessCallback<Page<User>> successCallback = sc -> {
            ResponseEntity<Page<User>> responseEntity = new ResponseEntity<>(sc, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        };
        FailureCallback failureCallback = throwable -> {
            log.error("Failed to retrieve result from ds", throwable);
            ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            deferredResult.setResult(responseEntity);
        };

        listener.addCallback(successCallback, failureCallback);
        return deferredResult;

    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    // TODO correct this method
    public DeferredResult<ResponseEntity<?>> insertUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        ListenableFuture<User> listener = userAccountAsyncService.saveUser(user);
        userCallBack(deferredResult, listener);
        return deferredResult;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSeries(@PathVariable("id") long id) {
        User loaded = userRepository.findOne(id);
        if (loaded != null) {
            ListenableFuture<Void> listener = userAccountAsyncService.removeUser(loaded);
            // TODO correct this method
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void userCallBack(DeferredResult<ResponseEntity<?>> deferredResult, ListenableFuture<User> listener) {
        SuccessCallback<User> successCallback = user -> {
            ResponseEntity<User> responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
            deferredResult.setResult(responseEntity);
        };
        FailureCallback failureCallback = throwable -> {
            log.error("Failed to retrieve result from ds", throwable);
            ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            deferredResult.setResult(responseEntity);
        };

        listener.addCallback(successCallback, failureCallback);
    }

}

// TODO try to make generic mechanism

@Slf4j
class SuccessCallBackUtil<T, E> {
    public static <T extends ResponseEntity<?>, E extends Serializable> void callBack(DeferredResult<T> deferredResult, ListenableFuture<E> listener) {
        SuccessCallback<E> successCallback = obj -> {
            ResponseEntity<E> responseEntity = new ResponseEntity<>(obj, HttpStatus.OK);
            deferredResult.setResult((T) responseEntity);
        };
        FailureCallback failureCallback = throwable -> {
            log.error("Failed to retrieve result from ds", throwable);
            ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            deferredResult.setResult((T) responseEntity);
        };

        // listener.addCallback(successCallback, failureCallback);
    }

}
