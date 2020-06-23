package com.esl.SysDogN.teste.S3;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.junit.Test;

import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.service.UsuarioService;
import com.esl.SysDogN.util.EntityManagerProducer;



public class UsuarioLogado implements Serializable{
	private static final long serialVersionUID = 1L;

	//@Inject
	//private EntityManager manager;
	
	
   //@Inject
	//private UsuarioService usuarioService;
	
	
	
	public Usuario UsuariopPorEmail(String email) {
		
		EntityManager manager = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		
		//email = "antonio@gmail.com"	;
		
		//Usuario usuarioEmail = usuarioService.buscaporEmail("rrr");
		 
		
		//System.out.println(usuarioEmail.getNome());
		
		
		//String jpql = "SELECT a FROM usuario a WHERE a.email=:email";
	   // TypedQuery<Usuario> query = manager.createQuery(jpql, Usuario.class);
	   // query.setParameter("email", "ramon@gmail.com");
	    
	   /// Usuario usuario = query.getSingleResult();
	    
		
		//Usuario usuario = (Usuario) manager.createQuery("SELECT p FROM usuario p WHERE p.email=:pmail").setParameter("pemail", email).getSingleResult();
		
		TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u where u.email = :email ", Usuario.class);
        query.setParameter("email", email);
		
		
		try{
            return query.getSingleResult();
        }catch(NoResultException e){
            e.printStackTrace();
            return null;
        }
	    //return usuario;
	  
	}
	
	
	public Usuario busca(String login, String email) {
		EntityManager manager = com.esl.SysDogN.util.JpaUtil.getEntityManager();
        TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u where u.login = :login and u.email = :email", Usuario.class);
        query.setParameter("login", login);
        query.setParameter("email", email);

        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            e.printStackTrace();
            return null;
        }

    }
	
	public Usuario buscar2(String email){
    	
		EntityManager manager = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		
    	try {
    		
    		
    		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
    		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
    		Root<Usuario> root = criteriaQuery.from(Usuario.class);
    		criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("email"), email)));
    		Usuario resultado = (Usuario) manager.createQuery(criteriaQuery).getSingleResult();
    		
  
    		return resultado;
    	}catch(RuntimeException erro) {
    		throw erro;
    	}finally {
    		manager.close();
    	}
}
	
	
	public Usuario comEmail() {
		EntityManager manager = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		return (Usuario) manager.createNativeQuery("select * from usuario where email='ramon@gmail.com'", Usuario.class).getSingleResult();
	}
	
	
	@Test
	public void test() {
	
		//UsuarioService uc = new UsuarioService();
		//Long t = uc.getTotalRegistros();
		//System.out.println(t);
		
	UsuarioService usuarioService = new UsuarioService();	
	//Usuario usuario = buscar2("ramon@gmail.com");
		Usuario usuario = usuarioService.buscaporEmail("ramon@gmail.com");
		
	System.out.println(usuario.getNome());
		
	}
	
	
	
	
}
