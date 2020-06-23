package com.esl.SysDogN.teste.S3;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;

import org.apache.commons.mail.EmailException;
import org.junit.Ignore;
import org.junit.Test;

import com.esl.SysDogN.dao.ResetarSenhaDAO;
import com.esl.SysDogN.dao.UsuarioDAO;
import com.esl.SysDogN.model.ResetarSenha;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.security.BCryptPasswordService;
import com.esl.SysDogN.service.ResetarSenhaService;
import com.esl.SysDogN.service.SmtpMailService;
import com.esl.SysDogN.service.UsuarioService;

public class NovaSenhaTest {

private String email;
	
	private Random rand = new Random();
	
	ResetarSenhaDAO resetarSenhaDAO = new ResetarSenhaDAO();
	UsuarioService usuarioService = new UsuarioService();
	

	
	
	@Test
	@Ignore
    public void ResetarSenha() throws IOException, EmailException {
	
	
		ResetarSenha resetarSenha = new ResetarSenha();
		
		
		
		ResetarSenhaService resetarSenhaService = new ResetarSenhaService();
		
		BCryptPasswordService bps = new BCryptPasswordService();
		
		
		LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		Instant agorainstant = agora.toInstant(ZoneOffset.of("-03:00"));
		Date agoraconvertido = Date.from(agorainstant);
		System.out.println("Data e hora atual: " + agoraconvertido);
		
		
		LocalDateTime agora20 = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(5);
		Instant agora20instant = agora20.toInstant(ZoneOffset.of("-03:00"));
		Date agora20convertido = Date.from(agora20instant);	
		System.out.println("Data e hora quando o token expira(atual + 20 minutos): " + agora20convertido);
		
		Usuario usuario = usuarioService.buscaporEmail("eliederson210@outlook.com");
		
		if(usuario == null) {
			System.out.println("email não existente");
		}else {
		
		
		System.out.println("realiza a primeira busca pelo email inserido");
		System.out.println("Codigo do usuario: " + usuario.getCodigo());
		//Boolean rs3 = resetarSenhaDAO.buscaPorUsuario2(usuario);
		
		if(usuario.getAlterandoSenha()) {
			System.out.println("Existe um pedido de resete de senha no nome desse usuario...");
			
			ResetarSenha rs2 = resetarSenhaDAO.buscaPorUsuario(usuario);
			System.out.println("Pesquisa esse resete vinculado a esse usuario...");
			
			System.out.println(rs2.getDataExpiracao().before(agoraconvertido));
			System.out.println(rs2.getDataExpiracao().after(agoraconvertido));
			
			if(rs2.getDataExpiracao().after(agoraconvertido)) {
				
				System.out.println("já existe um procedimento de alteração de senha para esse usuario,espere o tempo acabar para gerar um novo pedido");
			}else {
			
		    System.out.println("o procedimento ja venceu, excluiremos o antigo...");
		    resetarSenhaDAO.excluirPoCodigo(rs2.getCodigo());
			
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
			
			resetarSenhaDAO.merge(resetarSenha);
			
			usuario.setAlterandoSenha(true);
			usuarioService.merge(usuario);
			System.out.println("Alterando usuario para em alterandosenha");
			
			
			System.out.println("novo resete de senha salvo");
			
			//System.out.println("Tenta enviar email com os dados...");
			//SmtpMailService smtp = new SmtpMailService();
			// smtp.enviarEmailHtmlAlterarSenha(resetarSenha, hashencripitado);
			// System.out.println("Email contendo os dados enviado...");
			
	
			}
		}
		else {
			
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
		
		resetarSenhaDAO.merge(resetarSenha);
		
		usuario.setAlterandoSenha(true);
		
		System.out.println("Alterando usuario para em alterandosenha");
	
		usuarioService.merge(usuario);
		System.out.println("novo resete de senha salvo");
		
		//System.out.println("Tenta enviar email com os dados...");
		//SmtpMailService smtp = new SmtpMailService();
		// smtp.enviarEmailHtmlAlterarSenha(resetarSenha, hashencripitado);
		// System.out.println("Email contendo os dados enviado...");
		
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

     @Test
     @Ignore
    public void testaexclusao() {
    	 Usuario usuario = usuarioService.buscaporEmail("bcrypt@hotmail.com");
    	 ResetarSenha rs2 = resetarSenhaDAO.buscaPorUsuario(usuario);
    	 
    	 
    	resetarSenhaDAO.excluirPoCodigo(rs2.getCodigo());
    }
     
     @Test
     public void confirmaalteração() {
    	 
    	 BCryptPasswordService bps = new BCryptPasswordService();
    	 
    	 
    	 LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
 		Instant agorainstant = agora.toInstant(ZoneOffset.of("-03:00"));
 		Date agoraconvertido = Date.from(agorainstant);
 		System.out.println("Data e hora atual: " + agoraconvertido);
    	 
    	 String hash = "$2a$10$PLPoUW2XY49M01JloM3xYOmusIbNdZDJesf513oJ/NannhXDlwrAS";
    	 String novaSenha = "aaaaaaaa";
    	 String confirmarSenha = "aaaaaaa";
    	
    	 ResetarSenha rs = resetarSenhaDAO.buscaporHash(hash);
    	 if(rs == null ) {
    		 System.out.println("Codigo invalido");
    	 }else {
    		 if (rs.getDataExpiracao().before(agoraconvertido)) {
    			 System.out.println("Codigo com tempo expirado, efetue um novo pedido de alteração de senha");
    		 }else {
    			 
    			 System.out.println("Hash é valido prosseguindo com operação de troca de senhas");
    			 if(novaSenha.equals(confirmarSenha)) {
    				 System.out.println("Atençaõ as senhas digitadas não são iguais!");
    			 }else {
    				 System.out.println("Senha digitadas iguais...");
    				 System.out.println("buscando usuario vinculado a esse pedido de troca de senha...");
    				 Usuario usuario = usuarioService.buscaporCodigo(rs.getUsuario().getCodigo());
    				 System.out.println("O usuario é: " + usuario.getNome());
    				 
    				 System.out.println("Iniciando a gravação da nova senha");
    				 System.out.println("A senha escolhida é: " + novaSenha);
    				 usuario.setSenha(bps.encryptPassword(novaSenha));
    				 System.out.println("Senha nova encriptada");
    				 usuario.setAlterandoSenha(false);
    				 System.out.println("Alterando o status do usuario no campo AlterandoSenha para false");
    				 usuarioService.merge(usuario);
    				 System.out.println("Gravação das alterações efetuadas");
    				 
    				 System.out.println("não precisamos mais desse porcedimento vamos exclui-lo...");
    				 resetarSenhaDAO.excluirPoCodigo(rs.getCodigo());
    				 System.out.println("pedido de resete excluido...");
    			 }
    			 
    		 }
    	 }
    	 
     }
     
}
