package com.esl.SysDogN.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name ="local")
public class Local  {
	
	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(length=50, nullable = false)
	private String descricao;
	
	@Column(length = 100, nullable=false)
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
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
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


	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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
	
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
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
			Local other = (Local) obj;
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
