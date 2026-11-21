import {
  useAceitarSolicitacaoAmizade,
  useCancelarSolicitacaoAmizade,
  useEnviarSolicitacaoAmizade,
  useRecusarSolicitacaoAmizade,
  useRemoverAmizade,
} from '../amizade/amizadeConsultas';
import type { PerfilComunitarioVisaoDto } from './perfilConsultas';
import { usePerfilStore } from './perfilStore';

export function PerfilCabecalhoCard({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const abrirModalEdicao = usePerfilStore((estado) => estado.abrirModalEdicao);
  const enviarSolicitacao = useEnviarSolicitacaoAmizade();
  const aceitarSolicitacao = useAceitarSolicitacaoAmizade();
  const recusarSolicitacao = useRecusarSolicitacaoAmizade();
  const cancelarSolicitacao = useCancelarSolicitacaoAmizade();
  const removerAmizade = useRemoverAmizade();

  const inicial = (perfil.apelido || perfil.usuario).slice(0, 2).toUpperCase();
  const pendente =
    enviarSolicitacao.isPending ||
    aceitarSolicitacao.isPending ||
    recusarSolicitacao.isPending ||
    cancelarSolicitacao.isPending ||
    removerAmizade.isPending;

  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-xs">
      <div className="flex flex-col gap-5 md:flex-row md:items-start md:justify-between">
        <div className="flex items-start gap-4">
          <div className="relative flex h-20 w-20 shrink-0 items-center justify-center overflow-hidden rounded-full border-2 border-amber-400 bg-slate-900 text-xl font-bold text-white">
            {perfil.foto ? (
              <img
                src={perfil.foto}
                alt={`Foto de ${perfil.apelido}`}
                className="h-full w-full object-cover"
              />
            ) : (
              <span>{inicial}</span>
            )}
            <span
              title="Perfil ativo"
              className="absolute right-1 bottom-1 h-3.5 w-3.5 rounded-full border-2 border-white bg-emerald-500"
            />
          </div>

          <div className="space-y-2">
            <div className="flex flex-wrap items-center gap-2">
              <h1 className="m-0 text-2xl font-bold text-slate-900">{perfil.apelido}</h1>
              <span className="rounded-full bg-amber-100 px-2.5 py-0.5 text-xs font-semibold text-amber-800">
                Colecionador Verificado
              </span>
              {!perfil.ehProprietario && perfil.statusRelacao === 'AMIGO' && (
                <span className="rounded-full bg-emerald-100 px-2.5 py-0.5 text-xs font-semibold text-emerald-800">
                  Amigo
                </span>
              )}
            </div>

            <p className="m-0 text-sm text-slate-600">
              {perfil.descricao?.trim() ? (
                perfil.descricao
              ) : (
                <span className="italic text-slate-400">Nenhuma descrição adicionada</span>
              )}
            </p>

            <div className="flex flex-wrap gap-x-4 gap-y-1 text-xs text-slate-500">
              <span>@{perfil.usuario}</span>
              {perfil.cidade?.trim() && <span>Localização: {perfil.cidade}</span>}
              {perfil.preferenciasPublicas?.trim() && (
                <span>Preferências: {perfil.preferenciasPublicas}</span>
              )}
            </div>
          </div>
        </div>

        <div className="flex flex-wrap items-center gap-2">
          {perfil.ehProprietario ? (
            <button
              type="button"
              onClick={abrirModalEdicao}
              className="rounded-lg bg-slate-900 px-4 py-2 text-sm font-medium text-white transition hover:bg-slate-800"
            >
              Editar Perfil
            </button>
          ) : (
            <>
              {perfil.statusRelacao === 'NENHUMA' && (
                <button
                  type="button"
                  disabled={pendente}
                  onClick={() => enviarSolicitacao.mutate(perfil.usuario)}
                  className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-indigo-500 disabled:opacity-50"
                >
                  Adicionar Amigo
                </button>
              )}

              {perfil.statusRelacao === 'PENDENTE_ENVIADA' && perfil.amizadeId && (
                <button
                  type="button"
                  disabled={pendente}
                  onClick={() => cancelarSolicitacao.mutate(perfil.amizadeId!)}
                  className="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50 disabled:opacity-50"
                >
                  Cancelar Solicitação
                </button>
              )}

              {perfil.statusRelacao === 'PENDENTE_RECEBIDA' && perfil.amizadeId && (
                <>
                  <button
                    type="button"
                    disabled={pendente}
                    onClick={() => aceitarSolicitacao.mutate(perfil.amizadeId!)}
                    className="rounded-lg bg-emerald-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-emerald-500 disabled:opacity-50"
                  >
                    Aceitar
                  </button>
                  <button
                    type="button"
                    disabled={pendente}
                    onClick={() => recusarSolicitacao.mutate(perfil.amizadeId!)}
                    className="rounded-lg border border-rose-300 bg-white px-4 py-2 text-sm font-medium text-rose-700 transition hover:bg-rose-50 disabled:opacity-50"
                  >
                    Recusar
                  </button>
                </>
              )}

              {perfil.statusRelacao === 'AMIGO' && perfil.amizadeId && (
                <button
                  type="button"
                  disabled={pendente}
                  onClick={() => removerAmizade.mutate(perfil.amizadeId!)}
                  className="rounded-lg border border-rose-300 bg-white px-4 py-2 text-sm font-medium text-rose-700 transition hover:bg-rose-50 disabled:opacity-50"
                >
                  Remover Amigo
                </button>
              )}
            </>
          )}
        </div>
      </div>
    </section>
  );
}
