package com.esl.SysDogN.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;


@Entity
@Table(name ="usuario")
public class Usuario  {
	
	
	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	private Long codigo;

	
	@Column(length = 50, nullable = false)
	private String nome;
	
	@Column(unique = true,length= 50, nullable = false)
	@Email
	private String email;
	
	@Column(length = 70, nullable = false)
	private String senha;
	
	@Transient
	private String senhaTemporaria;
	
	@Transient
	private String senhaSemCriptografia;

	@Column(nullable = false)
	private String tipo;
	
	@Column(nullable = false)
	private Boolean ativo;
	
	private Boolean comFoto;
	
	@Column(nullable = false)
	private Boolean alterandoSenha;
	
	
	public Boolean getAlterandoSenha() {
		return alterandoSenha;
	}

	public void setAlterandoSenha(Boolean alterandoSenha) {
		this.alterandoSenha = alterandoSenha;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}


public String getSenhaSemCriptografia() {
	return senhaSemCriptografia;
}
public void setSenhaSemCriptografia(String senhaSemCriptografia) {
	this.senhaSemCriptografia = senhaSemCriptografia;
}


public String getNome() {
	return nome;
}

public void setNome(String nome) {
	this.nome = nome;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getSenhaTemporaria() {
	return senhaTemporaria;
}

public void setSenhaTemporaria(String senhaTemporaria) {
	this.senhaTemporaria = senhaTemporaria;
}

public Boolean getComFoto() {
	return comFoto;
}

public void setComFoto(Boolean comFoto) {
	this.comFoto = comFoto;
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
	Usuario other = (Usuario) obj;
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

