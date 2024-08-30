# Teste de Java
Este teste foi aplicado para testar meus conhecimentos.

# Framework
Foi utilizado como framework para essa api o Spring Boot + Lombok para diminuir a verbosidade das classes e JPA para melhor tratar os repositórios.

# Banco de dados
MySQL

# Testes Unitários
JUnit, Mockito e Jacoco

# Como rodar
## Inicializando o projeto
O projeto utiliza-se do gerenciador de dependências MAVEN e para rodar recomendo que use o comando ```mvn clean install -U``` para baixar as dependencias sem nenhum cache.
## Rodar os testes
Embora o iniciar o projeto ja rode os testes por padrão ele ignora os warnings e erros possiveis da cobertura de testes. Coverage nunca foi meu forte, mas resolvi aplicar aqui para me testar tambem.
Para rodar os testes basta rodar ``` mvn test``` e então ele rodara os testes.

# Observações
1 - Lembrem-se de rodar os comandos sempre na raiz do projeto onde temos o arquivos de dependencias pom.xml pois durante o build ou o test ele ira ler este arquivo afim de baixar dependencias necessarias para o projeto.
2 - Para visualizar o relatório de cobertura de testes do Jacoco basta acessar /target/jacoco/index.html da aplicação e abrir no browse. (Live Server ou Live Preview que são extensões VSCode podem facilitar a visualização)