# demo-project-dashboard

Projeto de demonstração da API REST da plataforma InterSCity desenvolvido para ajudar os integrantes da disciplina 
Cidades Inteligentes do curso de pós-graduação em Ciência da Computação da Universidade Federal do Maranhão
na utilização da plataforma.

Autor: Luiz Carlos Melo Muniz &lt;lcmuniz@lsdi.ufma.br>

Desenvolvido utilizando-se Spring Boot + Vaadin.

Este é um projeto em desenvolvimento. Novas funcionalidades serão adicionadas conforme o andamento dos trabalhos.

Como executar a aplicação:
  - O projeto utiliza o Spring Boot. Para iniciar dentro da IDE, basta usar a classe DashboardLabApplication
  (ela possui o método main). O Spring Boot utiliza um Tomcat embarcado e a aplicação pode ser
  acessado usando a URL http://localhost:8080
  - Para executar na linha de comando, deve-se antes criar o .jar usando o comando
  mvn package. Isto irá gerar um arquivo em target/dashboard-lab-0.0.1-SNAPSHOT.jar.
  Para executar use o comando java -jar dashboard-lab-0.0.1-SNAPSHOT.jar.
  

Atualmente as funcionalidades implementadas são:

* Listagem de todos os recursos da plataforma.

* Listagem de todos os recursos com capacidades sensores da plataforma.

* Listagem de todos os recursos com capacidades atuadores da plataforma.

* Listagem de todas as capacidades da plataforma

* Listagem de todas as capacidades do tipo sensor

* Listagem de todas as capacidades do tipo atuador

* Listagem dos dados de contexto
