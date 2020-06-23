package com.esl.SysDogN.mb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.esl.SysDogN.model.Despesa;
import com.esl.SysDogN.service.DespesaService;


@Named
@ViewScoped
public class ListaDespesaMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DespesaService despesaService;
	
     private LazyDataModel<Despesa> dataModel;
	
	public LazyDataModel<Despesa> getDataModel() {
		return dataModel;
	}
	
	public ListaDespesaMB() {
		dataModel = new LazyDataModel<Despesa>() {
				
				
				private static final long serialVersionUID = 1L;
				
				 @Override
				  public List<Despesa> load(int first, int pageSize, String sortField,
				                             SortOrder sortOrder, Map<String, Object> filters) {
					 
					 this.setRowCount( despesaService.getTotalRegistros().intValue());
				     
					 List<Despesa> list =  despesaService.listarLazy(first, pageSize, filters,sortField, sortOrder);
				      if (filters != null && filters.size() > 0) {
				          this.setRowCount( despesaService.getColunasFiltradas(filters));
				      }
				      return list;
				  }
				
			};
	  }
	
}
