import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router';
import { CommunitySubNav } from '../../../../components/layout/CommunitySubNav';
import { Icon } from '../../../../components/ui/Icon';
import { useReviewsUiStore } from '../../../../stores/reviewsUiStore';
import { useProfileUiStore } from '../../profile/stores/useProfileUiStore';
import {
  consultaDetalhesAvaliacoesCarta,
  useReembolsarCompraVerificada,
  useRegistrarCompraVerificada,
} from '../api/reviewsQueries';
import { RatingDistributionBars } from '../components/RatingDistributionBars';
import { ReviewFormModal } from '../components/ReviewFormModal';
import { StarRating } from '../components/StarRating';

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function formatarData(iso: string): string {
  const data = new Date(iso);
  if (Number.isNaN(data.getTime())) {
    return iso;
  }
  return data.toLocaleDateString('pt-BR');
}

export function CardReviewDetailPage() {
  const { produtoId } = useParams();
  const { data: detalhe } = useSuspenseQuery(consultaDetalhesAvaliacoesCarta(produtoId!));

  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);
  const abrirModal = useReviewsUiStore((estado) => estado.abrirModalAvaliacao);
  const painelPedidosAberto = useReviewsUiStore((estado) => estado.painelPedidosAberto);
  const alternarPainelPedidos = useReviewsUiStore((estado) => estado.alternarPainelPedidos);
  const [imagemFalhou, definirImagemFalhou] = useState(false);

  const registrarCompra = useRegistrarCompraVerificada();
  const reembolsar = useReembolsarCompraVerificada();

  useEffect(() => {
    definirAbaAtiva('avaliacoes');
  }, [definirAbaAtiva]);

  const { carta, resumo, podeAvaliar, motivoInelegibilidade, minhaAvaliacao, comprasDoUsuario, avaliacoes } =
    detalhe;
  const temMinha = Boolean(minhaAvaliacao && minhaAvaliacao.valida);
  const exibirImagem = Boolean(carta.card_image) && !imagemFalhou;

  function registrarPedidoDemo(paga: boolean) {
    const sufixo = Math.floor(1000 + Math.random() * 9000);
    registrarCompra.mutate({
      pedidoId: `PED-${sufixo}`,
      produtoId: carta.produtoId,
      paga,
    });
  }

  return (
    <div className="mx-auto max-w-7xl space-y-space-xl px-margin py-space-lg md:px-margin-lg">
      <CommunitySubNav abaAtiva="avaliacoes" />

      <div>
        <Link
          to="/comunidade/avaliacoes"
          className="inline-flex items-center gap-1 font-label-sm text-label-sm font-semibold text-ocean-blue no-underline hover:text-deep-navy"
        >
          <Icon nome="arrow_back" className="text-[16px]" />
          <span>Voltar para Avaliações de Cartas</span>
        </Link>
      </div>

      {/* Ficha da Carta (P11) */}
      <section className="flex flex-col gap-space-lg rounded-2xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm md:flex-row md:items-center">
        <div className="flex aspect-[2.5/3.5] w-40 flex-shrink-0 items-center justify-center overflow-hidden rounded-xl bg-deep-navy text-center font-headline-sm text-sm text-pure-white shadow-sm">
          {exibirImagem ? (
            <img
              src={carta.card_image!}
              alt={carta.card_name}
              onError={() => definirImagemFalhou(true)}
              className="h-full w-full object-cover"
            />
          ) : (
            <span>{carta.card_set_id}</span>
          )}
        </div>

        <div className="flex-1 space-y-3">
          <div className="flex flex-wrap items-center gap-2">
            <span className="rounded bg-deep-navy px-2.5 py-0.5 font-label-sm text-xs font-bold text-pure-white uppercase">
              {carta.rarity}
            </span>
            <span className="rounded bg-sky-tint px-2.5 py-0.5 font-mono text-xs font-bold text-ocean-blue">
              {carta.card_set_id}
            </span>
            <span className="rounded bg-surface-container px-2.5 py-0.5 font-label-sm text-xs text-secondary">
              {carta.card_color}
              {carta.card_type ? ` • ${carta.card_type}` : ''}
            </span>
          </div>

          <h1 className="font-headline-xl text-headline-lg text-deep-navy">{carta.card_name}</h1>

          <p className="font-body-md text-body-md text-text-muted">
            Coleção: <strong className="text-deep-navy">{carta.set_name ?? carta.set_id}</strong>
            {carta.card_cost ? ` • Custo ${carta.card_cost}` : ''}
            {carta.card_power ? ` • Poder ${carta.card_power}` : ''}
          </p>

          <p className="font-tabular-price text-xl text-deep-navy">
            {formatarPreco(carta.market_price)}{' '}
            <span className="font-body-sm text-xs font-normal text-secondary">• Preço de Mercado</span>
          </p>
        </div>
      </section>

      {/* Avaliações da Comunidade (P12) */}
      <section className="space-y-space-lg rounded-2xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
        <div className="flex flex-col justify-between gap-space-md sm:flex-row sm:items-center">
          <div>
            <h2 className="font-headline-lg text-headline-lg text-deep-navy">
              Avaliações da Comunidade
            </h2>
            <p className="font-body-sm text-body-sm text-text-muted">
              Opiniões verificadas de colecionadores com compra confirmada no TCGHub
            </p>
          </div>

          <button
            type="button"
            onClick={() => abrirModal(carta.produtoId)}
            className="inline-flex items-center gap-1.5 rounded-xl bg-ocean-blue px-5 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy"
          >
            <Icon nome="star" preenchido className="text-[18px]" />
            <span>{temMinha ? 'Editar Minha Avaliação' : 'Avaliar Carta'}</span>
          </button>
        </div>

        {!podeAvaliar && motivoInelegibilidade && (
          <div className="flex items-start gap-2 rounded-xl bg-status-warning-bg p-3 font-body-sm text-body-sm text-status-warning-text">
            <Icon nome="info" className="text-[18px]" />
            <span>{motivoInelegibilidade}</span>
          </div>
        )}

        <div className="grid grid-cols-1 gap-space-lg rounded-xl bg-surface-container-low/60 p-space-lg md:grid-cols-12">
          <div className="flex flex-col items-center justify-center space-y-2 text-center md:col-span-4">
            <span className="font-display text-5xl font-extrabold text-deep-navy">
              {resumo.media.toFixed(1)}
            </span>
            <StarRating nota={resumo.media} somenteLeitura tamanho="md" />
            <p className="font-body-sm text-body-sm text-text-muted">
              Baseado em{' '}
              <strong className="text-deep-navy">
                {resumo.quantidadeValida}{' '}
                {resumo.quantidadeValida === 1 ? 'avaliação válida' : 'avaliações válidas'}
              </strong>
            </p>
            {temMinha && (
              <span className="rounded-full bg-sky-tint px-3 py-0.5 font-label-sm text-xs font-semibold text-ocean-blue">
                Sua nota: {minhaAvaliacao!.nota}.0 ★
              </span>
            )}
          </div>

          <div className="flex flex-col justify-center md:col-span-8">
            <RatingDistributionBars
              distribuicao={resumo.distribuicao}
              totalValido={resumo.quantidadeValida}
            />
          </div>
        </div>

        {avaliacoes.length === 0 ? (
          <div className="rounded-xl border border-dashed border-steel-border bg-surface-container-low/40 p-space-xl text-center font-body-md text-body-md text-text-muted">
            Esta carta ainda não possui avaliações da comunidade.
          </div>
        ) : (
          <ul className="m-0 list-none space-y-3 p-0">
            {avaliacoes.map((item) => (
              <li
                key={item.id}
                className="space-y-2 rounded-xl border border-ice-border bg-surface-container-lowest p-4"
              >
                <div className="flex flex-wrap items-center justify-between gap-2">
                  <div className="flex flex-wrap items-center gap-2">
                    <span className="font-headline-sm text-sm text-deep-navy">
                      @{item.apelido}
                    </span>
                    {item.compraVerificada && (
                      <span className="inline-flex items-center gap-1 rounded-full bg-status-positive-bg px-2.5 py-0.5 font-label-sm text-[11px] font-semibold text-status-positive-text">
                        <Icon nome="verified" preenchido className="text-[14px]" />
                        <span>✓ Compra Verificada</span>
                      </span>
                    )}
                  </div>
                  <span className="font-body-sm text-xs text-text-muted">
                    {formatarData(item.avaliadoEm)}
                  </span>
                </div>

                <div className="flex items-center gap-2">
                  <StarRating nota={item.nota} somenteLeitura tamanho="sm" />
                  <span className="font-tabular-stat text-xs text-ocean-blue">
                    {item.nota}.0 / 5.0
                  </span>
                </div>

                {item.comentario && (
                  <p className="font-body-md text-body-md text-on-surface">{item.comentario}</p>
                )}
              </li>
            ))}
          </ul>
        )}
      </section>

      {/* Painel Colapsável Demonstrativo de Pedidos (CompraVerificada — RN001 / RN005) */}
      <section className="overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-sm">
        <button
          type="button"
          onClick={alternarPainelPedidos}
          aria-expanded={painelPedidosAberto}
          className="flex w-full items-center justify-between px-space-lg py-4 text-left transition-colors hover:bg-surface-container-low/50"
        >
          <div className="flex items-center gap-2">
            <Icon nome="receipt_long" className="text-[20px] text-ocean-blue" />
            <span className="font-headline-sm text-headline-sm text-deep-navy">
              Meus Pedidos desta Carta (CompraVerificada — RN001 / RN005)
            </span>
          </div>
          <Icon
            nome={painelPedidosAberto ? 'expand_less' : 'expand_more'}
            className="text-[22px] text-secondary"
          />
        </button>

        {painelPedidosAberto && (
          <div className="space-y-4 border-t border-ice-border p-space-lg">
            <div className="flex flex-wrap items-center justify-between gap-2">
              <p className="font-body-sm text-body-sm text-text-muted">
                Gerencie pedidos demonstrativos na projeção <code>CompraVerificada</code> para testar
                elegibilidade (RN001) e invalidação síncrona após reembolso (RN005).
              </p>
              <div className="flex flex-wrap gap-2">
                <button
                  type="button"
                  disabled={registrarCompra.isPending}
                  onClick={() => registrarPedidoDemo(true)}
                  className="rounded-lg bg-ocean-blue px-3.5 py-1.5 font-label-sm text-label-sm font-semibold text-pure-white hover:bg-deep-navy disabled:opacity-50"
                >
                  + Registrar Compra Paga
                </button>
                <button
                  type="button"
                  disabled={registrarCompra.isPending}
                  onClick={() => registrarPedidoDemo(false)}
                  className="rounded-lg bg-surface-container px-3.5 py-1.5 font-label-sm text-label-sm font-semibold text-secondary hover:bg-surface-container-high disabled:opacity-50"
                >
                  + Registrar Pedido Pendente
                </button>
              </div>
            </div>

            {comprasDoUsuario.length === 0 ? (
              <p className="rounded-xl bg-surface-container-low p-3 font-body-sm text-body-sm text-text-muted">
                Você ainda não possui pedidos registrados para esta carta.
              </p>
            ) : (
              <ul className="m-0 list-none space-y-2 p-0">
                {comprasDoUsuario.map((compra) => (
                  <li
                    key={compra.id}
                    className="flex flex-wrap items-center justify-between gap-2 rounded-xl border border-ice-border bg-surface-container-low/40 p-3"
                  >
                    <div className="flex flex-wrap items-center gap-2">
                      <span className="font-mono text-xs font-bold text-deep-navy">
                        {compra.pedidoId}
                      </span>
                      <span
                        className={`rounded-full px-2.5 py-0.5 font-label-sm text-[11px] font-semibold ${
                          compra.elegivel
                            ? 'bg-status-positive-bg text-status-positive-text'
                            : compra.reembolsada
                              ? 'bg-error-container text-on-error-container'
                              : 'bg-status-warning-bg text-status-warning-text'
                        }`}
                      >
                        {compra.elegivel
                          ? 'Pago • Elegível'
                          : compra.reembolsada
                            ? 'Reembolsado'
                            : 'Pagamento Pendente'}
                      </span>
                    </div>

                    {compra.elegivel && (
                      <button
                        type="button"
                        disabled={reembolsar.isPending}
                        onClick={() =>
                          reembolsar.mutate({
                            pedidoId: compra.pedidoId,
                            produtoId: compra.produtoId,
                          })
                        }
                        className="rounded-lg border border-error/40 bg-pure-white px-3 py-1 font-label-sm text-label-sm font-semibold text-error transition-colors hover:bg-error-container/30 disabled:opacity-50"
                      >
                        Simular Reembolso
                      </button>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}
      </section>

      <ReviewFormModal
        carta={carta}
        mediaComunidade={resumo.media}
        quantidadeValida={resumo.quantidadeValida}
        podeAvaliar={podeAvaliar}
        motivoInelegibilidade={motivoInelegibilidade}
        minhaAvaliacao={minhaAvaliacao}
      />
    </div>
  );
}
