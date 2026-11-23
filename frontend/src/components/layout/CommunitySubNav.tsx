import { Link } from 'react-router';
import {
  type AbaComunidade,
  useProfileUiStore,
} from '../../features/community/profile/stores/useProfileUiStore';
import { useToastStore } from '../../stores/useToastStore';
import { Icon } from '../ui/Icon';

interface CommunitySubNavProps {
  abaAtiva: AbaComunidade;
  totalAmigos?: number;
  linkVoltarAmigos?: boolean;
}

export function CommunitySubNav({
  abaAtiva,
  totalAmigos,
  linkVoltarAmigos = false,
}: CommunitySubNavProps) {
  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);
  const publicar = useToastStore((estado) => estado.publicar);

  const classeAba = (ativa: boolean) =>
    `flex flex-shrink-0 items-center gap-space-xs rounded-lg px-space-md py-2 font-label-md text-label-md no-underline transition-all ${
      ativa
        ? 'bg-deep-navy font-semibold text-pure-white shadow-sm'
        : 'text-secondary hover:bg-surface-container-low hover:text-deep-navy'
    }`;

  return (
    <div className="flex flex-col justify-between gap-space-md rounded-xl border border-ice-border/60 bg-pure-white p-space-sm shadow-sm sm:flex-row sm:items-center">
      <nav
        aria-label="Navegação da Comunidade"
        className="no-scrollbar flex w-full items-center gap-space-xs overflow-x-auto py-0.5"
      >
        <Link
          to="/comunidade/perfil"
          onClick={() => definirAbaAtiva('meu-perfil')}
          className={classeAba(abaAtiva === 'meu-perfil')}
        >
          <Icon nome="badge" className="text-[18px]" />
          <span>Meu Perfil</span>
        </Link>

        <Link
          to="/comunidade/decks-favoritos"
          onClick={() => definirAbaAtiva('decks-favoritos')}
          className={classeAba(abaAtiva === 'decks-favoritos')}
        >
          <Icon nome="view_carousel" className="text-[18px]" />
          <span>Decks Favoritos</span>
        </Link>

        <Link
          to="/comunidade/avaliacoes"
          onClick={() => definirAbaAtiva('avaliacoes')}
          className={classeAba(abaAtiva === 'avaliacoes')}
        >
          <Icon nome="rate_review" className="text-[18px]" />
          <span>Avaliações</span>
        </Link>

        <button
          type="button"
          onClick={() => publicar('info', 'Navegando para Minha Coleção...')}
          className={classeAba(false)}
        >
          <Icon nome="auto_stories" className="text-[18px]" />
          <span>Minha Coleção</span>
        </button>

        <Link
          to="/comunidade/amigos"
          onClick={() => definirAbaAtiva('rede-amigos')}
          className={classeAba(abaAtiva === 'rede-amigos')}
        >
          <Icon nome="group" className="text-[18px]" />
          <span>Rede de Amigos</span>
          {typeof totalAmigos === 'number' && (
            <span
              className={`rounded-full px-2 py-0.5 font-label-sm text-[11px] font-bold ${
                abaAtiva === 'rede-amigos'
                  ? 'bg-ocean-blue text-pure-white'
                  : 'bg-surface-container text-deep-navy'
              }`}
            >
              {totalAmigos}
            </span>
          )}
        </Link>

        <Link
          to="/comunidade/colecionadores"
          onClick={() => definirAbaAtiva('buscar-colecionadores')}
          className={classeAba(abaAtiva === 'buscar-colecionadores')}
        >
          <Icon nome="person_search" className="text-[18px]" />
          <span>Buscar Colecionadores</span>
        </Link>

        <Link
          to="/comunidade/aberturas"
          onClick={() => definirAbaAtiva('abertura-preferida')}
          className={classeAba(abaAtiva === 'abertura-preferida')}
        >
          <Icon nome="library_music" className="text-[18px]" />
          <span>Abertura Preferida</span>
        </Link>
      </nav>

      {linkVoltarAmigos && (
        <Link
          to="/comunidade/amigos"
          className="flex flex-shrink-0 items-center gap-1 pr-2 font-label-sm text-label-sm font-semibold text-ocean-blue no-underline hover:text-deep-navy"
        >
          <Icon nome="arrow_back" className="text-[16px]" />
          <span>Voltar para Amigos</span>
        </Link>
      )}
    </div>
  );
}
