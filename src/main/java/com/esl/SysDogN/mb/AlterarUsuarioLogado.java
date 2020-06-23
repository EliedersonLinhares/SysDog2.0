package com.esl.SysDogN.mb;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authc.credential.PasswordService;
import org.mindrot.jbcrypt.BCrypt;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;

import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.security.BCryptPasswordService;
import com.esl.SysDogN.security.PasswordValidate;
import com.esl.SysDogN.service.ImageService;
import com.esl.SysDogN.service.S3Service;
import com.esl.SysDogN.service.UsuarioService;

@Named
@ViewScoped
public class AlterarUsuarioLogado implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private ImageService imageService;
	
	@Inject
	private S3Service s3service;

	private Usuario usuarioAtual;

	
	PasswordService ps = new BCryptPasswordService();
	
	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}

	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}


   @PostConstruct
	public void init() {
	  buscarUsuarioAtual();
	}


	public void salvarAtual() throws IOException {
		List<String> errorList = new ArrayList<String>();
		UsuarioService usuarioService = new UsuarioService();
		char[] pass = usuarioAtual.getSenhaTemporaria().toCharArray();

		if (!PasswordValidate.isValid(usuarioAtual.getSenhaSemCriptografia(), errorList)) {
			for (String error : errorList) {
				Faces.getFlash().setKeepMessages(true);
				Messages.addError(null, error);
			}
		} else {

			if (ps.passwordsMatch(pass, usuarioAtual.getSenha())) {

				// String encryptedPassword1 =
				// ps.encryptPassword(usuarioAtual.getSenhaSemCriptografia());
				usuarioAtual.setSenha(ps.encryptPassword(usuarioAtual.getSenhaSemCriptografia()));

				usuarioService.merge(usuarioAtual);
				AutenticaMB aut = new AutenticaMB();
				aut.Logout();

				Faces.getFlash().setKeepMessages(true);
				Messages.addError(null, "Altera��o de senha efetuada com sucesso!Logue-se novamente");

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha atual est� errada", ""));

			}
		}

	}
   
   
	public void buscarUsuarioAtual() {
		AutenticaMB  aut = new AutenticaMB();
		String email = aut.getCurrentUser();
		UsuarioService usuarioService = new UsuarioService();
		usuarioAtual = usuarioService.buscaporEmail(email);
	}
	
	
	
		 public void uploadProfilePicture(FileUploadEvent event) throws IOException, Exception {
			 UsuarioService usuarioService = new UsuarioService();
			Integer size = 200;
			 
			    BufferedImage jpgImage = imageService.getJpgImageFromFile(event);
				jpgImage = imageService.cropSquare(jpgImage);
				jpgImage = imageService.resize(jpgImage, size);
			 
			 
			 String prefix = "us";
		     String fileName = prefix + usuarioAtual.getCodigo() + ".jpg";
			 s3service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
			 
			 if( usuarioAtual.getComFoto() == false) {
				 usuarioAtual.setComFoto(true);
				 usuarioService.merge(usuarioAtual);
				 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Foto inserida com sucesso, para vizualizar loge-se novamente",""));
			 }else {
			 usuarioAtual.setComFoto(true);
			 usuarioService.merge(usuarioAtual);
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Foto alterada com sucesso, para vizualizar recarregue a pagina ou loge-se novamente",""));
			 }
			 /*v 0.1
			  * Na Tabela usuario do banco de dados existe um campo "comFoto" que � usado para controlar as imagens para 
			  * exibi��o na pagina Lista-usuario.xhtml.
			  *Pela l�gica criada a imagem n�o � salva quando o registro � salvo, mais na tabela da pagina  
			  *Lista-usuario.xhtml existe um campo que exibe a foto se houver, sen�o(comFoto=false), mostra um link para a pagina
			  *uploadFoto-usuario.xhtml onde a foto � escolhida e salva no AWS S3 com o nome 'an' + o codigo do usuario,
			  *alterando tambem o campo comFoto para true
			  *
			  *Se o campo comFoto for true indicando que o usuario tem foto, quando clicado no nome com o link a pagina de altera��o
			  *ir� mostrar o campo para altera��o da imagem 
			  *
			  *
			  *v 0.2
			  *
			  *Agora a fot � inserida ou alterada pelo menu superior, clicando em 'alterar dados' � exibido uma tela
			  *onde o usuario altera a foto do perfil e sua senha 
			  */
		 }
		 
		 
		 

}
