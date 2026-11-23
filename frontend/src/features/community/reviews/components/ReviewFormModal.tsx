import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Icon } from '../../../../components/ui/Icon';
import { useReviewsUiStore } from '../../../../stores/reviewsUiStore';
import { useSalvarAvaliacao } from '../api/reviewsQueries';
import type { AvaliacaoItemDto, CartaAvaliacaoBaseDto } from '../types/reviews.types';
import { StarRating } from './StarRating';

interface FormularioAvaliacao {
  nota: number;
  comentario: string;
}

interface ReviewFormModalProps {
  carta: CartaAvaliacaoBaseDto;
  mediaComunidade: number;
  quantidadeValida: number;
  podeAvaliar: boolean;
  motivoInelegibilidade: string | null;
  minhaAvaliacao: AvaliacaoItemDto | null;
}

export function ReviewFormModal({
  carta,
  mediaComunidade,
  quantidadeValida,
  podeAvaliar,
  motivoInelegibilidade,
  minhaAvaliacao,
}: ReviewFormModalProps) {
  const modalAberto = useReviewsUiStore((estado) => estado.modalAvaliacaoAberto);
  const produtoModalAberto = useReviewsUiStore((estado) => estado.produtoModalAberto);
  const fecharModal = useReviewsUiStore((estado) => estado.fecharModalAvaliacao);
  const [erroNota, definirErroNota] = useState<string | null>(null);

  const salvar = useSalvarAvaliacao(carta.produtoId);
  const abertoParaEstaCarta = modalAberto && produtoModalAberto === carta.produtoId;
  const ehEdicao = Boolean(minhaAvaliacao && minhaAvaliacao.valida);

  const { register, handleSubmit, reset, setValue, watch } = useForm<FormularioAvaliacao>({
    defaultValues: {
      nota: minhaAvaliacao?.nota ?? 0,
      comentario: minhaAvaliacao?.comentario ?? '',
    },
  });

  useEffect(() => {
    if (abertoParaEstaCarta) {
      reset({
        nota: minhaAvaliacao?.nota ?? 0,
        comentario: minhaAvaliacao?.comentario ?? '',
      });
      definirErroNota(null);
    }
  }, [abertoParaEstaCarta, minhaAvaliacao, reset]);

  if (!abertoParaEstaCarta) {
    return null;
  }

  const notaAtual = watch('nota');

  function cancelar() {
    reset({
      nota: minhaAvaliacao?.nota ?? 0,
      comentario: minhaAvaliacao?.comentario ?? '',
    });
    definirErroNota(null);
    fecharModal();
  }

  async function aoSubmeter(dados: FormularioAvaliacao) {
    if (dados.nota < 1 || dados.nota > 5) {
      definirErroNota('Selecione uma nota entre 1 e 5 estrelas.');
      return;
    }
    definirErroNota(null);
    try {
      await salvar.mutateAsync({
        nota: dados.nota,
        comentario: dados.comentario.trim() || null,
      });
      fecharModal();
    } catch {
      // Erro publicado pelo hook via toast.
    }
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-modal-avaliacao"
      className="fixed inset-0 z-50 flex items-center justify-center overflow-y-auto bg-deep-navy/60 p-4 backdrop-blur-sm"
    >
      <div className="my-auto w-full max-w-xl overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-level-4">
        <div className="flex items-start justify-between gap-2 border-b border-ice-border px-space-lg py-space-md">
          <div>
            <div className="flex items-center gap-2">
              <h2
                id="titulo-modal-avaliacao"
                className="font-headline-md text-headline-md text-deep-navy"
              >
                {ehEdicao ? 'Sua Avaliação da Carta' : 'Avaliar Carta'}
              </h2>
              {carta.card_type && (
                <span className="rounded bg-deep-navy px-2 py-0.5 font-label-sm text-[11px] font-bold text-pure-white uppercase">
                  {carta.card_type}
                </span>
              )}
            </div>
            <p className="mt-0.5 font-body-sm text-body-sm text-text-muted">
              {carta.card_set_id} • {carta.card_name} ({carta.card_color})
            </p>
          </div>
          <button
            type="button"
            aria-label="Fechar avaliação"
            onClick={cancelar}
            className="rounded-lg p-1.5 text-secondary transition-colors hover:bg-surface-container-low hover:text-deep-navy"
          >
            <Icon nome="close" className="text-[20px]" />
          </button>
        </div>

        <form onSubmit={handleSubmit(aoSubmeter)} noValidate className="space-y-space-md p-space-lg">
          <div className="flex items-center justify-between gap-3 rounded-xl bg-surface-container-low p-3.5">
            <div>
              <span className="block font-headline-sm text-sm text-deep-navy">
                {carta.card_name}
              </span>
              <span className="block font-body-sm text-xs text-text-muted">
                {carta.set_name ?? carta.set_id} • {carta.rarity}
              </span>
            </div>
            <div className="text-right">
              <span className="block font-tabular-stat text-sm text-ocean-blue">
                ★ {mediaComunidade.toFixed(1)}
              </span>
              <span className="block font-body-sm text-[11px] text-secondary">
                {quantidadeValida} {quantidadeValida === 1 ? 'avaliação válida' : 'avaliações válidas'}
              </span>
            </div>
          </div>

          {!podeAvaliar && (
            <div
              role="alert"
              className="flex items-start gap-2 rounded-xl bg-error-container p-3 font-body-sm text-body-sm text-on-error-container"
            >
              <Icon nome="block" className="text-[20px] text-error" />
              <span>
                {motivoInelegibilidade ??
                  'Apenas compradores com pedido pago e não reembolsado podem avaliar esta carta.'}
              </span>
            </div>
          )}

          {ehEdicao && (
            <div className="flex items-center justify-between rounded-xl bg-status-positive-bg px-3.5 py-2 font-label-sm text-label-sm text-status-positive-text">
              <span className="inline-flex items-center gap-1 font-semibold">
                <Icon nome="verified" preenchido className="text-[16px]" />
                <span>✓ Compra Verificada • Avaliação Enviada</span>
              </span>
              <span className="font-bold">{minhaAvaliacao!.nota}.0 ★</span>
            </div>
          )}

          <div className="space-y-2">
            <span className="block font-label-md text-label-md font-semibold text-deep-navy">
              Sua Avaliação *
            </span>
            <StarRating
              nota={notaAtual}
              tamanho="lg"
              mostrarLegenda
              aoSelecionar={(novaNota) => {
                setValue('nota', novaNota, { shouldDirty: true });
                definirErroNota(null);
              }}
            />
            {erroNota && (
              <p role="alert" className="font-body-sm text-body-sm font-semibold text-error">
                {erroNota}
              </p>
            )}
          </div>

          <div>
            <label
              htmlFor="avaliacao-comentario"
              className="block font-label-md text-label-md font-semibold text-deep-navy"
            >
              Seu Comentário (opcional)
            </label>
            <textarea
              id="avaliacao-comentario"
              rows={3}
              maxLength={1000}
              placeholder="Compartilhe sua impressão sobre jogabilidade, arte ou custo-benefício..."
              {...register('comentario')}
              className="mt-1 w-full rounded-lg bg-surface-container-low px-3 py-2 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>

          <div className="flex justify-end gap-space-sm border-t border-ice-border pt-space-md">
            <button
              type="button"
              onClick={cancelar}
              className="rounded-lg bg-surface-container-low px-4 py-2.5 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={!podeAvaliar || salvar.isPending}
              className="rounded-lg bg-ocean-blue px-5 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:cursor-not-allowed disabled:opacity-50"
            >
              {ehEdicao ? 'Atualizar Avaliação' : 'Salvar Avaliação'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
