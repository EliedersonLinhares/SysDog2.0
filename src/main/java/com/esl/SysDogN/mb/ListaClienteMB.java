package com.esl.SysDogN.mb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.esl.SysDogN.model.Cliente;
import com.esl.SysDogN.service.ClienteService;


@Named
@ViewScoped
public class ListaClienteMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClienteService clienteService;
	
     private LazyDataModel<Cliente> dataModel;
	
	public LazyDataModel<Cliente> getDataModel() {
		return dataModel;
	}
	
	public ListaClienteMB() {
		dataModel = new LazyDataModel<Cliente>() {
				
				
				private static final long serialVersionUID = 1L;
				
				 @Override
				  public List<Cliente> load(int first, int pageSize, String sortField,
				                             SortOrder sortOrder, Map<String, Object> filters) {
					 
					 this.setRowCount( clienteService.getTotalRegistros().intValue());
				     
					 List<Cliente> list =  clienteService.listarLazy(first, pageSize, filters,sortField, sortOrder);
				      if (filters != null && filters.size() > 0) {
				          this.setRowCount( clienteService.getColunasFiltradas(filters));
				      }
				      return list;
				  }
				
			};
	  }
	
}
