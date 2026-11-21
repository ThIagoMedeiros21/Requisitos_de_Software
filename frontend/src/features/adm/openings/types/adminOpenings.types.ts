export interface AberturaAdminDto {
  id: string;
  numero: number;
  nome: string;
  artista: string;
  episodios: string;
  imagem: string;
  video: string;
  ativa: boolean;
  quantidadePreferencias: number;
}

export interface ListaAberturasAdminDto {
  total: number;
  ativas: number;
  inativas: number;
  itens: AberturaAdminDto[];
}

export interface SalvarAberturaEntrada {
  numero: number;
  nome: string;
  artista: string;
  episodios: string;
  imagem: string;
  video: string;
  ativa: boolean;
}
