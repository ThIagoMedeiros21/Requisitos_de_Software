export interface MinhaAberturaPreferidaDto {
  id: string;
  aberturaId: string;
  numero: number;
  nome: string;
  artista: string;
  episodios: string;
  imagem: string;
  video: string;
  ativa: boolean;
  visivelNoPerfil: boolean;
  escolhidaEm: string;
}

export interface AberturaAtivaCatalogoDto {
  id: string;
  numero: number;
  nome: string;
  artista: string;
  episodios: string;
  imagem: string;
  video: string;
  ativa: boolean;
  quantidadeAmigos: number;
  ehMinhaPreferida: boolean;
  posicaoRanking: number | null;
}

export interface ItemRankingAberturaDto {
  posicao: number;
  aberturaId: string;
  numero: number;
  nome: string;
  artista: string;
  imagem: string;
  quantidadeEscolhas: number;
  percentual: number;
}

export interface RankingAberturasDto {
  totalEscolhas: number;
  itens: ItemRankingAberturaDto[];
}

export interface PesquisaAberturasComunidadeDto {
  minhaPreferida: MinhaAberturaPreferidaDto | null;
  aberturasAtivas: AberturaAtivaCatalogoDto[];
  ranking: RankingAberturasDto;
}

export interface AberturaPerfilResumoDto {
  aberturaId: string;
  numero: number;
  nome: string;
  artista: string;
  episodios: string;
  imagem: string;
  video: string;
  ativa: boolean;
  escolhidaEm: string;
}

export interface AberturaPerfilVisaoDto {
  perfilId: string;
  ehProprietario: boolean;
  possuiPreferidaVisivel: boolean;
  visivelNoPerfil: boolean;
  mesmaPreferenciaDoVisitante: boolean;
  abertura: AberturaPerfilResumoDto | null;
}

export interface AmigoMesmaAberturaDto {
  usuario: string;
  apelido: string;
  perfilId: string;
}

export interface AberturaDetalhesComunidadeDto {
  id: string;
  numero: number;
  nome: string;
  artista: string;
  episodios: string;
  imagem: string;
  video: string;
  ativa: boolean;
  ehMinhaPreferida: boolean;
  visivelNoPerfil: boolean;
  posicaoRanking: number | null;
  totalEscolhasComunidade: number;
  quantidadeAmigos: number;
  amigosComMesmaPreferencia: AmigoMesmaAberturaDto[];
}
