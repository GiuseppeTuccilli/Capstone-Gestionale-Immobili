package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Setter
@Getter

@NoArgsConstructor
@Table(name = "comuni")
public class Comune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    long id;
    private String denominazione;
    private long progressivoComune;
    private long codicePro;

    @ManyToOne
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;

    public Comune(String denominazione, long progressivoComune, long codiceProvincia, Provincia provincia) {
        this.denominazione = denominazione;
        this.progressivoComune = progressivoComune;
        this.codicePro = codiceProvincia;
        this.provincia = provincia;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comune comune = (Comune) o;
        return id == comune.id && progressivoComune == comune.progressivoComune && codicePro == comune.codicePro && Objects.equals(denominazione, comune.denominazione) && Objects.equals(provincia, comune.provincia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, denominazione, progressivoComune, codicePro, provincia);
    }
}
