package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "foto_immobile")
@Getter
@Setter
@NoArgsConstructor
public class FotoImmobile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    private String urlFoto;

    @ManyToOne
    @JoinColumn(name = "id_immobile")
    private Immobile immobile;

    public FotoImmobile(String foto, Immobile immobile) {
        this.urlFoto = foto;
        this.immobile = immobile;
    }
}
