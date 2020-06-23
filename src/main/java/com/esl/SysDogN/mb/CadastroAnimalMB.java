package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;

import com.esl.SysDogN.model.Animal;
import com.esl.SysDogN.service.AnimalService;
import com.esl.SysDogN.service.ImageService;
import com.esl.SysDogN.service.S3Service;

@Named
@ViewScoped
public class CadastroAnimalMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private ImageService imageService;
	
	@Inject
	private S3Service s3service;
	
	private Animal animal = new Animal();
	
	private Long CodigoAnimal;
	
	@Inject
	private AnimalService animalService;
	
	
	public List<SelectItem> getClientes(){
		return animalService.getListCliente();
	}
	
	// usado para edicao inciando se não for nulo
	public void inicializar() {
		if(CodigoAnimal != null) {
			animal = animalService.porCodigo(CodigoAnimal);
		}
	}
	
	//apos o salvamento faz o redirecionamento para a pagina da string
	public String salvar() throws Exception {
		
		
		if(CodigoAnimal == null) {
			    animal.setComFoto(false);
			    animal.setAlocado(false);
				animalService.save(animal);
				addDetailMessage("Animal " + animal.getNome() + " adicionado com sucesso");
		
		}else {	
			
			
			
			animalService.save(animal);
			addDetailMessage("Animal " + animal.getNome() + " alterado com sucesso");

		}
		Faces.getFlash().setKeepMessages(true);
		return "lista-animal.xhtml?faces-redirect=true";
	}
	
	public String excluir() {
	
		animalService.delete(animal);
		deletePicture();
		
		addDetailMessage("Animal " + animal.getNome() +" removido com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-animal.xhtml?faces-redirect=true";
	
		
	}
	public List<String> pesquisarDescricoesRaca(String raca){
		return animalService.descricoesQueContemRaca(raca);	
		}
	
	
	//Metodo para fazer o upload da foto usando a classe S3service
	 public void uploadProfilePicture(FileUploadEvent event) throws IOException, Exception {
	
		Integer size = 200;
		 
		    BufferedImage jpgImage = imageService.getJpgImageFromFile(event);
			jpgImage = imageService.cropSquare(jpgImage);
			jpgImage = imageService.resize(jpgImage, size);
		 
		 
		 String prefix = "an";
	     String fileName = prefix + animal.getCodigo() + ".jpg";
		 s3service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		 
		 animal.setComFoto(true);
		 animalService.save(animal);
		 
		 /*
		  * Na Tabela animal do banco de dados existe um campo "comFoto" que é usado para controlar as imagens para 
		  * exibição na pagina Lista-animal.xhtml.
		  *Pela lógica criada a imagem não é salva quando o registro é salvo, mais na tabela da pagina  
		  *Lista-animal.xhtml existe um campo que exibe a foto se houver, senão(comFoto=false), mostra um link para a pagina
		  *uploadFoto-animal.xhtml onde a foto é escolhida e salva no AWS S3 com o nome 'an' + o codigo do animal,
		  *alterando tambem o campo comFoto para true
		  *
		  *Se o campo comFoto for true indicando que o animal tem foto, quando clicado no nome com o link a pagina de alteração
		  *irá mostrar o campo para alteração da imagem 
		  *
		  */
	 }
	
	//metod para apagar a foto junto com o cadastro
	 public void deletePicture() {
		 String prefix = "an";
	     String fileName = prefix + animal.getCodigo() + ".jpg";
		 s3service.deleteFile(fileName);
	 }
	 
	
	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Long getCodigoAnimal() {
		return CodigoAnimal;
	}

	public void setCodigoAnimal(Long codigoAnimal) {
		CodigoAnimal = codigoAnimal;
	}
	

	/*
	 * 
	 * 
                    <p:separator/>
                   
                     <p:outputLabel value="Alterar foto do animal"  style="font-weight: bold"/>
                     <p/>
                    
                       <p:fileUpload  fileUploadListener="#{cadastroAnimalMB.uploadProfilePicture}" filemode="advanced" fileLimit="1" 
                    allowTypes="/(\.|\/)(jpe?g|png)$/" rendered="#{cadastroAnimalMB.codigoAnimal != null and cadastroAnimalMB.animal.comFoto eq true}"
                    cancelLabel="Cancelar"   invalidFileMessage="Arquivo inválido! A imagem precisa ser .png ou .jpg"
                     invalidSizeMessage="A imagem é muito grande, escolha uma imagem menor"   label="Escolher"/>
	 */
}
