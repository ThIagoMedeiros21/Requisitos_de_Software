import type { ComposicaoDeckDto, ItemComposicaoDeckDto } from '../types/decks.types';

const PRIORIDADE_TIPO: Record<string, number> = {
  Leader: 0,
  Character: 1,
  Event: 2,
  Stage: 3,
};

function ordemTipo(tipo: string): number {
  return PRIORIDADE_TIPO[tipo] ?? 4;
}

function custoNumerico(item: ItemComposicaoDeckDto): number {
  if (!item.card_cost) {
    return 999;
  }
  const parsed = Number.parseInt(item.card_cost, 10);
  return Number.isNaN(parsed) ? 999 : parsed;
}

/**
 * Formata a composição de um deck no formato oficial Bandai TCG+ (`1xST01-001`, `4xOP01-016`...),
 * sempre iniciando pelo Líder e ordenando as demais entradas por tipo, custo e código.
 */
export function formatarDecklistBandai(composicao: ComposicaoDeckDto): string {
  if (composicao.decklistBandai?.trim()) {
    return composicao.decklistBandai.trim();
  }

  const ordenados = [...composicao.itens].sort((a, b) => {
    const diffTipo = ordemTipo(a.card_type) - ordemTipo(b.card_type);
    if (diffTipo !== 0) {
      return diffTipo;
    }
    const diffCusto = custoNumerico(a) - custoNumerico(b);
    if (diffCusto !== 0) {
      return diffCusto;
    }
    return a.card_set_id.localeCompare(b.card_set_id);
  });

  return ordenados.map((item) => `${item.quantidade}x${item.card_set_id}`).join('\n');
}
