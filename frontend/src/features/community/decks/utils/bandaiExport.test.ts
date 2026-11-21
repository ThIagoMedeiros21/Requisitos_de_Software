import type { ComposicaoDeckDto } from '../types/decks.types';
import { formatarDecklistBandai } from './bandaiExport';

function composicaoExemplo(): ComposicaoDeckDto {
  return {
    deckId: 'ST01',
    nomeDeck: 'Straw Hat Crew',
    lider: 'Monkey D. Luffy (ST01-001)',
    cor: 'Vermelho',
    ativo: true,
    totalCartasMainDeck: 50,
    conformidadeBandai: true,
    custoEstimadoMercado: 890,
    totaisPorTipo: { Leader: 1, Character: 36, Event: 10, Stage: 4 },
    itens: [
      {
        card_set_id: 'OP01-016',
        card_name: 'Nami',
        set_id: 'OP-01',
        set_name: 'Romance Dawn',
        rarity: 'R',
        card_color: 'Vermelho',
        card_type: 'Character',
        card_cost: '1',
        card_power: '2000',
        sub_types: 'Straw Hat Crew',
        counter_amount: '1000',
        attribute: 'Special',
        market_price: 25,
        inventory_price: 22,
        card_image_id: 'OP01-016',
        card_image: null,
        quantidade: 4,
        subtotal: 100,
      },
      {
        card_set_id: 'ST01-014',
        card_name: 'Guard Point',
        set_id: 'ST-01',
        set_name: 'Straw Hat Crew',
        rarity: 'C',
        card_color: 'Vermelho',
        card_type: 'Event',
        card_cost: '1',
        card_power: null,
        sub_types: 'Animal/Straw Hat Crew',
        counter_amount: null,
        attribute: null,
        market_price: 10,
        inventory_price: 9,
        card_image_id: 'ST01-014',
        card_image: null,
        quantidade: 4,
        subtotal: 40,
      },
      {
        card_set_id: 'ST01-001',
        card_name: 'Monkey D. Luffy',
        set_id: 'ST-01',
        set_name: 'Straw Hat Crew',
        rarity: 'L',
        card_color: 'Vermelho',
        card_type: 'Leader',
        card_cost: null,
        card_power: '5000',
        sub_types: 'Supernovas/Straw Hat Crew',
        counter_amount: null,
        attribute: 'Strike',
        market_price: 45,
        inventory_price: 42,
        card_image_id: 'ST01-001',
        card_image: null,
        quantidade: 1,
        subtotal: 45,
      },
    ],
  };
}

describe('formatarDecklistBandai', () => {
  it('ordena iniciando pelo Líder e seguindo tipo e código no formato Bandai', () => {
    const texto = formatarDecklistBandai(composicaoExemplo());

    expect(texto).toBe(['1xST01-001', '4xOP01-016', '4xST01-014'].join('\n'));
  });

  it('prioriza decklistBandai já fornecido pelo backend quando preenchido', () => {
    const texto = formatarDecklistBandai({
      ...composicaoExemplo(),
      decklistBandai: '1xST01-001\n4xST01-012',
    });

    expect(texto).toBe('1xST01-001\n4xST01-012');
  });
});
