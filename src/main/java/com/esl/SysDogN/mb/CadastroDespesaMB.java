package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.esl.SysDogN.model.Despesa;
import com.esl.SysDogN.service.DespesaService;

@Named
@ViewScoped
public class CadastroDespesaMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private Despesa despesa = new Despesa();
	
	private Long CodigoDespesa;
	
	@Inject
	private DespesaService despesaService;
	
	// usado para edicao inciando se não for nulo
	public void inicializar() {
		if(CodigoDespesa != null) {
			despesa = despesaService.porCodigo(CodigoDespesa);
		}
	}
	
	//apos o salvamento faz o redirecionamento para a pagina da string
	public String salvar() {

		if(CodigoDespesa == null) {

	    despesa.setStatus("EM ABERTO");
	    despesa.setDataInclusao(new Date());
	    
		despesaService.save(despesa);
		addDetailMessage("Despesa adicionada com sucesso");
		}else {
			despesaService.save(despesa);
			addDetailMessage("Despesa alterada com sucesso");
		}
		 Faces.getFlash().setKeepMessages(true);
			return "lista-despesa.xhtml?faces-redirect=true";
	}
	
	public String excluir() {
		
		despesaService.delete(despesa);
		addDetailMessage("Despesa removida com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-despesa.xhtml?faces-redirect=true";
	}

	public String pagar() {

		if(despesa.getDataInclusao().getTime() <= despesa.getDataPagamento().getTime()) {
	    despesa.setStatus("PAGO");

		despesaService.save(despesa);
		addDetailMessage("Pagamento da despesa confirmada");
		 Faces.getFlash().setKeepMessages(true);
			return "lista-despesa.xhtml?faces-redirect=true";
			
		}else {
			addDetailMessage("Data de pagamento não pode ser anterior a data de inclusão", FacesMessage.SEVERITY_ERROR);
			 Faces.getFlash().setKeepMessages(true);
			 return "";
		}
	}
	
	
	
	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public Long getCodigoDespesa() {
		return CodigoDespesa;
	}

	public void setCodigoDespesa(Long codigoDespesa) {
		CodigoDespesa = codigoDespesa;
	}
	

}
