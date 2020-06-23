package com.esl.SysDogN.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name ="estadia")
public class Estadia {
	
	
	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	private Long codigo;

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEntrada;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataSaida;
	
	@Column(nullable=false, precision= 7, scale= 2)
	private BigDecimal valorCobrado;
	
	@Column(nullable=false, precision= 7, scale= 2)
	private BigDecimal gastoRacao;
	
	@Column(nullable=false, precision= 7, scale= 2)
	private BigDecimal gastoVacina;
	
	@Column(nullable=false, precision= 7, scale= 2)
	private BigDecimal gastoOutros;
	
	@Column( nullable=false ,precision= 7, scale= 2)
	private BigDecimal desconto;
	
	@Column( nullable=false ,precision= 7, scale= 2)
	private BigDecimal total;
	
	@Column(nullable = false)
	private Boolean status;
	
	@Column(length=100)
	private String descricao;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Local local;
	
	@OneToOne
	@JoinColumn(nullable=false)
	private Animal animal;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Usuario usuario;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}


	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}



	public BigDecimal getValorCobrado() {
		return valorCobrado;
	}

	public void setValorCobrado(BigDecimal valorCobrado) {
		this.valorCobrado = valorCobrado;
	}




	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getDescricao() {
	return descricao;
}

public void setDescricao(String descricao) {
	this.descricao = descricao;
}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}


	
public BigDecimal getGastoRacao() {
		return gastoRacao;
	}

	public void setGastoRacao(BigDecimal gastoRacao) {
		this.gastoRacao = gastoRacao;
	}

	public BigDecimal getGastoVacina() {
		return gastoVacina;
	}

	public void setGastoVacina(BigDecimal gastoVacina) {
		this.gastoVacina = gastoVacina;
	}

	public BigDecimal getGastoOutros() {
		return gastoOutros;
	}

	public void setGastoOutros(BigDecimal gastoOutros) {
		this.gastoOutros = gastoOutros;
	}

public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

@Override
public String toString() {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	StringBuilder builder = new StringBuilder();
	builder.append("Ol�,\n Estamos enviando esse email para comfirmar a conclus�o da estadia de seul animal conosco");
	builder.append("\nEstadia C�digo: ");
	builder.append(getCodigo());
	builder.append(", Cliente: ");
	builder.append(getAnimal().getCliente().getNome());
	builder.append(", Animal: ");
	builder.append(getAnimal().getNome());
	builder.append("\nData de Entrada: ");
	builder.append(sdf.format(getDataEntrada()));
	builder.append(", Data de Sa�da: ");
	builder.append(sdf.format(getDataSaida()));
	
	return builder.toString();
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
	Estadia other = (Estadia) obj;
	if (codigo == null) {
		if (other.codigo != null)
			return false;
	} else if (!codigo.equals(other.codigo))
		return false;
	return true;
}
//Saber se � uma inclus�o ou edi��o
		public boolean isInclusao() {//se o id � nulo � inclus�o sen�o � edi��o
			return getCodigo() == null ? true : false;
		}
		
		public boolean isEdicao() {
			return !isInclusao();
		}
	

}
