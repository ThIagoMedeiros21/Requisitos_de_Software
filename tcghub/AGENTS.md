# tcghub

Projeto focado em cardgame.

> [!IMPORTANT]
> Este módulo integra o repositório [Requisitos_de_Software](../AGENTS.md). Leia [../AGENTS.md](../AGENTS.md) para a governança multi-equipes, a **Trava Rígida de Padrões GoF**, o **escopo por ondas** e o **gate obrigatório de confirmação com o usuário** antes de executar workflows de planejamento.

## Engenharia de software
- MDE (Model-Driven Engineering)
- DDD (Domain-Driven Design — preliminar, estratégico, tático e operacional)
- Evitar antipadrão modelo de domínio anêmico
- CQRS (Command Query Responsibility Segregation)
- Arquitetura limpa
- SPI (Service Provider Interface)
- BDD (Behaviour-Driven Development)
- Programação defensiva
- ORM (Object-Relational Mapping)
- BFF (Backend For Frontend)

## Tecnologias
- Context Mapper 6.12.0
- Java 25
- Cucumber 7.34.8
- Mockito 5.23.0
- JSpecify 1.0
- DBML (Database Markup Language) 10.1.1
- Spring Boot 4.1.1
- React 19.3.0
- React Router 8.5.0
- TanStack Query 5.104.0
- React Hook Form 7.90.0
- React Bootstrap 2.10.10
- Bootstrap 5.3.8
- D5 1.0.0-SNAPSHOT

## Subdomínios
- adm (subdomínio principal): Administrador
- comunidade (subdomínio de suporte): Comunidade
- loja (subdomínio principal): Usuario

## Maven
- groupId: com.cesar.tgchub
- artifactId do projeto raiz: tcghub-pai
- version: 1.0.0-SNAPSHOT

### Módulos
- dominio-compartilhado (conceitos que não pertencem a nenhum subdomínio específico e são comuns a todos eles, por exemplo, os papéis de autorização)
- dominio-**S** (entidades, objetos de valor, serviços e SPIs de um subdomínio **S**, por exemplo, interfaces de repositórios)
- aplicacao (serviços de aplicação, projeções de entidades e SPIs da camada de aplicação, por exemplo, interfaces de repositórios de aplicação)
- infraestrutura (implementações das SPIs)
- ui-backend (controladores REST do backend para o frontend)
- ui-frontend (aplicação React do frontend)

Um conceito que pertence a um subdomínio fica no módulo dele, mesmo que outros subdomínios o usem. Todo módulo dominio-**S** depende de dominio-compartilhado e pode depender também do módulo de outro subdomínio cujos conceitos ele referencia, desde que as dependências entre módulos de subdomínio não formem um ciclo.

## Documentação e modelos
Para cada subdomínio **S**:

- docs/**S**/funcionalidades/*.md (especificações de funcionalidades de **S** no formato Markdown)
- dominio-**S**/src/test/resources/features/*.feature (especificações de testes de **S** na linguagem Gherkin)
- docs/**S**/projeto.cml (modelo de projeto de software de **S** na linguagem do Context Mapper)
- docs/**S**/projeto.dbml (modelo de projeto de banco de dados relacional de **S** na linguagem DBML)

Os arquivos [compartilhado/projeto.cml](docs/compartilhado/projeto.cml) e [projeto.cml](docs/projeto.cml) complementam o modelo de projeto de software. Já o arquivo [projeto.dbml](docs/projeto.dbml) complementa o modelo do projeto de banco de dados.

## Idioma
Todos os artefatos do projeto (documentação, modelos, código, comentários, etc.) devem ser escritos em português do Brasil, sempre que possível. Mensagens de commit no Git devem ser escritas em **inglês** seguindo o padrão **Conventional Commits** (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`, `build:`, `ci:`, etc.).

## Pacotes
O código de objetos de valor, entidades e serviço de um agregado **A** de um subdomínio **S** pertencem ao pacote com.cesar.tgchub.tcghub.dominio.**S**.**A**.

A interface do repositório de **A** pertence ao pacote com.cesar.tgchub.tcghub.dominio.**S**.**A**.spi.

Projeções e serviços de aplicação de **A** pertencem ao pacote com.cesar.tgchub.tcghub.aplicacao.**S**.**A**.

A interface do repositório de aplicação de **A** pertence ao pacote com.cesar.tgchub.tcghub.aplicacao.**S**.**A**.spi.

A implementação dos repositórios de **A** pertence ao pacote com.cesar.tgchub.tcghub.infraestrutura.**S**.**A**.

O controlador REST de **A** pertence ao pacote com.cesar.tgchub.tcghub.ui.backend.**S**.**A**.

Todos os pacotes são anotados com @NullMarked.

## Escopo e Trava de Padrões GoF
- **Funcionalidades completas**: nenhuma funcionalidade pode ser apenas de leitura.
- **Escopo Onda 1**: apenas BDD (`.feature` em português) e esqueletos `*StepDefinitions.java` para as 8 funcionalidades de **Lucas Menezes** e **Ricardo Freitas** (sem implementar código de negócio).
- **Escopo Onda 2+**: este teamwork implementa **exclusivamente as 4 funcionalidades de Lucas Menezes** (`Perfil e amigos`, `Decks favoritos`, `Avaliação de cartas`, `Abertura preferida do anime One Piece`). Ricardo e Thiago (4 funcionalidades pendentes em [../docs/thiago-pendente/README.md](../docs/thiago-pendente/README.md)) seguem em outras equipes.
- **Trava rígida GoF (1 padrão único por membro, mínimo 4 distintos no projeto, zero repetição)**:
  - **Ricardo Freitas** → **Observer** *(exclusivo; bloqueado para Lucas, Thiago e camada compartilhada)* — Comprar produto (`loja`), Recomendação de cartas (`loja`), Wishlist (`loja`), Minha coleção (`comunidade`).
  - **Lucas Menezes** → **Proxy** *(exclusivo; bloqueado para Ricardo e Thiago)* — Perfil e amigos (`comunidade`), Decks favoritos (`comunidade`), Avaliação de cartas (`comunidade`), Abertura preferida do anime One Piece (`comunidade` + `adm`).
  - **Thiago Medeiros** *(pendente)* → **1 padrão reservado** (a escolher entre `Strategy`, `Decorator` ou `Iterator` quando os artefatos chegarem).
  - **Camada Compartilhada / Transversal** → **Template Method** *(exclusivo transversal em `dominio-compartilhado`, `aplicacao` e `infraestrutura` sobre os fluxos base do D5)*.
  - Ver tabela detalhada com justificativas em [../AGENTS.md](../AGENTS.md#4-trava-rígida-de-padrões-de-projeto-gof).

## Workflow Obrigatório e Skills de Papéis
Sempre **verificar e confirmar com o usuário antes de executar os workflows de planejamento** (Etapa 1). Toda funcionalidade segue as 5 etapas governadas pelas skills em [../.agents/skills/](../.agents/skills/):

1. **Planejar** ([architect](../.agents/skills/architect/SKILL.md) + [designer](../.agents/skills/designer/SKILL.md)) — após confirmação do usuário
2. **Validar planejamento** ([supervisor](../.agents/skills/supervisor/SKILL.md))
3. **Implementar** ([backend-developer](../.agents/skills/backend-developer/SKILL.md) + [frontend-developer](../.agents/skills/frontend-developer/SKILL.md))
4. **Validar cenários BDD no Cucumber** ([qa-engineer](../.agents/skills/qa-engineer/SKILL.md))
5. **Auditar conformidade e relatar** ([supervisor](../.agents/skills/supervisor/SKILL.md))

Além das 5 etapas, o papel [repo-guardian](../.agents/skills/repo-guardian/SKILL.md) acompanha continuamente mudanças no repositório, analisa impactos sobre `compartilhado`, `adm`, `comunidade` e as funcionalidades de Lucas Menezes, e consolida entregas em **commits pequenos e incrementais em inglês no padrão Conventional Commits** (mantendo código, documentação, modelos e testes em português do Brasil, sem incluir `target/` e sem executar `git push` sem autorização explícita do usuário).

## Processo de trabalho (Skills Técnicas D5)
1) [Especificar funcionalidade](.agents/skills/especificar-funcionalidade/SKILL.md)
2) [Especificar testes](.agents/skills/especificar-testes/SKILL.md)
3) [Projetar software](.agents/skills/projetar-software/SKILL.md)
4) [Desenvolver camada de domínio](.agents/skills/desenvolver-dominio-d5/SKILL.md)
5) [Desenvolver testes](.agents/skills/desenvolver-testes/SKILL.md)
6) [Projetar banco de dados](.agents/skills/projetar-banco/SKILL.md)
7) [Desenvolver camada de infraestrutura para a camada de domínio](.agents/skills/desenvolver-infraestrutura-dominio-d5/SKILL.md)
8) [Desenvolver backend](.agents/skills/desenvolver-backend-d5/SKILL.md)
9) [Desenvolver frontend](.agents/skills/desenvolver-frontend/SKILL.md)

## Execução
- Subir aplicação completa (BFF + frontend): `mvn spring-boot:test-run -pl ui-backend`
- Executar suítes BDD/Cucumber dos módulos de domínio: `mvn test -pl dominio-adm,dominio-comunidade,dominio-loja`
