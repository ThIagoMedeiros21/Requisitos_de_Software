# tcghub

Projeto focado em cardgame (**TCGHub** — plataforma web de comunidade, catálogo e comércio de cardgames).

> [!NOTE]
> Este arquivo define as convenções técnicas, arquiteturais, de stack e de qualidade compartilhadas do monorepo `tcghub/`. A orquestração multiagente e as skills de papéis pertencem exclusivamente ao fluxo privado de Lucas Menezes fora deste escopo compartilhado.

## Escopo e Distribuição de Funcionalidades
Toda funcionalidade do sistema deve ser **completa** (operações de escrita, validações, regras de negócio e consulta — nunca apenas leitura), persistida em banco de dados relacional e exposta em interface web.

| Integrante | Área | Funcionalidades (12 no total) | Subdomínios | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Lucas Menezes** | Comunidade e interação | 1. Perfil e amigos<br>2. Decks favoritos<br>3. Avaliação de cartas<br>4. Abertura preferida do anime One Piece | `comunidade` (+ gestão admin em `adm`) | Em implementação fatiada |
| **Ricardo Freitas** | Compras e organização de cartas | 1. Comprar produto<br>2. Recomendação de cartas<br>3. Wishlist<br>4. Minha coleção | `loja` (1–3) e `comunidade` (4) | BDD + `.cml` incorporados; implementação em equipe paralela |
| **Thiago Medeiros** | Operação comercial e notificações | 1. Gestão do catálogo<br>2. Gestão de pedidos<br>3. Dashboard de vendas<br>4. Avise-me | `adm` (ver [../docs/thiago-pendente/README.md](../docs/thiago-pendente/README.md)) | `.cml` incorporado; demais artefatos pendentes |

## Engenharia de Software e Arquitetura (Clean Architecture + 4 Níveis DDD)
- **MDE** (Model-Driven Engineering) com **Context Mapper** (`.cml`) e **DBML** (`.dbml`)
- **Clean Architecture + DDD nos 4 níveis**:
  1. **DDD Preliminar**: Linguagem Onipresente, Mapa de História do Usuário e Protótipos de Alta Fidelidade.
  2. **DDD Estratégico**: Subdomínios (`adm`, `comunidade`, `loja`, `compartilhado`) e relações `Downstream-Upstream` em grafo estritamente acíclico (`loja -> comunidade -> adm -> compartilhado`).
  3. **DDD Tático**: Agregados (`IAgregado`, exatamente 1 entidade raiz por agregado), Entidades, Objetos de Valor (`ObjetoValorStringAbstrato`, `ObjetoValorRegexAbstrato`, `IObjetoValor`), Serviços de Domínio (`AgregadoServicoAbstrato`) e SPIs de Repositório (`IAgregadoRepositorio`). **Proibido** modelo de domínio anêmico.
  4. **DDD Operacional**: Persistência JPA/ORM, projeções **CQRS**, exposição **BFF** REST em `ui-backend`, apresentação web em React (`../frontend/`) e **programação defensiva** estrita (ver [programar-defensivamente](.agents/skills/programar-defensivamente/SKILL.md)).
- **Isolamento entre subdomínios**: um conceito pertence ao módulo de seu subdomínio; integrações intercontextos utilizam identificadores e contratos SPI (jamais acoplamento direto ao banco de outro subdomínio, e jamais dependências cíclicas).

## Tecnologias
- **Linguagem / Backend**: Java 25, Spring Boot `4.1.1`, Framework D5 `1.0.0-SNAPSHOT`
- **Persistência / Banco Relacional**: JPA / ORM com **PostgreSQL** (execução do ambiente em Docker) e **H2** (testes `@DataJpaTest`)
- **Frontend** (`../frontend/`, standalone e desacoplado do reator Maven): React `19.3.0`, Vite `8.3.0`, Tailwind CSS v4 (Design System *Nautical Blue & White Marketplace*, sem Bootstrap e sem Angular), TanStack Query `5.104.0`, React Router `8.5.0`, Zustand, React Hook Form `7.90.0`
- **Modelagem**: Context Mapper `6.12.0` (`.cml`) e DBML `10.1.1` (`.dbml`)
- **Qualidade e Testes**: Cucumber `7.34.8`, Mockito `5.23.0`, JSpecify `1.0` (`@NullMarked` obrigatório em todos os pacotes)

## Subdomínios
- `adm` (subdomínio principal): Administrador
- `comunidade` (subdomínio de suporte): Comunidade
- `loja` (subdomínio principal): Usuario

## Maven
- `groupId`: `com.cesar.tgchub`
- `artifactId` do projeto raiz: `tcghub-pai`
- `version`: `1.0.0-SNAPSHOT`

### Módulos
- `dominio-compartilhado` (conceitos que não pertencem a nenhum subdomínio específico e são comuns a todos eles, por exemplo, os papéis de autorização e `UsuarioIdentificador`)
- `dominio-S` (entidades, objetos de valor, serviços e SPIs de um subdomínio **S**, por exemplo, interfaces de repositórios)
- `aplicacao` (serviços de aplicação, projeções de entidades e SPIs da camada de aplicação, por exemplo, interfaces de repositórios de aplicação)
- `infraestrutura` (implementações das SPIs)
- `ui-backend` (controladores REST do backend BFF para o frontend standalone em `../frontend/`)

Um conceito que pertence a um subdomínio fica no módulo dele, mesmo que outros subdomínios o usem. Todo módulo `dominio-S` depende de `dominio-compartilhado` e pode depender também do módulo de outro subdomínio cujos conceitos ele referencia, desde que as dependências entre módulos de subdomínio não formem um ciclo.

## Documentação e Modelos
Para cada subdomínio **S**:

- `docs/S/funcionalidades/*.md` (especificações de funcionalidades de **S** no formato Markdown)
- `dominio-S/src/test/resources/features/*.feature` (especificações de testes de **S** na linguagem Gherkin)
- `docs/S/projeto.cml` (modelo de projeto de software de **S** na linguagem do Context Mapper)
- `docs/S/projeto.dbml` (modelo de projeto de banco de dados relacional de **S** na linguagem DBML)

Os arquivos [compartilhado/projeto.cml](docs/compartilhado/projeto.cml) e [projeto.cml](docs/projeto.cml) complementam o modelo de projeto de software. Já o arquivo [projeto.dbml](docs/projeto.dbml) complementa o modelo do projeto de banco de dados.

## Trava Rígida de Padrões de Projeto GoF
Para cumprir o mínimo de **4 padrões de projeto GoF distintos** (dentre `Iterator`, `Decorator`, `Observer`, `Proxy`, `Strategy` e `Template Method`) **sem repetição**, vigora a regra inflexível de **1 único padrão exclusivo por integrante**:

| Integrante / Camada | Funcionalidade | Subdomínio | Padrão GoF Exclusivo | Justificativa Arquitetural |
| :--- | :--- | :--- | :--- | :--- |
| **Ricardo Freitas** | Comprar produto | `loja` | **Observer** *(exclusivo)* | Desacopla reações a eventos do ciclo de vida do pedido (aprovação de pagamento, expiração de reserva em 30 min e eventos de timeline) sem acoplar estoque, pagamento e notificações. |
| **Ricardo Freitas** | Recomendação de cartas | `loja` | **Observer** *(exclusivo)* | Atualiza o perfil de consumo de forma reativa a eventos de compra paga e reembolso confirmado, evitando contagem duplicada e acoplamento com o checkout. |
| **Ricardo Freitas** | Wishlist | `loja` | **Observer** *(exclusivo)* | Monitora transições de preço e reposição de estoque compatível (disparando alertas na transição falso → verdadeiro) e sincroniza reservas de presente. |
| **Ricardo Freitas** | Minha coleção | `comunidade` | **Observer** *(exclusivo)* | Reage a atualizações de cotação de referência e mantém consistência de consultas entre acervo pessoal e avisos informativos na wishlist. |
| **Lucas Menezes** | Perfil e amigos | `comunidade` | **Proxy** *(exclusivo)* | Proxy de proteção/visibilidade (`PerfilComunitarioVisibilidadeProxy`) controlando leitura pública vs. proprietário e bloqueando mutações de terceiros. |
| **Lucas Menezes** | Decks favoritos | `comunidade` | **Proxy** *(exclusivo)* | Proxy de integração/ACL (`CatalogoDeckProxy`) sobre SPI do Catálogo para consultar composição e preços sem acoplamento ao banco externo. |
| **Lucas Menezes** | Avaliação de cartas | `comunidade` | **Proxy** *(exclusivo)* | Proxy de elegibilidade (`AvaliacaoElegibilidadeProxy`) validando compra paga e não reembolsada (`CompraVerificada`) e selo de compra verificada. |
| **Lucas Menezes** | Abertura preferida do anime One Piece | `comunidade` + `adm` | **Proxy** *(exclusivo)* | Proxy de privacidade e autorização (`AberturaPreferidaPrivacidadeProxy`) ocultando a escolha no perfil público/amigos sem removê-la do ranking Top 5. |
| **Thiago Medeiros** *(pendente)* | Gestão do catálogo, Gestão de pedidos, Dashboard de vendas, Avise-me | `adm` | **1 padrão reservado** *(escolher entre `Strategy`, `Decorator` ou `Iterator`)* | A definir na entrega dos artefatos restantes. **`Observer`**, **`Proxy`** e **`Template Method`** permanecem **bloqueados** para evitar repetição. |
| **Camada Compartilhada / Transversal** | Fluxos estruturais de domínio, aplicação e infraestrutura D5 | `dominio-compartilhado`, `aplicacao`, `infraestrutura` | **Template Method** *(exclusivo transversal)* | Define o esqueleto invariante das operações de serviço, consulta e persistência (`AgregadoServicoAbstrato`, `AgregadoRepositorioAbstrato`, `AgregadoAplicacaoRepositorioAbstrato`), delegando variações aos ganchos das subclasses. |

## Idioma e Governança de IA
- **Artefatos**: documentação, modelos, código, testes BDD e comentários devem ser escritos em **português do Brasil**, sempre que possível.
- **Commits Git**: mensagens de commit devem ser escritas em **inglês** seguindo o padrão **Conventional Commits** (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`, `build:`, `ci:`, etc.). Nunca incluir `target/`, `node_modules/` ou `.env` nos commits e nunca executar `git push` sem pedido explícito do usuário.
- **Registros de IA e Auditoria**: manter evidências das interações com IAs em `../docs/ai-interactions/` e relatórios de auditoria em `../docs/reports/`.

## Pacotes
O código de objetos de valor, entidades e serviço de um agregado **A** de um subdomínio **S** pertencem ao pacote `com.cesar.tgchub.tcghub.dominio.S.A`.

A interface do repositório de **A** pertence ao pacote `com.cesar.tgchub.tcghub.dominio.S.A.spi`.

Projeções e serviços de aplicação de **A** pertencem ao pacote `com.cesar.tgchub.tcghub.aplicacao.S.A`.

A interface do repositório de aplicação de **A** pertence ao pacote `com.cesar.tgchub.tcghub.aplicacao.S.A.spi`.

A implementação dos repositórios de **A** pertence ao pacote `com.cesar.tgchub.tcghub.infraestrutura.S.A`.

O controlador REST de **A** pertence ao pacote `com.cesar.tgchub.tcghub.ui.backend.S.A`.

Todos os pacotes são anotados com `@NullMarked`.

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

## Execução e Validação
- **Ambiente padrão (Backend + PostgreSQL no Docker e Frontend local)**:
  - Subir `backend` (`:8080`) + `PostgreSQL` (`:5432`) via Docker (a partir da raiz `../`): `docker compose -f tcghub/compose.yaml up --build` (ou `docker compose -f compose.yaml up --build` dentro de `tcghub/`)
  - Subir frontend (`:4200`, proxy `/backend` → `http://localhost:8080`) em `../frontend`: `pnpm dev`
- **Alternativa backend em memória (H2)**: `mvn spring-boot:test-run -pl ui-backend`
- **Compilação de testes Java**: `mvn test-compile`
- **Suítes BDD / Cucumber**: `mvn test -pl dominio-adm,dominio-comunidade,dominio-loja`
- **Validação Context Mapper (`.cml`)**: `mvn -q exec:java -f .agents/skills/validar-modelo/scripts/pom.xml -Dexec.args="validate --input docs/<arquivo>.cml"`
- **Testes e build Frontend** (em `../frontend`): `pnpm test` e `pnpm build`
