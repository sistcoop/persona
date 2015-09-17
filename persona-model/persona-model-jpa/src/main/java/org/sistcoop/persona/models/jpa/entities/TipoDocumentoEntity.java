package org.sistcoop.persona.models.jpa.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.sistcoop.persona.models.enums.TipoPersona;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

@Audited
@Cacheable
@Entity
@Table(name = "TIPO_DOCUMENTO")
@NamedQueries(value = {
        @NamedQuery(name = "TipoDocumentoEntity.findAll", query = "SELECT t FROM TipoDocumentoEntity t"),
        @NamedQuery(name = "TipoDocumentoEntity.findByAbreviatura", query = "SELECT t FROM TipoDocumentoEntity t WHERE t.abreviatura = :abreviatura") })
public class TipoDocumentoEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Size(min = 1, max = 20)
    @Id
    @Column(name = "ABREVIATURA")
    private String abreviatura;

    @NotNull
    @Size(min = 1, max = 60)
    @NotBlank
    @Column(name = "DENOMINACION")
    private String denominacion;

    @NotNull
    @Min(value = 1)
    @Max(value = 20)
    @Column(name = "CANTIDAD_CARACTERES")
    private int cantidadCaracteres;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_PERSONA")
    private TipoPersona tipoPersona;

    @NotNull
    @Type(type = "org.hibernate.type.TrueFalseType")
    @Column(name = "ESTADO")
    private boolean estado;

    @Version
    private Timestamp optlk;

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public int getCantidadCaracteres() {
        return cantidadCaracteres;
    }

    public void setCantidadCaracteres(int cantidadCaracteres) {
        this.cantidadCaracteres = cantidadCaracteres;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Timestamp getOptlk() {
        return optlk;
    }

    public void setOptlk(Timestamp optlk) {
        this.optlk = optlk;
    }

    @Override
    public String toString() {
        return "(TipoDocumentoEntity abreviatura=" + this.abreviatura + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abreviatura == null) ? 0 : abreviatura.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TipoDocumentoEntity))
            return false;
        TipoDocumentoEntity other = (TipoDocumentoEntity) obj;
        if (abreviatura == null) {
            if (other.abreviatura != null)
                return false;
        } else if (!abreviatura.equals(other.abreviatura))
            return false;
        return true;
    }

}
