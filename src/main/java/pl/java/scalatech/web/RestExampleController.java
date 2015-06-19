package pl.java.scalatech.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.User;

@RestController
@Slf4j
@RequestMapping("/example")
public class RestExampleController {

    private Random random = new Random();

    @RequestMapping("/builderSample")
    public ResponseEntity<String> check() {
        BodyBuilder builder = ResponseEntity.ok();
        builder.allow(HttpMethod.GET);
        builder.contentType(MediaType.TEXT_HTML);
        builder.eTag("Etag_my");
        return builder.body("Hello World!");
    }

    @RequestMapping("/status_body_set")
    public ResponseEntity<String> handle() {
        return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body("BANDWIDTH LIMIT EXCEEDED");

    }

    @RequestMapping("/status_body_set2")
    public ResponseEntity<String> handle2() {
        return new ResponseEntity<>("handle2", HttpStatus.OK);

    }

    @RequestMapping("/created")
    public ResponseEntity<Void> created() throws URISyntaxException {
        URI location = new URI("http://myPage/");
        return ResponseEntity.created(location).build();
    }

    @RequestMapping("/accepted")
    public ResponseEntity<String> accepted() {
        return ResponseEntity.accepted().body("Hello World!");
    }

    @RequestMapping("/noContent")
    // 204
    public ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    @RequestMapping("/badRequest")
    // 400
    public ResponseEntity<String> badRequest() {
        return ResponseEntity.badRequest().body("Bad Request");
    }

    @RequestMapping("/noFound")
    // 404
    public ResponseEntity<Void> noFound() {
        return ResponseEntity.notFound().build();
    }

    // -i -H "Content-type: application/json" http://localhost:8777/example/optional
    @RequestMapping("/optional")
    public ResponseEntity<User> getUser() {
        return getUserFromStubService().map(u -> new ResponseEntity<>(u, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private Optional<User> getUserFromStubService() {
        if (random.nextBoolean()) { return Optional.empty(); }
        return Optional.of(User.builder().login("przodownik").city("Warsaw")
                .birthdate(LocalDate.parse("1979-05-03", DateTimeFormatter.ofPattern("yyyy-MM-dd"))).build());
    }
}
// for post curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":100,...}' http://localhost:8777/api/users