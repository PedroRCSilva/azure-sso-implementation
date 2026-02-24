azure-sso-implementation

## Visão geral

Aplicação de exemplo usando **Spring Boot**, **Spring Security** e **Azure AD** como *OAuth2 Resource Server* para autenticar e autorizar acesso a um endpoint protegido.

Atualmente, a aplicação expõe um endpoint principal (`/welcome`) que retorna uma mensagem simples quando o usuário autenticado possui o perfil adequado.

## Endpoints

### `GET /welcome`

- **Descrição**: Retorna uma mensagem de boas-vindas (`\"Hello world\"`).
- **Classe**: `WelcomeController`
- **Autenticação**: Sim, via **JWT** emitido pelo Azure AD.
- **Autorização**:
  - Configurada em `SecurityConfig` para exigir a autoridade `ADMIN` (enum `UserRole.ADMIN`).
  - Somente usuários cujo registro na base contenha `role = 'ADMIN'` conseguem acessar.
- **Códigos de resposta esperados**:
  - `200 OK` – requisição autenticada com token válido e usuário com role `ADMIN`.
  - `401 UNAUTHORIZED` – token ausente/inválido, ou usuário não encontrado na base.

## Fluxo de autenticação/autorização

1. O cliente obtém um **access token JWT** junto ao Azure AD.
2. O token é enviado na requisição HTTP no header `Authorization: Bearer <token>`.
3. O Spring Security, configurado como **Resource Server**, valida o token usando as chaves públicas do Azure.
4. O `AuthDecoder` é chamado para:
   - Ler o `preferred_username` do JWT (email do usuário).
   - Buscar o usuário na base por email (`IUserService` / `UserServiceImpl` / `UserAdapter` / `IUserRepository`).
   - Mapear a role do usuário (`UserRole`) para uma `GrantedAuthority` do Spring (`SimpleGrantedAuthority`).
5. A `SecurityConfig` exige que qualquer requisição (exceto URLs liberadas) tenha a autoridade `ADMIN`.

Caso o usuário não seja encontrado, é lançada `UserNotFoundException` e o `ExceptionHandler` converte isso em `401 Unauthorized`.

## Configuração de segurança (Spring Security / Azure AD)

### Classe `SecurityConfig`

Local: `application.configuration.SecurityConfig`

Principais pontos:

- **CSRF desabilitado** e **sessão stateless**:
  - `sessionCreationPolicy(SessionCreationPolicy.STATELESS)`
- **H2 Console liberado** e outras URLs públicas:
  - `"/swagger-ui/**"`, `"/swagger-ui.html"`, `"/swagger-resources/**"`, `"/v3/api-docs/**"`, `"/actuator/*"`, `"/h2-console/**"`.
- **Resource Server com JWT**:
  - `.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new AuthDecoder(userPersistence))))`
  - Usa o `AuthDecoder` para traduzir o JWT em um `JwtAuthenticationToken` com authorities vindas do usuário da base.
- **Regra de autorização padrão**:
  - `.anyRequest().hasAuthority(UserRole.ADMIN.toString())`

### Classe `AuthDecoder`

Local: `application.configuration.AuthDecoder`

Responsável por:

- Implementar `Converter<Jwt, AbstractAuthenticationToken>`.
- Ler o claim `preferred_username` do token JWT para obter o email.
- Buscar o usuário via `IUserService`.
- Criar um `JwtAuthenticationToken` com a authority baseada na role (`UserRole`) do usuário.

## Configuração de aplicação (`application.yaml`)

Trechos relevantes:

- Configuração do **Resource Server JWT**:
  - `spring.security.oauth2.resourceserver.jwt.jwk-set-uri` aponta para o endpoint público de chaves do Azure AD:
    - `https://login.microsoftonline.com/common/discovery/v2.0/keys`
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri` define o issuer do tenant:
    - `https://login.microsoftonline.com/<tenant-id>/v2.0`
- Configuração do **H2** (banco em memória) e **Flyway**:
  - H2 configurado para rodar em memória.
  - Flyway habilitado para executar as migrações em `db/migration`.

## Estrutura de domínio de usuário

- **Enum `UserRole`**: define os papéis disponíveis (ex.: `ADMIN`).
- **Entidade JPA `UserEntity`**:
  - Tabela `users` com colunas `id`, `email`, `name`, `role`, `created_at`, `updated_at`.
- **Modelo de domínio `User`**: representa o usuário na camada de domínio.
- **Repositório `IUserRepository`**: interface Spring Data JPA com método `findByEmail`.
- **Adapter `UserAdapter`**: implementa `IUserPersistence`, traduzindo `UserEntity` para `User`.
- **Service `UserServiceImpl` / interface `IUserService`**:
  - Fornece o método `findById(String email)` (na prática, busca por email).
  - Lança `UserNotFoundException` se o usuário não existir.

## Tratamento de erros

- **Classe `ExceptionHandler`**:
  - `@RestControllerAdvice` responsável por capturar `UserNotFoundException`.
  - Retorna `401 Unauthorized` (`ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()`).

## Como testar localmente

1. Suba a aplicação (por exemplo, com `mvn spring-boot:run`).
2. Gere um **access token** válido no Azure AD para o tenant configurado e para a aplicação (client) correta.
3. Garanta que exista um registro na tabela `users` com:
   - `email` igual ao claim `preferred_username` do token.
   - `role = 'ADMIN'`.
   - A migração inicial já insere um usuário admin de exemplo.
4. Faça uma requisição:

   - Método: `GET`
   - URL: `http://localhost:8080/welcome`
   - Header: `Authorization: Bearer <seu_token_jwt>`

5. Se tudo estiver correto, você deverá receber `200 OK` com o corpo:

   ```text
   Hello world
   ```
