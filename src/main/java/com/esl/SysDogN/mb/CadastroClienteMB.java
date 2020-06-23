package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.esl.SysDogN.model.Cliente;
import com.esl.SysDogN.service.ClienteService;
import com.esl.SysDogN.util.CpfCnpjUtils;

@Named
@ViewScoped
public class CadastroClienteMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private Cliente cliente = new Cliente();
	
	private Long CodigoCliente;
	
	@Inject
	private ClienteService clienteService;
	
	// usado para edicao inciando se não for nulo
	public void inicializar() {
		if(CodigoCliente != null) {
			cliente = clienteService.porCodigo(CodigoCliente);
		}
	}
	
	//apos o salvamento faz o redirecionamento para a pagina da string
	public String salvar() {
		if(CodigoCliente == null) {
			//usando validor de CPFouCNPJ
			if(CpfCnpjUtils.isValid(cliente.getCpfOuCnpj()) == false) {
				addDetailMessage("CPF ou CNPJ invalido", FacesMessage.SEVERITY_ERROR);
				Faces.getFlash().setKeepMessages(true);
				return "";
			}else {

				clienteService.save(cliente);
				addDetailMessage("Cliente " + cliente.getNome() + " adicionado com sucesso");
			}
		}else {
			clienteService.save(cliente);
			addDetailMessage("Cliente " + cliente.getNome() + " alterado com sucesso");

		}
		Faces.getFlash().setKeepMessages(true);
		return "lista-cliente.xhtml?faces-redirect=true";
	}
	
	public String excluir() {
	
		clienteService.delete(cliente);
		addDetailMessage("Cliente " + cliente.getNome() +" removido com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-cliente.xhtml?faces-redirect=true";
	
		
	}
	public List<String> pesquisarDescricoesBairro(String bairro){
		return clienteService.descricoesQueContemBairro(bairro);	
		}
	public List<String> pesquisarDescricoesCidade(String cidade){
		return clienteService.descricoesQueContemBairro(cidade);	
		}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Long getCodigoCliente() {
		return CodigoCliente;
	}

	public void setCodigoCliente(Long codigoCliente) {
		CodigoCliente = codigoCliente;
	}
	

}
