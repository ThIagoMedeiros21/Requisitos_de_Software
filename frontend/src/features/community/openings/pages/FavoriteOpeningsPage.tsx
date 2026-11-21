import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { CommunitySubNav } from '../../../../components/layout/CommunitySubNav';
import { Icon } from '../../../../components/ui/Icon';
import {
  type FiltroComunidadeAbertura,
  useOpeningsUiStore,
} from '../../../../stores/openingsUiStore';
import { useProfileUiStore } from '../../profile/stores/useProfileUiStore';
import {
  consultaAberturasComunidade,
  useAlternarVisibilidadeAbertura,
  useEscolherAberturaPreferida,
} from '../api/openingsQueries';
import { OpeningCover } from '../components/OpeningCover';
import { OpeningDetailsModal } from '../components/OpeningDetailsModal';

export function FavoriteOpeningsPage() {
  const { data: dados } = useSuspenseQuery(consultaAberturasComunidade());
  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);
  const termo = useOpeningsUiStore((estado) => estado.termoBusca);
  const filtro = useOpeningsUiStore((estado) => estado.filtroComunidade);
  const definirTermo = useOpeningsUiStore((estado) => estado.definirTermoBusca);
  const definirFiltro = useOpeningsUiStore((estado) => estado.definirFiltroComunidade);
  const abrirDetalhes = useOpeningsUiStore((estado) => estado.abrirDetalhesAbertura);

  const escolher = useEscolherAberturaPreferida();
  const alternarVisibilidade = useAlternarVisibilidadeAbertura();

  useEffect(() => {
    definirAbaAtiva('abertura-preferida');
  }, [definirAbaAtiva]);

  const termoNormalizado = termo.trim().toLowerCase();
  const aberturasFiltradas = dados.aberturasAtivas.filter((abertura) => {
    if (filtro === 'amigos' && abertura.quantidadeAmigos <= 0) {
      return false;
    }
    if (filtro === 'ranking' && abertura.posicaoRanking === null) {
      return false;
    }
    if (!termoNormalizado) {
      return true;
    }
    return (
      abertura.nome.toLowerCase().includes(termoNormalizado) ||
      abertura.artista.toLowerCase().includes(termoNormalizado) ||
      String(abertura.numero).includes(termoNormalizado)
    );
  });

  const filtros: { chave: FiltroComunidadeAbertura; rotulo: string }[] = [
    { chave: 'todas', rotulo: 'Todas as Aberturas' },
    { chave: 'amigos', rotulo: 'Escolhidas por Amigos' },
    { chave: 'ranking', rotulo: 'Top Ranking' },
  ];

  const minha = dados.minhaPreferida;

  return (
    <div className="mx-auto max-w-7xl space-y-space-xl px-margin py-space-lg md:px-margin-lg">
      <CommunitySubNav abaAtiva="abertura-preferida" />

      {/* Banner da Abertura Escolhida */}
      <section className="rounded-2xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
        {minha ? (
          <div className="flex flex-col justify-between gap-space-md lg:flex-row lg:items-center">
            <div className="flex flex-col gap-space-md sm:flex-row sm:items-center">
              <OpeningCover
                numero={minha.numero}
                nome={minha.nome}
                imagem={minha.imagem}
                className="aspect-video w-full flex-shrink-0 rounded-xl shadow-sm sm:w-44"
              />
              <div className="space-y-1">
                <div className="flex flex-wrap items-center gap-2">
                  <span className="rounded-full bg-sky-tint px-2.5 py-0.5 font-label-sm text-label-sm font-bold text-ocean-blue uppercase">
                    Abertura Escolhida
                  </span>
                  {!minha.ativa && (
                    <span className="rounded-full border border-steel-border bg-status-warning-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-warning-text">
                      Inativa no Catálogo — Escolha Preservada
                    </span>
                  )}
                </div>
                <h1 className="font-headline-lg text-headline-lg text-deep-navy">
                  Op. {String(minha.numero).padStart(2, '0')} — {minha.nome}
                </h1>
                <p className="font-body-md text-body-md text-text-muted">
                  {minha.artista} • Episódios {minha.episodios}
                </p>
              </div>
            </div>

            <div className="flex flex-wrap items-center gap-space-md">
              <div className="flex items-center gap-3 rounded-xl border border-ice-border bg-surface-container-low/60 px-3.5 py-2">
                <div>
                  <span className="block font-label-sm text-label-sm font-semibold text-deep-navy">
                    Exibir no Perfil Público
                  </span>
                  <span className="block font-body-sm text-[11px] text-text-muted">
                    {minha.visivelNoPerfil
                      ? 'Visível para outros nakamas'
                      : 'Oculta no perfil público'}
                  </span>
                </div>
                <button
                  type="button"
                  role="switch"
                  aria-checked={minha.visivelNoPerfil}
                  aria-label="Exibir no Perfil Público"
                  disabled={alternarVisibilidade.isPending}
                  onClick={() => alternarVisibilidade.mutate(!minha.visivelNoPerfil)}
                  className={`relative inline-flex h-6 w-11 flex-shrink-0 items-center rounded-full transition-colors ${
                    minha.visivelNoPerfil ? 'bg-ocean-blue' : 'bg-steel-border'
                  }`}
                >
                  <span
                    className={`inline-block h-4 w-4 transform rounded-full bg-pure-white shadow-sm transition-transform ${
                      minha.visivelNoPerfil ? 'translate-x-6' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>

              <button
                type="button"
                onClick={() => abrirDetalhes(minha.aberturaId)}
                className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-4 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
              >
                <Icon nome="play_circle" className="text-[18px]" />
                <span>Ver Detalhes</span>
              </button>
            </div>
          </div>
        ) : (
          <div className="flex items-center gap-space-md">
            <span className="flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-2xl bg-sky-tint text-ocean-blue">
              <Icon nome="library_music" className="text-[28px]" />
            </span>
            <div>
              <h1 className="font-headline-lg text-headline-lg text-deep-navy">
                Abertura Preferida de One Piece
              </h1>
              <p className="font-body-md text-body-md text-text-muted">
                Escolha sua abertura preferida abaixo para destacá-la no seu perfil e participar do
                Ranking Top 5 da comunidade!
              </p>
            </div>
          </div>
        )}
      </section>

      <div className="grid grid-cols-1 items-start gap-space-xl lg:grid-cols-12">
        {/* Catálogo de Aberturas Ativas */}
        <div className="space-y-space-md lg:col-span-8">
          <div className="flex flex-col gap-3 rounded-xl border border-ice-border/60 bg-pure-white p-4 shadow-sm sm:flex-row sm:items-center sm:justify-between">
            <div className="relative flex-1">
              <Icon
                nome="search"
                className="pointer-events-none absolute top-2.5 left-3 text-[20px] text-secondary"
              />
              <label htmlFor="busca-abertura-comunidade" className="sr-only">
                Filtrar por nome, número ou artista
              </label>
              <input
                id="busca-abertura-comunidade"
                type="search"
                value={termo}
                onChange={(evento) => definirTermo(evento.target.value)}
                placeholder="Filtrar por nome, número de abertura ou artista..."
                className="w-full rounded-lg bg-surface-container-low py-2 pr-3 pl-10 font-body-sm text-body-sm text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
              />
            </div>

            <div className="flex flex-wrap gap-1.5">
              {filtros.map((item) => {
                const ativo = filtro === item.chave;
                return (
                  <button
                    key={item.chave}
                    type="button"
                    onClick={() => definirFiltro(item.chave)}
                    className={`rounded-lg px-3 py-1.5 font-label-sm text-label-sm transition-colors ${
                      ativo
                        ? 'bg-deep-navy font-semibold text-pure-white shadow-sm'
                        : 'bg-surface-container-low text-secondary hover:bg-sky-tint hover:text-deep-navy'
                    }`}
                  >
                    {item.rotulo}
                  </button>
                );
              })}
            </div>
          </div>

          {aberturasFiltradas.length === 0 ? (
            <div className="rounded-xl border border-dashed border-steel-border bg-pure-white p-space-xl text-center font-body-md text-body-md text-text-muted">
              Nenhuma abertura ativa encontrada para os filtros selecionados.
            </div>
          ) : (
            <div className="grid grid-cols-1 gap-space-md sm:grid-cols-2">
              {aberturasFiltradas.map((abertura) => (
                <article
                  key={abertura.id}
                  className="flex flex-col justify-between overflow-hidden rounded-2xl border border-ice-border/60 bg-pure-white shadow-sm transition-shadow hover:shadow-md"
                >
                  <div>
                    <div className="relative">
                      <OpeningCover
                        numero={abertura.numero}
                        nome={abertura.nome}
                        imagem={abertura.imagem}
                        className="aspect-video w-full"
                      />
                      <div className="absolute top-2.5 left-2.5 flex flex-wrap gap-1.5">
                        <span className="rounded bg-deep-navy/90 px-2 py-0.5 font-label-sm text-[11px] font-bold text-pure-white uppercase">
                          Op. {String(abertura.numero).padStart(2, '0')}
                        </span>
                        {abertura.ehMinhaPreferida && (
                          <span className="rounded bg-cobalt-accent px-2 py-0.5 font-label-sm text-[11px] font-bold text-pure-white uppercase">
                            ★ Sua Preferida
                          </span>
                        )}
                        {abertura.posicaoRanking && (
                          <span className="rounded bg-pure-white/95 px-2 py-0.5 font-label-sm text-[11px] font-bold text-ocean-blue shadow-sm">
                            #{abertura.posicaoRanking} no Ranking
                          </span>
                        )}
                      </div>
                    </div>

                    <div className="space-y-1.5 p-4">
                      <h2 className="font-headline-sm text-headline-sm text-deep-navy">
                        {abertura.nome}
                      </h2>
                      <p className="font-body-sm text-body-sm text-text-muted">
                        {abertura.artista} • Ep. {abertura.episodios}
                      </p>
                      <div className="flex items-center gap-2 pt-1 font-label-sm text-[12px] text-secondary">
                        <Icon nome="group" className="text-[16px] text-ocean-blue" />
                        <span>
                          {abertura.quantidadeAmigos}{' '}
                          {abertura.quantidadeAmigos === 1 ? 'amigo escolheu' : 'amigos escolheram'}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="flex items-center gap-2 border-t border-ice-border bg-surface-container-low/30 p-3">
                    {abertura.ehMinhaPreferida ? (
                      <span className="flex flex-1 items-center justify-center gap-1 rounded-lg bg-sky-tint px-3 py-2 font-label-sm text-label-sm font-bold text-ocean-blue">
                        <Icon nome="check_circle" preenchido className="text-[16px]" />
                        <span>Ativa no Perfil</span>
                      </span>
                    ) : (
                      <button
                        type="button"
                        disabled={escolher.isPending}
                        onClick={() => escolher.mutate(abertura.id)}
                        className="flex flex-1 items-center justify-center gap-1 rounded-lg bg-ocean-blue px-3 py-2 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
                      >
                        <Icon nome="star" className="text-[16px]" />
                        <span>Escolher como Preferida</span>
                      </button>
                    )}

                    <button
                      type="button"
                      onClick={() => abrirDetalhes(abertura.id)}
                      aria-label={`Ver detalhes de ${abertura.nome}`}
                      className="rounded-lg border border-ice-border bg-pure-white p-2 text-ocean-blue transition-colors hover:bg-sky-tint"
                    >
                      <Icon nome="info" className="text-[18px]" />
                    </button>
                  </div>
                </article>
              ))}
            </div>
          )}
        </div>

        {/* Sidebar Ranking Top 5 da Comunidade */}
        <aside className="space-y-space-md rounded-2xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm lg:col-span-4">
          <div className="flex items-start justify-between gap-2">
            <div>
              <span className="font-label-sm text-[11px] font-bold tracking-wider text-ocean-blue uppercase">
                Ranking da Comunidade
              </span>
              <h2 className="font-headline-md text-headline-md text-deep-navy">
                Top 5 Escolhas Oficiais
              </h2>
            </div>
            <Icon nome="leaderboard" className="text-[24px] text-ocean-blue" />
          </div>

          <div className="rounded-xl bg-surface-container-low p-3">
            <span className="block font-label-sm text-[11px] text-secondary uppercase">
              Total de Votos Ativos
            </span>
            <span className="font-headline-lg text-headline-lg text-deep-navy">
              {dados.ranking.totalEscolhas}
            </span>
          </div>

          {dados.ranking.itens.length === 0 ? (
            <div className="rounded-xl border border-dashed border-steel-border bg-surface-container-low/50 p-4 text-center font-body-sm text-body-sm text-text-muted">
              Nenhuma escolha registrada na comunidade ainda.
            </div>
          ) : (
            <ol className="m-0 list-none space-y-3 p-0">
              {dados.ranking.itens.map((item) => (
                <li key={item.aberturaId} className="space-y-1.5">
                  <div className="flex items-center justify-between gap-2">
                    <button
                      type="button"
                      onClick={() => abrirDetalhes(item.aberturaId)}
                      className="flex min-w-0 items-center gap-2 text-left transition-colors hover:text-ocean-blue"
                    >
                      <span className="flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-deep-navy font-label-sm text-[11px] font-bold text-pure-white">
                        #{item.posicao}
                      </span>
                      <div className="min-w-0">
                        <span className="block truncate font-headline-sm text-sm text-deep-navy">
                          Op. {String(item.numero).padStart(2, '0')} — {item.nome}
                        </span>
                        <span className="block truncate font-body-sm text-[11px] text-text-muted">
                          {item.artista}
                        </span>
                      </div>
                    </button>

                    <div className="flex-shrink-0 text-right">
                      <span className="block font-tabular-stat text-tabular-stat text-ocean-blue">
                        {item.percentual.toFixed(1)}%
                      </span>
                      <span className="block font-body-sm text-[11px] text-text-muted">
                        {item.quantidadeEscolhas}{' '}
                        {item.quantidadeEscolhas === 1 ? 'voto' : 'votos'}
                      </span>
                    </div>
                  </div>

                  <div className="h-2 w-full overflow-hidden rounded-full bg-surface-container">
                    <div
                      className="h-full rounded-full bg-ocean-blue transition-all"
                      style={{ width: `${Math.min(100, Math.max(4, item.percentual))}%` }}
                    />
                  </div>
                </li>
              ))}
            </ol>
          )}

          <p className="rounded-xl bg-sky-tint/60 p-3 font-body-sm text-[11px] text-deep-navy">
            Preferências com privacidade restrita somam no cálculo proporcional sem expor o usuário.
          </p>
        </aside>
      </div>

      <OpeningDetailsModal />
    </div>
  );
}
