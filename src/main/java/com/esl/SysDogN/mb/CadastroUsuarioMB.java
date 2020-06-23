package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authc.credential.PasswordService;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;

import com.esl.SysDogN.model.ResetarSenha;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.security.BCryptPasswordService;
import com.esl.SysDogN.service.ImageService;
import com.esl.SysDogN.service.ResetarSenhaService;
import com.esl.SysDogN.service.S3Service;
import com.esl.SysDogN.service.UsuarioService;

@Named
@ViewScoped
public class CadastroUsuarioMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Random rand = new Random();
	
	ResetarSenha resetarSenha = new ResetarSenha();
	ResetarSenhaService  resetarSenhaService = new ResetarSenhaService();
	
	public ResetarSenha getResetarSenha() {
		return resetarSenha;
	}

	public void setResetarSenha(ResetarSenha resetarSenha) {
		this.resetarSenha = resetarSenha;
	}

	@Inject
	private ImageService imageService;
	
	@Inject
	private S3Service s3service;
	
	private Usuario usuario = new Usuario();
	
	private Long CodigoUsuario;
	
	@Inject
	private UsuarioService usuarioService;
	
	PasswordService ps = new BCryptPasswordService();
	
	// usado para edicao inciando se não for nulo
	public void inicializar() {
		if(CodigoUsuario != null) {
			usuario = usuarioService.porCodigo(CodigoUsuario);
		}
	}
	
	//apos o salvamento faz o redirecionamento para a pagina da string
	public String salvar() throws Exception {
		if(CodigoUsuario == null) {
			
			usuario.setAtivo(true);
			usuario.setComFoto(false);
			usuario.setAlterandoSenha(true);
			
			String novoHash = NovoHash();
			
			//String encryptedPassword1 = ps.encryptPassword(usuario.getSenhaSemCriptografia());
			usuario.setSenha(ps.encryptPassword(novoHash));
			
				usuarioService.save(usuario);
				addDetailMessage("Usuario " + usuario.getNome() + " adicionado com sucesso");
				
				//cadastroDeNovoUsuario();	
		
		}else {	
		
			usuarioService.save(usuario);
			addDetailMessage("Usuario " + usuario.getNome() + " alterado com sucesso");

		}
		Faces.getFlash().setKeepMessages(true);
		return "lista-usuario.xhtml?faces-redirect=true";
	}
	
	
	public Date agora() {
		LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		Instant agorainstant = agora.toInstant(ZoneOffset.of("-03:00"));
		Date agoraconvertido = Date.from(agorainstant);
		
		return agoraconvertido;
	}

	public Date agora20() {
		LocalDateTime agora20 = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(40);
		Instant agora20instant = agora20.toInstant(ZoneOffset.of("-03:00"));
		Date agora20convertido = Date.from(agora20instant);
		
		return agora20convertido;
	}
	
	
	public void cadastroDeNovoUsuario() {
		
		
		
		String novoHash = NovoHash(); // Cria o novo hash
		//	String hashencripitado = ps.encryptPassword(novoHash);//encripta o hash
			
        //seta  os dados que serão gravados
			resetarSenha.setHash(ps.encryptPassword(novoHash));
			resetarSenha.setUsuario(usuario);
			resetarSenha.setDataInicial(agora());
			resetarSenha.setDataExpiracao(agora20());
			resetarSenhaService.save(resetarSenha);
	}
	
	private String NovoHash() {
		char[] vet = new char[10];
		for(int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return  new String(vet);
	}
	 private char randomChar() {
		
		int opt = rand.nextInt(3);//quantidade de characteres diferentes
		if(opt == 0) { //gera um digito
			return(char)(rand.nextInt(10) + 48);//1
		}
		else if(opt == 1) {//gera uma letra maiuscula
			return(char)(rand.nextInt(26) + 65);//2
		}
		else {//gera uma letra minuscula
			return(char)(rand.nextInt(26) + 97);//3
		}
		
		/*--1
		 * O numero 10 se refere a quantidade de numeros 0-9
		 * e o numero 48 ao numero da posicão 0 na tabela unicode
		 * 
		 * --2
		 * O numero 26 se refere a quantidade de letras A-Z
		 * e o numero 65 ao numero da posicão A na tabela unicode
		 * 
		 * --3
		 * O numero 26 se refere a quantidade de letras a-z
		 * e o numero 97 ao numero da posicão A na tabela unicode
		 */
	}
	
	
	
	
	
	public String excluir() {
	
		usuarioService.delete(usuario);
		deletePicture();
		
		addDetailMessage("Usuario " + usuario.getNome() +" removido com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-usuario.xhtml?faces-redirect=true";
		
	}

	public String ativarDesativarConta() {
	
	if(usuario.getAtivo() == true) {	
		usuario.setAtivo(false);
		usuarioService.save(usuario);
		addDetailMessage("Usuario " + usuario.getNome() +" foi desativado com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-usuario.xhtml?faces-redirect=true";
		
	}else {
		usuario.setAtivo(true);
		usuarioService.save(usuario);
		addDetailMessage("Usuario " + usuario.getNome() +" foi ativado com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-usuario.xhtml?faces-redirect=true";
		
	}
	
	}
	
	
	//Metodo para fazer o upload da foto usando a classe S3service
	 public void uploadProfilePicture(FileUploadEvent event) throws IOException, Exception {
	
		Integer size = 200;
		 
		    BufferedImage jpgImage = imageService.getJpgImageFromFile(event);
			jpgImage = imageService.cropSquare(jpgImage);
			jpgImage = imageService.resize(jpgImage, size);
		 
		 
		 String prefix = "us";
	     String fileName = prefix + usuario.getCodigo() + ".jpg";
		 s3service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		 
		 usuario.setComFoto(true);
		 usuarioService.save(usuario);
		 
		 /*
		  * Na Tabela usuario do banco de dados existe um campo "comFoto" que é usado para controlar as imagens para 
		  * exibição na pagina Lista-usuario.xhtml.
		  *Pela lógica criada a imagem não é salva quando o registro é salvo, mais na tabela da pagina  
		  *Lista-usuario.xhtml existe um campo que exibe a foto se houver, senão(comFoto=false), mostra um link para a pagina
		  *uploadFoto-usuario.xhtml onde a foto é escolhida e salva no AWS S3 com o nome 'an' + o codigo do usuario,
		  *alterando tambem o campo comFoto para true
		  *
		  *Se o campo comFoto for true indicando que o usuario tem foto, quando clicado no nome com o link a pagina de alteração
		  *irá mostrar o campo para alteração da imagem 
		  *
		  */
	 }
	
	//metod para apagar a foto junto com o cadastro
	 public void deletePicture() {
		 String prefix = "an";
	     String fileName = prefix + usuario.getCodigo() + ".jpg";
		 s3service.deleteFile(fileName);
	 }
	 
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getCodigoUsuario() {
		return CodigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		CodigoUsuario = codigoUsuario;
	}
	

}
