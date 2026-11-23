import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { Link } from 'react-router';
import { CommunitySubNav } from '../../../../components/layout/CommunitySubNav';
import { Icon } from '../../../../components/ui/Icon';
import {
  type FiltroElegibilidadeAvaliacao,
  useReviewsUiStore,
} from '../../../../stores/reviewsUiStore';
import { useProfileUiStore } from '../../profile/stores/useProfileUiStore';
import { consultaCatalogoAvaliacoes } from '../api/reviewsQueries';
import { ReviewFormModal } from '../components/ReviewFormModal';
import { StarRating } from '../components/StarRating';

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function CardReviewsCatalogPage() {
  const { data: cartas } = useSuspenseQuery(consultaCatalogoAvaliacoes());
  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);
  const termo = useReviewsUiStore((estado) => estado.termoBusca);
  const filtro = useReviewsUiStore((estado) => estado.filtroElegibilidade);
  const produtoModal = useReviewsUiStore((estado) => estado.produtoModalAberto);
  const definirTermo = useReviewsUiStore((estado) => estado.definirTermoBusca);
  const definirFiltro = useReviewsUiStore((estado) => estado.definirFiltroElegibilidade);
  const abrirModal = useReviewsUiStore((estado) => estado.abrirModalAvaliacao);
  const [imagensComFalha, definirImagensComFalha] = useState<Record<string, boolean>>({});

  useEffect(() => {
    definirAbaAtiva('avaliacoes');
  }, [definirAbaAtiva]);

  const termoNormalizado = termo.trim().toLowerCase();
  const cartasFiltradas = cartas.filter((carta) => {
    if (filtro === 'elegiveis' && !carta.podeAvaliar) {
      return false;
    }
    if (filtro === 'avaliadas' && !(carta.minhaAvaliacao && carta.minhaAvaliacao.valida)) {
      return false;
    }
    if (!termoNormalizado) {
      return true;
    }
    return (
      carta.card_name.toLowerCase().includes(termoNormalizado) ||
      carta.card_set_id.toLowerCase().includes(termoNormalizado) ||
      (carta.set_name?.toLowerCase().includes(termoNormalizado) ?? false)
    );
  });

  const cartaSelecionada = cartas.find((c) => c.produtoId === produtoModal) ?? null;

  const filtros: { chave: FiltroElegibilidadeAvaliacao; rotulo: string }[] = [
    { chave: 'todas', rotulo: 'Todas as Cartas' },
    { chave: 'elegiveis', rotulo: 'Elegíveis para Avaliar' },
    { chave: 'avaliadas', rotulo: 'Minhas Avaliações' },
  ];

  return (
    <div className="mx-auto max-w-7xl space-y-space-xl px-margin py-space-lg md:px-margin-lg">
      <CommunitySubNav abaAtiva="avaliacoes" />

      <section className="flex flex-col justify-between gap-space-md rounded-2xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm lg:flex-row lg:items-center">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <Icon nome="rate_review" className="text-[26px] text-ocean-blue" />
            <h1 className="font-headline-lg text-headline-lg text-deep-navy">
              Avaliações de Cartas
            </h1>
          </div>
          <p className="font-body-sm text-body-sm text-text-muted">
            Consulte médias comunitárias e registre avaliações com selo obrigatório de{' '}
            <strong>Compra Verificada</strong>
          </p>
        </div>

        <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
          <div className="relative w-full sm:w-72">
            <Icon
              nome="search"
              className="pointer-events-none absolute top-2.5 left-3 text-[18px] text-secondary"
            />
            <label htmlFor="busca-cartas-avaliacao" className="sr-only">
              Buscar carta por nome, código ou coleção
            </label>
            <input
              id="busca-cartas-avaliacao"
              type="search"
              value={termo}
              onChange={(evento) => definirTermo(evento.target.value)}
              placeholder="Buscar carta por nome ou código..."
              className="w-full rounded-lg bg-surface-container-low py-2 pr-3 pl-9 font-body-sm text-body-sm text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>

          <div className="flex flex-wrap gap-1.5">
            {filtros.map((f) => {
              const ativo = filtro === f.chave;
              return (
                <button
                  key={f.chave}
                  type="button"
                  onClick={() => definirFiltro(f.chave)}
                  className={`rounded-lg px-3 py-1.5 font-label-sm text-label-sm transition-colors ${
                    ativo
                      ? 'bg-deep-navy font-semibold text-pure-white shadow-sm'
                      : 'bg-surface-container-low text-secondary hover:bg-sky-tint hover:text-deep-navy'
                  }`}
                >
                  {f.rotulo}
                </button>
              );
            })}
          </div>
        </div>
      </section>

      {cartasFiltradas.length === 0 ? (
        <div className="rounded-xl border border-dashed border-steel-border bg-pure-white p-space-xl text-center font-body-md text-body-md text-text-muted">
          Nenhuma carta encontrada para os filtros selecionados.
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-space-md sm:grid-cols-2 lg:grid-cols-3">
          {cartasFiltradas.map((carta) => {
            const exibirImagem = Boolean(carta.card_image) && !imagensComFalha[carta.produtoId];
            const temMinha = Boolean(carta.minhaAvaliacao && carta.minhaAvaliacao.valida);

            return (
              <article
                key={carta.produtoId}
                className="flex flex-col justify-between overflow-hidden rounded-2xl border border-ice-border/60 bg-pure-white p-space-md shadow-sm transition-shadow hover:shadow-md"
              >
                <div className="flex gap-space-md">
                  <div className="flex aspect-[2.5/3.5] w-24 flex-shrink-0 items-center justify-center overflow-hidden rounded-lg bg-deep-navy text-center font-label-sm text-[10px] font-bold text-pure-white">
                    {exibirImagem ? (
                      <img
                        src={carta.card_image!}
                        alt={carta.card_name}
                        onError={() =>
                          definirImagensComFalha((atual) => ({
                            ...atual,
                            [carta.produtoId]: true,
                          }))
                        }
                        className="h-full w-full object-cover"
                      />
                    ) : (
                      <span>{carta.card_set_id}</span>
                    )}
                  </div>

                  <div className="min-w-0 flex-1 space-y-1.5">
                    <div className="flex flex-wrap items-center gap-1.5">
                      <span className="rounded bg-deep-navy px-2 py-0.5 font-label-sm text-[10px] font-bold text-pure-white uppercase">
                        {carta.rarity}
                      </span>
                      <span className="font-mono text-xs font-semibold text-ocean-blue">
                        {carta.card_set_id}
                      </span>
                    </div>

                    <h2 className="truncate font-headline-sm text-headline-sm text-deep-navy">
                      {carta.card_name}
                    </h2>

                    <p className="truncate font-body-sm text-xs text-text-muted">
                      {carta.set_name ?? carta.set_id} • {carta.card_color}
                    </p>

                    <div className="flex items-center gap-1.5 pt-0.5">
                      <StarRating nota={carta.media} somenteLeitura tamanho="sm" />
                      <span className="font-tabular-stat text-tabular-stat text-deep-navy">
                        {carta.media.toFixed(1)}
                      </span>
                      <span className="font-body-sm text-xs text-text-muted">
                        ({carta.quantidadeValida === 0 ? 'Sem avaliações' : `${carta.quantidadeValida}`})
                      </span>
                    </div>

                    <p className="font-tabular-price text-sm text-deep-navy">
                      {formatarPreco(carta.market_price)}
                    </p>

                    <div className="flex flex-wrap gap-1.5 pt-1">
                      {temMinha && (
                        <span className="rounded-full bg-sky-tint px-2 py-0.5 font-label-sm text-[11px] font-semibold text-ocean-blue">
                          Sua: {carta.minhaAvaliacao!.nota}.0 ★
                        </span>
                      )}
                      {carta.podeAvaliar && (
                        <span className="rounded-full bg-status-positive-bg px-2 py-0.5 font-label-sm text-[11px] font-semibold text-status-positive-text">
                          ✓ Compra Verificada
                        </span>
                      )}
                    </div>
                  </div>
                </div>

                <div className="mt-4 flex items-center gap-2 border-t border-ice-border pt-3">
                  <Link
                    to={`/comunidade/avaliacoes/${encodeURIComponent(carta.produtoId)}`}
                    className="flex-1 rounded-lg bg-surface-container-low py-2 text-center font-label-sm text-label-sm font-semibold text-ocean-blue no-underline transition-colors hover:bg-sky-tint"
                  >
                    Ver Avaliações
                  </Link>
                  {carta.podeAvaliar && (
                    <button
                      type="button"
                      onClick={() => abrirModal(carta.produtoId)}
                      className="rounded-lg bg-ocean-blue px-3.5 py-2 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
                    >
                      {temMinha ? 'Editar Minha Avaliação' : 'Avaliar'}
                    </button>
                  )}
                </div>
              </article>
            );
          })}
        </div>
      )}

      {cartaSelecionada && (
        <ReviewFormModal
          carta={cartaSelecionada}
          mediaComunidade={cartaSelecionada.media}
          quantidadeValida={cartaSelecionada.quantidadeValida}
          podeAvaliar={cartaSelecionada.podeAvaliar}
          motivoInelegibilidade={cartaSelecionada.motivoInelegibilidade}
          minhaAvaliacao={cartaSelecionada.minhaAvaliacao}
        />
      )}
    </div>
  );
}
