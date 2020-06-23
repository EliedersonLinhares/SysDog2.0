package com.esl.SysDogN.mb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.service.UsuarioService;


@Named
@ViewScoped
public class ListaUsuarioMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
     private LazyDataModel<Usuario> dataModel;
	
	public LazyDataModel<Usuario> getDataModel() {
		return dataModel;
	}
	
	public ListaUsuarioMB() {
		dataModel = new LazyDataModel<Usuario>() {
				
				
				private static final long serialVersionUID = 1L;
				
				 @Override
				  public List<Usuario> load(int first, int pageSize, String sortField,
				                             SortOrder sortOrder, Map<String, Object> filters) {
					 
					 this.setRowCount( usuarioService.getTotalRegistros().intValue());
				     
					 List<Usuario> list =  usuarioService.listarLazy(first, pageSize, filters,sortField, sortOrder);
				      if (filters != null && filters.size() > 0) {
				          this.setRowCount( usuarioService.getColunasFiltradas(filters));
				      }
				      return list;
				  }
				
			};
	  }
	
}
