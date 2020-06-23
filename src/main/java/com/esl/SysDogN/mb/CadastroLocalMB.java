package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.esl.SysDogN.model.Local;
import com.esl.SysDogN.service.LocalService;


@Named
@ViewScoped
public class CadastroLocalMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private Local local = new Local();
	
	private Long CodigoLocal;
	
	@Inject
	private LocalService localService;
	
	// usado para edicao inciando se não for nulo
	public void inicializar() {
		if(CodigoLocal != null) {
			local = localService.porCodigo(CodigoLocal);
		}
	}
	
	//apos o salvamento faz o redirecionamento para a pagina da string
	public String salvar() {
		
		if(CodigoLocal == null) {
		localService.save(local);
		addDetailMessage("Local " + local.getDescricao() + " adicionado com sucesso");
		}else {
			localService.save(local);
			addDetailMessage("Local " + local.getDescricao() + " alterado com sucesso");
		}
		Faces.getFlash().setKeepMessages(true);
		return "lista-local.xhtml?faces-redirect=true";
	}
	
	public String excluir() {
	
		localService.delete(local);
		addDetailMessage("Local " + local.getDescricao() +" removido com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-local.xhtml?faces-redirect=true";
	
 
		
		
	}
	public List<String> pesquisarDescricoesBairro(String bairro){
		return localService.descricoesQueContemBairro(bairro);	
		}
	public List<String> pesquisarDescricoesCidade(String cidade){
		return localService.descricoesQueContemBairro(cidade);	
		}
	
	
	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Long getCodigoLocal() {
		return CodigoLocal;
	}

	public void setCodigoLocal(Long codigoLocal) {
		CodigoLocal = codigoLocal;
	}
	

}
