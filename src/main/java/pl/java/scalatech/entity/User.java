package pl.java.scalatech.entity;

import static com.google.common.collect.Lists.newArrayList;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String login;
    private String passwd;
    private LocalDate birthdate;
    private String city;
    private String address;
    private String email;
    private String nip;
    private String phone;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<BankAccount> accounts = newArrayList();

}
