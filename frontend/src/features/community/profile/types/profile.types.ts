export type StatusRelacaoPerfil =
  | 'PROPRIO'
  | 'NENHUMA'
  | 'PENDENTE_ENVIADA'
  | 'PENDENTE_RECEBIDA'
  | 'AMIGO';

/**
 * Contrato de carta favorito/coleção alinhado simultaneamente ao BFF do TCGHub e à API oficial
 * One Piece TCG (`https://optcgapi.com/api/sets/card/{card_set_id}/`).
 */
export interface CartaDestaqueDto {
  cartaId: string;
  codigo: string;
  nome: string;
  raridade: string;
  edicaoCondicao: string;
  cor: string;
  precoMercado: number;
  imagem: string | null;
  // Campos oficiais do contrato https://optcgapi.com
  card_set_id?: string;
  card_name?: string;
  set_id?: string;
  set_name?: string | null;
  rarity?: string;
  card_color?: string;
  card_type?: string | null;
  card_cost?: string | null;
  card_power?: string | null;
  sub_types?: string | null;
  counter_amount?: number | null;
  attribute?: string | null;
  market_price?: number;
  inventory_price?: number | null;
  card_image_id?: string;
  card_image?: string | null;
}

export interface CartaColecaoDisponivelDto extends CartaDestaqueDto {
  jaDestacada: boolean;
}

export interface PerfilComunitarioVisaoDto {
  id: string;
  usuario: string;
  apelido: string;
  cidade: string | null;
  descricao: string | null;
  foto: string | null;
  preferenciasPublicas: string | null;
  decksFavoritosVisiveis: boolean;
  ehProprietario: boolean;
  statusRelacao: StatusRelacaoPerfil;
  amizadeId: string | null;
  cartasFavoritas: CartaDestaqueDto[];
  cartasColecaoDisponiveis: CartaColecaoDisponivelDto[];
}

export interface PerfilComunitarioAlteracaoDto {
  id: string;
  usuario: string;
  apelido: string;
  cidade: string | null;
  descricao: string | null;
  foto: string | null;
  preferenciasPublicas: string | null;
  decksFavoritosVisiveis: boolean;
}

export interface AlterarPerfilEntrada {
  apelido: string;
  cidade: string | null;
  descricao: string | null;
  foto: string | null;
  preferenciasPublicas: string | null;
  decksFavoritosVisiveis: boolean;
}
