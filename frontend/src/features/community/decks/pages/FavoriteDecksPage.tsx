import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { CommunitySubNav } from '../../../../components/layout/CommunitySubNav';
import { Icon } from '../../../../components/ui/Icon';
import { useDecksUiStore } from '../../../../stores/decksUiStore';
import { useProfileUiStore } from '../../profile/stores/useProfileUiStore';
import {
  consultaMeusDecksFavoritos,
  useAlternarVisibilidadeDecks,
  useRemoverDeckFavorito,
} from '../api/decksQueries';
import { AddFavoriteDeckModal } from '../components/AddFavoriteDeckModal';
import { DeckCompositionModal } from '../components/DeckCompositionModal';

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function FavoriteDecksPage() {
  const { data: dados } = useSuspenseQuery(consultaMeusDecksFavoritos);
  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);
  const abrirModalAdicionar = useDecksUiStore((estado) => estado.abrirModalAdicionar);
  const abrirComposicao = useDecksUiStore((estado) => estado.abrirComposicao);
  const removerDeck = useRemoverDeckFavorito();
  const alternarVisibilidade = useAlternarVisibilidadeDecks();
  const [imagensComFalha, definirImagensComFalha] = useState<Record<string, boolean>>({});

  useEffect(() => {
    definirAbaAtiva('decks-favoritos');
  }, [definirAbaAtiva]);

  const limiteAtingido = dados.total >= dados.limite;

  return (
    <div className="mx-auto max-w-7xl space-y-space-xl px-margin py-space-lg md:px-margin-lg">
      <CommunitySubNav abaAtiva="decks-favoritos" />

      <section className="flex flex-col justify-between gap-space-md rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm lg:flex-row lg:items-center">
        <div className="space-y-1">
          <div className="flex flex-wrap items-center gap-2">
            <Icon nome="view_carousel" className="text-[26px] text-ocean-blue" />
            <h1 className="font-headline-lg text-headline-lg text-deep-navy">Decks Favoritos</h1>
            <span className="rounded-full bg-sky-tint px-3 py-0.5 font-label-sm text-label-sm font-semibold text-ocean-blue">
              {dados.total}/{dados.limite} decks favoritos
            </span>
          </div>
          <p className="font-body-sm text-body-sm text-text-muted">
            Formato Oficial: 1 Líder + 50 Cartas • Selecione até {dados.limite} decks competitivos
            do Catálogo para expor no seu perfil comunitário
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-space-md">
          <div className="flex items-center gap-3 rounded-xl border border-ice-border bg-surface-container-low/60 px-3.5 py-2">
            <div>
              <span className="block font-label-sm text-label-sm font-semibold text-deep-navy">
                Exibir no Perfil Público
              </span>
              <span className="block font-body-sm text-[11px] text-text-muted">
                {dados.decksFavoritosVisiveis
                  ? 'Visível para visitantes'
                  : 'Oculto no perfil público — somente você vê'}
              </span>
            </div>
            <button
              type="button"
              role="switch"
              aria-checked={dados.decksFavoritosVisiveis}
              aria-label="Exibir no Perfil Público"
              disabled={alternarVisibilidade.isPending}
              onClick={() => alternarVisibilidade.mutate(!dados.decksFavoritosVisiveis)}
              className={`relative inline-flex h-6 w-11 flex-shrink-0 items-center rounded-full transition-colors ${
                dados.decksFavoritosVisiveis ? 'bg-ocean-blue' : 'bg-steel-border'
              } disabled:opacity-50`}
            >
              <span
                className={`inline-block h-4 w-4 transform rounded-full bg-pure-white shadow-sm transition-transform ${
                  dados.decksFavoritosVisiveis ? 'translate-x-6' : 'translate-x-1'
                }`}
              />
            </button>
          </div>

          <button
            type="button"
            disabled={limiteAtingido}
            title={limiteAtingido ? 'Limite de 4 decks favoritos atingido' : undefined}
            onClick={abrirModalAdicionar}
            className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-4 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:cursor-not-allowed disabled:bg-surface-container disabled:text-secondary"
          >
            <Icon nome="add" className="text-[18px]" />
            <span>+ Adicionar deck favorito</span>
          </button>
        </div>
      </section>

      {dados.decks.length === 0 ? (
        <section className="flex flex-col items-center space-y-4 rounded-2xl border-2 border-dashed border-steel-border bg-pure-white/80 p-space-xxl text-center">
          <span className="flex h-14 w-14 items-center justify-center rounded-2xl bg-sky-tint text-ocean-blue">
            <Icon nome="view_carousel" className="text-[30px]" />
          </span>
          <div className="max-w-md space-y-1">
            <h2 className="font-headline-md text-headline-md text-deep-navy">
              Nenhum deck favorito fixado
            </h2>
            <p className="font-body-md text-body-md text-text-muted">
              Você ainda não favoritou nenhum deck. Explore o catálogo oficial e fixe até 4 decks
              competitivos.
            </p>
          </div>
          <button
            type="button"
            onClick={abrirModalAdicionar}
            className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-5 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
          >
            <Icon nome="add" className="text-[18px]" />
            <span>+ Adicionar deck favorito</span>
          </button>
        </section>
      ) : (
        <ul className="m-0 list-none space-y-space-md p-0">
          {dados.decks.map((deck) => {
            const exibirImagem = Boolean(deck.liderImagem) && !imagensComFalha[deck.deckId];
            return (
              <li
                key={deck.deckId}
                className="flex flex-col justify-between gap-space-md rounded-2xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm transition-shadow hover:shadow-md md:flex-row md:items-center"
              >
                <div className="flex items-start gap-space-md sm:items-center">
                  <div className="flex aspect-[2.5/3.5] w-16 flex-shrink-0 items-center justify-center overflow-hidden rounded-lg bg-deep-navy text-center font-label-sm text-xs font-bold text-pure-white shadow-sm">
                    {exibirImagem ? (
                      <img
                        src={deck.liderImagem!}
                        alt={deck.nomeDeck}
                        onError={() =>
                          definirImagensComFalha((atual) => ({ ...atual, [deck.deckId]: true }))
                        }
                        className="h-full w-full object-cover"
                      />
                    ) : (
                      <span>{deck.liderCodigo || deck.deckId}</span>
                    )}
                  </div>

                  <div className="space-y-1">
                    <div className="flex flex-wrap items-center gap-2">
                      <h2 className="font-headline-md text-headline-md text-deep-navy">
                        {deck.nomeDeck}
                      </h2>
                      <span className="rounded bg-sky-tint px-2 py-0.5 font-label-sm text-label-sm font-bold text-ocean-blue">
                        {deck.totalCartasMainDeck} + 1
                      </span>
                      <span className="rounded bg-surface-container px-2 py-0.5 font-label-sm text-label-sm text-secondary">
                        {deck.cor}
                      </span>
                      {!deck.ativo && (
                        <span className="inline-flex items-center gap-1 rounded-full border border-steel-border bg-status-warning-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-warning-text">
                          <Icon nome="inventory_2" className="text-[14px]" />
                          <span>Descontinuado / Inativo no Catálogo</span>
                        </span>
                      )}
                    </div>

                    <p className="font-body-md text-body-md text-text-muted">
                      Líder: <strong className="text-deep-navy">{deck.lider}</strong> ({deck.deckId})
                    </p>

                    <p className="font-tabular-price text-tabular-price text-deep-navy">
                      {formatarPreco(deck.custoEstimadoMercado)}{' '}
                      <span className="font-body-sm text-body-sm font-normal text-secondary">
                        • Base do Mercado
                      </span>
                    </p>
                  </div>
                </div>

                <div className="flex flex-wrap items-center gap-2">
                  <button
                    type="button"
                    onClick={() => abrirComposicao(deck.deckId)}
                    className="inline-flex items-center gap-1.5 rounded-xl bg-ocean-blue px-4 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
                  >
                    <Icon nome="visibility" className="text-[18px]" />
                    <span>Visualizar deck</span>
                  </button>

                  <button
                    type="button"
                    disabled={removerDeck.isPending}
                    onClick={() => removerDeck.mutate(deck.deckId)}
                    className="inline-flex items-center gap-1 rounded-xl border border-ice-border bg-pure-white px-4 py-2.5 font-label-md text-label-md font-semibold text-error transition-colors hover:bg-error-container/30 disabled:opacity-50"
                  >
                    <Icon nome="delete" className="text-[18px]" />
                    <span>Remover</span>
                  </button>
                </div>
              </li>
            );
          })}
        </ul>
      )}

      <AddFavoriteDeckModal totalFavoritos={dados.total} limite={dados.limite} />
      <DeckCompositionModal />
    </div>
  );
}
