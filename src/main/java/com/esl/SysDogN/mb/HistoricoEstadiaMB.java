package com.esl.SysDogN.mb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.service.EstadiaService;


@Named
@ViewScoped
public class HistoricoEstadiaMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstadiaService estadiaService;
	
     private LazyDataModel<Estadia> dataModel;
	
	public LazyDataModel<Estadia> getDataModel() {
		return dataModel;
	}
	
	public HistoricoEstadiaMB() {
		dataModel = new LazyDataModel<Estadia>() {
				
				
				private static final long serialVersionUID = 1L;
				
				 @Override
				  public List<Estadia> load(int first, int pageSize, String sortField,
				                             SortOrder sortOrder, Map<String, Object> filters) {
					
					 this.setRowCount( estadiaService.getTotalRegistros().intValue());
				     
					 List<Estadia> list =  estadiaService.listarLazy(first, pageSize, filters,sortField, sortOrder);
				      if (filters != null && filters.size() > 0) {
				          this.setRowCount( estadiaService.getColunasFiltradas(filters));
				      }
				      return list;
				  }
				
			};
	  }
	
}
