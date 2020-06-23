# SysDog 2.0

Sistema de Gerenciamento de Hospedaria de animais

## Descri��o

Est� � a atualiza��o do sistema anterior, usando uma nova interface gr�fica e novas fun��es.

## Telas


![](t1.png)

![](t2.png)

![](t3.png)

![](t4.png)

![](t5.png)

![](t6.png)

![](t7.png)

![](t8.png)

![](t9.png)


### Requisitos

Este sistema atualmente roda com o MariaDB 10.x e Apache 9.0

### Instala��o

Para usar pela primeira vez execute os seguintes passos: 

```
Usando seu gerenciador de BD crie o Banco de dados "sysdog3"
```

```
No pacote com.esl.test execute o arquivo UsuarioDAOTest.java com "run as..." , "Junit Test"
```
```
Este arquivo contem o m�todo que executar� a inser��o de um usuario.
```
```
Agora basta executar o sistema com o Apache, os logins colocados para exemplo s�o

login: tes@hotmail.com  senha: 87654321     Administrador
```

## Testes

Neste sistema eu usei o JUnit para testar m�todos durante a fase de desenvolvimento.
elas est�o na pasta src/test/java

## Bugs

O sistema de envio ainda ainda tem alguns problemas na configura��o que ser�o solucionados 
em futuras atualiza��es 

## Softwares Usados


* [Primefaces 7.0](https://www.primefaces.org/) - Gerenciamento da interface JSF
* [Adminfaces](https://github.com/adminfaces) - Tema customizado para o primefaces
* [MariaDB 10.x](https://mariadb.org/) - Banco de dados usado
* [Maven](https://maven.apache.org/) - Gerenciamento de depend�ncias
* [Apache 9.0](https://www.apache.org/) - Servidor usado
* [JasperSoft](https://community.jaspersoft.com/download) - Cria��o de relat�rios
* [Apache Shiro](https://shiro.apache.org) - Seguran�a 
* [jBCrypt](https://github.com/jeremyh/jBCrypt) - Encripta��o de senhas com bcrypt
* [AWS S3](https://aws.amazon.com/pt/s3/) - Armazenamento de imagens

## Vers�o

A vers�o colocada aqui � a usada durante o desenvolvimento na vers�o 2.0

## Sobre o Projeto

* Esta � uma vers�o aperfei�oada do projeto anterior usando novas tecnlogias 
* Houve uma mudan�a no aspecto visual com uso de uma interface mais amigavel com o AdminFaces
* O sistema de seguran�a foi alterado do PhaseListener para o Apache shiro que tem um melhor desempenho
 com JSF
* A encripta��o das senhas agora � feita com o brcypt.
* Agora os avatares dos animais,usuarios s�o gravada em um servidor AWS S3.
* Tambem foi feito o uso de charts na pagina inicial, com informa��es resumidas
* Foi implementado a primeira parte do uso de email para recuper��o e altera��o de senha.
 

## Futuro

* Esta previsto um aperfei�oamento do uso dos charts em uma parte especifica com mais dados disponiveis
* Uso de email para envio ao cliente sobre abertura de nova estadia, confirma��o de pagamento, entre outros 

## Refer�ncias

As seguintes refer�ncias foram usadas no densenvolvimento desse projeto

* [Programa��o web com java - Sergio Roberto delfino] (https://www.youtube.com/watch?v=9PGp1T242hA&list=PL_GwGUsBlNyfI0W3ggfffhBdJUqB4981Z)
* [Curso Spring Boot, Hibernate, REST, Ionic, JWT, S3, MySQL, MongoDB] (https://www.udemy.com/course/spring-boot-ionic/)

## Autor

* **Eliederson Linhares**  - (https://github.com/EliedersonLinhares)
*   Email: eliederson210@outlook.com


## Licensa

Este projeto estar� sobre a licensa MIT.

