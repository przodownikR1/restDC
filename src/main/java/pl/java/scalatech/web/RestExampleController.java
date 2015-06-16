package pl.java.scalatech.web;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/example")
public class RestExampleController {
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
    public ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }
}
