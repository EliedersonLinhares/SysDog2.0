package com.esl.SysDogN.teste.S3;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.junit.Ignore;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.esl.SysDogN.dao.UsuarioDAO;

import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.security.BCryptPasswordService;



public class UsuarioDAOTest {
	
	
	PasswordService ps = new BCryptPasswordService();
	
	
	@Test
	
	public void salvar() {
		

		Usuario usuario1 = new Usuario();
		usuario1.setNome("test");
		usuario1.setEmail("tes@hotmail.com");
		usuario1.setAtivo(true);
		usuario1.setSenhaSemCriptografia("87654321");
		usuario1.setComFoto(false);
		//Sha256Hash hash= new Sha256Hash("sha256",usuario1.getSenhaSemCriptografia(),5000);
		//BCrypt psw = BCrypt.hashpw(usuario1.getSenhaSemCriptografia(), BCrypt.gensalt());
		String encryptedPassword = ps.encryptPassword(usuario1.getSenhaSemCriptografia());
		
		usuario1.setSenha(encryptedPassword);
		usuario1.setTipo("admin");
		
		
		
		
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.merge(usuario1);
		
		
		
	}

	@Test
	@Ignore
	public void autenticar() {
		String email = "bcrypt@gmail.com";
		String senha = "456";
		char[] pass =senha.toCharArray();
		
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.porCodigoLogado(11L);
		//boolean autenticado = BCrypt.checkpw(senha, usuario.getSenha());
		boolean autenticado = ps.passwordsMatch(pass, usuario.getSenha());
		
		System.out.println(autenticado);
			
		}
	

	
	
    
	
}
