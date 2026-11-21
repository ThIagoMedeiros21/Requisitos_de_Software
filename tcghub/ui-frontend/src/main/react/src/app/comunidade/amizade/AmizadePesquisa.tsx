import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { Link } from 'react-router';
import { BarraNavegacaoComunidade } from '../perfil/BarraNavegacaoComunidade';
import { type SubAbaAmizade, usePerfilStore } from '../perfil/perfilStore';
import {
  consultaRedeAmizades,
  useAceitarSolicitacaoAmizade,
  useCancelarSolicitacaoAmizade,
  useRecusarSolicitacaoAmizade,
  useRemoverAmizade,
} from './amizadeConsultas';

export function AmizadePesquisa() {
  const { data: rede } = useSuspenseQuery(consultaRedeAmizades);
  const subAba = usePerfilStore((estado) => estado.subAbaAmizade);
  const definirSubAba = usePerfilStore((estado) => estado.definirSubAbaAmizade);
  const alvoRemocao = usePerfilStore((estado) => estado.alvoRemocaoAmizade);
  const abrirConfirmacao = usePerfilStore((estado) => estado.abrirConfirmacaoRemocao);
  const fecharConfirmacao = usePerfilStore((estado) => estado.fecharConfirmacaoRemocao);
  const definirAbaAtiva = usePerfilStore((estado) => estado.definirAbaAtiva);

  const aceitar = useAceitarSolicitacaoAmizade();
  const recusar = useRecusarSolicitacaoAmizade();
  const cancelar = useCancelarSolicitacaoAmizade();
  const remover = useRemoverAmizade();

  useEffect(() => {
    definirAbaAtiva('rede-amigos');
  }, [definirAbaAtiva]);

  const abas: { chave: SubAbaAmizade; rotulo: string; total: number }[] = [
    { chave: 'meus-amigos', rotulo: 'Meus Amigos', total: rede.amigos.length },
    { chave: 'recebidas', rotulo: 'Recebidas', total: rede.recebidas.length },
    { chave: 'enviadas', rotulo: 'Enviadas', total: rede.enviadas.length },
  ];

  async function confirmarRemocao() {
    if (!alvoRemocao) {
      return;
    }
    try {
      await remover.mutateAsync(alvoRemocao.id);
      fecharConfirmacao();
    } catch {
      // Erro já publicado pelo hook `useRemoverAmizade`.
    }
  }

  return (
    <div className="mx-auto max-w-6xl space-y-6 px-4 py-6">
      <BarraNavegacaoComunidade abaAtual="rede-amigos" />

      <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-xs">
        <div className="mb-5 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="m-0 text-xl font-bold text-slate-900">Rede de Amigos</h1>
            <p className="m-0 mt-1 text-xs text-slate-500">
              Gerencie suas amizades ativas e solicitações pendentes na comunidade
            </p>
          </div>
          <Link
            to="/comunidade/amizade/inclusao"
            className="rounded-lg bg-indigo-600 px-4 py-2 text-center text-sm font-semibold text-white no-underline transition hover:bg-indigo-500"
          >
            Buscar Colecionadores
          </Link>
        </div>

        <div role="tablist" aria-label="Filtros da rede de amigos" className="mb-5 flex flex-wrap gap-2">
          {abas.map((aba) => {
            const ativa = aba.chave === subAba;
            return (
              <button
                key={aba.chave}
                type="button"
                role="tab"
                aria-selected={ativa}
                onClick={() => definirSubAba(aba.chave)}
                className={`rounded-lg px-4 py-2 text-sm font-medium transition ${
                  ativa
                    ? 'bg-slate-900 text-white'
                    : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
              >
                {aba.rotulo} ({aba.total})
              </button>
            );
          })}
        </div>

        {subAba === 'meus-amigos' && (
          <>
            {rede.amigos.length === 0 ? (
              <div className="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-500">
                Você ainda não possui amigos adicionados.
              </div>
            ) : (
              <ul className="m-0 list-none space-y-3 p-0">
                {rede.amigos.map((amigo) => (
                  <li
                    key={amigo.amizadeId}
                    className="flex flex-col justify-between gap-3 rounded-xl border border-slate-200 p-4 sm:flex-row sm:items-center"
                  >
                    <div>
                      <p className="m-0 text-sm font-bold text-slate-900">{amigo.apelido}</p>
                      <p className="m-0 text-xs text-slate-500">
                        @{amigo.usuario}
                        {amigo.cidade ? ` • ${amigo.cidade}` : ''}
                      </p>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      <Link
                        to={`/comunidade/perfil-comunitario/${encodeURIComponent(amigo.perfilId)}/visualizacao`}
                        className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 no-underline hover:bg-slate-50"
                      >
                        Ver Perfil
                      </Link>
                      <button
                        type="button"
                        onClick={() =>
                          abrirConfirmacao({ id: amigo.amizadeId, apelido: amigo.apelido })
                        }
                        className="rounded-lg border border-rose-300 bg-white px-3 py-1.5 text-xs font-semibold text-rose-700 hover:bg-rose-50"
                      >
                        Remover Amigo
                      </button>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </>
        )}

        {subAba === 'recebidas' && (
          <>
            {rede.recebidas.length === 0 ? (
              <div className="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-500">
                Nenhuma solicitação de amizade recebida.
              </div>
            ) : (
              <ul className="m-0 list-none space-y-3 p-0">
                {rede.recebidas.map((item) => (
                  <li
                    key={item.amizadeId}
                    className="flex flex-col justify-between gap-3 rounded-xl border border-slate-200 p-4 sm:flex-row sm:items-center"
                  >
                    <div>
                      <p className="m-0 text-sm font-bold text-slate-900">{item.apelido}</p>
                      <p className="m-0 text-xs text-slate-500">
                        @{item.usuario}
                        {item.cidade ? ` • ${item.cidade}` : ''}
                      </p>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      <Link
                        to={`/comunidade/perfil-comunitario/${encodeURIComponent(item.perfilId)}/visualizacao`}
                        className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 no-underline hover:bg-slate-50"
                      >
                        Ver Perfil
                      </Link>
                      <button
                        type="button"
                        disabled={aceitar.isPending}
                        onClick={() => aceitar.mutate(item.amizadeId)}
                        className="rounded-lg bg-emerald-600 px-3 py-1.5 text-xs font-semibold text-white hover:bg-emerald-500 disabled:opacity-50"
                      >
                        Aceitar
                      </button>
                      <button
                        type="button"
                        disabled={recusar.isPending}
                        onClick={() => recusar.mutate(item.amizadeId)}
                        className="rounded-lg border border-rose-300 bg-white px-3 py-1.5 text-xs font-semibold text-rose-700 hover:bg-rose-50 disabled:opacity-50"
                      >
                        Recusar
                      </button>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </>
        )}

        {subAba === 'enviadas' && (
          <>
            {rede.enviadas.length === 0 ? (
              <div className="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-500">
                Nenhuma solicitação de amizade enviada.
              </div>
            ) : (
              <ul className="m-0 list-none space-y-3 p-0">
                {rede.enviadas.map((item) => (
                  <li
                    key={item.amizadeId}
                    className="flex flex-col justify-between gap-3 rounded-xl border border-slate-200 p-4 sm:flex-row sm:items-center"
                  >
                    <div>
                      <p className="m-0 text-sm font-bold text-slate-900">{item.apelido}</p>
                      <p className="m-0 text-xs text-slate-500">
                        @{item.usuario}
                        {item.cidade ? ` • ${item.cidade}` : ''}
                      </p>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      <Link
                        to={`/comunidade/perfil-comunitario/${encodeURIComponent(item.perfilId)}/visualizacao`}
                        className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 no-underline hover:bg-slate-50"
                      >
                        Ver Perfil
                      </Link>
                      <button
                        type="button"
                        disabled={cancelar.isPending}
                        onClick={() => cancelar.mutate(item.amizadeId)}
                        className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 hover:bg-slate-50 disabled:opacity-50"
                      >
                        Cancelar Solicitação
                      </button>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </>
        )}
      </section>

      {alvoRemocao && (
        <div
          role="dialog"
          aria-modal="true"
          aria-labelledby="titulo-confirmar-remocao"
          className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/60 p-4"
        >
          <div className="w-full max-w-md rounded-2xl bg-white p-6 shadow-xl">
            <h2 id="titulo-confirmar-remocao" className="m-0 text-lg font-bold text-slate-900">
              Remover Amigo
            </h2>
            <p className="m-0 mt-2 text-sm text-slate-600">
              Deseja remover <strong>{alvoRemocao.apelido}</strong> da sua rede? A remoção é{' '}
              <strong>bilateral</strong>: o vínculo será encerrado para ambos os usuários.
            </p>
            <div className="mt-5 flex justify-end gap-2">
              <button
                type="button"
                onClick={fecharConfirmacao}
                className="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
              >
                Cancelar
              </button>
              <button
                type="button"
                disabled={remover.isPending}
                onClick={confirmarRemocao}
                className="rounded-lg bg-rose-600 px-4 py-2 text-sm font-semibold text-white hover:bg-rose-500 disabled:opacity-50"
              >
                Confirmar Remoção
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
