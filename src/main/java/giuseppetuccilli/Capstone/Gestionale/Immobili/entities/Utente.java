package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.RuoliUtente;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utenti")
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"password", "authorities", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired", "username"})
public class Utente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String telefono;
    @Enumerated(EnumType.STRING)
    private RuoliUtente ruolo;

    @ManyToOne
    @JoinColumn(name = "id_ditta")
    private Ditta ditta;

    public Utente(String nome, String cognome, String email, String password, String telefono, RuoliUtente ruolo, Ditta ditta) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.ruolo = ruolo;
        this.ditta = ditta;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRuolo().name()));
    }

    @Override
    public String getUsername() {
        return "";
    }
}
