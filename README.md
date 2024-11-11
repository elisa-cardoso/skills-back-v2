## Gerenciamento de Skills projeto técnico Neki | BackEnd
Este projeto é uma aplicação backend desenvolvida com Spring Boot, que oferece funcionalidades para autenticação, gerenciamento de skills e segurança baseada em JWT.

### Funcionalidades 🌟
**Login**
- [x] Recebe login e senha para autenticação.
- [x] Verifica se o login e a senha correspondem aos dados na base de dados.
- [x] Criptografa a senha.
- [x] Retorna um token JWT para acesso aos demais serviços.

**Gerenciamento de Skills**
- [x] Recebe o ID do usuário e retorna suas skills com o nível.
- [x] Associa uma skill ao usuário com um nível.
- [x] Atualiza o nível de uma skill associada ao usuário aos responder um questionário.
- [x] Remove a skill associada ao usuário.
- [x] Permite o usuário favorita uma skill.

**Utilidades**
- [x] Busca por título da skill.
- [x] Páginação.
- [x] Filtro por categoria.
- [x] CRUD para o gerenciamento das categorias.
- [x] CRUD para o gerenciamento das questões.

**Segurança**
- [x] Apenas o serviço de login é público.
- [x] Os demais serviços requerem um token JWT válido.

**Documentação**
- [x] Documentação gerada automaticamente com Swagger.
Acesse a documentação em: http://localhost:8080/swagger-ui/index.html

### Principais tecnologias utilizadas 🧩
- Java 11
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Swagger (SpringFox)
- Insomnia (para testes de API)
- CORS (Cross-Origin Resource Sharing)

### Script SQL 💾
O projeto inclui um arquivo SistemaSkill.sql que contém a criação das tabelas necessárias e a configuração do banco de dados.
