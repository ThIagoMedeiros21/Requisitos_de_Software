import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router';
import { Icon } from '../../../../components/ui/Icon';
import {
  type FiltroStatusAdminAbertura,
  useOpeningsUiStore,
} from '../../../../stores/openingsUiStore';
import { useAutenticacao } from '../../../auth/api/authApi';
import { OpeningCover } from '../../../community/openings/components/OpeningCover';
import {
  consultaAberturasAdmin,
  useAlternarStatusAberturaAdmin,
  useExcluirAberturaAdmin,
} from '../api/adminOpeningsQueries';
import { AdminOpeningFormModal } from '../components/AdminOpeningFormModal';

export function AdminOpeningsPage() {
  const { autorizacoesDe } = useAutenticacao();
  const ehAdmin = Boolean(autorizacoesDe('abertura')?.incluir);

  const termo = useOpeningsUiStore((estado) => estado.termoBuscaAdmin);
  const status = useOpeningsUiStore((estado) => estado.filtroStatusAdmin);
  const definirTermo = useOpeningsUiStore((estado) => estado.definirTermoBuscaAdmin);
  const definirStatus = useOpeningsUiStore((estado) => estado.definirFiltroStatusAdmin);
  const abrirNova = useOpeningsUiStore((estado) => estado.abrirModalNovaAbertura);
  const abrirEditar = useOpeningsUiStore((estado) => estado.abrirModalEditarAbertura);
  const alvoExclusao = useOpeningsUiStore((estado) => estado.alvoExclusaoAdmin);
  const abrirConfirmacaoExclusao = useOpeningsUiStore(
    (estado) => estado.abrirConfirmacaoExclusaoAdmin,
  );
  const fecharConfirmacaoExclusao = useOpeningsUiStore(
    (estado) => estado.fecharConfirmacaoExclusaoAdmin,
  );

  const alternarStatus = useAlternarStatusAberturaAdmin();
  const excluir = useExcluirAberturaAdmin();

  const consulta = useQuery({
    ...consultaAberturasAdmin(termo, status),
    enabled: ehAdmin,
  });

  if (!ehAdmin) {
    return (
      <div className="mx-auto max-w-3xl px-margin py-space-xxl">
        <div
          role="alert"
          className="flex items-start gap-space-md rounded-2xl border border-ice-border bg-pure-white p-space-xl shadow-sm"
        >
          <span className="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-xl bg-error-container text-error">
            <Icon nome="lock" className="text-[26px]" />
          </span>
          <div className="space-y-2">
            <h1 className="font-headline-md text-headline-md text-deep-navy">
              Acesso restrito a administradores
            </h1>
            <p className="font-body-md text-body-md text-text-muted">
              Apenas usuários com autorização <strong>ADMINISTRADOR</strong> podem gerenciar o
              catálogo administrativo de aberturas de One Piece (RN002).
            </p>
            <Link
              to="/comunidade/aberturas"
              className="inline-flex items-center gap-1 font-label-md text-label-md font-semibold text-ocean-blue no-underline hover:text-deep-navy"
            >
              <Icon nome="arrow_back" className="text-[18px]" />
              <span>Ir para Aberturas da Comunidade</span>
            </Link>
          </div>
        </div>
      </div>
    );
  }

  const dados = consulta.data;
  const itens = dados?.itens ?? [];

  const abasStatus: { chave: FiltroStatusAdminAbertura; rotulo: string; total: number }[] = [
    { chave: 'TODAS', rotulo: 'Todas', total: dados?.total ?? 0 },
    { chave: 'ATIVAS', rotulo: 'Ativas', total: dados?.ativas ?? 0 },
    { chave: 'INATIVAS', rotulo: 'Inativas', total: dados?.inativas ?? 0 },
  ];

  async function confirmarExclusao() {
    if (!alvoExclusao) {
      return;
    }
    try {
      await excluir.mutateAsync(alvoExclusao.id);
      fecharConfirmacaoExclusao();
    } catch {
      // Erro RN006 já publicado pelo hook via toast.
      fecharConfirmacaoExclusao();
    }
  }

  return (
    <div className="mx-auto max-w-7xl space-y-space-lg px-margin py-space-lg md:px-margin-lg">
      <div className="flex flex-col justify-between gap-space-md rounded-2xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm sm:flex-row sm:items-center">
        <div className="space-y-1">
          <p className="font-label-sm text-label-sm text-secondary">
            Início &gt; Mídia &amp; Catálogo &gt; Aberturas de One Piece
          </p>
          <div className="flex items-center gap-2">
            <Icon nome="admin_panel_settings" className="text-[26px] text-ocean-blue" />
            <h1 className="font-headline-lg text-headline-lg text-deep-navy">
              Gestão de Aberturas
            </h1>
          </div>
          <p className="font-body-sm text-body-sm text-text-muted">
            Gerenciamento do catálogo oficial de temas musicais de One Piece (AnimeThemes)
          </p>
        </div>

        <button
          type="button"
          onClick={abrirNova}
          className="inline-flex items-center gap-1.5 rounded-xl bg-ocean-blue px-4 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
        >
          <Icon nome="add" className="text-[18px]" />
          <span>+ Cadastrar abertura</span>
        </button>
      </div>

      <section className="flex flex-col justify-between gap-4 rounded-xl border border-ice-border/60 bg-pure-white p-4 shadow-sm lg:flex-row lg:items-center">
        <div className="flex flex-wrap items-center gap-2">
          {abasStatus.map((aba) => {
            const ativa = status === aba.chave;
            return (
              <button
                key={aba.chave}
                type="button"
                onClick={() => definirStatus(aba.chave)}
                className={`rounded-lg px-3.5 py-1.5 font-label-sm text-label-sm transition-colors ${
                  ativa
                    ? 'bg-deep-navy font-semibold text-pure-white shadow-sm'
                    : 'bg-surface-container-low text-secondary hover:bg-sky-tint hover:text-deep-navy'
                }`}
              >
                {aba.rotulo} ({aba.total})
              </button>
            );
          })}
        </div>

        <div className="relative w-full lg:w-80">
          <Icon
            nome="search"
            className="pointer-events-none absolute top-2.5 left-3 text-[18px] text-secondary"
          />
          <label htmlFor="busca-abertura-admin" className="sr-only">
            Buscar abertura administrativa
          </label>
          <input
            id="busca-abertura-admin"
            type="search"
            value={termo}
            onChange={(evento) => definirTermo(evento.target.value)}
            placeholder="Buscar por número ou nome (ex: Hope, 20)..."
            className="w-full rounded-lg bg-surface-container-low py-2 pr-3 pl-9 font-body-sm text-body-sm text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
          />
        </div>
      </section>

      <section className="overflow-hidden rounded-2xl border border-ice-border/60 bg-pure-white shadow-sm">
        {consulta.isLoading ? (
          <div className="space-y-2 p-6" role="status" aria-label="Carregando catálogo administrativo">
            {[0, 1, 2, 3].map((i) => (
              <div key={i} className="h-14 animate-pulse rounded-lg bg-surface-container-low" />
            ))}
          </div>
        ) : itens.length === 0 ? (
          <div className="p-space-xl text-center font-body-md text-body-md text-text-muted">
            Nenhuma abertura encontrada para os filtros selecionados.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full border-collapse text-left">
              <thead>
                <tr className="bg-deep-navy font-label-sm text-[11px] tracking-wider text-pure-white uppercase">
                  <th className="px-4 py-3">Mídia</th>
                  <th className="px-4 py-3">Nº</th>
                  <th className="px-4 py-3">Nome da Abertura</th>
                  <th className="px-4 py-3">Artista</th>
                  <th className="px-4 py-3">Episódios</th>
                  <th className="px-4 py-3">Preferências</th>
                  <th className="px-4 py-3">Status</th>
                  <th className="px-4 py-3 text-right">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-ice-border">
                {itens.map((item) => (
                  <tr
                    key={item.id}
                    className="transition-colors hover:bg-surface-container-low/40"
                  >
                    <td className="px-4 py-3">
                      <OpeningCover
                        numero={item.numero}
                        nome={item.nome}
                        imagem={item.imagem}
                        className="aspect-video w-20 rounded-md"
                      />
                    </td>
                    <td className="px-4 py-3 font-tabular-stat text-tabular-stat text-ocean-blue">
                      #{String(item.numero).padStart(2, '0')}
                    </td>
                    <td className="px-4 py-3 font-headline-sm text-sm text-deep-navy">
                      {item.nome}
                    </td>
                    <td className="px-4 py-3 font-body-sm text-body-sm text-text-muted">
                      {item.artista}
                    </td>
                    <td className="px-4 py-3 font-body-sm text-body-sm text-text-muted">
                      {item.episodios}
                    </td>
                    <td className="px-4 py-3 font-tabular-stat text-tabular-stat text-deep-navy">
                      <span className="inline-flex items-center gap-1">
                        <Icon nome="favorite" className="text-[15px] text-ocean-blue" />
                        <span>{item.quantidadePreferencias}</span>
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <span
                        className={`inline-flex items-center rounded-full px-2.5 py-0.5 font-label-sm text-[11px] font-semibold ${
                          item.ativa
                            ? 'bg-status-positive-bg text-status-positive-text'
                            : 'bg-surface-container text-secondary'
                        }`}
                      >
                        {item.ativa ? '• Ativa' : '• Inativa'}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center justify-end gap-1.5">
                        <button
                          type="button"
                          onClick={() => abrirEditar(item)}
                          className="rounded-lg bg-surface-container-low px-2.5 py-1.5 font-label-sm text-label-sm font-semibold text-ocean-blue transition-colors hover:bg-sky-tint"
                        >
                          Editar
                        </button>
                        <button
                          type="button"
                          disabled={alternarStatus.isPending}
                          onClick={() =>
                            alternarStatus.mutate({ id: item.id, ativar: !item.ativa })
                          }
                          className="rounded-lg bg-surface-container-low px-2.5 py-1.5 font-label-sm text-label-sm font-semibold text-secondary transition-colors hover:bg-surface-container hover:text-deep-navy disabled:opacity-50"
                        >
                          {item.ativa ? 'Inativar' : 'Reativar'}
                        </button>
                        <button
                          type="button"
                          onClick={() => abrirConfirmacaoExclusao(item)}
                          className="rounded-lg px-2.5 py-1.5 font-label-sm text-label-sm font-semibold text-error transition-colors hover:bg-error-container/30"
                        >
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      <AdminOpeningFormModal />

      {alvoExclusao && (
        <div
          role="dialog"
          aria-modal="true"
          aria-labelledby="titulo-excluir-abertura"
          className="fixed inset-0 z-50 flex items-center justify-center bg-deep-navy/60 p-4 backdrop-blur-sm"
        >
          <div className="w-full max-w-md space-y-4 rounded-2xl bg-pure-white p-6 shadow-level-4">
            <h2
              id="titulo-excluir-abertura"
              className="font-headline-md text-headline-md text-deep-navy"
            >
              Excluir abertura #{String(alvoExclusao.numero).padStart(2, '0')}?
            </h2>

            {alvoExclusao.quantidadePreferencias > 0 ? (
              <div className="rounded-xl bg-status-warning-bg p-3 font-body-sm text-body-sm text-status-warning-text">
                <strong>Atenção (RN006):</strong> Esta abertura já foi escolhida por{' '}
                <strong>{alvoExclusao.quantidadePreferencias}</strong> usuário(s). A exclusão será
                bloqueada pelo servidor — utilize <strong>Inativar</strong> para preservar escolhas
                existentes.
              </div>
            ) : (
              <p className="font-body-sm text-body-sm text-text-muted">
                Deseja excluir permanentemente <strong>{alvoExclusao.nome}</strong> do catálogo
                oficial?
              </p>
            )}

            <div className="flex justify-end gap-2">
              <button
                type="button"
                onClick={fecharConfirmacaoExclusao}
                className="rounded-lg bg-surface-container-low px-4 py-2 font-label-sm text-label-sm font-semibold text-secondary hover:bg-surface-container"
              >
                Cancelar
              </button>
              <button
                type="button"
                disabled={excluir.isPending}
                onClick={() => void confirmarExclusao()}
                className="rounded-lg bg-error px-4 py-2 font-label-sm text-label-sm font-semibold text-on-error hover:opacity-90 disabled:opacity-50"
              >
                Confirmar Exclusão
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
