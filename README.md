# PESCD

Sistema para gerenciamento do Programa de Estagio Supervisionado de Capacitacao Docente.

## Contribuicoes por integrante

### Renato Dias

Responsavel pela implementacao das funcionalidades de autenticacao/controle de acesso e das user stories do perfil Secretario.

#### Login e controle de acesso

- Implementacao da autenticacao com Spring Security.
- Criacao dos perfis de usuario e controle de acesso por role.
- Redirecionamento pos-login conforme o perfil do usuario.
- Telas de login, acesso negado e dashboard inicial.
- Carga inicial de usuarios/perfis para testes.

Commits e PR associados:

- [f12d4ed0c041b59d6628c82521966678b05a9e00](https://github.com/Hakirius/PESCD/commit/f12d4ed0c041b59d6628c82521966678b05a9e00) - Implementando login e autenticacao para os diferentes usuarios.
- [f2014f347272be607d715312acd403d4df9198e3](https://github.com/Hakirius/PESCD/commit/f2014f347272be607d715312acd403d4df9198e3) - Merge da pull request de login e controle de acesso.
- [Pull Request #1](https://github.com/Hakirius/PESCD/pull/1) - `feature/u01-login-controle-acesso`.

#### User stories do Secretario: S1, S2, S3 e S4

- Criacao e listagem de ofertas pelo secretario.
- Definicao do professor responsavel pela oferta.
- Visualizacao de detalhes da oferta, alunos vinculados e status de acompanhamento.
- Associacao, edicao e remocao de alunos em ofertas.
- Importacao de alunos por CSV, com criacao automatica de usuario aluno quando necessario.
- Validacoes de negocio para evitar duplicidade de matricula, RA/e-mail duplicado e edicao de oferta encerrada.
- Registro de historico/status da matricula.
- Encerramento de oferta pelo secretario apos conclusao pelo professor responsavel.
- Exportacao dos resultados finais da oferta em CSV.
- Criacao das telas do modulo Secretario para ofertas e alunos.
- Internacionalizacao das mensagens do fluxo em portugues e ingles.

Commit associado:

- [08f4da10d1f942cdc3bd6843fc8b6102d75bb11c](https://github.com/Hakirius/PESCD/commit/08f4da10d1f942cdc3bd6843fc8b6102d75bb11c) - Implementacao das user stories S1, S2, S3 e S4.
