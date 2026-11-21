import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useSearchParams } from 'react-router';
import { BarraNavegacaoComunidade } from '../perfil/BarraNavegacaoComunidade';
import { usePerfilStore } from '../perfil/perfilStore';
import {
  consultaColecionadores,
  useCancelarSolicitacaoAmizade,
  useEnviarSolicitacaoAmizade,
} from './amizadeConsultas';

interface FormularioBusca {
  termo: string;
}

export function AmizadeInclusao() {
  const [parametros, definirParametros] = useSearchParams();
  const termoUrl = parametros.get('termo') ?? '';
  const { data: colecionadores } = useSuspenseQuery(consultaColecionadores(termoUrl));

  const filtroColecionadores = usePerfilStore((estado) => estado.filtroColecionadores);
  const definirFiltro = usePerfilStore((estado) => estado.definirFiltroColecionadores);
  const definirAbaAtiva = usePerfilStore((estado) => estado.definirAbaAtiva);
  const definirSubAba = usePerfilStore((estado) => estado.definirSubAbaAmizade);

  const enviar = useEnviarSolicitacaoAmizade();
  const cancelar = useCancelarSolicitacaoAmizade();

  const { register, handleSubmit, setValue } = useForm<FormularioBusca>({
    defaultValues: { termo: termoUrl || filtroColecionadores },
  });

  useEffect(() => {
    definirAbaAtiva('buscar-colecionadores');
  }, [definirAbaAtiva]);

  useEffect(() => {
    setValue('termo', termoUrl);
    definirFiltro(termoUrl);
  }, [termoUrl, setValue, definirFiltro]);

  function aoBuscar({ termo }: FormularioBusca) {
    const limpo = termo.trim();
    definirFiltro(limpo);
    if (limpo) {
      definirParametros({ termo: limpo });
    } else {
      definirParametros({});
    }
  }

  return (
    <div className="mx-auto max-w-6xl space-y-6 px-4 py-6">
      <BarraNavegacaoComunidade abaAtual="buscar-colecionadores" />

      <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-xs">
        <h1 className="m-0 text-xl font-bold text-slate-900">Buscar Colecionadores</h1>
        <p className="m-0 mt-1 text-xs text-slate-500">
          Encontre jogadores e colecionadores por apelido ou cidade para enviar solicitações de
          amizade
        </p>

        <form onSubmit={handleSubmit(aoBuscar)} className="mt-4 flex flex-col gap-2 sm:flex-row">
          <label htmlFor="busca-colecionador" className="sr-only">
            Buscar colecionador por apelido
          </label>
          <input
            id="busca-colecionador"
            type="search"
            {...register('termo')}
            placeholder="Buscar colecionador por apelido..."
            className="flex-1 rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 focus:border-indigo-500 focus:outline-none"
          />
          <button
            type="submit"
            className="rounded-lg bg-slate-900 px-5 py-2 text-sm font-semibold text-white transition hover:bg-slate-800"
          >
            Buscar
          </button>
        </form>

        <div className="mt-6">
          {colecionadores.length === 0 ? (
            <div className="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-500">
              Nenhum colecionador encontrado para o termo pesquisado.
            </div>
          ) : (
            <ul className="m-0 list-none space-y-3 p-0">
              {colecionadores.map((item) => (
                <li
                  key={item.perfilId}
                  className="flex flex-col justify-between gap-3 rounded-xl border border-slate-200 p-4 sm:flex-row sm:items-center"
                >
                  <div>
                    <p className="m-0 text-sm font-bold text-slate-900">{item.apelido}</p>
                    <p className="m-0 text-xs text-slate-500">
                      @{item.usuario}
                      {item.cidade ? ` • ${item.cidade}` : ''}
                    </p>
                  </div>

                  <div className="flex flex-wrap items-center gap-2">
                    <Link
                      to={`/comunidade/perfil-comunitario/${encodeURIComponent(item.perfilId)}/visualizacao`}
                      className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 no-underline hover:bg-slate-50"
                    >
                      Ver Perfil
                    </Link>

                    {item.statusRelacao === 'NENHUMA' && (
                      <button
                        type="button"
                        disabled={enviar.isPending}
                        onClick={() => enviar.mutate(item.usuario)}
                        className="rounded-lg bg-indigo-600 px-3 py-1.5 text-xs font-semibold text-white hover:bg-indigo-500 disabled:opacity-50"
                      >
                        Adicionar Amigo
                      </button>
                    )}

                    {item.statusRelacao === 'PENDENTE_ENVIADA' && (
                      <>
                        <span className="rounded-full bg-amber-100 px-2.5 py-1 text-xs font-semibold text-amber-800">
                          Solicitação Enviada
                        </span>
                        {item.amizadeId && (
                          <button
                            type="button"
                            disabled={cancelar.isPending}
                            onClick={() => cancelar.mutate(item.amizadeId!)}
                            className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 hover:bg-slate-50 disabled:opacity-50"
                          >
                            Cancelar
                          </button>
                        )}
                      </>
                    )}

                    {item.statusRelacao === 'PENDENTE_RECEBIDA' && (
                      <Link
                        to="/comunidade/amizade/pesquisa"
                        onClick={() => definirSubAba('recebidas')}
                        className="rounded-lg bg-emerald-600 px-3 py-1.5 text-xs font-semibold text-white no-underline hover:bg-emerald-500"
                      >
                        Responder Solicitação
                      </Link>
                    )}

                    {item.statusRelacao === 'AMIGO' && (
                      <span className="rounded-full bg-emerald-100 px-2.5 py-1 text-xs font-semibold text-emerald-800">
                        Amigos
                      </span>
                    )}
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </section>
    </div>
  );
}
