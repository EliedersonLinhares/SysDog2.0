package com.esl.SysDogN.mb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.esl.SysDogN.model.Local;
import com.esl.SysDogN.service.LocalService;


@Named
@ViewScoped
public class ListaLocalMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private LocalService localService;
	
     private LazyDataModel<Local> dataModel;
	
	public LazyDataModel<Local> getDataModel() {
		return dataModel;
	}
	
	public ListaLocalMB() {
		dataModel = new LazyDataModel<Local>() {
				
				
				private static final long serialVersionUID = 1L;
				
				 @Override
				  public List<Local> load(int first, int pageSize, String sortField,
				                             SortOrder sortOrder, Map<String, Object> filters) {
					 
					 this.setRowCount( localService.getTotalRegistros().intValue());
				     
					 List<Local> list =  localService.listarLazy(first, pageSize, filters,sortField, sortOrder);
				      if (filters != null && filters.size() > 0) {
				          this.setRowCount( localService.getColunasFiltradas(filters));
				      }
				      return list;
				  }
				
			};
	  }
	
}
