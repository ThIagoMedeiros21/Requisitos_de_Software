import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { Icon } from '../../../../components/ui/Icon';
import { ErroHttp, mensagemErro } from '../../../../lib/api';
import {
  type FiltroTipoComposicao,
  useDecksUiStore,
} from '../../../../stores/decksUiStore';
import { useToastStore } from '../../../../stores/useToastStore';
import { consultaComposicaoDeck } from '../api/decksQueries';
import type { ItemComposicaoDeckDto } from '../types/decks.types';
import { formatarDecklistBandai } from '../utils/bandaiExport';

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function rotuloTipo(tipo: string): string {
  switch (tipo) {
    case 'Leader':
      return 'Líder';
    case 'Character':
      return 'Personagens';
    case 'Event':
      return 'Eventos';
    case 'Stage':
      return 'Estágios';
    default:
      return tipo;
  }
}

function custoNumerico(item: ItemComposicaoDeckDto): number {
  if (!item.card_cost) {
    return -1;
  }
  const n = Number.parseInt(item.card_cost, 10);
  return Number.isNaN(n) ? -1 : n;
}

export function DeckCompositionModal() {
  const deckId = useDecksUiStore((estado) => estado.deckIdComposicaoAberta);
  const filtroTipo = useDecksUiStore((estado) => estado.filtroTipoComposicao);
  const ordenacao = useDecksUiStore((estado) => estado.ordenacaoComposicao);
  const definirFiltroTipo = useDecksUiStore((estado) => estado.definirFiltroTipoComposicao);
  const definirOrdenacao = useDecksUiStore((estado) => estado.definirOrdenacaoComposicao);
  const fechar = useDecksUiStore((estado) => estado.fecharComposicao);
  const publicar = useToastStore((estado) => estado.publicar);
  const [imagensComFalha, definirImagensComFalha] = useState<Record<string, boolean>>({});

  const consulta = useQuery({
    ...consultaComposicaoDeck(deckId ?? ''),
    enabled: Boolean(deckId),
  });

  if (!deckId) {
    return null;
  }

  const composicao = consulta.data;
  const erro503 = consulta.error instanceof ErroHttp && consulta.error.status === 503;

  function copiarBandai() {
    if (!composicao) {
      return;
    }
    const texto = formatarDecklistBandai(composicao);
    if (navigator.clipboard?.writeText) {
      void navigator.clipboard.writeText(texto).catch(() => undefined);
    }
    publicar('sucesso', 'Decklist copiada no formato Bandai!');
  }

  const itensFiltrados = (composicao?.itens ?? []).filter(
    (item) => filtroTipo === 'TODOS' || item.card_type === filtroTipo,
  );

  const grupos = new Map<string, ItemComposicaoDeckDto[]>();
  const ordenados = [...itensFiltrados].sort((a, b) => {
    if (ordenacao === 'CUSTO') {
      return custoNumerico(a) - custoNumerico(b) || a.card_set_id.localeCompare(b.card_set_id);
    }
    return a.card_set_id.localeCompare(b.card_set_id);
  });

  for (const item of ordenados) {
    const chave =
      ordenacao === 'CUSTO'
        ? item.card_type === 'Leader'
          ? 'Líder'
          : `Custo ${item.card_cost ?? '0'}`
        : rotuloTipo(item.card_type);
    const lista = grupos.get(chave) ?? [];
    lista.push(item);
    grupos.set(chave, lista);
  }

  const filtrosTipo: { chave: FiltroTipoComposicao; rotulo: string }[] = [
    { chave: 'TODOS', rotulo: 'Todos' },
    {
      chave: 'Leader',
      rotulo: `${composicao?.totaisPorTipo?.['Leader'] ?? 1} Líder`,
    },
    {
      chave: 'Character',
      rotulo: `${composicao?.totaisPorTipo?.['Character'] ?? 0} Personagens`,
    },
    {
      chave: 'Event',
      rotulo: `${composicao?.totaisPorTipo?.['Event'] ?? 0} Eventos`,
    },
    {
      chave: 'Stage',
      rotulo: `${composicao?.totaisPorTipo?.['Stage'] ?? 0} Estágios`,
    },
  ];

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-composicao-deck"
      className="fixed inset-0 z-50 flex items-center justify-center bg-deep-navy/60 p-4 backdrop-blur-sm"
    >
      <div className="flex max-h-[90vh] w-full max-w-4xl flex-col overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-level-4">
        <div className="flex items-start justify-between gap-space-md border-b border-ice-border px-space-lg py-space-md">
          <div>
            <div className="flex flex-wrap items-center gap-2">
              <h2
                id="titulo-composicao-deck"
                className="font-headline-lg text-headline-lg text-deep-navy"
              >
                {composicao ? composicao.nomeDeck : `Composição do Deck ${deckId}`}
              </h2>
              {composicao && (
                <span className="rounded-full bg-sky-tint px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-ocean-blue">
                  {composicao.totalCartasComLider ?? composicao.totalCartasMainDeck + 1} cartas (1
                  Líder + {composicao.totalCartasMainDeck} Cartas)
                </span>
              )}
              {composicao && !composicao.ativo && (
                <span className="inline-flex items-center gap-1 rounded-full border border-steel-border bg-status-warning-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-warning-text">
                  <Icon nome="inventory_2" className="text-[14px]" />
                  <span>Descontinuado / Inativo no Catálogo</span>
                </span>
              )}
            </div>
            {composicao && (
              <p className="mt-1 font-body-sm text-body-sm text-text-muted">
                Líder: <strong className="text-deep-navy">{composicao.lider}</strong> • Cor:{' '}
                {composicao.cor}
              </p>
            )}
          </div>

          <button
            type="button"
            aria-label="Fechar composição"
            onClick={fechar}
            className="rounded-lg p-1.5 text-secondary transition-colors hover:bg-surface-container-low hover:text-deep-navy"
          >
            <Icon nome="close" className="text-[22px]" />
          </button>
        </div>

        <div className="flex-1 space-y-space-md overflow-y-auto p-space-lg">
          {consulta.isLoading ? (
            <div className="space-y-3" role="status" aria-label="Carregando composição do deck">
              <div className="h-14 animate-pulse rounded-xl bg-surface-container-low" />
              {[0, 1, 2, 3, 4, 5].map((i) => (
                <div key={i} className="h-16 animate-pulse rounded-xl bg-surface-container-low" />
              ))}
            </div>
          ) : consulta.isError || !composicao ? (
            <div
              role="alert"
              className="flex flex-col items-center space-y-4 rounded-xl border border-ice-border bg-error-container/40 p-space-xl text-center"
            >
              <span className="flex h-14 w-14 items-center justify-center rounded-2xl bg-error-container text-error">
                <Icon nome="cloud_off" className="text-[30px]" />
              </span>
              <div className="max-w-md space-y-1">
                <h3 className="font-headline-md text-headline-md text-deep-navy">
                  Catálogo Temporariamente Indisponível
                </h3>
                <p className="font-body-md text-body-md text-text-muted">
                  {erro503
                    ? 'Não foi possível carregar a composição porque o serviço de Catálogo está indisponível.'
                    : mensagemErro(consulta.error)}
                </p>
              </div>
              <div className="flex items-center gap-2">
                <button
                  type="button"
                  onClick={() => void consulta.refetch()}
                  className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-4 py-2 font-label-md text-label-md font-semibold text-pure-white transition-colors hover:bg-deep-navy"
                >
                  <Icon nome="refresh" className="text-[18px]" />
                  <span>Tentar novamente</span>
                </button>
                <button
                  type="button"
                  onClick={fechar}
                  className="rounded-lg bg-surface-container px-4 py-2 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container-high"
                >
                  Fechar
                </button>
              </div>
            </div>
          ) : (
            <>
              <div className="flex flex-col justify-between gap-space-md rounded-xl bg-surface-container-low p-4 lg:flex-row lg:items-center">
                <div className="flex flex-wrap items-center gap-1.5">
                  {filtrosTipo.map((f) => {
                    const ativo = filtroTipo === f.chave;
                    return (
                      <button
                        key={f.chave}
                        type="button"
                        onClick={() => definirFiltroTipo(f.chave)}
                        className={`rounded-lg px-3 py-1.5 font-label-sm text-label-sm transition-colors ${
                          ativo
                            ? 'bg-deep-navy font-semibold text-pure-white shadow-sm'
                            : 'bg-pure-white text-secondary hover:bg-sky-tint hover:text-deep-navy'
                        }`}
                      >
                        {f.rotulo}
                      </button>
                    );
                  })}
                </div>

                <div className="flex flex-wrap items-center justify-between gap-4">
                  <div className="flex items-center gap-1 rounded-lg bg-pure-white p-1">
                    <button
                      type="button"
                      onClick={() => definirOrdenacao('TIPO')}
                      className={`rounded-md px-2.5 py-1 font-label-sm text-[11px] font-semibold transition-colors ${
                        ordenacao === 'TIPO'
                          ? 'bg-ocean-blue text-pure-white'
                          : 'text-secondary hover:text-deep-navy'
                      }`}
                    >
                      Agrupar por Tipo
                    </button>
                    <button
                      type="button"
                      onClick={() => definirOrdenacao('CUSTO')}
                      className={`rounded-md px-2.5 py-1 font-label-sm text-[11px] font-semibold transition-colors ${
                        ordenacao === 'CUSTO'
                          ? 'bg-ocean-blue text-pure-white'
                          : 'text-secondary hover:text-deep-navy'
                      }`}
                    >
                      Agrupar por Custo
                    </button>
                  </div>

                  <div className="text-right">
                    <span className="block font-label-sm text-[11px] text-secondary">
                      Custo Estimado no Mercado
                    </span>
                    <span className="font-tabular-price text-tabular-price text-deep-navy">
                      {formatarPreco(composicao.custoEstimadoMercado)}
                    </span>
                  </div>
                </div>
              </div>

              <div className="flex flex-wrap items-center justify-between gap-2">
                <span className="font-label-sm text-label-sm font-bold tracking-wider text-secondary uppercase">
                  Composição Detalhada e Agrupada ({itensFiltrados.length} entradas de catálogo)
                </span>
                {composicao.conformidadeBandai && (
                  <span className="inline-flex items-center gap-1 rounded-full bg-status-positive-bg px-3 py-1 font-label-sm text-[11px] font-semibold text-status-positive-text">
                    <Icon nome="verified" preenchido className="text-[15px]" />
                    <span>Conformidade Estrita (1 Líder + 50 Cartas • Máx 4 cópias)</span>
                  </span>
                )}
              </div>

              <div className="space-y-space-md">
                {Array.from(grupos.entries()).map(([nomeGrupo, itensGrupo]) => (
                  <div key={nomeGrupo} className="space-y-2">
                    <h3 className="font-label-sm text-label-sm font-bold tracking-wider text-ocean-blue uppercase">
                      {nomeGrupo}
                    </h3>
                    <ul className="m-0 list-none space-y-2 p-0">
                      {itensGrupo.map((item) => {
                        const exibirImagem =
                          Boolean(item.card_image) && !imagensComFalha[item.card_set_id];
                        return (
                          <li
                            key={item.card_set_id}
                            className="flex flex-col justify-between gap-3 rounded-xl border border-ice-border bg-surface-container-lowest p-3 sm:flex-row sm:items-center"
                          >
                            <div className="flex items-center gap-3">
                              <div className="flex aspect-[2.5/3.5] w-11 flex-shrink-0 items-center justify-center overflow-hidden rounded-md bg-deep-navy text-center font-label-sm text-[9px] font-bold text-pure-white">
                                {exibirImagem ? (
                                  <img
                                    src={item.card_image!}
                                    alt={item.card_name}
                                    onError={() =>
                                      definirImagensComFalha((atual) => ({
                                        ...atual,
                                        [item.card_set_id]: true,
                                      }))
                                    }
                                    className="h-full w-full object-cover"
                                  />
                                ) : (
                                  <span>{item.card_set_id}</span>
                                )}
                              </div>

                              <div className="space-y-0.5">
                                <div className="flex flex-wrap items-center gap-1.5">
                                  <span className="rounded bg-deep-navy px-1.5 py-0.5 font-label-sm text-[10px] font-bold text-pure-white uppercase">
                                    {item.rarity}
                                  </span>
                                  <span className="font-mono text-xs font-semibold text-ocean-blue">
                                    {item.card_set_id}
                                  </span>
                                  <span className="rounded bg-surface-container px-1.5 py-0.5 font-label-sm text-[10px] text-secondary">
                                    {item.card_type} • {item.card_color}
                                  </span>
                                </div>
                                <p className="font-headline-sm text-headline-sm text-deep-navy">
                                  {item.card_name}
                                </p>
                                <p className="font-body-sm text-[11px] text-text-muted">
                                  {item.card_cost ? `Custo: ${item.card_cost}` : 'Líder'}
                                  {item.card_power ? ` • Poder: ${item.card_power}` : ''}
                                  {item.set_name ? ` • ${item.set_name}` : ''}
                                </p>
                              </div>
                            </div>

                            <div className="flex items-center justify-between gap-4 sm:justify-end">
                              <span className="rounded-lg bg-sky-tint px-2.5 py-1 font-label-sm text-label-sm font-bold text-ocean-blue">
                                {item.card_type === 'Leader'
                                  ? 'x1 Líder'
                                  : `x${item.quantidade} no Deck`}
                              </span>
                              <div className="text-right">
                                <span className="block font-tabular-price text-sm text-deep-navy">
                                  {formatarPreco(item.subtotal)}
                                </span>
                                <span className="block font-body-sm text-[11px] text-text-muted">
                                  {formatarPreco(item.market_price)} unit.
                                </span>
                              </div>
                            </div>
                          </li>
                        );
                      })}
                    </ul>
                  </div>
                ))}
              </div>
            </>
          )}
        </div>

        <div className="flex flex-col justify-between gap-3 border-t border-ice-border bg-surface-container-low/40 px-space-lg py-space-sm sm:flex-row sm:items-center">
          <span className="font-body-sm text-body-sm text-secondary">
            Deck válido de 50 cartas • Estrutura oficial Bandai TCG
          </span>
          <div className="flex flex-wrap items-center gap-2">
            <button
              type="button"
              onClick={fechar}
              className="rounded-lg bg-surface-container px-4 py-2 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container-high hover:text-deep-navy"
            >
              Fechar
            </button>
            {composicao && (
              <button
                type="button"
                onClick={copiarBandai}
                className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-4 py-2 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
              >
                <Icon nome="content_copy" className="text-[18px]" />
                <span>Copiar Decklist (Formato Bandai)</span>
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
