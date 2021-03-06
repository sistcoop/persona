package org.sistcoop.persona.models;

import java.util.Date;

import org.sistcoop.persona.models.enums.EstadoCivil;
import org.sistcoop.persona.models.enums.Sexo;

public interface PersonaNaturalModel extends Model {

	String APELLIDO_PATERNO = "apellidoPaterno";
	String APELLIDO_MATERNO = "apellidoMaterno";
	String NOMBRES = "nombres";
	String NUMERO_DOCUMENTO = "numeroDocumento";

	String getId();

	String getCodigoPais();

	void setCodigoPais(String codigoPais);

	TipoDocumentoModel getTipoDocumento();

	String getNumeroDocumento();

	String getApellidoPaterno();

	void setApellidoPaterno(String apellidoPaterno);

	String getApellidoMaterno();

	void setApellidoMaterno(String apellidoMaterno);

	String getNombres();

	void setNombres(String nombres);

	Date getFechaNacimiento();

	void setFechaNacimiento(Date fechaNacimiento);

	Sexo getSexo();

	void setSexo(Sexo sexo);

	EstadoCivil getEstadoCivil();

	void setEstadoCivil(EstadoCivil estadoCivil);

	String getOcupacion();

	void setOcupacion(String ocupacion);

	String getUrlFoto();

	void setUrlFoto(String urlFoto);

	String getUrlFirma();

	void setUrlFirma(String urlFirma);

	String getUbigeo();

	void setUbigeo(String ubigeo);

	String getDireccion();

	void setDireccion(String direccion);

	String getReferencia();

	void setReferencia(String referencia);

	String getTelefono();

	void setTelefono(String telefono);

	String getCelular();

	void setCelular(String celular);

	String getEmail();

	void setEmail(String email);

	FileStoreModel getFoto();

	FileStoreModel getFirma();

}