# Funcionalidades Pendentes — Thiago Medeiros

> [!IMPORTANT]
> As 4 funcionalidades abaixo pertencem a **Thiago Medeiros** e já tiveram seus modelos **Context Mapper (`.cml`)** incorporados em [tcghub/docs/adm/projeto.cml](../../tcghub/docs/adm/projeto.cml) no padrão D5 (sem `Observer` e sem `Proxy`). Os demais artefatos abaixo permanecem **pendentes de entrega** pela equipe responsável.

## Funcionalidades
1. **Gestão do catálogo** (`Admin`, `Colecao`, `Carta`, `Deck`, `Produto`, `VendasGateway`, `EstoqueServico`) — incorporado ao CML de `adm`
2. **Gestão de pedidos** (`PedidoAdm`, `GestaoPedidoServico`, `PagamentoAdmServico`, `ConsultaVendaServico`) — incorporado ao CML de `adm`
3. **Dashboard de vendas** (`EventoNegocio`, `ProjecaoPedido`, `ProjecaoProduto`, `ProjecaoColecao`, `SnapshotDashboard`, `DashboardServico`, `SnapshotServico`, `ProjecaoServico`) — incorporado ao CML de `adm`
4. **Avise-me** (`ProdutoDisponibilidade`, `AvisoReposicao`, `LoteDisparo`, `Notificacao`, `AviseMeServico`, `FilaReposicaoServico`, `SincronizacaoServico`, `NotificacaoServico`) — incorporado ao CML de `adm`

## Status dos Artefatos de Thiago Medeiros

| Artefato | Status | Observações |
| :--- | :--- | :--- |
| **Modelagem `.cml` (D5)** | **Incorporado** | Validado sem erros em [tcghub/docs/adm/projeto.cml](../../tcghub/docs/adm/projeto.cml) com 1 entidade raiz por agregado, `BigDecimal`, repositórios D5 e contratos explícitos (zero `Observer`). |
| **Especificações `FXXX.md`** | Pendente | Criar `tcghub/docs/adm/funcionalidades/F002..F005.md` quando as RNs consolidadas forem entregues. |
| **Cenários BDD `.feature`** | Pendente | Criar cenários Gherkin (`# language: pt`) em `tcghub/dominio-adm/src/test/resources/features/`. |
| **Modelo relacional `.dbml`** | Pendente | Expandir `tcghub/docs/adm/projeto.dbml` com prefixos únicos de 3 letras (sem colisão com `ABR_`, `PCM_`, `PCF_`, `AMI_`, `DFA_`, `CVE_`, `AVA_`, `APR_`). |
| **Protótipos e DDD Preliminar** | Pendente | Incorporar telas e termos ao User Story Map e Protótipos quando disponibilizados. |
| **Escolha final do Padrão GoF** | Pendente | Escolher **exatamente 1 padrão exclusivo** dentre **`Strategy`**, **`Decorator`** ou **`Iterator`**. **`Observer`** (Ricardo), **`Proxy`** (Lucas) e **`Template Method`** (transversal D5) estão **bloqueados**. |

## Regras de Incorporação Contínua
- **Escopo**: Todas as 4 funcionalidades devem ser completas (escrita + leitura, nunca apenas leitura), seguindo Clean Architecture + DDD (4 níveis) + stack D5 (`Java 25`, `Spring Boot 4.1.1`, `JPA/ORM`, `React 19`), em conformidade com [.agents.md](../../.agents.md).
- **Gate de Planejamento**: Antes de planejar ou implementar qualquer uma dessas funcionalidades neste repositório, verificar e confirmar com o usuário.
