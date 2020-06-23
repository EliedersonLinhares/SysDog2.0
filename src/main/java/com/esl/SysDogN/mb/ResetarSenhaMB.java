package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.esl.SysDogN.model.ResetarSenha;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.security.BCryptPasswordService;
import com.esl.SysDogN.service.ResetarSenhaService;
import com.esl.SysDogN.service.SmtpMailService;
import com.esl.SysDogN.service.UsuarioService;


@Named
@SessionScoped
public class ResetarSenhaMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
	
	private Random rand = new Random();
	
	ResetarSenha resetarSenha = new ResetarSenha();
	
	public ResetarSenha getResetarSenha() {
		return resetarSenha;
	}

	public void setResetarSenha(ResetarSenha resetarSenha) {
		this.resetarSenha = resetarSenha;
	}

	
	
	 private Usuario UsuarioComum;
	
	//@Inject
	//private ResetarSenhaService  resetarSenhaService;
	
//	@Inject
	//private UsuarioService usuarioService;
	
	public Usuario getUsuarioComum() {
		return UsuarioComum;
	}

	public void setUsuarioComum(Usuario usuarioComum) {
		UsuarioComum = usuarioComum;
	}

	@Inject
	private BCryptPasswordService bps;
	
	
	 @PostConstruct
	    public void init() {
		 
	 }
	
	public void ResetarSenha() {
		
		ResetarSenhaService  resetarSenhaService = new ResetarSenhaService();
		UsuarioService usuarioService = new UsuarioService();
		
		Usuario usuario = usuarioService.buscaporEmail(username);
		
		LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		Instant agorainstant = agora.toInstant(ZoneOffset.of("-03:00"));
		Date agoraconvertido = Date.from(agorainstant);
		System.out.println("Data e hora atual: " + agoraconvertido);
		
		
		LocalDateTime agora20 = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(1);
		Instant agora20instant = agora20.toInstant(ZoneOffset.of("-03:00"));
		Date agora20convertido = Date.from(agora20instant);	
		System.out.println("Data e hora quando o token expira(atual + 20 minutos): " + agora20convertido);
		
		
		
		
		if(usuario == null) {
			
			Faces.getFlash().setKeepMessages(true);
            Messages.addError(null,"Email não existente!");
            
			System.out.println("email não existente");
		}else {
		
		
		System.out.println("realiza a primeira busca pelo email inserido");
		System.out.println("Codigo do usuario: " + usuario.getCodigo());
		//Boolean rs3 = resetarSenhaDAO.buscaPorUsuario2(usuario);
		
		if(usuario.getAlterandoSenha()) {
			System.out.println("Existe um pedido de resete de senha no nome desse usuario...");
			
			ResetarSenha rs2 = resetarSenhaService.buscaPorUsuario(usuario);
			System.out.println("Pesquisa esse resete vinculado a esse usuario...");
			
			System.out.println(rs2.getDataExpiracao().before(agoraconvertido));
			System.out.println(rs2.getDataExpiracao().after(agoraconvertido));
			
			if(rs2.getDataExpiracao().after(agoraconvertido)) {
				
				Faces.getFlash().setKeepMessages(true);
	            Messages.addError(null,"Já existe um procedimento de alteração de senha para esse usuario,espere o tempo acabar para gerar um novo pedido!");
				
				System.out.println("já existe um procedimento de alteração de senha para esse usuario,espere o tempo acabar para gerar um novo pedido");
			}else {
			
		    System.out.println("o procedimento ja venceu, excluiremos o antigo...");
		    resetarSenhaService.excluirPorCodigo(rs2.getCodigo());
			
			System.out.println("continuando para criar outro novo ...");

			System.out.println("email encontrado: " + usuario.getEmail() + " Nome: " + usuario.getNome() );
			
			String novoHash = NovoHash(); //Cria o novo hash
			 
			System.out.println("hash criado: " + novoHash );
			
			String hashencripitado = bps.encryptPassword(novoHash);
			
			System.out.println("Hash encriptado com o bcrypt: " + hashencripitado );
			
			resetarSenha.setHash(hashencripitado);

			resetarSenha.setUsuario(usuario);
			System.out.println("Setando usuario...");
			
			resetarSenha.setDataInicial(agoraconvertido);
			System.out.println("Setando Data inicial...");
			
			resetarSenha.setDataExpiracao(agora20convertido);
			System.out.println("Setando Data de expiração...");
			
			resetarSenhaService.save(resetarSenha);
			
			usuario.setAlterandoSenha(true);
			usuarioService.merge(usuario);
			System.out.println("Alterando usuario para em alterandosenha");
			
			
			System.out.println("novo resete de senha salvo");
			
			 addDetailMessage("Um email foi enviado com os procedimentos para alteração de senha.Obrigado.");
             Faces.getExternalContext().getFlash().setKeepMessages(true);
      
             redirecionar("login.xhtml");
	
			}
		}else {
			
		System.out.println("Não existe procedimento de resete no nome desse usuario, continuando para criar outro novo ...");
		
		System.out.println("email encontrado: " + usuario.getEmail() + " Nome: " + usuario.getNome() );
		
		String novoHash = NovoHash(); //Cria o novo hash
		 
		System.out.println("hash criado: " + novoHash );
		
		String hashencripitado = bps.encryptPassword(novoHash);
		
		System.out.println("Hash encriptado com o bcrypt: " + hashencripitado );
		
		resetarSenha.setHash(hashencripitado);
	
		resetarSenha.setUsuario(usuario);
		System.out.println("Setando usuario...");
		
		resetarSenha.setDataInicial(agoraconvertido);
		System.out.println("Setando Data inicial...");
		
		resetarSenha.setDataExpiracao(agora20convertido);
		System.out.println("Setando Data de expiração...");
		
		resetarSenhaService.save(resetarSenha);
		
		usuario.setAlterandoSenha(true);
		
		System.out.println("Alterando usuario para em alterandosenha");
	
		usuarioService.merge(usuario);
		System.out.println("novo resete de senha salvo");
		
		addDetailMessage("Um email foi enviado com os procedimentos para alteração de senha.Obrigado.");
        Faces.getExternalContext().getFlash().setKeepMessages(true);
 
        redirecionar("login.xhtml");
		
		
		
		}
		}
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

     
   //metodo para unificar redirecionamentos que forem nescessarios
 	 public void redirecionar(String destino) {
 		try {
			Faces.redirect(destino);
		} catch (IOException e) {
			 Faces.getFlash().setKeepMessages(true);
	    	 Messages.addError(null,"Erro ao redirecionar para a pagina desejada");
		}
 	 }
     
     
     


}
