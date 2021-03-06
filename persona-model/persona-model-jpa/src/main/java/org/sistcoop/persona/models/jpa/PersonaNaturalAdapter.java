package org.sistcoop.persona.models.jpa;

import java.util.Date;

import javax.persistence.EntityManager;

import org.sistcoop.persona.models.FileStoreModel;
import org.sistcoop.persona.models.PersonaNaturalModel;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.enums.EstadoCivil;
import org.sistcoop.persona.models.enums.Sexo;
import org.sistcoop.persona.models.jpa.entities.PersonaNaturalEntity;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

public class PersonaNaturalAdapter implements PersonaNaturalModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PersonaNaturalEntity personaNaturalEntity;
	private transient EntityManager em;

	public PersonaNaturalAdapter(EntityManager em, PersonaNaturalEntity personaNaturalEntity) {
		this.em = em;
		this.personaNaturalEntity = personaNaturalEntity;
	}

	public PersonaNaturalEntity getPersonaNaturalEntity() {
		return this.personaNaturalEntity;
	}

	public static PersonaNaturalEntity toPersonaNaturalEntity(PersonaNaturalModel model, EntityManager em) {
		if (model instanceof PersonaNaturalAdapter) {
			return ((PersonaNaturalAdapter) model).getPersonaNaturalEntity();
		}
		return em.getReference(PersonaNaturalEntity.class, model.getId());
	}

	@Override
	public void commit() {
		em.merge(personaNaturalEntity);
	}

	@Override
	public String getId() {
		return personaNaturalEntity.getId();
	}

	@Override
	public String getCodigoPais() {
		return personaNaturalEntity.getCodigoPais();
	}

	@Override
	public void setCodigoPais(String codigoPais) {
		personaNaturalEntity.setCodigoPais(codigoPais);
	}

	@Override
	public TipoDocumentoModel getTipoDocumento() {
		return new TipoDocumentoAdapter(em, personaNaturalEntity.getTipoDocumento());
	}

	@Override
	public String getNumeroDocumento() {
		return personaNaturalEntity.getNumeroDocumento();
	}

	@Override
	public String getApellidoPaterno() {
		return personaNaturalEntity.getApellidoPaterno();
	}

	@Override
	public void setApellidoPaterno(String apellidoPaterno) {
		personaNaturalEntity.setApellidoPaterno(apellidoPaterno);
	}

	@Override
	public String getApellidoMaterno() {
		return personaNaturalEntity.getApellidoMaterno();
	}

	@Override
	public void setApellidoMaterno(String apellidoMaterno) {
		personaNaturalEntity.setApellidoMaterno(apellidoMaterno);
	}

	@Override
	public String getNombres() {
		return personaNaturalEntity.getNombres();
	}

	@Override
	public void setNombres(String nombres) {
		personaNaturalEntity.setNombres(nombres);
	}

	@Override
	public Date getFechaNacimiento() {
		return personaNaturalEntity.getFechaNacimiento();
	}

	@Override
	public void setFechaNacimiento(Date fechaNacimiento) {
		personaNaturalEntity.setFechaNacimiento(fechaNacimiento);
	}

	@Override
	public Sexo getSexo() {
		return personaNaturalEntity.getSexo();
	}

	@Override
	public void setSexo(Sexo sexo) {
		personaNaturalEntity.setSexo(sexo);
	}

	@Override
	public EstadoCivil getEstadoCivil() {
		return personaNaturalEntity.getEstadoCivil();
	}

	@Override
	public void setEstadoCivil(EstadoCivil estadoCivil) {
		personaNaturalEntity.setEstadoCivil(estadoCivil);
	}

	@Override
	public String getOcupacion() {
		return personaNaturalEntity.getOcupacion();
	}

	@Override
	public void setOcupacion(String ocupacion) {
		personaNaturalEntity.setOcupacion(ocupacion);
	}

	@Override
	public String getUrlFoto() {
		return personaNaturalEntity.getUrlFoto();
	}

	@Override
	public void setUrlFoto(String urlFoto) {
		personaNaturalEntity.setUrlFoto(urlFoto);
	}

	@Override
	public String getUrlFirma() {
		return personaNaturalEntity.getUrlFirma();
	}

	@Override
	public void setUrlFirma(String urlFirma) {
		personaNaturalEntity.setUrlFirma(urlFirma);
	}

	@Override
	public String getUbigeo() {
		return personaNaturalEntity.getUbigeo();
	}

	@Override
	public void setUbigeo(String ubigeo) {
		personaNaturalEntity.setUbigeo(ubigeo);
	}

	@Override
	public String getDireccion() {
		return personaNaturalEntity.getDireccion();
	}

	@Override
	public void setDireccion(String direccion) {
		personaNaturalEntity.setDireccion(direccion);
	}

	@Override
	public String getReferencia() {
		return personaNaturalEntity.getReferencia();
	}

	@Override
	public void setReferencia(String referencia) {
		personaNaturalEntity.setReferencia(referencia);
	}

	@Override
	public String getTelefono() {
		return personaNaturalEntity.getTelefono();
	}

	@Override
	public void setTelefono(String telefono) {
		personaNaturalEntity.setTelefono(telefono);
	}

	@Override
	public String getCelular() {
		return personaNaturalEntity.getCelular();
	}

	@Override
	public void setCelular(String celular) {
		personaNaturalEntity.setCelular(celular);
	}

	@Override
	public String getEmail() {
		return personaNaturalEntity.getEmail();
	}

	@Override
	public void setEmail(String email) {
		personaNaturalEntity.setEmail(email);
	}

	@Override
	public FileStoreModel getFoto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileStoreModel getFirma() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNumeroDocumento() == null) ? 0 : getNumeroDocumento().hashCode());
		result = prime * result + ((getTipoDocumento() == null) ? 0 : getTipoDocumento().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PersonaNaturalModel))
			return false;
		PersonaNaturalModel other = (PersonaNaturalModel) obj;
		if (getNumeroDocumento() == null) {
			if (other.getNumeroDocumento() != null)
				return false;
		} else if (!getNumeroDocumento().equals(other.getNumeroDocumento()))
			return false;
		if (getTipoDocumento() == null) {
			if (other.getTipoDocumento() != null)
				return false;
		} else if (!getTipoDocumento().equals(other.getTipoDocumento()))
			return false;
		return true;
	}

}
