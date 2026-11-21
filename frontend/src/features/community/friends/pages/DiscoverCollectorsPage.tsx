import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useSearchParams } from 'react-router';
import { CommunitySubNav } from '../../../../components/layout/CommunitySubNav';
import { Avatar } from '../../../../components/ui/Avatar';
import { Icon } from '../../../../components/ui/Icon';
import { useAutenticacao } from '../../../auth/api/authApi';
import { useProfileUiStore } from '../../profile/stores/useProfileUiStore';
import {
  consultaColecionadores,
  useCancelarSolicitacaoAmizade,
  useEnviarSolicitacaoAmizade,
} from '../api/friendsQueries';

interface FormularioBusca {
  termo: string;
}

const PASSOS_CONEXAO = [
  {
    numero: '1',
    icone: 'handshake',
    titulo: 'Vínculo Mútuo',
    descricao: 'Ao aceitar a solicitação, o vínculo é estabelecido simultaneamente para ambos os perfis.',
  },
  {
    numero: '2',
    icone: 'swap_calls',
    titulo: 'Trade Radar',
    descricao: 'Permite cruzar cartas disponíveis na coleção com as preferências públicas do amigo.',
  },
  {
    numero: '3',
    icone: 'sync_alt',
    titulo: 'Sincronia Bilateral',
    descricao: 'Qualquer remoção posterior desfaz imediatamente a relação nos dois lados (RN006).',
  },
];

export function DiscoverCollectorsPage() {
  const [parametros, definirParametros] = useSearchParams();
  const termoUrl = parametros.get('termo') ?? '';
  const { usuario: usuarioAutenticado } = useAutenticacao();
  const { data: colecionadores } = useSuspenseQuery(consultaColecionadores(termoUrl));

  const filtroColecionadores = useProfileUiStore((estado) => estado.filtroColecionadores);
  const definirFiltro = useProfileUiStore((estado) => estado.definirFiltroColecionadores);
  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);
  const definirSubAba = useProfileUiStore((estado) => estado.definirSubAbaAmizade);

  const enviar = useEnviarSolicitacaoAmizade();
  const cancelar = useCancelarSolicitacaoAmizade();

  const { register, handleSubmit, setValue, watch } = useForm<FormularioBusca>({
    defaultValues: { termo: termoUrl || filtroColecionadores },
  });

  const termoDigitado = (watch('termo') ?? '').trim().replace(/^@/, '');
  const ehAutoBusca =
    Boolean(usuarioAutenticado) &&
    termoDigitado.length > 0 &&
    termoDigitado.toLowerCase() === usuarioAutenticado!.toLowerCase();

  useEffect(() => {
    definirAbaAtiva('buscar-colecionadores');
  }, [definirAbaAtiva]);

  useEffect(() => {
    setValue('termo', termoUrl);
    definirFiltro(termoUrl);
  }, [termoUrl, setValue, definirFiltro]);

  function aoBuscar({ termo }: FormularioBusca) {
    const limpo = termo.trim().replace(/^@/, '');
    definirFiltro(limpo);
    if (limpo) {
      definirParametros({ termo: limpo });
    } else {
      definirParametros({});
    }
  }

  return (
    <div className="mx-auto max-w-7xl space-y-space-xl px-margin py-space-lg md:px-margin-lg">
      <CommunitySubNav abaAtiva="buscar-colecionadores" />

      <div className="grid grid-cols-1 items-start gap-space-xl lg:grid-cols-12">
        <div className="space-y-space-lg lg:col-span-7">
          <div className="space-y-space-md rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
            <div className="flex items-start justify-between gap-space-md">
              <div>
                <div className="flex items-center gap-space-xs text-ocean-blue">
                  <Icon nome="manage_search" className="text-[20px]" />
                  <span className="font-label-sm text-label-sm font-bold tracking-wider uppercase">
                    Busca de Duelista
                  </span>
                </div>
                <h1 className="mt-1 font-headline-md text-headline-md text-deep-navy">
                  Buscar Colecionadores
                </h1>
                <p className="mt-1 font-body-sm text-body-sm text-text-muted">
                  Conecte-se diretamente com outros jogadores por apelido ou cidade para enviar
                  solicitações de amizade.
                </p>
              </div>
              <span className="rounded bg-sky-tint px-2.5 py-1 font-label-sm text-[11px] font-bold whitespace-nowrap text-ocean-blue">
                Regra Oficial TCGHub
              </span>
            </div>

            <form onSubmit={handleSubmit(aoBuscar)} className="pt-space-xs">
              <div className="relative flex items-center">
                <Icon
                  nome="person_search"
                  className="pointer-events-none absolute left-3.5 text-[22px] text-secondary"
                />
                <label htmlFor="busca-colecionador" className="sr-only">
                  Buscar colecionador por apelido
                </label>
                <input
                  id="busca-colecionador"
                  type="search"
                  {...register('termo')}
                  placeholder="Buscar colecionador por apelido..."
                  className="w-full rounded-lg bg-surface-container-low py-3 pr-28 pl-11 font-body-md text-body-md text-deep-navy shadow-inner ring-1 ring-steel-border transition-all outline-none placeholder:text-outline focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
                />
                <button
                  type="submit"
                  className="absolute right-1.5 flex items-center gap-1.5 rounded-md bg-ocean-blue px-4 py-2 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
                >
                  <Icon nome="search" className="text-[18px]" />
                  <span>Buscar</span>
                </button>
              </div>
            </form>

            {ehAutoBusca ? (
              <div
                role="alert"
                className="flex items-start gap-space-sm rounded-lg bg-error-container p-3 text-on-error-container"
              >
                <Icon nome="block" className="text-[20px] text-error" />
                <div className="font-body-sm text-body-sm">
                  <strong className="block font-headline-sm text-[14px]">
                    Auto-adição Bloqueada (RN004)
                  </strong>
                  Você não pode adicionar a si mesmo como amigo. Seu usuário autenticado é{' '}
                  <span className="font-bold underline">@{usuarioAutenticado}</span>.
                </div>
              </div>
            ) : (
              <div className="flex items-start gap-space-sm rounded-lg bg-surface-container-low p-3 text-deep-navy">
                <Icon nome="info" className="text-[20px] text-ocean-blue" />
                <div className="font-body-sm text-body-sm">
                  <strong className="block font-semibold text-deep-navy">
                    Validador de Rede Ativo
                  </strong>
                  Informe o apelido ou cidade para localizar colecionadores elegíveis para conexão.
                </div>
              </div>
            )}
          </div>

          <div className="space-y-space-md rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
            <div className="flex items-center gap-2 text-ocean-blue">
              <Icon nome="sync_alt" className="text-[20px]" />
              <h2 className="font-headline-sm text-headline-sm text-deep-navy">
                Como funciona a Conexão Bilateral
              </h2>
            </div>

            <div className="grid grid-cols-1 gap-space-md pt-space-xs sm:grid-cols-3">
              {PASSOS_CONEXAO.map((passo) => (
                <div
                  key={passo.numero}
                  className="flex flex-col justify-between space-y-2 rounded-lg bg-surface-container-low p-3.5"
                >
                  <div className="flex items-center justify-between">
                    <span className="flex h-6 w-6 items-center justify-center rounded-full bg-ocean-blue text-[12px] font-bold text-pure-white">
                      {passo.numero}
                    </span>
                    <Icon nome={passo.icone} className="text-[18px] text-secondary" />
                  </div>
                  <div>
                    <span className="block font-headline-sm text-[13px] font-bold text-deep-navy">
                      {passo.titulo}
                    </span>
                    <p className="mt-0.5 font-body-sm text-[12px] text-text-muted">
                      {passo.descricao}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="space-y-space-md lg:col-span-5">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-space-xs">
              <Icon nome="explore" className="text-[20px] text-ocean-blue" />
              <h2 className="font-headline-sm text-headline-sm text-deep-navy">
                Colecionadores Encontrados
              </h2>
            </div>
            <span className="font-body-sm text-body-sm text-text-muted">
              {colecionadores.length} {colecionadores.length === 1 ? 'perfil' : 'perfis'}
            </span>
          </div>

          {colecionadores.length === 0 ? (
            <div className="rounded-xl border border-dashed border-steel-border bg-pure-white p-space-lg text-center font-body-md text-body-md text-text-muted">
              Nenhum colecionador encontrado para o termo pesquisado.
            </div>
          ) : (
            <ul className="m-0 list-none space-y-space-sm p-0">
              {colecionadores.map((item) => (
                <li
                  key={item.perfilId}
                  className="space-y-3 rounded-xl border border-ice-border/60 bg-pure-white p-space-md shadow-sm transition-shadow hover:shadow-md"
                >
                  <div className="flex items-start justify-between gap-space-sm">
                    <div className="flex items-center gap-3">
                      <Avatar apelido={item.apelido} foto={item.foto} className="h-12 w-12" />
                      <div>
                        <div className="flex items-center gap-1.5">
                          <h3 className="font-headline-sm text-[15px] font-bold text-deep-navy">
                            {item.apelido}
                          </h3>
                          <Icon
                            nome="verified"
                            preenchido
                            className="text-[16px] text-cobalt-accent"
                          />
                        </div>
                        <p className="font-body-sm text-[12px] text-text-muted">
                          @{item.usuario}
                          {item.cidade ? ` • ${item.cidade}` : ''}
                        </p>
                      </div>
                    </div>
                  </div>

                  <div className="flex flex-wrap items-center justify-between gap-2 border-t border-ice-border pt-2">
                    <Link
                      to={`/comunidade/perfil/${encodeURIComponent(item.perfilId)}`}
                      className="rounded-lg bg-surface-container-low px-3 py-1.5 font-label-sm text-label-sm font-semibold text-ocean-blue no-underline transition-colors hover:bg-sky-tint"
                    >
                      Ver Perfil
                    </Link>

                    <div className="flex flex-wrap items-center gap-2">
                      {item.statusRelacao === 'NENHUMA' && (
                        <button
                          type="button"
                          disabled={enviar.isPending}
                          onClick={() => enviar.mutate(item.usuario)}
                          className="inline-flex items-center gap-1 rounded-lg bg-ocean-blue px-3.5 py-1.5 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-all hover:bg-deep-navy disabled:opacity-50"
                        >
                          <Icon nome="person_add" className="text-[16px]" />
                          <span>Adicionar Amigo</span>
                        </button>
                      )}

                      {item.statusRelacao === 'PENDENTE_ENVIADA' && (
                        <>
                          <span className="rounded-full bg-status-warning-bg px-2.5 py-1 font-label-sm text-[11px] font-semibold text-status-warning-text">
                            Solicitação Enviada
                          </span>
                          {item.amizadeId && (
                            <button
                              type="button"
                              disabled={cancelar.isPending}
                              onClick={() => cancelar.mutate(item.amizadeId!)}
                              className="rounded-lg border border-steel-border bg-pure-white px-3 py-1.5 font-label-sm text-label-sm font-semibold text-secondary transition-colors hover:bg-surface-container disabled:opacity-50"
                            >
                              Cancelar
                            </button>
                          )}
                        </>
                      )}

                      {item.statusRelacao === 'PENDENTE_RECEBIDA' && (
                        <Link
                          to="/comunidade/amigos"
                          onClick={() => definirSubAba('recebidas')}
                          className="rounded-lg bg-ocean-blue px-3 py-1.5 font-label-sm text-label-sm font-semibold text-pure-white no-underline transition-colors hover:bg-deep-navy"
                        >
                          Responder Solicitação
                        </Link>
                      )}

                      {item.statusRelacao === 'AMIGO' && (
                        <span className="rounded-full bg-status-positive-bg px-2.5 py-1 font-label-sm text-[11px] font-semibold text-status-positive-text">
                          Amigos
                        </span>
                      )}
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}
