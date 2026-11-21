# Context Mapper (DDD Estratégico e Tático) — TCGHub

> [!NOTE]
> Espaço reservado (**Onda 1**). A expansão dos agregados, entidades, objetos de valor, serviços e repositórios nos arquivos `.cml` será realizada na **Onda 2+** após confirmação explícita do usuário, conforme [AGENTS.md](../../AGENTS.md).

## Modelos CML Existentes (`tcghub/docs/`)
- **Raiz (importação global)**: [tcghub/docs/projeto.cml](../../tcghub/docs/projeto.cml)
- **Domínio e Contexto Compartilhado (`compartilhado`)**: [tcghub/docs/compartilhado/projeto.cml](../../tcghub/docs/compartilhado/projeto.cml)
- **Subdomínio `adm` (CORE_DOMAIN — Administrador)**: [tcghub/docs/adm/projeto.cml](../../tcghub/docs/adm/projeto.cml)
- **Subdomínio `comunidade` (SUPPORTING_DOMAIN — Comunidade)**: [tcghub/docs/comunidade/projeto.cml](../../tcghub/docs/comunidade/projeto.cml)
- **Subdomínio `loja` (CORE_DOMAIN — Usuario)**: [tcghub/docs/loja/projeto.cml](../../tcghub/docs/loja/projeto.cml)

## Regras Arquiteturais Aplicáveis
- Nenhum ciclo de dependência entre `adm`, `comunidade` e `loja`.
- Validação obrigatória de cada arquivo `.cml` isoladamente com Context Mapper `6.12.0` (`tcghub/.agents/skills/validar-modelo/SKILL.md`).
- Aplicação estrita da trava GoF: `Proxy` exclusivo nas funcionalidades de Lucas Menezes, `Observer` exclusivo nas de Ricardo Freitas, `Template Method` na camada compartilhada/transversal.
