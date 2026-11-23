export function RatingDistributionBars({
  distribuicao,
  totalValido,
}: {
  distribuicao: Record<string, number>;
  totalValido: number;
}) {
  const niveis = [5, 4, 3, 2, 1];

  return (
    <div className="space-y-2">
      {niveis.map((estrela) => {
        const contagem = distribuicao[String(estrela)] ?? 0;
        const percentual = totalValido > 0 ? Math.round((contagem / totalValido) * 100) : 0;

        return (
          <div key={estrela} className="flex items-center gap-3">
            <span className="w-10 flex-shrink-0 font-tabular-stat text-tabular-stat text-deep-navy">
              {estrela} ★
            </span>
            <div className="h-2.5 flex-1 overflow-hidden rounded-full bg-surface-container">
              <div
                className="h-full rounded-full bg-ocean-blue transition-all"
                style={{ width: `${percentual}%` }}
              />
            </div>
            <span className="w-8 flex-shrink-0 text-right font-tabular-stat text-tabular-stat text-secondary">
              {contagem}
            </span>
          </div>
        );
      })}
    </div>
  );
}
