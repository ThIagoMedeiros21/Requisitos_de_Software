import { Icon } from '../../../../components/ui/Icon';
import { useReviewsUiStore } from '../../../../stores/reviewsUiStore';

const LEGENDAS_NOTA: Record<number, string> = {
  1: 'Fraca - 1.0 estrela',
  2: 'Regular - 2.0 estrelas',
  3: 'Boa - 3.0 estrelas',
  4: 'Muito boa - 4.0 estrelas',
  5: 'Excelente - 5.0 estrelas',
};

interface StarRatingProps {
  nota: number;
  somenteLeitura?: boolean;
  aoSelecionar?: (nota: number) => void;
  tamanho?: 'sm' | 'md' | 'lg';
  mostrarLegenda?: boolean;
}

export function StarRating({
  nota,
  somenteLeitura = false,
  aoSelecionar,
  tamanho = 'md',
  mostrarLegenda = false,
}: StarRatingProps) {
  const notaHover = useReviewsUiStore((estado) => estado.notaHover);
  const definirNotaHover = useReviewsUiStore((estado) => estado.definirNotaHover);

  const classeTamanho =
    tamanho === 'lg' ? 'text-[28px]' : tamanho === 'sm' ? 'text-[16px]' : 'text-[22px]';
  const notaExibida = !somenteLeitura && notaHover !== null ? notaHover : nota;

  if (somenteLeitura) {
    return (
      <div
        className="inline-flex items-center gap-0.5"
        aria-label={`Nota ${nota.toFixed(1)} de 5 estrelas`}
      >
        {[1, 2, 3, 4, 5].map((estrela) => {
          const cheia = estrela <= Math.round(nota);
          return (
            <Icon
              key={estrela}
              nome="star"
              preenchido={cheia}
              className={`${classeTamanho} ${cheia ? 'text-ocean-blue' : 'text-steel-border'}`}
            />
          );
        })}
      </div>
    );
  }

  return (
    <div className="space-y-1.5">
      <div
        role="group"
        aria-label="Selecionar nota de 1 a 5 estrelas"
        className="inline-flex items-center gap-1"
        onMouseLeave={() => definirNotaHover(null)}
      >
        {[1, 2, 3, 4, 5].map((estrela) => {
          const ativa = estrela <= notaExibida;
          return (
            <button
              key={estrela}
              type="button"
              aria-label={`${estrela} ${estrela === 1 ? 'estrela' : 'estrelas'}`}
              aria-pressed={estrela === nota}
              onMouseEnter={() => definirNotaHover(estrela)}
              onClick={() => aoSelecionar?.(estrela)}
              className="rounded-lg p-1 transition-transform hover:scale-110"
            >
              <Icon
                nome="star"
                preenchido={ativa}
                className={`${classeTamanho} ${
                  ativa ? 'text-ocean-blue' : 'text-steel-border'
                }`}
              />
            </button>
          );
        })}
      </div>

      {mostrarLegenda && (
        <p className="font-label-sm text-label-sm font-semibold text-ocean-blue">
          {notaExibida >= 1 && notaExibida <= 5
            ? LEGENDAS_NOTA[notaExibida]
            : 'Selecione uma nota de 1 a 5 estrelas'}
        </p>
      )}
    </div>
  );
}
