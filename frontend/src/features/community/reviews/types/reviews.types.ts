export interface CartaAvaliacaoBaseDto {
  produtoId: string;
  card_set_id: string;
  card_name: string;
  set_id: string;
  set_name: string | null;
  rarity: string;
  card_color: string;
  card_type: string | null;
  card_cost: string | null;
  card_power: string | null;
  market_price: number;
  inventory_price: number | null;
  card_image_id: string;
  card_image: string | null;
}

export interface AvaliacaoItemDto {
  id: string;
  usuario: string;
  apelido: string;
  produtoId: string;
  nota: number;
  comentario: string | null;
  compraVerificada: boolean;
  selo: string;
  valida: boolean;
  avaliadoEm: string;
}

export interface CompraVerificadaItemDto {
  id: string;
  pedidoId: string;
  produtoId: string;
  paga: boolean;
  reembolsada: boolean;
  elegivel: boolean;
  atualizadoEm: string;
}

export interface ResumoAvaliacaoDto {
  produtoId: string;
  media: number;
  quantidadeValida: number;
  distribuicao: Record<string, number>;
}

export interface CartaCatalogoAvaliacaoDto extends CartaAvaliacaoBaseDto {
  media: number;
  quantidadeValida: number;
  distribuicao: Record<string, number>;
  podeAvaliar: boolean;
  motivoInelegibilidade: string | null;
  minhaAvaliacao: AvaliacaoItemDto | null;
}

export interface DetalheAvaliacoesCartaDto {
  carta: CartaAvaliacaoBaseDto;
  resumo: ResumoAvaliacaoDto;
  podeAvaliar: boolean;
  motivoInelegibilidade: string | null;
  minhaAvaliacao: AvaliacaoItemDto | null;
  comprasDoUsuario: CompraVerificadaItemDto[];
  avaliacoes: AvaliacaoItemDto[];
}

export interface RegistrarAvaliacaoEntrada {
  nota: number;
  comentario?: string | null;
}

export interface RegistrarCompraEntrada {
  pedidoId: string;
  produtoId: string;
  paga: boolean;
}
