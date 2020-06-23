package com.esl.SysDogN.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;


@Entity
@Table(name ="cliente")
public class Cliente  {
	
	
	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(length = 50, nullable=false)
	private String nome;
	
	@Column(unique = true,nullable=false)
	private String cpfOuCnpj;
	
	@Column(length = 150, nullable=false)
	private String endereco;
	
	@Column(length = 30, nullable=false)
	private String bairro;
	
	@Column(length = 30, nullable=false)
	private String cidade;
	
	@Column(length = 2, nullable=false)
	private String estado;
	
	@Column(length = 10, nullable=false)
	private String cep;
	
	@Column(length = 13)
	private String telefone;
	
	@Column(length = 14, nullable=false)
	private String celular;
	
	@Column(length = 14)
	private String celular2;
	
	@Column(unique = true,length = 50)
	@Email
	private String email;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	public String getCelular2() {
		return celular2;
	}

	public void setCelular2(String celular2) {
		this.celular2 = celular2;
	}

	@Override
	public String toString() {
		return String.format("%s[codigo=%d]", getClass().getSimpleName(), getCodigo());
		//uso do Ominifaces para conversão de dados de caixas de seleção
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
	
	//Saber se é uma inclusão ou edição
	public boolean isInclusao() {//se o id é nulo é inclusão senão é edição
		return getCodigo() == null ? true : false;
	}
	
	public boolean isEdicao() {
		return !isInclusao();
	}
	

}
/* @Temporal é usado para manipulação de datas no hibernate, sendo 
 * DATE somente dadas TIME somente horas e TIMESTAMP data e horas
 * 
 * @OnetoOne é usado pois um cliente tem somente somente um cadastro como pessoa e pessoa so tem um cliente
 * isso impede cadastros duplicados.
 */
