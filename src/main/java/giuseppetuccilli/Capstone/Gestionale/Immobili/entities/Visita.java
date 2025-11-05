package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "visite")
@NoArgsConstructor
@Getter
@Setter
public class Visita {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_immobile")
    private Immobile immobile;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    public Visita(LocalDate data, Immobile immobile, Cliente cliente, Utente utente) {
        this.data = data;
        this.immobile = immobile;
        this.cliente = cliente;
        this.utente = utente;
    }
}
