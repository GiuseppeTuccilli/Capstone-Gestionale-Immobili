package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "codidi_reset_password")
@Getter
@NoArgsConstructor
public class CodiceResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String codice;
    private LocalDateTime scadenza;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    public CodiceResetPassword(String codice, LocalDateTime scadenza, Utente utente) {
        this.utente = utente;
        this.codice = codice;
        this.scadenza = scadenza;
    }

}
