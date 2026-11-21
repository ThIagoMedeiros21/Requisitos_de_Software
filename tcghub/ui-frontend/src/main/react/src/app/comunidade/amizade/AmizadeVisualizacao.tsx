import { useSuspenseQuery } from '@tanstack/react-query';
import { Link, useNavigate, useParams } from 'react-router';
import { BarraNavegacaoComunidade } from '../perfil/BarraNavegacaoComunidade';
import {
  consultaAmizadePorId,
  useAceitarSolicitacaoAmizade,
  useCancelarSolicitacaoAmizade,
  useRecusarSolicitacaoAmizade,
  useRemoverAmizade,
} from './amizadeConsultas';

export function AmizadeVisualizacao() {
  const { id } = useParams();
  const navegar = useNavigate();
  const { data: amizade } = useSuspenseQuery(consultaAmizadePorId(id!));

  const aceitar = useAceitarSolicitacaoAmizade();
  const recusar = useRecusarSolicitacaoAmizade();
  const cancelar = useCancelarSolicitacaoAmizade();
  const remover = useRemoverAmizade();

  const pendente =
    aceitar.isPending || recusar.isPending || cancelar.isPending || remover.isPending;

  async function executarERetornar(acao: () => Promise<unknown>) {
    try {
      await acao();
      navegar('/comunidade/amizade/pesquisa');
    } catch {
      // Erro já publicado via `useNotificacoes`.
    }
  }

  return (
    <div className="mx-auto max-w-4xl space-y-6 px-4 py-6">
      <BarraNavegacaoComunidade abaAtual="rede-amigos" />

      <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-xs">
        <div className="flex flex-wrap items-center justify-between gap-2">
          <h1 className="m-0 text-xl font-bold text-slate-900">Detalhes da Amizade</h1>
          <span
            className={`rounded-full px-3 py-1 text-xs font-semibold ${
              amizade.status === 'ACEITA'
                ? 'bg-emerald-100 text-emerald-800'
                : 'bg-amber-100 text-amber-800'
            }`}
          >
            {amizade.status === 'ACEITA' ? 'Amizade Ativa' : 'Solicitação Pendente'}
          </span>
        </div>

        <dl className="mt-4 grid grid-cols-1 gap-3 text-sm sm:grid-cols-2">
          <div className="rounded-xl bg-slate-50 p-3">
            <dt className="text-xs font-semibold text-slate-500 uppercase">Solicitante</dt>
            <dd className="m-0 mt-1 font-semibold text-slate-900">
              {amizade.solicitanteApelido} (@{amizade.solicitante})
            </dd>
          </div>
          <div className="rounded-xl bg-slate-50 p-3">
            <dt className="text-xs font-semibold text-slate-500 uppercase">Destinatário</dt>
            <dd className="m-0 mt-1 font-semibold text-slate-900">
              {amizade.destinatarioApelido} (@{amizade.destinatario})
            </dd>
          </div>
        </dl>

        <div className="mt-6 flex flex-wrap gap-2">
          <Link
            to="/comunidade/amizade/pesquisa"
            className="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 no-underline hover:bg-slate-50"
          >
            Voltar para Rede de Amigos
          </Link>

          {amizade.status === 'PENDENTE' && amizade.ehDestinatario && (
            <>
              <button
                type="button"
                disabled={pendente}
                onClick={() => executarERetornar(() => aceitar.mutateAsync(amizade.amizadeId))}
                className="rounded-lg bg-emerald-600 px-4 py-2 text-sm font-semibold text-white hover:bg-emerald-500 disabled:opacity-50"
              >
                Aceitar
              </button>
              <button
                type="button"
                disabled={pendente}
                onClick={() => executarERetornar(() => recusar.mutateAsync(amizade.amizadeId))}
                className="rounded-lg border border-rose-300 bg-white px-4 py-2 text-sm font-semibold text-rose-700 hover:bg-rose-50 disabled:opacity-50"
              >
                Recusar
              </button>
            </>
          )}

          {amizade.status === 'PENDENTE' && amizade.ehSolicitante && (
            <button
              type="button"
              disabled={pendente}
              onClick={() => executarERetornar(() => cancelar.mutateAsync(amizade.amizadeId))}
              className="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-50 disabled:opacity-50"
            >
              Cancelar Solicitação
            </button>
          )}

          {amizade.status === 'ACEITA' && (
            <button
              type="button"
              disabled={pendente}
              onClick={() => executarERetornar(() => remover.mutateAsync(amizade.amizadeId))}
              className="rounded-lg border border-rose-300 bg-white px-4 py-2 text-sm font-semibold text-rose-700 hover:bg-rose-50 disabled:opacity-50"
            >
              Remover Amigo
            </button>
          )}
        </div>
      </section>
    </div>
  );
}
