package com.esl.SysDogN.mb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.esl.SysDogN.model.Animal;
import com.esl.SysDogN.service.AnimalService;


@Named
@ViewScoped
public class ListaAnimalMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AnimalService animalService;
	
     private LazyDataModel<Animal> dataModel;
	
	public LazyDataModel<Animal> getDataModel() {
		return dataModel;
	}
	
	public ListaAnimalMB() {
		dataModel = new LazyDataModel<Animal>() {
				
				
				private static final long serialVersionUID = 1L;
				
				 @Override
				  public List<Animal> load(int first, int pageSize, String sortField,
				                             SortOrder sortOrder, Map<String, Object> filters) {
					 
					 this.setRowCount( animalService.getTotalRegistros().intValue());
				     
					 List<Animal> list =  animalService.listarLazy(first, pageSize, filters,sortField, sortOrder);
				      if (filters != null && filters.size() > 0) {
				          this.setRowCount( animalService.getColunasFiltradas(filters));
				      }
				      return list;
				  }
				
			};
	  }
	
}
