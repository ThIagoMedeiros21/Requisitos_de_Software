import { useSuspenseQuery } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router';
import { BarraNavegacaoComunidade } from './BarraNavegacaoComunidade';
import { PerfilEdicaoModal } from './PerfilEdicaoModal';
import { consultaPerfilAlteracao } from './perfilConsultas';

export function PerfilEdicao() {
  const { id } = useParams();
  const navegar = useNavigate();
  const { data: perfil } = useSuspenseQuery(consultaPerfilAlteracao(id!));

  return (
    <div className="mx-auto max-w-6xl px-4 py-6">
      <BarraNavegacaoComunidade abaAtual="meu-perfil" />
      <PerfilEdicaoModal
        perfil={perfil}
        aberto
        emPagina
        aoFechar={() => navegar(`/comunidade/perfil-comunitario/${encodeURIComponent(perfil.id)}/visualizacao`)}
      />
    </div>
  );
}
