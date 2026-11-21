import { chamarApi } from '../../../../lib/api';
import type { FiltroStatusAdminAbertura } from '../../../../stores/openingsUiStore';
import type {
  AberturaAdminDto,
  ListaAberturasAdminDto,
  SalvarAberturaEntrada,
} from '../types/adminOpenings.types';

const BASE = '/backend/adm/abertura';

export function pesquisarAberturasAdmin(
  termo = '',
  status: FiltroStatusAdminAbertura = 'TODAS',
) {
  const params = new URLSearchParams();
  if (termo.trim()) {
    params.set('termo', termo.trim());
  }
  if (status && status !== 'TODAS') {
    params.set('status', status);
  }
  const qs = params.toString();
  return chamarApi<ListaAberturasAdminDto>(`${BASE}/pesquisa${qs ? `?${qs}` : ''}`);
}

export function incluirAberturaAdmin(entrada: SalvarAberturaEntrada) {
  return chamarApi<AberturaAdminDto>(`${BASE}/incluir`, {
    method: 'POST',
    corpo: entrada,
  });
}

export function alterarAberturaAdmin(id: string, entrada: SalvarAberturaEntrada) {
  return chamarApi<AberturaAdminDto>(`${BASE}/${encodeURIComponent(id)}/alterar`, {
    method: 'POST',
    corpo: entrada,
  });
}

export function inativarAberturaAdmin(id: string) {
  return chamarApi<void>(`${BASE}/${encodeURIComponent(id)}/inativar`, {
    method: 'POST',
    corpo: {},
  });
}

export function reativarAberturaAdmin(id: string) {
  return chamarApi<void>(`${BASE}/${encodeURIComponent(id)}/reativar`, {
    method: 'POST',
    corpo: {},
  });
}

export function excluirAberturaAdmin(id: string) {
  return chamarApi<void>(`${BASE}/${encodeURIComponent(id)}/excluir`, {
    method: 'POST',
    corpo: {},
  });
}
