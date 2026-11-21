import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { Icon } from '../../../../components/ui/Icon';
import { ErroHttp, mensagemErro } from '../../../../lib/api';
import { useDecksUiStore } from '../../../../stores/decksUiStore';
import { consultaCatalogoDecks, useAdicionarDeckFavorito } from '../api/decksQueries';

const CORES_FILTRO = [
  { rotulo: 'Todas', valor: '' },
  { rotulo: 'Vermelho', valor: 'Vermelho' },
  { rotulo: 'Verde', valor: 'Verde' },
  { rotulo: 'Azul', valor: 'Azul' },
  { rotulo: 'Roxo', valor: 'Roxo' },
  { rotulo: 'Preto', valor: 'Preto' },
  { rotulo: 'Amarelo', valor: 'Amarelo' },
];

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function AddFavoriteDeckModal({
  totalFavoritos,
  limite = 4,
}: {
  totalFavoritos: number;
  limite?: number;
}) {
  const modalAberto = useDecksUiStore((estado) => estado.modalAdicionarAberto);
  const termo = useDecksUiStore((estado) => estado.termoBuscaCatalogo);
  const cor = useDecksUiStore((estado) => estado.corFiltroCatalogo);
  const definirTermo = useDecksUiStore((estado) => estado.definirTermoBuscaCatalogo);
  const definirCor = useDecksUiStore((estado) => estado.definirCorFiltroCatalogo);
  const fecharModal = useDecksUiStore((estado) => estado.fecharModalAdicionar);
  const [imagensComFalha, definirImagensComFalha] = useState<Record<string, boolean>>({});

  const catalogo = useQuery({
    ...consultaCatalogoDecks(termo, cor),
    enabled: modalAberto,
  });

  const adicionarDeck = useAdicionarDeckFavorito();

  if (!modalAberto) {
    return null;
  }

  const limiteAtingido = totalFavoritos >= limite;
  const erro503 = catalogo.error instanceof ErroHttp && catalogo.error.status === 503;

  async function aoFavoritar(deckId: string) {
    try {
      await adicionarDeck.mutateAsync(deckId);
    } catch {
      // Erro publicado pelo hook `useAdicionarDeckFavorito` via toast.
    }
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-adicionar-deck-favorito"
      className="fixed inset-0 z-50 flex items-center justify-center bg-deep-navy/55 p-4 backdrop-blur-sm"
    >
      <div className="flex max-h-[88vh] w-full max-w-3xl flex-col overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-level-4">
        <div className="flex items-start justify-between gap-space-sm border-b border-ice-border px-space-lg py-space-md">
          <div>
            <div className="flex items-center gap-space-xs">
              <Icon nome="view_carousel" className="text-[22px] text-ocean-blue" />
              <h2
                id="titulo-adicionar-deck-favorito"
                className="font-headline-md text-headline-md text-deep-navy"
              >
                Adicionar Deck Favorito
              </h2>
            </div>
            <p className="mt-0.5 font-body-sm text-body-sm text-text-muted">
              Pesquise decks ativos no catálogo oficial Bandai e fixe nos seus favoritos
            </p>
          </div>
          <button
            type="button"
            aria-label="Fechar busca de decks"
            onClick={fecharModal}
            className="rounded-lg p-1.5 text-secondary transition-colors hover:bg-surface-container-low hover:text-deep-navy"
          >
            <Icon nome="close" className="text-[20px]" />
          </button>
        </div>

        <div className="space-y-space-md overflow-y-auto p-space-lg">
          <div
            className={`flex items-center justify-between gap-2 rounded-xl p-3 font-body-sm text-body-sm ${
              limiteAtingido
                ? 'bg-status-warning-bg text-status-warning-text'
                : 'bg-status-positive-bg text-status-positive-text'
            }`}
          >
            <div className="flex items-center gap-2">
              <Icon nome={limiteAtingido ? 'warning' : 'info'} className="text-[18px]" />
              <span>
                <strong>Limite oficial:</strong> máximo de {limite} decks favoritos •{' '}
                {totalFavoritos} de {limite} slots ocupados
              </span>
            </div>
            <span className="rounded-full bg-pure-white/80 px-2.5 py-0.5 font-label-sm text-label-sm font-bold text-deep-navy">
              {totalFavoritos}/{limite}
            </span>
          </div>

          <div className="space-y-space-sm">
            <div className="relative flex items-center">
              <Icon
                nome="search"
                className="pointer-events-none absolute left-3 text-[20px] text-secondary"
              />
              <label htmlFor="busca-deck-catalogo" className="sr-only">
                Buscar deck por nome, líder ou código
              </label>
              <input
                id="busca-deck-catalogo"
                type="search"
                value={termo}
                onChange={(evento) => definirTermo(evento.target.value)}
                placeholder="Buscar deck por nome, líder ou código (ex: ST01, Zoro)..."
                className="w-full rounded-lg bg-surface-container-low py-2.5 pr-3 pl-10 font-body-sm text-body-sm text-deep-navy ring-1 ring-steel-border transition-all outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
              />
            </div>

            <div className="flex flex-wrap items-center gap-1.5" role="group" aria-label="Filtrar decks por cor">
              {CORES_FILTRO.map((opcao) => {
                const ativa = cor === opcao.valor;
                return (
                  <button
                    key={opcao.rotulo}
                    type="button"
                    onClick={() => definirCor(opcao.valor)}
                    className={`rounded-lg px-3 py-1 font-label-sm text-label-sm transition-colors ${
                      ativa
                        ? 'bg-deep-navy font-semibold text-pure-white shadow-sm'
                        : 'bg-surface-container-low text-secondary hover:bg-sky-tint hover:text-deep-navy'
                    }`}
                  >
                    {opcao.rotulo}
                  </button>
                );
              })}
            </div>
          </div>

          {catalogo.isLoading ? (
            <div className="space-y-2" role="status" aria-label="Carregando catálogo de decks">
              {[0, 1, 2].map((indice) => (
                <div
                  key={indice}
                  className="h-20 animate-pulse rounded-xl bg-surface-container-low"
                />
              ))}
            </div>
          ) : catalogo.isError ? (
            <div
              role="alert"
              className="flex flex-col items-start justify-between gap-3 rounded-xl bg-error-container p-4 text-on-error-container sm:flex-row sm:items-center"
            >
              <div className="flex items-start gap-2.5">
                <Icon nome="cloud_off" className="text-[22px] text-error" />
                <p className="font-body-sm text-body-sm">
                  {erro503
                    ? 'Serviço de Catálogo temporariamente indisponível. Tente novamente em instantes.'
                    : mensagemErro(catalogo.error)}
                </p>
              </div>
              <button
                type="button"
                onClick={() => void catalogo.refetch()}
                className="flex-shrink-0 rounded-lg bg-deep-navy px-3.5 py-2 font-label-sm text-label-sm font-semibold text-pure-white transition-colors hover:bg-ocean-blue"
              >
                Tentar novamente
              </button>
            </div>
          ) : (catalogo.data?.length ?? 0) === 0 ? (
            <div className="rounded-xl border border-dashed border-steel-border bg-surface-container-low p-space-lg text-center font-body-sm text-body-sm text-text-muted">
              Nenhum deck ativo encontrado para os filtros selecionados.
            </div>
          ) : (
            <ul className="m-0 list-none space-y-2.5 p-0">
              {catalogo.data!.map((deck) => {
                const exibirImagem = Boolean(deck.liderImagem) && !imagensComFalha[deck.deckId];
                const desabilitado =
                  deck.jaFavoritado || limiteAtingido || adicionarDeck.isPending;

                return (
                  <li
                    key={deck.deckId}
                    className="flex flex-col justify-between gap-3 rounded-xl border border-ice-border bg-surface-container-lowest p-3.5 transition-colors hover:bg-surface-container-low/40 sm:flex-row sm:items-center"
                  >
                    <div className="flex items-center gap-3">
                      <div className="flex h-16 w-12 flex-shrink-0 items-center justify-center overflow-hidden rounded-lg bg-deep-navy text-center font-label-sm text-[10px] font-bold text-pure-white">
                        {exibirImagem ? (
                          <img
                            src={deck.liderImagem!}
                            alt={deck.nomeDeck}
                            onError={() =>
                              definirImagensComFalha((atual) => ({
                                ...atual,
                                [deck.deckId]: true,
                              }))
                            }
                            className="h-full w-full object-cover"
                          />
                        ) : (
                          <span>{deck.liderCodigo || deck.deckId}</span>
                        )}
                      </div>

                      <div className="min-w-0 space-y-0.5">
                        <div className="flex flex-wrap items-center gap-1.5">
                          <h3 className="font-headline-sm text-headline-sm text-deep-navy">
                            {deck.nomeDeck}
                          </h3>
                          <span className="rounded bg-sky-tint px-2 py-0.5 font-label-sm text-[11px] font-bold text-ocean-blue">
                            {deck.deckId}
                          </span>
                          <span className="rounded bg-surface-container px-2 py-0.5 font-label-sm text-[11px] text-secondary">
                            {deck.cor}
                          </span>
                        </div>
                        <p className="font-body-sm text-body-sm text-text-muted">
                          Líder: <strong className="text-deep-navy">{deck.lider}</strong>
                        </p>
                        <p className="font-tabular-price text-sm text-deep-navy">
                          {formatarPreco(deck.custoEstimadoMercado)}{' '}
                          <span className="font-body-sm text-[11px] font-normal text-secondary">
                            • {deck.totalCartasMainDeck} + 1 cartas
                          </span>
                        </p>
                      </div>
                    </div>

                    <button
                      type="button"
                      disabled={desabilitado}
                      onClick={() => void aoFavoritar(deck.deckId)}
                      className={`inline-flex flex-shrink-0 items-center justify-center gap-1 rounded-lg px-4 py-2 font-label-sm text-label-sm font-semibold transition-colors ${
                        deck.jaFavoritado
                          ? 'bg-sky-tint text-ocean-blue'
                          : limiteAtingido
                            ? 'bg-surface-container text-secondary'
                            : 'bg-ocean-blue text-pure-white shadow-sm hover:bg-deep-navy'
                      } disabled:cursor-not-allowed`}
                    >
                      {deck.jaFavoritado ? (
                        <>
                          <Icon nome="check" className="text-[16px]" />
                          <span>Adicionado</span>
                        </>
                      ) : limiteAtingido ? (
                        <span>Limite {limite}/{limite}</span>
                      ) : (
                        <>
                          <Icon nome="add" className="text-[16px]" />
                          <span>Favoritar</span>
                        </>
                      )}
                    </button>
                  </li>
                );
              })}
            </ul>
          )}
        </div>

        <div className="flex items-center justify-between border-t border-ice-border bg-surface-container-low/40 px-space-lg py-space-sm">
          <span className="font-body-sm text-body-sm text-secondary">
            Formato Oficial: 1 Líder + 50 Cartas
          </span>
          <button
            type="button"
            onClick={fecharModal}
            className="rounded-lg bg-surface-container px-4 py-2 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container-high hover:text-deep-navy"
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}
