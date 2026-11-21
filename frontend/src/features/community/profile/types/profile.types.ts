export type StatusRelacaoPerfil =
  | 'PROPRIO'
  | 'NENHUMA'
  | 'PENDENTE_ENVIADA'
  | 'PENDENTE_RECEBIDA'
  | 'AMIGO';

export interface CartaDestaqueDto {
  cartaId: string;
  codigo: string;
  nome: string;
  raridade: string;
  edicaoCondicao: string;
  cor: string;
  precoMercado: number;
  imagem: string | null;
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
