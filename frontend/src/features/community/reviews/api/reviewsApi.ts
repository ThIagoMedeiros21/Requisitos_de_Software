import { chamarApi } from '../../../../lib/api';
import type {
  AvaliacaoItemDto,
  CartaCatalogoAvaliacaoDto,
  CompraVerificadaItemDto,
  DetalheAvaliacoesCartaDto,
  RegistrarAvaliacaoEntrada,
  RegistrarCompraEntrada,
  ResumoAvaliacaoDto,
} from '../types/reviews.types';

const BASE = '/backend/comunidade/avaliacao-produto';

export function listarCatalogoAvaliacoes(termo = '') {
  const qs = termo.trim() ? `?termo=${encodeURIComponent(termo.trim())}` : '';
  return chamarApi<CartaCatalogoAvaliacaoDto[]>(`${BASE}/catalogo${qs}`);
}

export function obterDetalhesAvaliacoesCarta(produtoId: string) {
  return chamarApi<DetalheAvaliacoesCartaDto>(
    `${BASE}/${encodeURIComponent(produtoId)}/pesquisa`,
  );
}

export function registrarOuAtualizarAvaliacao(
  produtoId: string,
  entrada: RegistrarAvaliacaoEntrada,
) {
  return chamarApi<AvaliacaoItemDto>(`${BASE}/${encodeURIComponent(produtoId)}/avaliar`, {
    method: 'POST',
    corpo: entrada,
  });
}

export function registrarCompraVerificada(entrada: RegistrarCompraEntrada) {
  return chamarApi<CompraVerificadaItemDto>(`${BASE}/compras/registrar`, {
    method: 'POST',
    corpo: entrada,
  });
}

export function reembolsarCompraVerificada(pedidoId: string, produtoId: string) {
  return chamarApi<ResumoAvaliacaoDto>(
    `${BASE}/compras/${encodeURIComponent(pedidoId)}/reembolsar`,
    {
      method: 'POST',
      corpo: { produtoId },
    },
  );
}
