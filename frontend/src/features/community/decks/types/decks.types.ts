export interface DeckFavoritoResumoDto {
  id: string;
  deckId: string;
  ordem: number;
  adicionadoEm: string;
  nomeDeck: string;
  lider: string;
  liderCodigo: string;
  liderImagem: string | null;
  cor: string;
  ativo: boolean;
  totalCartasMainDeck: number;
  custoEstimadoMercado: number;
}

export interface ListaDecksFavoritosDto {
  perfilId?: string;
  ehProprietario?: boolean;
  total: number;
  limite: number;
  decksFavoritosVisiveis: boolean;
  decks: DeckFavoritoResumoDto[];
}

export interface DeckCatalogoDto {
  deckId: string;
  nomeDeck: string;
  lider: string;
  liderCodigo: string;
  liderImagem: string | null;
  cor: string;
  ativo: boolean;
  totalCartasMainDeck: number;
  custoEstimadoMercado: number;
  jaFavoritado: boolean;
}

export interface ItemComposicaoDeckDto {
  cartaId?: string;
  card_set_id: string;
  card_name: string;
  set_id: string;
  set_name: string | null;
  rarity: string;
  card_color: string;
  card_type: string;
  card_cost: string | null;
  card_power: string | null;
  sub_types: string | null;
  counter_amount: string | number | null;
  attribute: string | null;
  market_price: number;
  inventory_price: number | null;
  card_image_id: string;
  card_image: string | null;
  quantidade: number;
  subtotal: number;
}

export interface ComposicaoDeckDto {
  deckId: string;
  nomeDeck: string;
  lider: string;
  liderCodigo?: string;
  liderImagem?: string | null;
  cor: string;
  ativo: boolean;
  totalCartasMainDeck: number;
  totalCartasComLider?: number;
  conformidadeBandai: boolean;
  custoEstimadoMercado: number;
  totaisPorTipo: Record<string, number>;
  decklistBandai?: string | null;
  itens: ItemComposicaoDeckDto[];
}
