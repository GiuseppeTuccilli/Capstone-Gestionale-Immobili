package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.RuoliUtente;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utenti")
@NoArgsConstructor
@Getter
@Setter
public class Utente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private RuoliUtente ruolo;

    public Utente(String nome, String cognome, String email, String telefono, RuoliUtente ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.ruolo = ruolo;
    }
}
