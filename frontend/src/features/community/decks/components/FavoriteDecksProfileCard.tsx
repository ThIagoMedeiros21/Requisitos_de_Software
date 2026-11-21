import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { Link } from 'react-router';
import { Icon } from '../../../../components/ui/Icon';
import { useDecksUiStore } from '../../../../stores/decksUiStore';
import type { PerfilComunitarioVisaoDto } from '../../profile/types/profile.types';
import {
  consultaDecksDoPerfil,
  useRemoverDeckFavorito,
} from '../api/decksQueries';
import { AddFavoriteDeckModal } from './AddFavoriteDeckModal';
import { DeckCompositionModal } from './DeckCompositionModal';

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function FavoriteDecksProfileCard({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const abrirModalAdicionar = useDecksUiStore((estado) => estado.abrirModalAdicionar);
  const abrirComposicao = useDecksUiStore((estado) => estado.abrirComposicao);
  const removerDeck = useRemoverDeckFavorito();
  const [imagensComFalha, definirImagensComFalha] = useState<Record<string, boolean>>({});

  const consultaDecks = useQuery({
    ...consultaDecksDoPerfil(perfil.id),
    enabled: perfil.ehProprietario || perfil.decksFavoritosVisiveis,
  });

  const dados = consultaDecks.data;
  const visiveisNoPublico = dados?.decksFavoritosVisiveis ?? perfil.decksFavoritosVisiveis;

  if (!perfil.ehProprietario && !visiveisNoPublico) {
    return null;
  }

  const decks = dados?.decks ?? [];
  const total = dados?.total ?? decks.length;
  const limite = dados?.limite ?? 4;
  const limiteAtingido = total >= limite;

  return (
    <>
      <section className="flex flex-col justify-between rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
        <div className="space-y-space-md">
          <div className="flex flex-wrap items-start justify-between gap-2">
            <div>
              <div className="flex flex-wrap items-center gap-2">
                <Icon nome="view_carousel" className="text-[22px] text-ocean-blue" />
                <h2 className="font-headline-md text-headline-md text-deep-navy">
                  Decks Favoritos
                </h2>
                <span className="rounded-full bg-sky-tint px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-ocean-blue">
                  {total}/{limite} decks favoritos
                </span>
                {perfil.ehProprietario && !visiveisNoPublico && (
                  <span className="rounded-full bg-surface-container px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-secondary">
                    Oculto no perfil público
                  </span>
                )}
              </div>
              <p className="mt-1 font-body-sm text-body-sm text-text-muted">
                Formato Oficial: 1 Líder + 50 Cartas
              </p>
            </div>

            {perfil.ehProprietario && (
              <Link
                to="/comunidade/decks-favoritos"
                className="inline-flex items-center gap-1 rounded-lg bg-surface-container-low px-3 py-1.5 font-label-sm text-label-sm font-semibold text-ocean-blue no-underline transition-colors hover:bg-sky-tint"
              >
                <Icon nome="tune" className="text-[16px]" />
                <span>Gerenciar Decks</span>
              </Link>
            )}
          </div>

          {decks.length === 0 ? (
            <div className="rounded-xl border border-dashed border-steel-border bg-surface-container-low/50 p-4 text-center font-body-sm text-body-sm text-text-muted">
              {perfil.ehProprietario
                ? 'Você ainda não adicionou decks favoritos. Adicione até 4 decks do Catálogo.'
                : 'Este colecionador ainda não destacou decks favoritos.'}
            </div>
          ) : (
            <ul className="m-0 list-none space-y-2.5 p-0">
              {decks.map((deck) => {
                const exibirImagem = Boolean(deck.liderImagem) && !imagensComFalha[deck.deckId];
                return (
                  <li
                    key={deck.deckId}
                    className="flex flex-col justify-between gap-3 rounded-xl border border-ice-border bg-surface-container-lowest p-3 sm:flex-row sm:items-center"
                  >
                    <div className="flex items-center gap-3">
                      <div className="flex aspect-[2.5/3.5] w-11 flex-shrink-0 items-center justify-center overflow-hidden rounded-md bg-deep-navy text-center font-label-sm text-[9px] font-bold text-pure-white">
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
                          <h3 className="truncate font-headline-sm text-headline-sm text-deep-navy">
                            {deck.nomeDeck}
                          </h3>
                          <span className="rounded bg-sky-tint px-1.5 py-0.5 font-label-sm text-[10px] font-bold text-ocean-blue">
                            {deck.totalCartasMainDeck} + 1
                          </span>
                          {!deck.ativo && (
                            <span className="rounded border border-steel-border bg-status-warning-bg px-2 py-0.5 font-label-sm text-[10px] font-semibold text-status-warning-text">
                              Descontinuado / Inativo no Catálogo
                            </span>
                          )}
                        </div>
                        <p className="truncate font-body-sm text-[12px] text-text-muted">
                          Líder: {deck.lider}
                        </p>
                        <p className="font-tabular-price text-xs text-deep-navy">
                          {formatarPreco(deck.custoEstimadoMercado)}
                        </p>
                      </div>
                    </div>

                    <div className="flex flex-wrap items-center gap-2">
                      <button
                        type="button"
                        onClick={() => abrirComposicao(deck.deckId)}
                        className="inline-flex items-center gap-1 rounded-lg bg-ocean-blue px-3 py-1.5 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
                      >
                        <Icon nome="visibility" className="text-[16px]" />
                        <span>Visualizar deck</span>
                      </button>

                      {perfil.ehProprietario && (
                        <button
                          type="button"
                          disabled={removerDeck.isPending}
                          onClick={() => removerDeck.mutate(deck.deckId)}
                          className="rounded-lg px-2.5 py-1.5 font-label-sm text-label-sm font-semibold text-error transition-colors hover:bg-error-container/30 disabled:opacity-50"
                        >
                          Remover
                        </button>
                      )}
                    </div>
                  </li>
                );
              })}
            </ul>
          )}
        </div>

        {perfil.ehProprietario && (
          <div className="pt-space-md">
            <button
              type="button"
              disabled={limiteAtingido}
              title={limiteAtingido ? 'Limite de 4 decks favoritos atingido' : undefined}
              onClick={abrirModalAdicionar}
              className="inline-flex items-center gap-1.5 rounded-lg bg-surface-container-low px-4 py-2 font-label-sm text-label-sm font-semibold text-ocean-blue transition-colors hover:bg-sky-tint disabled:cursor-not-allowed disabled:opacity-50"
            >
              <Icon nome="add" className="text-[18px]" />
              <span>+ Adicionar deck favorito</span>
            </button>
          </div>
        )}
      </section>

      {perfil.ehProprietario && (
        <AddFavoriteDeckModal totalFavoritos={total} limite={limite} />
      )}
      <DeckCompositionModal />
    </>
  );
}
