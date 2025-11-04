package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "incroci")
@NoArgsConstructor
@Getter
public class Incrocio {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_immobile")
    private Immobile immobile;

    @ManyToOne
    @JoinColumn
    private Richiesta richiesta;

    public Incrocio(Immobile immobile, Richiesta richiesta) {
        this.immobile = immobile;
        this.richiesta = richiesta;
    }
}
