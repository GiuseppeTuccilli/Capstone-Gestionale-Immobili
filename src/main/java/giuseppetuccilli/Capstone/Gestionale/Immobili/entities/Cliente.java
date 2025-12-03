package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clienti")
@NoArgsConstructor
@Getter
@Setter
public class Cliente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private String nome;
    private String cognome;
    private String telefono;
    private String indirizzo;
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    public Cliente(String nome, String cognome, String telefono, String indirizzo, String email, Utente utente) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.email = email;
        this.utente = utente;
    }
}
