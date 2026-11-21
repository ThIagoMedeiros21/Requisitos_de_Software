# Relatório de Auditoria — Onda 1: Governança, BDD e Cucumber (TCGHub)

**Parecer Final:** **APROVADO**  
**Escopo Auditado:** Onda 1 (Governança multi-equipes, Trava de Padrões GoF, Especificações BDD em Gherkin e Esqueletos Cucumber/Step Definitions — sem código de negócio)  
**Responsável pela Auditoria:** `Supervisor`

---

## 1. Resumo Executivo

A entrega da **Onda 1** do projeto **TCGHub** foi integralmente auditada em conformidade com os requisitos da disciplina ([Trabalho_em_grupo.pdf](file:///Users/menezes/.gemini/antigravity/brain/75345d26-b0e1-41d5-a7b2-212a186dd48d/scratch/requisitos/Trabalho_em_grupo.pdf)), as especificações de funcionalidades de **Lucas Menezes** ([TCGHub_Funcionalidades_Lucas.txt](file:///Users/menezes/.gemini/antigravity/brain/75345d26-b0e1-41d5-a7b2-212a186dd48d/scratch/requisitos/TCGHub_Funcionalidades_Lucas.txt)) e **Ricardo Freitas** ([TCGHub_Funcionalidades_Ricardo.txt](file:///Users/menezes/.gemini/antigravity/brain/75345d26-b0e1-41d5-a7b2-212a186dd48d/scratch/requisitos/TCGHub_Funcionalidades_Ricardo.txt)), além das convenções do framework **D5** no subdiretório `tcghub/`.

Todos os critérios de auditoria foram satisfeitos:
- Governança completa na raiz (`AGENTS.md`), atualização compatível em `tcghub/AGENTS.md`, `.gitignore` configurado, 7 skills de papéis em `.agents/skills/` (incluindo `repo-guardian`) e 5 diretórios reservados em `docs/`.
- Trava rígida de padrões GoF cumprida com **4 padrões distintos**, **1 padrão por integrante**, e **zero repetição**.
- **9 arquivos `.feature`** em português (`# language: pt`) totalizando **143 cenários** e cobrindo **100%** das Regras de Negócio (`RN`) e fluxos das **8 funcionalidades** ativas (Lucas + Ricardo).
- **9 classes `*StepDefinitions.java`** com **514 métodos anotados** (`@Dado`, `@Quando`, `@Entao`) lançando `PendingException`, sem implementação prematura de código de negócio em `src/main/java`.
- Compilação Java bem-sucedida em todos os 7 módulos Maven Java e execução Cucumber verificada sem `UndefinedStepException` nem `DuplicateStepDefinitionException`.

---

## 2. Auditoria de Governança, Skills e Placeholders

### 2.1 Arquivos de Governança e Documentação Verificados
| Artefato | Caminho | Status | Observações |
| :--- | :--- | :--- | :--- |
| Governança Raiz | [AGENTS.md](file:///Users/menezes/faculdade/Requisitos_de_Software/AGENTS.md) | Aprovado | Define stack obrigatória, Clean Architecture + 4 níveis DDD, funcionalidades completas, escopo por ondas (Onda 1 BDD Lucas+Ricardo; Onda 2+ código apenas Lucas), trava GoF, gate antes do planejamento e workflow de 5 etapas. |
| Convenções Monorepo | [tcghub/AGENTS.md](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/AGENTS.md) | Aprovado | Preserva stack D5 (`Java 25`, `Spring Boot 4.1.1`, `JPA/ORM`, `React 19.3.0`, `Cucumber 7.34.8`, `Mockito 5.23.0`, `JSpecify 1.0`, `Context Mapper 6.12.0`, `DBML 10.1.1`), módulos Maven (`com.cesar.tgchub:tcghub-pai`), pacotes `@NullMarked` e referencia `../AGENTS.md`. |
| Skill Arquiteto | [architect/SKILL.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents/skills/architect/SKILL.md) | Aprovado | Cobre DDD preliminar/estratégico/tático/operacional, CML, DBML, gate com usuário e trava GoF. |
| Skill Designer | [designer/SKILL.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents/skills/designer/SKILL.md) | Aprovado | Cobre Linguagem Onipresente, User Story Map, protótipos alinhados às rotas D5 e gate com usuário. |
| Skill Backend | [backend-developer/SKILL.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents/skills/backend-developer/SKILL.md) | Aprovado | Cobre Java 25, Spring Boot, D5, `@NullMarked`, programação defensiva, Mockito, restrição Onda 1 e trava GoF (`Proxy` para Lucas). |
| Skill Frontend | [frontend-developer/SKILL.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents/skills/frontend-developer/SKILL.md) | Aprovado | Cobre React 19, rotas D5, TanStack Query, React Hook Form, React Bootstrap e restrição Onda 1. |
| Skill QA | [qa-engineer/SKILL.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents/skills/qa-engineer/SKILL.md) | Aprovado | Cobre especificação Gherkin `# language: pt` e validação Cucumber (`mvn test -pl dominio-adm,dominio-comunidade,dominio-loja`). |
| Skill Supervisor | [supervisor/SKILL.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents/skills/supervisor/SKILL.md) | Aprovado | Cobre Etapa 2 (validação de planejamento) e Etapa 5 (auditoria final, relatórios e registro de IA). |
| Skill Guardião do Repositório | [repo-guardian/SKILL.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents/skills/repo-guardian/SKILL.md) | Aprovado | Cobre monitoramento Git, análise de impacto em `compartilhado`/`adm`/`comunidade`/Lucas e commits simples, pequenos e incrementais em inglês no padrão Conventional Commits (sem `target/` e sem `git push` automático). |
| Ignore do Repositório | [.gitignore](file:///Users/menezes/faculdade/Requisitos_de_Software/.gitignore) | Aprovado | Ignora `**/target/`, `.DS_Store`, `*.log`, `.idea/`, `*.iml`, mantendo `.agents/` e `docs/` rastreados. |
| Linguagem Onipresente | [docs/linguagem-onipresente/README.md](file:///Users/menezes/faculdade/Requisitos_de_Software/docs/linguagem-onipresente/README.md) | Aprovado | Placeholder reservado para DDD Preliminar. |
| User Story Map | [docs/user-story-map/README.md](file:///Users/menezes/faculdade/Requisitos_de_Software/docs/user-story-map/README.md) | Aprovado | Placeholder reservado para mapa de histórias da Onda 2+. |
| Context Mapper | [docs/context-mapper/README.md](file:///Users/menezes/faculdade/Requisitos_de_Software/docs/context-mapper/README.md) | Aprovado | Vincula `tcghub/docs/projeto.cml` e `.cml` dos subdomínios `compartilhado`, `adm`, `comunidade` e `loja`. |
| Protótipos | [docs/prototipos/README.md](file:///Users/menezes/faculdade/Requisitos_de_Software/docs/prototipos/README.md) | Aprovado | Placeholder reservado para protótipos de alta fidelidade da Onda 2+. |
| Pendências Thiago | [docs/thiago-pendente/README.md](file:///Users/menezes/faculdade/Requisitos_de_Software/docs/thiago-pendente/README.md) | Aprovado | Registra as 4 funcionalidades pendentes de Thiago Medeiros e os padrões GoF bloqueados. |

---

## 3. Auditoria da Trava Rígida de Padrões GoF

Regra verificada: mínimo de **4 padrões GoF distintos** (dentre `Iterator`, `Decorator`, `Observer`, `Proxy`, `Strategy`, `Template Method`), **somente 1 padrão por integrante**, e **zero repetição**.

| Integrante / Camada | Funcionalidades | Subdomínio(s) | Padrão GoF Exclusivo | Resultado da Auditoria |
| :--- | :--- | :--- | :--- | :--- |
| **Ricardo Freitas** | Comprar produto, Recomendação de cartas, Wishlist, Minha coleção | `loja` (1–3), `comunidade` (4) | **Observer** *(exclusivo)* | Conforme — bloqueado para Lucas, Thiago e camada compartilhada. |
| **Lucas Menezes** | Perfil e amigos, Decks favoritos, Avaliação de cartas, Abertura preferida do anime One Piece | `comunidade` (+ `adm` admin aberturas) | **Proxy** *(exclusivo)* | Conforme — único padrão atribuído a Lucas; bloqueado para Ricardo, Thiago e camada compartilhada. |
| **Camada Compartilhada / Transversal** | Fluxos estruturais D5 de domínio, aplicação e infraestrutura | `dominio-compartilhado`, `aplicacao`, `infraestrutura` | **Template Method** *(exclusivo transversal)* | Conforme — garante o mínimo de 4 padrões distintos mantendo 1 padrão por membro. |
| **Thiago Medeiros** *(pendente)* | Gestão do catálogo, Gestão de pedidos, Dashboard de vendas, Avise-me | A confirmar | **1 padrão reservado** (`Strategy`, `Decorator` ou `Iterator`) | Conforme — `Observer`, `Proxy` e `Template Method` explicitamente bloqueados. |

---

## 4. Matriz de Cobertura BDD (`.feature`)

Todos os 9 arquivos `.feature` iniciam com `# language: pt` e empregam palavras-chave Gherkin em português (`Funcionalidade`, `Contexto`, `Cenário`, `Dado`, `Quando`, `Então`, `E`) no estilo declarativo do D5.

| Integrante | Funcionalidade | Módulo | Arquivo `.feature` | Cenários | Regras de Negócio Cobertas |
| :--- | :--- | :--- | :--- | :---: | :--- |
| **Lucas** | Abertura preferida (Admin) | `dominio-adm` | [abertura.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-adm/src/test/resources/features/abertura.feature) | 19 | RN 1 (CRUD admin, número único, status ativa/inativa) e RN 6 (inativação/reativação e bloqueio de exclusão se escolhida), além de autorização admin. |
| **Lucas** | Perfil e amigos | `dominio-comunidade` | [perfil-e-amigos.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/resources/features/perfil-e-amigos.feature) | 26 | RN 1 (apelido único e obrigatório), RN 2 (amizade válida sem auto-adição/duplicidade, solicitação/aceite/recusa/cancelamento), RN 3 (remoção bilateral), além de edição/cancelamento e destaques (até 4 cartas). |
| **Lucas** | Decks favoritos | `dominio-comunidade` | [decks-favoritos.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/resources/features/decks-favoritos.feature) | 15 | RN 1 (limite de 4 decks e remoção para liberar vaga), RN 2 (sem duplicata, deck inativo/inexistente), RN 3 (composição via interface do Catálogo + tratamento de indisponibilidade), além de pesquisa e ocultação no perfil público. |
| **Lucas** | Avaliação de cartas | `dominio-comunidade` | [avaliacao-de-cartas.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/resources/features/avaliacao-de-cartas.feature) | 16 | RN 1 (apenas compradores com pedido pago não reembolsado), RN 2 (1 nota por usuário com atualização), RN 3 (faixa 1..5), RN 4 (média e contagem no catálogo), RN 5 (reembolso invalida e recalcula), RN 6 (selo compra verificada), além de cancelamento e autorização. |
| **Lucas** | Abertura preferida (Comunidade) | `dominio-comunidade` | [abertura-preferida.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/resources/features/abertura-preferida.feature) | 16 | RN 1 (apenas ativas na escolha e pesquisa), RN 2 (1 preferida por usuário com substituição), RN 3 (exibição e ocultação no perfil), RN 4 (ranking Top 5 com contagem oculta e percentuais), RN 5 (amigos com mesma preferência e privacidade), RN 6 (preservação após inativação). |
| **Ricardo** | Minha coleção | `dominio-comunidade` | [minha-colecao.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/resources/features/minha-colecao.feature) | 9 | RN 1 (cadastro/edição/remoção com idioma, condição, observações), RN 2 (quantidade positiva e remoção ao zerar), RN 3 (consolidação por carta+idioma+condição), RN 4 (progresso por coleção), RN 5 (valuation e ausência de cotação), RN 6 (visibilidade do resumo público), RN 7 (aviso informativo na wishlist). |
| **Ricardo** | Comprar produto | `dominio-loja` | [comprar-produto.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-loja/src/test/resources/features/comprar-produto.feature) | 21 | RN 1 (quantidade positiva e estoque), RN 2 (reserva 30 min), RN 3 (frete PAC/Sedex por CEP e peso), RN 4 (total e 5% desconto PIX com recálculo), RN 5 (preservação de endereço), RN 6 (pagamento aprovado sem duplicar baixa), RN 7 (falha e nova tentativa sem reiniciar prazo), RN 8 (expiração liberando reserva), RN 9 (privacidade de pedidos), RN 10 (invalidação da tentativa anterior e conciliação), além de catálogo, carrinho e rastreamento. |
| **Ricardo** | Recomendação de cartas | `dominio-loja` | [recomendacao-de-cartas.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-loja/src/test/resources/features/recomendacao-de-cartas.feature) | 10 | RN 1 (perfil de consumo por pedidos pagos), RN 2 (CRUD de preferências e faixa de preço), RN 3 (combinação 40/60 ou 100% fonte única, pesos 30/25/15/15/15, unicidade por carta), RN 4 (penalização -0,10 sem negativar), RN 5 (variantes ativas com estoque e revalidação), RN 6 (fallback para populares), RN 7 (atualização e reversão por eventos com idempotência). |
| **Ricardo** | Wishlist | `dominio-loja` | [wishlist.feature](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-loja/src/test/resources/features/wishlist.feature) | 11 | RN 1 (CRUD de itens), RN 2 (unicidade carta+condição), RN 3 (privacidade padrão, token único e revogação), RN 4 (indisponíveis visíveis e não compráveis), RN 5 (alerta de menor preço compatível), RN 6 (alerta de reposição), RN 7 (compra de presente ocultando endereço), RN 8 (validação de endereço do destinatário), RN 9 (limite de presentes), RN 10 (efetivação e liberação em cancelamento/expiração), RN 11 (alerta apenas na transição falso → verdadeiro). |
| **Total** | **8 funcionalidades** | **3 módulos** | **9 arquivos `.feature`** | **143** | **100% das RNs e fluxos cobertos** |

---

## 5. Auditoria de Esqueletos `*StepDefinitions.java` e Código de Negócio

### 5.1 Verificação da Restrição da Onda 1
Inspeção direta em `tcghub/dominio-{adm,comunidade,loja}/src/main/java` confirmou que existem apenas os arquivos `.gitkeep` originais — **nenhum código de negócio** (entidades, objetos de valor, serviços, repositórios, controladores ou telas) foi implementado prematuramente.

### 5.2 Classes `*StepDefinitions.java` Auditadas
| Módulo | Classe `*StepDefinitions.java` | Métodos Anotados | Estado |
| :--- | :--- | :---: | :--- |
| `dominio-adm` | [AberturaStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-adm/src/test/java/com/cesar/tgchub/tcghub/dominio/adm/abertura/AberturaStepDefinitions.java) | 46 | Todos lançam `PendingException` |
| `dominio-comunidade` | [PerfilEAmigosStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/perfil/PerfilEAmigosStepDefinitions.java) | 59 | Todos lançam `PendingException` |
| `dominio-comunidade` | [DecksFavoritosStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/deckfavorito/DecksFavoritosStepDefinitions.java) | 44 | Todos lançam `PendingException` |
| `dominio-comunidade` | [AvaliacaoDeCartasStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/avaliacao/AvaliacaoDeCartasStepDefinitions.java) | 31 | Todos lançam `PendingException` |
| `dominio-comunidade` | [AberturaPreferidaStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/aberturapreferida/AberturaPreferidaStepDefinitions.java) | 52 | Todos lançam `PendingException` |
| `dominio-comunidade` | [MinhaColecaoStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/colecaopessoal/MinhaColecaoStepDefinitions.java) | 50 | Todos lançam `PendingException` |
| `dominio-loja` | [ComprarProdutoStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-loja/src/test/java/com/cesar/tgchub/tcghub/dominio/loja/compra/ComprarProdutoStepDefinitions.java) | 97 | Todos lançam `PendingException` |
| `dominio-loja` | [RecomendacaoDeCartasStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-loja/src/test/java/com/cesar/tgchub/tcghub/dominio/loja/recomendacao/RecomendacaoDeCartasStepDefinitions.java) | 56 | Todos lançam `PendingException` |
| `dominio-loja` | [WishlistStepDefinitions.java](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/dominio-loja/src/test/java/com/cesar/tgchub/tcghub/dominio/loja/wishlist/WishlistStepDefinitions.java) | 79 | Todos lançam `PendingException` |
| **Total** | **9 classes** | **514** | **100% esqueletos limpos** |

---

## 6. Resultados Reais da Execução Maven e Cucumber

Comandos executados diretamente pelo `Supervisor` com `JAVA_HOME="/Applications/IntelliJ IDEA.app/Contents/jbr/Contents/Home"` e Maven `3.9.16`:

### 6.1 Compilação de Testes nos 7 Módulos Java
Comando: `mvn -f tcghub/pom.xml test-compile -pl dominio-compartilhado,dominio-adm,dominio-comunidade,dominio-loja,aplicacao,infraestrutura,ui-backend`  
Resultado: **`BUILD SUCCESS`** (`0.612 s` no reactor; todos os 7 módulos em `SUCCESS`).

### 6.2 Execução Cucumber em Dry-Run (Reconhecimento Integral de Steps e Glue)
Comando: `mvn -f tcghub/pom.xml test -Dcucumber.execution.dry-run=true -pl dominio-adm,dominio-comunidade,dominio-loja`  
Resultado: **`BUILD SUCCESS`** (`2.098 s`)
- `tcghub-dominio-adm`: **Tests run: 19, Failures: 0, Errors: 0, Skipped: 0**
- `tcghub-dominio-comunidade`: **Tests run: 82, Failures: 0, Errors: 0, Skipped: 0**
- `tcghub-dominio-loja`: **Tests run: 42, Failures: 0, Errors: 0, Skipped: 0**
- **Total consolidado:** **143 cenários** reconhecidos, **0 falhas**, **0 erros**, **0 ignorados**.

### 6.3 Execução Cucumber Padrão (Verificação de `PendingException` Esperada na Onda 1)
Comando: `mvn -f tcghub/pom.xml test -fae -pl dominio-adm,dominio-comunidade,dominio-loja`  
Resultado observado:
- `tcghub-dominio-adm`: **Tests run: 19, Failures: 0, Errors: 19, Skipped: 0**
- `tcghub-dominio-comunidade`: **Tests run: 82, Failures: 0, Errors: 82, Skipped: 0**
- `tcghub-dominio-loja`: **Tests run: 42, Failures: 0, Errors: 42, Skipped: 0**
- Contagem exata de exceções na saída completa:
  - `UndefinedStepException`: **0**
  - `DuplicateStepDefinitionException`: **0**
  - `PendingException` (`io.cucumber.java.PendingException: TODO: implement me`): **143** (100% dos cenários atingem o esqueleto esperado da Onda 1).

---

## 7. Próximos Passos (Onda 2+)

- A **Onda 2+** (planejamento e implementação de código de negócio) atuará **exclusivamente nas 4 funcionalidades de Lucas Menezes** (`Perfil e amigos`, `Decks favoritos`, `Avaliação de cartas` e `Abertura preferida do anime One Piece`) utilizando o padrão exclusivo **`Proxy`** (e **`Template Method`** na camada compartilhada/transversal D5).
- **Gate obrigatório:** Qualquer execução da **Etapa 1 (Planejar)** exige **confirmação explícita prévia do usuário**.
