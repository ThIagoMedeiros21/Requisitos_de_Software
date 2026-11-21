import { useState } from 'react';
import { Icon } from '../../../../components/ui/Icon';
import { useToastStore } from '../../../../stores/useToastStore';
import { useRemoverCartaFavorita } from '../api/profileQueries';
import { useProfileUiStore } from '../stores/useProfileUiStore';
import type { CartaDestaqueDto, PerfilComunitarioVisaoDto } from '../types/profile.types';

const TOTAL_SLOTS = 4;

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function IlustracaoCartaNautica({ carta }: { carta: CartaDestaqueDto }) {
  const codigo = carta.card_set_id ?? carta.codigo;
  const nome = carta.card_name ?? carta.nome;

  return (
    <div className="flex h-full w-full flex-col items-center justify-center bg-gradient-to-br from-deep-navy via-primary to-ocean-blue p-4 text-center text-pure-white">
      <svg
        viewBox="0 0 80 100"
        className="mb-2 h-20 w-16 opacity-90"
        fill="none"
        aria-hidden="true"
      >
        <rect
          x="6"
          y="6"
          width="68"
          height="88"
          rx="8"
          stroke="#A0C9FF"
          strokeWidth="2.5"
          fill="rgba(255,255,255,0.06)"
        />
        <circle cx="40" cy="38" r="14" stroke="#E0ECF8" strokeWidth="2" />
        <path
          d="M40 28v20M31 38h18M30 48c3 4 6 6 10 6s7-2 10-6"
          stroke="#FFFFFF"
          strokeWidth="2"
          strokeLinecap="round"
        />
        <rect x="18" y="68" width="44" height="6" rx="3" fill="#2563EB" />
        <rect x="24" y="78" width="32" height="4" rx="2" fill="#8EBDF9" />
      </svg>
      <span className="font-headline-sm text-xs font-bold tracking-wider text-sky-tint uppercase">
        {codigo}
      </span>
      <span className="mt-0.5 line-clamp-2 font-body-sm text-[11px] text-pure-white/85">
        {nome}
      </span>
    </div>
  );
}

export function FavoriteCardsSection({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const abrirModalSlot = useProfileUiStore((estado) => estado.abrirModalSlot);
  const publicar = useToastStore((estado) => estado.publicar);
  const removerCarta = useRemoverCartaFavorita(perfil.id);
  const [imagensComFalha, definirImagensComFalha] = useState<Record<string, boolean>>({});

  const preenchidos = perfil.cartasFavoritas.length;
  const slots = Array.from({ length: TOTAL_SLOTS }, (_, indice) => {
    return perfil.cartasFavoritas[indice] ?? null;
  });

  return (
    <section className="space-y-space-md">
      <div className="flex flex-col justify-between gap-space-xs sm:flex-row sm:items-end">
        <div>
          <h2 className="font-headline-lg text-headline-lg tracking-tight text-deep-navy">
            Cartas Favoritas
          </h2>
          <p className="font-body-sm text-body-sm text-text-muted">
            Destaques da coleção pessoal expostos na comunidade (Máximo de 4 slots estritos)
          </p>
        </div>

        <div className="flex items-center gap-2 font-label-sm text-label-sm text-ocean-blue">
          <span
            id="fav-slot-counter"
            className="inline-flex items-center gap-1 rounded-full bg-sky-tint px-2.5 py-1 font-label-sm text-label-sm font-semibold text-ocean-blue"
          >
            <Icon nome="stars" className="text-[15px]" />
            <span>
              {preenchidos} de {TOTAL_SLOTS} slots preenchidos
            </span>
          </span>

          {perfil.ehProprietario && (
            <button
              type="button"
              onClick={() => abrirModalSlot(preenchidos < TOTAL_SLOTS ? preenchidos : null)}
              className="inline-flex items-center gap-1 rounded-lg bg-surface-container-low px-3 py-1 font-label-sm text-label-sm font-semibold text-ocean-blue transition-colors hover:bg-sky-tint"
            >
              <Icon nome="tune" className="text-[16px]" />
              <span>Gerenciar Slots</span>
            </button>
          )}
        </div>
      </div>

      {!perfil.ehProprietario && preenchidos === 0 ? (
        <div className="rounded-xl border border-dashed border-steel-border bg-pure-white p-space-lg text-center font-body-md text-body-md text-text-muted">
          Este colecionador ainda não destacou cartas favoritas
        </div>
      ) : (
        <div
          id="favorites-grid"
          className="grid grid-cols-1 gap-space-md sm:grid-cols-2 lg:grid-cols-4"
        >
          {slots.map((carta, indice) => {
            if (!carta) {
              if (!perfil.ehProprietario) {
                return null;
              }
              return (
                <div
                  key={`slot-vazio-${indice}`}
                  className="flex min-h-80 flex-col items-center justify-between rounded-xl border-2 border-dashed border-steel-border bg-pure-white/70 p-space-md text-center transition-colors hover:border-ocean-blue hover:bg-sky-tint/20"
                >
                  <div className="mt-8 flex flex-col items-center gap-space-xs">
                    <span className="flex h-12 w-12 items-center justify-center rounded-full bg-sky-tint text-ocean-blue shadow-sm">
                      <Icon nome="add" className="text-[24px]" />
                    </span>
                    <p className="font-headline-sm text-headline-sm text-deep-navy">
                      Slot Disponível
                    </p>
                    <p className="font-body-sm text-body-sm text-text-muted">
                      Toque para adicionar da coleção
                    </p>
                  </div>
                  <button
                    type="button"
                    onClick={() => abrirModalSlot(indice)}
                    className="mt-4 w-full rounded-lg bg-ocean-blue px-3 py-2 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
                  >
                    + Adicionar Carta
                  </button>
                </div>
              );
            }

            const codigo = carta.card_set_id ?? carta.codigo;
            const nome = carta.card_name ?? carta.nome;
            const raridade = carta.rarity ?? carta.raridade;
            const cor = carta.card_color ?? carta.cor;
            const preco = carta.market_price ?? carta.precoMercado;
            const urlImagem = carta.card_image ?? carta.imagem;
            const exibirImagem = Boolean(urlImagem) && !imagensComFalha[carta.cartaId];

            return (
              <div
                key={carta.cartaId}
                id={`slot-${indice}`}
                className="fav-slot group flex flex-col justify-between rounded-xl border border-ice-border/60 bg-pure-white p-space-sm shadow-sm transition-shadow hover:shadow-md"
              >
                <div className="relative mb-space-sm aspect-[2.5/3.5] w-full overflow-hidden rounded-lg bg-surface-container-low">
                  {exibirImagem ? (
                    <img
                      src={urlImagem!}
                      alt={nome}
                      onError={() =>
                        definirImagensComFalha((atual) => ({ ...atual, [carta.cartaId]: true }))
                      }
                      className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
                    />
                  ) : (
                    <IlustracaoCartaNautica carta={carta} />
                  )}

                  <div className="absolute top-2 left-2 flex flex-col gap-1">
                    <span className="rounded bg-deep-navy px-2 py-0.5 font-label-sm text-[11px] font-bold tracking-wider text-pure-white uppercase">
                      {raridade}
                    </span>
                  </div>

                  <div className="absolute top-2 right-2 flex items-center gap-1">
                    <span className="rounded bg-pure-white/95 px-2 py-0.5 font-label-sm text-[11px] font-semibold text-ocean-blue shadow-sm">
                      {carta.edicaoCondicao}
                    </span>
                    {perfil.ehProprietario && (
                      <button
                        type="button"
                        aria-label={`Remover ${nome} dos destaques`}
                        title="Remover slot favorito"
                        disabled={removerCarta.isPending}
                        onClick={() => removerCarta.mutate(carta.cartaId)}
                        className="flex h-6 w-6 items-center justify-center rounded-full bg-deep-navy/80 text-pure-white shadow-sm transition-colors hover:bg-error disabled:opacity-50"
                      >
                        <Icon nome="close" className="text-[14px]" />
                      </button>
                    )}
                  </div>

                  <div className="absolute right-2 bottom-2 left-2 flex items-center justify-between gap-1">
                    <span className="rounded bg-deep-navy/85 px-2 py-0.5 font-label-sm text-[11px] text-pure-white">
                      {cor}
                    </span>
                    {carta.card_type && (
                      <span className="rounded bg-pure-white/90 px-2 py-0.5 font-label-sm text-[10px] font-semibold text-deep-navy">
                        {carta.card_type}
                        {carta.card_power ? ` • ${carta.card_power}` : ''}
                      </span>
                    )}
                  </div>
                </div>

                <div className="space-y-1 px-1">
                  <div className="flex items-baseline justify-between gap-1">
                    <h3 className="truncate font-headline-sm text-headline-sm text-deep-navy">
                      {nome}
                    </h3>
                    <span className="font-body-sm text-body-sm font-medium text-secondary">
                      {codigo}
                    </span>
                  </div>

                  {(carta.set_name || carta.sub_types) && (
                    <p className="truncate font-body-sm text-[11px] text-text-muted">
                      {[carta.set_name, carta.sub_types].filter(Boolean).join(' • ')}
                    </p>
                  )}

                  <div className="flex items-center justify-between pt-1">
                    <span className="font-tabular-price text-tabular-price text-deep-navy">
                      {formatarPreco(preco)}
                    </span>
                    <span className="font-label-sm text-label-sm text-cobalt-accent">
                      Mkt Base
                    </span>
                  </div>

                  <div className="flex items-center gap-space-xs pt-space-xs">
                    <button
                      type="button"
                      onClick={() =>
                        publicar('info', `Exibindo ${nome} (${codigo}) no catálogo.`)
                      }
                      className="flex-1 rounded-lg bg-surface-container-low py-1.5 text-center font-label-sm text-label-sm font-semibold text-ocean-blue transition-colors hover:bg-sky-tint"
                    >
                      Ver no Catálogo
                    </button>
                    <button
                      type="button"
                      title="Desejar Carta"
                      aria-label={`Salvar ${nome} na Wishlist`}
                      onClick={() => publicar('sucesso', 'Carta salva na sua Wishlist!')}
                      className="rounded-lg bg-surface-container-low p-1.5 text-deep-navy transition-colors hover:bg-sky-tint"
                    >
                      <Icon nome="bookmark" className="text-[18px]" />
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </section>
  );
}
