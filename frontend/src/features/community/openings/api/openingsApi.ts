import { chamarApi } from '../../../../lib/api';
import type {
  AberturaDetalhesComunidadeDto,
  AberturaPerfilVisaoDto,
  MinhaAberturaPreferidaDto,
  PesquisaAberturasComunidadeDto,
  RankingAberturasDto,
} from '../types/openings.types';

const BASE = '/backend/comunidade/abertura-preferida';

export function pesquisarAberturasComunidade(termo = '', filtro = 'todas') {
  const params = new URLSearchParams();
  if (termo.trim()) {
    params.set('termo', termo.trim());
  }
  if (filtro && filtro !== 'todas') {
    params.set('filtro', filtro);
  }
  const qs = params.toString();
  return chamarApi<PesquisaAberturasComunidadeDto>(`${BASE}/pesquisa${qs ? `?${qs}` : ''}`);
}

export function obterRankingAberturas() {
  return chamarApi<RankingAberturasDto>(`${BASE}/ranking`);
}

export function obterAberturaDoPerfil(perfilId: string) {
  return chamarApi<AberturaPerfilVisaoDto>(`${BASE}/perfil/${encodeURIComponent(perfilId)}`);
}

export function obterDetalhesAberturaComunidade(aberturaId: string) {
  return chamarApi<AberturaDetalhesComunidadeDto>(
    `${BASE}/${encodeURIComponent(aberturaId)}/visualizacao`,
  );
}

export function escolherAberturaPreferida(aberturaId: string) {
  return chamarApi<MinhaAberturaPreferidaDto>(`${BASE}/escolher`, {
    method: 'POST',
    corpo: { aberturaId },
  });
}

export function ocultarAberturaPreferida() {
  return chamarApi<void>(`${BASE}/ocultar`, {
    method: 'POST',
    corpo: {},
  });
}

export function reexibirAberturaPreferida() {
  return chamarApi<void>(`${BASE}/reexibir`, {
    method: 'POST',
    corpo: {},
  });
}
