export type StatusAmizade = 'PENDENTE' | 'ACEITA';
export type StatusRelacaoBusca =
  | 'NENHUMA'
  | 'PENDENTE_ENVIADA'
  | 'PENDENTE_RECEBIDA'
  | 'AMIGO';

export interface ItemRedeAmizadeDto {
  amizadeId: string;
  perfilId: string;
  usuario: string;
  apelido: string;
  cidade: string | null;
  foto: string | null;
  status: StatusAmizade;
  criadoEm: string;
  atualizadoEm: string | null;
}

export interface RedeAmizadesDto {
  amigos: ItemRedeAmizadeDto[];
  recebidas: ItemRedeAmizadeDto[];
  enviadas: ItemRedeAmizadeDto[];
}

export interface ColecionadorBuscaDto {
  perfilId: string;
  usuario: string;
  apelido: string;
  cidade: string | null;
  foto: string | null;
  statusRelacao: StatusRelacaoBusca;
  amizadeId: string | null;
}

export interface AmizadeDetalheDto {
  amizadeId: string;
  solicitante: string;
  solicitanteApelido: string;
  destinatario: string;
  destinatarioApelido: string;
  status: StatusAmizade;
  ehSolicitante: boolean;
  ehDestinatario: boolean;
  criadoEm: string;
  atualizadoEm: string | null;
}
