package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.mail.EmailException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.esl.SysDogN.model.ResetarSenha;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.security.BCryptPasswordService;
import com.esl.SysDogN.security.PasswordValidate;
import com.esl.SysDogN.service.ResetarSenhaService;
import com.esl.SysDogN.service.SmtpMailService;
import com.esl.SysDogN.service.UsuarioService;


@ManagedBean
@SessionScoped
public class AutenticaMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Random rand = new Random();
	
	ResetarSenha resetarSenha = new ResetarSenha();
	ResetarSenhaService  resetarSenhaService = new ResetarSenhaService();
	UsuarioService usuarioService = new UsuarioService();
	PasswordService ps = new BCryptPasswordService();
	//PasswordValidate pv = new PasswordValidate();
	
	private String hash,novaSenha,confirmarSenha;
	
	private String username, password;
    private boolean rememberMe;

    private Long CodigoUsuarioLogado;
  
    private Usuario usuarioLogado;//Usuario depois de logado
    
    private Usuario UsuarioComum;//Usuario ante de se logar
    
	public Usuario getUsuarioComum() {
		return UsuarioComum;
	}

	public void setUsuarioComum(Usuario usuarioComum) {
		UsuarioComum = usuarioComum;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Long getCodigoUsuarioLogado() {
		return CodigoUsuarioLogado;
	}

	public void setCodigoUsuarioLogado(Long codigoUsuarioLogado) {
		CodigoUsuarioLogado = codigoUsuarioLogado;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    } 

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
	public ResetarSenha getResetarSenha() {
		return resetarSenha;
	}

	public void setResetarSenha(ResetarSenha resetarSenha) {
		this.resetarSenha = resetarSenha;
	}
	
    public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
    
    //Método para login atualizado para uso de senhas encriptadas com o Bcrypt
    public void loginUser(){

    	char[] pass = password.toCharArray();
    	
    	// Faz a busca pelo usuario que foi digitado no banco de dados 
    	
        UsuarioComum = usuarioService.buscaporEmail(username);
    	
        /*
         * Usa o bcrypt para conferir se a senha digitada é igual a que esta no banco 
         * 
         *Lembrando que não é possivel usar o método de encriptar a senha digitada para comparar com o banco
         *como é feito com hash do proprio shiro pois pelo bcrypt cada vez que é feito a encriptação gera um 
         *codigo diferente
         */
        if(!ps.passwordsMatch(pass, UsuarioComum.getSenha())) {
    		Faces.getFlash().setKeepMessages(true);
            Messages.addError(null,"Usuário e senha não conferem, tente novamente!");//senha errada
        
    	}else { //senha correta
    	
    	    	//pega o usuario 
    		      Subject currentUser = SecurityUtils.getSubject();
    		      
    		      //coloca o usuario e a senha dentro do token
                   UsernamePasswordToken token = new UsernamePasswordToken(username, UsuarioComum.getSenha());
             
                   //opçao de lembrar
                   token.setRememberMe(rememberMe);
                   
                   //efetua o login
                   currentUser.login(token);
            
                    UsuarioAut();
             
                    addDetailMessage("Login efetuado com sucesso");
                   Faces.getExternalContext().getFlash().setKeepMessages(true);
            
                  redirecionar("index.xhtml");
    	}
    }
    
  
 
    
    
    //retorna o tipo de usuario para uso na tela 
    public String TipoUsuario() {
    	if( SecurityUtils.getSubject().isPermitted("guest")){
    		return "funcionario";
    	}else{
    		return "administrador";
    	}
    }
    
  //retorna o usuario logado
    public String getCurrentUser() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }
   
    
   //Metodo para permitir exibição de objetos para determinados niveis 
  public boolean permitidoExibirMenuParaUsuariosLogados() {
	  if( SecurityUtils.getSubject().isPermitted("guest")){
  		return false;
  	}else{
  		return true;
  	}
  }
    

    //Saida do Sistema
    public void Logout() {
    	SecurityUtils.getSubject().logout();
    	redirecionar("login.xhtml");
    }

    /*
     * Metodo usado para capturar o usuario que se logou.
     */
    public void UsuarioAut(){
        String email = getCurrentUser();
    	//UsuarioService usuarioService = new UsuarioService();
    	usuarioLogado = usuarioService.buscaporEmail(email);
    	
    	if(usuarioLogado.getAlterandoSenha()) {
    		usuarioLogado.setAlterandoSenha(false);
    		usuarioService.merge(usuarioLogado);
    		
    		 ResetarSenha rs = resetarSenhaService.buscaPorUsuario(usuarioLogado);
    		resetarSenhaService.excluirPorCodigo(rs.getCodigo());
    		
    		
    	}	
    	
    	//impede que usuarios inativos se logem
    	if(usuarioLogado.getAtivo() == false) {
    		Faces.getFlash().setKeepMessages(true);
       	    Messages.addError(null,"Conta desativada contate o adminstrador!");
       	    Logout();
    	}
    }
    

    
 // usado para edicao inciando se não for nulo
 	public void inicializar() {
 		if(CodigoUsuarioLogado != null) {
 			UsuarioService usuarioService = new UsuarioService();
 			usuarioLogado = usuarioService.buscaporCodigo(CodigoUsuarioLogado);
 		}
 	}
 	
 	
 	//usado para caso o usuario fique inativo por muito tempo
 	 public void onIdle() throws IOException {
 		 Logout();
     	 Faces.getFlash().setKeepMessages(true);
   	     Messages.addError(null,"Sessão encerrada, logue-se novamente!");
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
 	 
	public Date agora() {
		LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		Instant agorainstant = agora.toInstant(ZoneOffset.of("-03:00"));
		Date agoraconvertido = Date.from(agorainstant);
		
		return agoraconvertido;
	}

	public Date agora20() {
		LocalDateTime agora20 = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(20);
		Instant agora20instant = agora20.toInstant(ZoneOffset.of("-03:00"));
		Date agora20convertido = Date.from(agora20instant);
		
		return agora20convertido;
	}
 	 
 	 
 	public void iniciaAlteraçãodeSenha() throws IOException, EmailException {

 		Usuario usuario = usuarioService.buscaporEmail(username);

 		//1ª Verifica se existe um usuario com o email digitado
 		if (usuario == null) {

 			Faces.getFlash().setKeepMessages(true);
 			Messages.addError(null, "Email não existente!");

 		} else {

 			//2ª 1-a - o usuario existe, agora verifica se o usuario tem status AlterandoSenha verdadeiro, indicando que já tem
 			// um pedido de alteração no nome desse usuário
 			if (usuario.getAlterandoSenha()) {  
 				ResetarSenha rs2 = resetarSenhaService.buscaPorUsuario(usuario);

 				//2ª 1-b - verifica se a data de expiração do pedido de alteração ainda é valida(antes da data e hora atual).se for não faz
 				if (rs2.getDataExpiracao().after(agora())) {
 					Faces.getFlash().setKeepMessages(true);
 					Messages.addError(null,
 							"Já existe um procedimento de alteração de senha para esse usuario,espere o tempo acabar para gerar um novo pedido!");
 				} else {
 					//Existe um cadastro de alteração, mas ele já expirou então continuamos...
 					
 					//excluimos o procedimento expirado
 					resetarSenhaService.excluirPorCodigo(rs2.getCodigo());
 					
 					String novoHash = NovoHash(); // Cria o novo hash
 					String hashencripitado = ps.encryptPassword(novoHash);//encripta o hash
 					
                    //seta  os dados que serão gravados
 					resetarSenha.setHash(hashencripitado);
 					resetarSenha.setUsuario(usuario);
 					resetarSenha.setDataInicial(agora());
 					resetarSenha.setDataExpiracao(agora20());
 					resetarSenhaService.save(resetarSenha);

 					usuario.setAlterandoSenha(true);//altera o status do usuario
 					usuarioService.merge(usuario);

 					Faces.getFlash().setKeepMessages(true);
 					Messages.addInfo(null,
 							"Um email foi enviado com os procedimentos para alteração de senha.Obrigado.");

 					//Enviamos o email com os dados 
 					SmtpMailService smtp = new SmtpMailService();
 					smtp.enviarEmailHtmlAlterarSenha(resetarSenha, hashencripitado);
 				}
 			} else {
 				//Não existe um cadastro de alteracao

 				String novoHash = NovoHash(); // Cria o novo hash
 				String hashencripitado = ps.encryptPassword(novoHash);//encripta o hash
 				resetarSenha.setHash(hashencripitado);

 				//seta  os dados da que serão gravados
 				resetarSenha.setUsuario(usuario);
 				resetarSenha.setDataInicial(agora());
 				resetarSenha.setDataExpiracao(agora20());
 				resetarSenhaService.save(resetarSenha);

 				usuario.setAlterandoSenha(true);//altera o status do usuario
 				usuarioService.merge(usuario);

 				Faces.getFlash().setKeepMessages(true);
 				Messages.addInfo(null, "Um email foi enviado com os procedimentos para alteração de senha.Obrigado.");

 				//Enviamos o email com os dados 
 				SmtpMailService smtp = new SmtpMailService();
 				smtp.enviarEmailHtmlAlterarSenha(resetarSenha, hashencripitado);

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

 
	public void confirmaAlteraçãodeSenha() {

		
		List<String> errorList = new ArrayList<String>();
		ResetarSenha rs = resetarSenhaService.buscaPorhash(hash);
		
		/*
		 * Busca um pedido de resete de senha pelo codigo de hash
		 */
		
		//1ª Verifica se existe um pedido com o hash usado
		if (rs == null) {
			Faces.getFlash().setKeepMessages(true);
			Messages.addError(null, "Codigo invalido");

		} else {
			//2ª - verifica se a data de expiração do pedido de alteração não é mais valida.
			if (rs.getDataExpiracao().before(agora())) {
				Faces.getFlash().setKeepMessages(true);
				Messages.addError(null, "Codigo com tempo expirado, efetue um novo pedido de alteração de senha");
			} else {
				
				//3ª o pedido existe e ainda é valido então prossegue com a troca das senhas, mais primeiro verifica se os dois campos são iguais
				if (!(novaSenha.equals(confirmarSenha))) {
					Faces.getFlash().setKeepMessages(true);
					Messages.addError(null, "Atençaõ as senhas digitadas não são iguais!");

				}else {
				
				//4ª faremos uma verificação na senha digitada para assegurar que não é uma senha fraca	
				if (!PasswordValidate.isValid(novaSenha, errorList)) {
					 for (String error : errorList) {
				            Faces.getFlash().setKeepMessages(true);
							Messages.addError(null, error);
				        }
				}else {

					//sendo iguais  e a senha boa, é feita a busca pelo codigo do usuario vinculado a este pedido de troca
					Usuario usuario = usuarioService.buscaporCodigo(rs.getUsuario().getCodigo());

					//encripita e salva a nova senha
					usuario.setSenha(ps.encryptPassword(novaSenha));
					usuario.setAlterandoSenha(false);
					usuarioService.merge(usuario);
					
                   //como não precisamos desse pedido de resete podemos exclui-lo
					resetarSenhaService.excluirPorCodigo(rs.getCodigo());

					Faces.getFlash().setKeepMessages(true);
					Messages.addInfo(null, "Senha alterada com sucesso.");
				}

			}
		}

	}


	}
}
